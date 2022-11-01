package com.ironsoftware.ironpdf.form;

public class CheckBoxField extends FormField {

    private final boolean BooleanValue;

    public CheckBoxField(int annotationIndex, String name, int pageIndex,
                         double x, double y, double width, double height, String value, boolean booleanValue, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.CHECK_BOX, x, y, width, height, value, readOnly);
        BooleanValue = booleanValue;
    }

    public final boolean getBooleanValue() {
        return BooleanValue;
    }
}
