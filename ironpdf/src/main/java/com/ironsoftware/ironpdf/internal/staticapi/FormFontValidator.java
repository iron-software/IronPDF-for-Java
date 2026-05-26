package com.ironsoftware.ironpdf.internal.staticapi;

/**
 * Validates form-font names and byte payloads before they cross the gRPC boundary
 * or are written into a PDF default-appearance string. Provides defense in depth
 * against PDF DA-string injection, denial-of-service via oversized uploads, and
 * malformed payloads that could destabilize the engine.
 *
 * <p>Sibling validators kept intentionally in sync (defense in depth at each layer).
 * If you change the rules here, also update:
 * <ul>
 *   <li>{@code IronPdf} (.NET): {@code IronPdf/src/IronPdf/Fonts/FormFontValidator.cs}</li>
 *   <li>{@code IronPdf.Core} (.NET): {@code IronSoftware.Pdfium/IronSoftware.Pdfium/Pdfium/PdfClient.Forms.cs}
 *       (private {@code ValidateFormFontName} / {@code ValidateFormFontData})</li>
 *   <li>{@code IronPdf.Core} (native): {@code IronPdf.ChromeRenderer/IronPdfInterop/IronPdfInterop.cpp}
 *       (inline checks in {@code IRS_SetFormFont})</li>
 * </ul>
 */
final class FormFontValidator {

    static final int MAX_FONT_NAME_LENGTH = 63;
    static final int MAX_FONT_DATA_BYTES = 50 * 1024 * 1024;
    static final int MIN_FONT_DATA_BYTES = 12;

    private FormFontValidator() {
    }

    /**
     * Validates a PDF font name. Throws {@link IllegalArgumentException} on failure.
     * Allowed characters: letters, digits, '-', '_', '.', '+'. The plus sign is
     * allowed to support PDF subset prefixes (e.g. "AAAAAA+Poppins-Regular").
     *
     * @param fontName PDF font name to validate
     */
    static void validateName(String fontName) {
        if (fontName == null) {
            throw new IllegalArgumentException("fontName must not be null");
        }
        if (fontName.isEmpty()) {
            throw new IllegalArgumentException("fontName must not be empty");
        }
        if (fontName.length() > MAX_FONT_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "fontName exceeds maximum length (" + fontName.length() + " > " + MAX_FONT_NAME_LENGTH + ")");
        }
        for (int i = 0; i < fontName.length(); i++) {
            char c = fontName.charAt(i);
            boolean ok = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
                    || (c >= '0' && c <= '9')
                    || c == '-' || c == '_' || c == '.' || c == '+';
            if (!ok) {
                throw new IllegalArgumentException(
                        "fontName contains disallowed character at position " + i + ": '" + c + "'. " +
                                "Allowed: letters, digits, '-', '_', '.', '+'.");
            }
        }
    }

    /**
     * Validates raw font bytes (TTF/OTF/TTC). Throws {@link IllegalArgumentException} on failure.
     * Performs a shallow sfnt magic-number check; a successful validation is not a guarantee
     * the file parses correctly — it only prevents obviously bogus payloads from being sent
     * to the engine.
     *
     * @param fontData Raw font bytes; must be non-empty in embedded mode
     */
    static void validateData(byte[] fontData) {
        if (fontData == null || fontData.length == 0) {
            throw new IllegalArgumentException("fontData must not be empty for embedded mode");
        }
        if (fontData.length < MIN_FONT_DATA_BYTES) {
            throw new IllegalArgumentException(
                    "fontData too small to be a valid font (" + fontData.length + " bytes; minimum " + MIN_FONT_DATA_BYTES + ")");
        }
        if (fontData.length > MAX_FONT_DATA_BYTES) {
            throw new IllegalArgumentException(
                    "fontData exceeds maximum allowed size (" + fontData.length + " > " + MAX_FONT_DATA_BYTES + " bytes)");
        }

        boolean magicOk =
                (fontData[0] == 0x00 && fontData[1] == 0x01 && fontData[2] == 0x00 && fontData[3] == 0x00)
                        || (fontData[0] == 0x4F && fontData[1] == 0x54 && fontData[2] == 0x54 && fontData[3] == 0x4F)
                        || (fontData[0] == 0x74 && fontData[1] == 0x74 && fontData[2] == 0x63 && fontData[3] == 0x66)
                        || (fontData[0] == 0x74 && fontData[1] == 0x72 && fontData[2] == 0x75 && fontData[3] == 0x65);
        if (!magicOk) {
            throw new IllegalArgumentException(
                    "fontData is not a recognized TrueType/OpenType file " +
                            "(expected sfnt magic 0x00010000, 'OTTO', 'ttcf' or 'true').");
        }
    }
}
