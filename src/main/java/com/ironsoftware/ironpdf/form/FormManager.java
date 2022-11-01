package com.ironsoftware.ironpdf.form;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FormManager {

    private final InternalPdfDocument internalPdfDocument;

    public FormManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Flattens a document (make the fields non-editable).
     */
    public final void Flatten() throws IOException {
        com.ironsoftware.ironpdf.staticapi.Form_Api.FlattenPdfFrom(internalPdfDocument);
    }

    /**
     * Flattens a document (make the fields non-editable).
     *
     * @param Pages Optional page indices to flatten (defaults to all pages)
     */
    public final void Flatten(java.lang.Iterable<Integer> Pages) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Form_Api.FlattenPdfFrom(internalPdfDocument, Pages);
    }

    /**
     * Get a collection of the user-editable form fields within a PDF document
     */
    public FormFieldsSet GetFields() throws IOException {

        List<CheckBoxField> checkBoxFields = new ArrayList<>();
        List<TextField> textFields = new ArrayList<>();
        List<ComboBoxField> comboBoxFields = new ArrayList<>();
        List<FormField> unknownFields = new ArrayList<>();
        for (FormField anyField : com.ironsoftware.ironpdf.staticapi.Form_Api.GetFields(
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
     */
    public void RenameField(String currentFieldName, String newFieldName) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Form_Api.RenameField(internalPdfDocument, currentFieldName,
                newFieldName);
    }

    /**
     * Set the value of a {@link FormField}
     *
     * @param fieldName Fully qualified field name
     * @param value     New value
     */
    public void SetFieldValue(String fieldName, String value) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Form_Api.SetFieldValue(internalPdfDocument, fieldName,
                value);
    }

    /**
     * Set the font of a {@link TextField}
     *
     * @param textFieldName Fully qualified field name
     * @param font          New font
     * @param fontSize      New font size
     */
    public void SetTextFieldFont(String textFieldName, FontTypes font, int fontSize)
            throws IOException {
        com.ironsoftware.ironpdf.staticapi.Form_Api.SetTextFieldFont(internalPdfDocument, textFieldName,
                font, fontSize);
    }

}
