package com.ironsoftware.ironpdf.annotation;

import com.ironsoftware.ironpdf.PdfDocument;

/**
 * Class used to configure annotations to a {@link PdfDocument}.
 * <p> See: {@link AnnotationManager}</p>
 * <p> See: {@link PdfDocument#getAnnotation()} </p>
 */
public class AnnotationOptions {

    /**
     * The title of the annotation
     */
    private String title ="";
    /**
     * The subject or 'subheading' of the annotation
     */
    private String subject = "";
    /**
     * The subject or 'subheading' of the annotation
     */
    private String contents;
    /**
     * The horizontal X position of the annotation on your page in pixels
     */
    private int x = 0;
    /**
     * The vertical Y position of the annotation on your page in pixels. Measured from bottom upwards.
     */
    private int y = 0;
    /**
     * The width of your annotation's icon and interactive area in pixels
     */
    private int width = 30;
    /**
     * The height of your annotation's icon and interactive area in pixels
     */
    private int height = 30;
    /**
     * The icon used to display the interactive annotation within the PDF
     */
    private AnnotationIcon icon = AnnotationIcon.NO_ICON;
    /**
     * A flag for annotation to be opened and readable by default, without user interaction
     */
    private boolean open = true;
    /**
     * A flag that makes the annotation readonly. Default is true.
     */
    private boolean readonly = true;
    /**
     * A flag that the annotation render during user PDF printing operations.
     */
    private boolean printable = false;
    /**
     * Color of the annotation's background 'sticky note' using Html color notation.  E.g. '#FFFF33'.
     */
    private String rgbColor = "#FFFF33";
    /**
     * The opacity of the annotation (valid values are from 0.0 to 1.0)
     */
    private double opacity = 0;
    /**
     * A flag that hides the annotation from users. Default is false.
     */
    private boolean hidden = false;
    /**
     * A flag that allows the annotation to be rotated.  E.g. when the containing page os rotated. Default is false.
     */
    private boolean rotateable = false;

    /**
     * Adds an annotation to a page of this {@link PdfDocument}
     *
     * @param title     The title of the annotation
     * @param contents  The text content of the annotation
     * @param x         The horizontal X position of the annotation on your page in pixels
     * @param y         The vertical Y position of the annotation on your page in pixels. Measured                  from bottom upwards.
     */
    public AnnotationOptions(String title, String contents, int x, int y) {
        this.title = title;
        this.contents = contents;
        this.x = x;
        this.y = y;
    }

