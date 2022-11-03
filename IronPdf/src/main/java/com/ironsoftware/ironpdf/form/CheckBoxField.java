package com.ironsoftware.ironpdf.form;

/**
 * The type Check box field.
 */
public class CheckBoxField extends FormField {

    private final boolean booleanValue;

    /**
     (INTERNAL) Please Get CheckBoxField from {@link FormFieldsSet#getCheckBoxFields()} from {@link FormManager#getFields()}
     */
    public CheckBoxField(int annotationIndex, String name, int pageIndex,
                         double x, double y, double width, double height, String value, boolean booleanValue, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.CHECK_BOX, x, y, width, height, value, readOnly);
        this.booleanValue = booleanValue;
    }

    /**
     * Gets boolean value of the checkbox.
     *
     * @return the boolean value
     */
    public final boolean getBooleanValue() {
        return booleanValue;
    }
}
