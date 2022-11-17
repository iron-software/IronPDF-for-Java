package com.ironsoftware.ironpdf.form;

/**
 * Generic base class from which all PDF Form fields are derived.
 */
public class FormField {

    /**
     * Index of annotation associated with this form field
     */
    private final int annotationIndex;
    /**
     * Gets the ID name of this field.
     */
    private final String name;
    /**
     * Gets the value of the editable PDF field.
     */
    private final String value;
    /**
     * Form field width
     */
    private final double width;
    /**
     * Form field height
     */
    private final double height;
    /**
     * Form field x position
     */
    private final double x;
    /**
     * Form field y position
     */
    private final double y;
    /**
     * Page index which contains this form field
     */
    private final int pageIndex;
    /**
     * Gets or sets the permissions for users to fill-in current form field.
     */
    private final boolean readOnly;

    /**
     * Form field types
     */
    private final FormFieldTypes type;

    /**
     * (INTERNAL) Please Get ComboBoxField from {@link FormFieldsSet#getUnknownFields()} from {@link FormManager#getFields()}
     *
     * @param annotationIndex the annotation index
     * @param name            the name
     * @param pageIndex       the page index
     * @param type            the type
     * @param x               the x
     * @param y               the y
     * @param width           the width
     * @param height          the height
     * @param value           the value
     * @param readOnly        the read only
     */
    public FormField(int annotationIndex, String name, int pageIndex, FormFieldTypes type, double x,
                     double y, double width, double height, String value, boolean readOnly) {
        this.annotationIndex = annotationIndex;
        this.name = name;
        this.pageIndex = pageIndex;
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.value = value;
        this.readOnly = readOnly;
    }

    /**
     * Gets annotation index. Index of annotation associated with this form field
     *
     * @return the annotation index
     */
    public final int getAnnotationIndex() {
        return annotationIndex;
    }

    /**
     * Gets the ID name of this field.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets page index. Page index which contains this form field
     *
     * @return the page index
     */
    public final int getPageIndex() {
        return pageIndex;
    }

    /**
     * Is read only boolean. The permissions for users to fill-in current form field.
     *
     * @return the boolean
     */
    public final boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Gets form field types.
     *
     * @return the type
     */
    public final FormFieldTypes getType() {
        return type;
    }

    /**
     * Gets the value of the editable PDF field.
     *
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * Gets form field width.
     *
     * @return the width
     */
    public final double getWidth() {
        return width;
    }

    /**
     * Gets form field height.
     *
     * @return the height
     */
    public final double getHeight() {
        return height;
    }

    /**
     * Gets form field x position.
     *
     * @return the x
     */
    public final double getX() {
        return x;
    }

    /**
     * Gets form field y position.
     *
     * @return the y
     */
    public final double getY() {
        return y;
    }
}
