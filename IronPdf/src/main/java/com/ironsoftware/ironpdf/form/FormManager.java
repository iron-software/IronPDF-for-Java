package com.ironsoftware.ironpdf.form;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.internal.staticapi.Form_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;

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
        List<FormField> unknownFields = new ArrayList<>();
        for (FormField anyField : Form_Api.getFields(
                internalPdfDocument)) {
            if (anyField instanceof CheckBoxField) {
                checkBoxFields.add((CheckBoxField) anyField);
            } else if (anyField instanceof TextField) {
                textFields.add((TextField) anyField);
            } else if (anyField instanceof ComboBoxField) {
                comboBoxFields.add((ComboBoxField) anyField);
            } else {
                unknownFields.add(anyField);
            }
        }
        return new FormFieldsSet(checkBoxFields, textFields, comboBoxFields, unknownFields);
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

}
