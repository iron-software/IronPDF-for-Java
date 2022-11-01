package com.ironsoftware.ironpdf.form;

/**
 * Generic base class from which all PDF Form fields are derived.
 */
public class FormField {

    /**
     * Index of annotation associated with this form field
     */
    private final int AnnotationIndex;
    /**
     * Gets the ID name of this field.
     */
    private final String Name;
    /**
     * Gets the value of the editable PDF field.
     */
    private final String Value;
    /**
     * Form field width
     */
    private final double Width;
    /**
     * Form field height
     */
    private final double Height;
    /**
     * Form field x position
     */
    private final double X;
    /**
     * Form field y position
     */
    private final double Y;
    /**
     * Page index which contains this form field
     */
    private final int PageIndex;
    /**
     * Gets or sets the permissions for users to fill-in current form field.
     */
    private final boolean ReadOnly;
    private final FormFieldTypes Type;

    public FormField(int annotationIndex, String name, int pageIndex, FormFieldTypes type, double x,
                     double y, double width, double height, String value, boolean readOnly) {
        this.AnnotationIndex = annotationIndex;
        this.Name = name;
        this.PageIndex = pageIndex;
        this.Type = type;
        this.X = x;
        this.Y = y;
        this.Width = width;
        this.Height = height;
        this.Value = value;
        this.ReadOnly = readOnly;
    }

    public final int getAnnotationIndex() {
        return AnnotationIndex;
    }

    public final String getName() {
        return Name;
    }

    public final int getPageIndex() {
        return PageIndex;
    }

    public final boolean getReadOnly() {
        return ReadOnly;
    }

    public final FormFieldTypes getType() {
        return Type;
    }

    public final String getValue() {
        return Value;
    }

    public final double getWidth() {
        return Width;
    }

    public final double getHeight() {
        return Height;
    }

    public final double getX() {
        return X;
    }

    public final double getY() {
        return Y;
    }
}
