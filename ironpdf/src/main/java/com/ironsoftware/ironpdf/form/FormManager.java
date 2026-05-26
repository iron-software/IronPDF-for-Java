package com.ironsoftware.ironpdf.form;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.internal.staticapi.Form_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class used to read and write data to AcroForms in a {@link com.ironsoftware.ironpdf.PdfDocument}.
 * <p> See: {@link com.ironsoftware.ironpdf.PdfDocument#getForm()} </p>
 */
public class FormManager {

    private final InternalPdfDocument internalPdfDocument;

    /**
     * Please get FormManager by {@link PdfDocument#getForm()} instead.
     *
     * @param internalPdfDocument the internal pdf document
     */
    public FormManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Flattens a document (make all form fields non-editable).
     */
    public final void flatten() {
        Form_Api.flattenPdfFrom(internalPdfDocument);
    }

    /**
     * Flattens a document (make  all form fields non-editable).
     *
     * @param pageSelection Selected page indexes. Default is all pages.
     */
    public final void flatten(PageSelection pageSelection) {
        Form_Api.flattenPdfFrom(internalPdfDocument, internalPdfDocument.getPageList(pageSelection));
    }

    /**
     * Get a collection of the user-editable form fields within a PDF document
     * @return FormFieldsSet
     */
    public FormFieldsSet getFields() {

        List<CheckBoxField> checkBoxFields = new ArrayList<>();
        List<TextField> textFields = new ArrayList<>();
        List<ComboBoxField> comboBoxFields = new ArrayList<>();
        List<ImageField> imageFields = new ArrayList<>();
        List<RadioField> radioFields = new ArrayList<>();
        List<SignatureField> signatureFields = new ArrayList<>();
        List<FormField> unknownFields = new ArrayList<>();
        for (FormField anyField : Form_Api.getFields(
                internalPdfDocument)) {
            if (anyField instanceof CheckBoxField) {
                checkBoxFields.add((CheckBoxField) anyField);
            } else if (anyField instanceof TextField) {
                textFields.add((TextField) anyField);
            } else if (anyField instanceof ComboBoxField) {
                comboBoxFields.add((ComboBoxField) anyField);
            } else if (anyField instanceof ImageField) {
                imageFields.add((ImageField) anyField);
            } else if (anyField instanceof RadioField) {
                radioFields.add((RadioField) anyField);
            } else if (anyField instanceof SignatureField) {
                signatureFields.add((SignatureField) anyField);
            } else {
                unknownFields.add(anyField);
            }
        }
        return new FormFieldsSet(checkBoxFields, textFields, comboBoxFields, imageFields, radioFields, signatureFields ,unknownFields);
    }

    /**
     * Rename a {@link FormField}
     *
     * @param currentFieldName Current fully qualified field name
     * @param newFieldName     New partial field name Please use a fully qualified field name for
     *                         CurrentFieldName, and a partial field name for NewFieldName
     * @return New fully-qualified field name
     */
    public String renameField(String currentFieldName, String newFieldName) {
        return Form_Api.renameField(internalPdfDocument, currentFieldName,
                newFieldName);
    }

    /**
     * Set the string value of a {@link FormField}
     *
     * @param fieldName Fully qualified field name
     * @param value     New value
     */
    public void setFieldValue(String fieldName, String value) {
        List<FormField> ss =this.getFields().getAllFields();
        Optional<FormField> optionalFormField = this.getFields().getAllFields().stream().filter(f->f.getName().equalsIgnoreCase(fieldName)).findFirst();
        if(optionalFormField.isPresent())
            Form_Api.setFieldValue(internalPdfDocument, optionalFormField.get().getAnnotationIndex(),
                value);
        else
            throw new RuntimeException(String.format("setFieldValue, not found field name: %s", fieldName));
    }

    /**
     * Set the is read only value of a {@link FormField}
     *
     * @param fieldName Fully qualified field name
     * @param value     is read only
     */
    public void setFieldReadOnly(String fieldName, boolean value) {
        Optional<FormField> optionalFormField = this.getFields().getAllFields().stream().filter(f->f.getName().equalsIgnoreCase(fieldName)).findFirst();
        if(optionalFormField.isPresent())
            Form_Api.setFormFieldIsReadOnly(internalPdfDocument, optionalFormField.get().getAnnotationIndex(),
                    value);
        else
            throw new RuntimeException(String.format("setFieldReadOnly, not found field name: %s", fieldName));
    }

    /**
     * @deprecated This method is deprecated and no longer has any effect.
     * Set the font of a {@link TextField}
     *
     * @param textFieldName Fully qualified field name
     * @param font          New font
     * @param fontSize      New font size
     */
    public void setTextFieldFont(String textFieldName, FontTypes font, int fontSize) {
        Form_Api.setTextFieldFont(internalPdfDocument, textFieldName,
                font, fontSize);
    }

    /**
     * Sets the document-wide font for all subsequent form-field fills on this document.
     * <p>
     * The engine only auto-embeds the Tahoma/Arial fallback when a form value containing
     * non-ASCII characters is set. Every other field update (renaming, toggling read-only,
     * etc.) leaves fonts untouched, so this method is only useful when at least one of your
     * fills contains non-ASCII characters.
     * </p>
     * <p>
     * If your form fields reference more than one font variant (e.g. Poppins-Regular and
     * Poppins-Bold), call this method once per variant.
     * </p>
     * <p>
     * <b>File-size impact</b>
     * <ul>
     *     <li>Name-only mode ({@code fontData} is null/empty): zero bytes added; the font must already be in the document.</li>
     *     <li>Bytes mode and the font is already embedded with usable glyph data: zero bytes added.</li>
     *     <li>Bytes mode and the font is missing or has no glyph data: the full font is embedded.</li>
     *     <li>Bytes mode with {@code forceEmbed = true}: a fresh font copy is added alongside any existing copy; the document grows by the size of the new font on every call. Use sparingly.</li>
     * </ul>
     *
     * @param fontName   PDF font name to register (e.g. "Poppins-Regular"). Allowed characters: letters, digits, '-', '_', '.', '+'.
     * @param fontData   Raw TrueType/OpenType bytes; pass null or empty for name-only mode (the font must already be in the document).
     * @param forceEmbed When true, embed {@code fontData} even if a font with the same name and usable glyph data is already present. Has no effect in name-only mode.
     * @throws IllegalArgumentException when {@code fontName} or {@code fontData} fails validation
     */
    public void setFormFont(String fontName, byte[] fontData, boolean forceEmbed) {
        Form_Api.setFormFont(internalPdfDocument, fontName, fontData, forceEmbed);
    }

    /**
     * Sets the document-wide form font without forcing a re-embed.
     * Equivalent to {@link #setFormFont(String, byte[], boolean) setFormFont(fontName, fontData, false)}.
     *
     * @param fontName PDF font name to register
     * @param fontData Raw font bytes, or null/empty for name-only mode
     * @throws IllegalArgumentException when {@code fontName} or {@code fontData} fails validation
     */
    public void setFormFont(String fontName, byte[] fontData) {
        Form_Api.setFormFont(internalPdfDocument, fontName, fontData, false);
    }

    /**
     * Sets the document-wide form font by name only. The font must already be in the document
     * (e.g. embedded by the template, or added via a previous call with bytes).
     *
     * @param fontName PDF font name as it appears in the document
     * @throws IllegalArgumentException when {@code fontName} fails validation
     */
    public void setFormFont(String fontName) {
        Form_Api.setFormFont(internalPdfDocument, fontName, null, false);
    }

    /**
     * Loads a TrueType/OpenType font from disk and applies it as the document-wide form font.
     * The PDF font name is derived from the file name (without extension).
     *
     * @param fontFilePath Path to a TTF/OTF/TTC font file
     * @throws IOException              when the font file cannot be read
     * @throws IllegalArgumentException when the file or derived font name fails validation
     */
    public void setFormFontFromFile(Path fontFilePath) throws IOException {
        setFormFontFromFile(fontFilePath, null, false);
    }

    /**
     * Loads a TrueType/OpenType font from disk and applies it as the document-wide form font.
     *
     * @param fontFilePath Path to a TTF/OTF/TTC font file
     * @param fontName     Optional PDF font name to register the font under. When null, the file name without extension is used.
     * @param forceEmbed   When true, embed even if the font is already present.
     * @throws IOException              when the font file cannot be read
     * @throws IllegalArgumentException when the file or derived font name fails validation
     */
    public void setFormFontFromFile(Path fontFilePath, String fontName, boolean forceEmbed) throws IOException {
        if (fontFilePath == null) {
            throw new IllegalArgumentException("fontFilePath must not be null");
        }
        byte[] bytes = Files.readAllBytes(fontFilePath);
        String resolvedName = fontName;
        if (resolvedName == null) {
            String fileName = fontFilePath.getFileName().toString();
            int dot = fileName.lastIndexOf('.');
            resolvedName = (dot > 0) ? fileName.substring(0, dot) : fileName;
        }
        Form_Api.setFormFont(internalPdfDocument, resolvedName, bytes, forceEmbed);
    }

    /**
     * Suppresses the automatic Tahoma/Arial fallback embed for non-ASCII form values without
     * registering any replacement font. Use when zero file-size growth is required and the
     * template's existing font references are sufficient for the values being filled.
     * <p>
     * Rendering of non-ASCII characters then depends entirely on the viewer's font substitution
     * — best for ASCII or Latin-Extended fills where the template already references suitable
     * fonts. For broader character ranges, use {@link #setFormFont(String, byte[], boolean)}
     * instead.
     * </p>
     */
    public void disableFormFontFallback() {
        Form_Api.disableFormFontFallback(internalPdfDocument);
    }

}
