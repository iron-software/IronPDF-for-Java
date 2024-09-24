package com.ironsoftware.ironpdf.form;

public class TextField extends FormField {

    /**
     (INTERNAL) Please Get TextField from {@link FormFieldsSet#getTextFields()} from {@link FormManager#getFields()}
     */
    public TextField(int annotationIndex, String name, int pageIndex, double x,
                     double y, double width, double height, String value, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.TEXT_FIELD.getFormType(), x, y, width, height, value, readOnly);
    }
}
