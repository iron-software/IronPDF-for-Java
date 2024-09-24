package com.ironsoftware.ironpdf.form;

public class SignatureField extends FormField {

    /**
     (INTERNAL) Please Get TextField from {@link FormFieldsSet#getSignatureFields()} from {@link FormManager#getFields()}
     */
    public SignatureField(int annotationIndex, String name, int pageIndex, double x,
                          double y, double width, double height, String value, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.SIGNATURE.getFormType(), x, y, width, height, value, readOnly);
    }
}
