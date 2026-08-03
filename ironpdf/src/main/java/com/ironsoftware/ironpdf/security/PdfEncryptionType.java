package com.ironsoftware.ironpdf.security;

/**
 * The encryption algorithm used to secure a PDF document's content when a password is applied. <p>See
 * {@link SecurityOptions#setEncryptionType(PdfEncryptionType)}</p>
 */
public enum PdfEncryptionType {
    /**
     * 128-bit RC4 encryption (PDF 1.4). This is the default and preserves the historical behavior of
     * IronPDF. RC4 is considered cryptographically weak; prefer AES for new documents.
     */
    RC4_128,

    /**
     * 128-bit AES encryption (PDF 1.5+). Supported by Adobe Acrobat 7 and all modern readers.
     */
    AES_128,

    /**
     * 256-bit AES encryption (PDF 2.0). The strongest option; supported by Adobe Acrobat 9+ and most
     * modern readers. Passwords are encoded as UTF-8 per ISO 32000-2.
     */
    AES_256
}