    /**
     * Adds an annotation to a page of this {@link PdfDocument}
     *
     * @param contents the text content of the annotation
     */
    public AnnotationOptions(String contents) {
        this.contents = contents;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param value the value
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets subject or 'subheading' of the annotation.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject or 'subheading' of the annotation.
     *
     * @param value The value
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets contents.
     *
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * Sets contents.
     *
     * @param value the value
     */
    public void setContents(String value) {
        this.contents = value;
    }

    /**
     * Gets the horizontal X position of the annotation on your page in pixels.
     *
     * @return the horizontal X position of the annotation on your page in pixels
     */
    public int getX() {
        return x;
    }

    /**
     * Sets horizontal X position of the annotation on your page in pixels.
     *
     * @param value the x value
     */
    public void setX(int value) {
        this.x = value;
    }

    /**
     * Gets the vertical Y position of the annotation on your page in pixels. Measured from bottom upwards.
     *
     * @return the vertical Y position of the annotation on your page in pixels. Measured from bottom upwards.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the vertical Y position of the annotation on your page in pixels. Measured from bottom upwards.
     *
     * @param value the y value
     */
    public void setY(int value) {
        this.y = value;
    }

    /**
     * Gets width of your annotation's icon and interactive area in pixels.
     *
     * @return the width of your annotation's icon and interactive area in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width of your annotation's icon and interactive area in pixels.
     *
     * @param value the value
     */
    public void setWidth(int value) {
        this.width = value;
    }

    /**
     * Gets height of your annotation's icon and interactive area in pixels.
     *
     * @return the height of your annotation's icon and interactive area in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height of your annotation's icon and interactive area in pixels.
     *
     * @param value the value
     */
    public void setHeight(int value) {
        this.height = value;
    }

    /**
     * Gets {@link AnnotationIcon} used to display the interactive annotation within the PDF.
     *
     * @return the {@link AnnotationIcon} used to display the interactive annotation within the PDF
     */
    public AnnotationIcon getIcon() {
        return icon;
    }

    /**
     * Sets {@link AnnotationIcon} used to display the interactive annotation within the PDF.
     *
     * @param value the {@link AnnotationIcon}
     */
    public void setIcon(AnnotationIcon value) {
        this.icon = value;
    }

    /**
     * Gets a flag for annotation to be opened and readable by default, without user interaction.
     *
     * @return a flag for annotation to be opened and readable by default, without user interaction
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets a flag for annotation to be opened and readable by default, without user interaction.
     *
     * @param value a flag for annotation to be opened and readable by default, without user interaction. Default is true.
     */
    public void setOpen(boolean value) {
        this.open = value;
    }

    /**
     * Gets a flag that makes the annotation readonly. Default is true.
     *
     * @return a flag that makes the annotation readonly. Default is true.
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Sets a flag that makes the annotation readonly. Default is true.
     *
     * @param value a flag that makes the annotation readonly. Default is true.
     */
    public void setReadonly(boolean value) {
        this.readonly = value;
    }

    /**
     * Gets a flag that the annotation render during user PDF printing operations..
     *
     * @return a flag that the annotation render during user PDF printing operations.
     */
    public boolean isPrintable() {
        return printable;
    }

    /**
     * Sets a flag that the annotation render during user PDF printing operations..
     *
     * @param value a flag that the annotation render during user PDF printing operations.
     */
    public void setPrintable(boolean value) {
        this.printable = value;
    }

    /**
     * Gets color of the annotation's background 'sticky note' using Html color notation.  E.g. '#FFFF33'.
     *
     * @return the color of the annotation's background 'sticky note' using Html color notation.  E.g. '#FFFF33'.
     */
    public String getRgbColor() {
        return rgbColor;
    }

    /**
     * Sets the color of the annotation's background 'sticky note' using Html color notation.  E.g. '#FFFF33'.
     *
     * @param value the color of the annotation's background 'sticky note' using Html color notation.  E.g. '#FFFF33'.
     */
    public void setRgbColor(String value) {
        this.rgbColor = value;
    }

    /**
     * Gets the opacity of the annotation (valid values are from 0.0 to 1.0).
     *
     * @return the opacity of the annotation (valid values are from 0.0 to 1.0)
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * Sets the opacity of the annotation (valid values are from 0.0 to 1.0).
     *
     * @param value the opacity of the annotation (valid values are from 0.0 to 1.0).
     */
    public void setOpacity(double value) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Opacity must be between 0 and 1");
        }
        this.opacity = value;
    }

    /**
     * Gets a flag that hides the annotation from users. Default is false..
     *
     * @return a flag that hides the annotation from users. Default is false.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets a flag that hides the annotation from users. Default is false..
     *
     * @param value a flag that hides the annotation from users. Default is false.
     */
    public void setHidden(boolean value) {
        this.hidden = value;
    }

    /**
     * Gets a flag that allows the annotation to be rotated.  E.g. when the containing page os rotated. Default is false..
     *
     * @return a flag that allows the annotation to be rotated.  E.g. when the containing page os rotated. Default is false.
     */
    public boolean isRotateable() {
        return rotateable;
    }

    /**
     * Sets A flag that allows the annotation to be rotated.  E.g. when the containing page os rotated. Default is false..
     *
     * @param value a flag that allows the annotation to be rotated.  E.g. when the containing page os rotated. Default is false.
     */
    public void setRotateable(boolean value) {
        this.rotateable = value;
    }
}
