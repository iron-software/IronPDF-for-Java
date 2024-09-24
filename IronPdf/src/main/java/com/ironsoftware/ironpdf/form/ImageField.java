package com.ironsoftware.ironpdf.form;

public class ImageField extends FormField {

    /**
     (INTERNAL) Please Get ImageField from {@link FormFieldsSet#getImageFields()} from {@link FormManager#getFields()}
     */
    public ImageField(int annotationIndex, String name, int pageIndex, double x,
                      double y, double width, double height, String value, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.IMAGE.getFormType(), x, y, width, height, value, readOnly);
    }
}
