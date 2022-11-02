package com.ironsoftware.ironpdf.annotation;

import com.ironsoftware.ironpdf.PdfDocument;

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
     * Index of the page to add the annotation. The first page has a PageIndex of 0
     */
    private int pageIndex = 0;
    /**
     * The horizontal X position of the annotation on your page in pixels
     */
    private int x = 0;
    /**
     * The vertical Y position of the annotation on your page in pixels. Measured from bottom
     * upwards.
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
     * Sets the annotation to be opened and readable by default, without user interaction
     */
    private boolean open = true;
    /**
     * Makes the annotation readonly
     */
    private boolean readonly = true;
    /**
     * Makes the annotation render during user PDF printing operations
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
     * Hides the annotation from users
     */
    private boolean hidden = false;
    /**
     * Allows the annotation to be rotated.  E.g. when the containing page os rotated
     */
    private boolean rotateable = false;

    /**
     * Adds an annotation to a page of this {@link PdfDocument}
     *
     * @param title     The title of the annotation
     * @param contents  The text content of the annotation
     * @param pageIndex Index of the page to add the annotation. The first page has a PageIndex of 0
     * @param x         The horizontal X position of the annotation on your page in pixels
     * @param y         The vertical Y position of the annotation on your page in pixels. Measured
     *                  from bottom upwards.
     */
    public AnnotationOptions(String title, String contents, int pageIndex, int x, int y) {
        this.title = title;
        this.contents = contents;
        this.pageIndex = pageIndex;
        this.x = x;
        this.y = y;
    }

    /**
     * Adds an annotation to a page of this {@link PdfDocument}
     *
     * @param contents  The text content of the annotation
     */
    public AnnotationOptions(String contents) {
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String value) {
        this.subject = value;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String value) {
        this.contents = value;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int value) {
        pageIndex = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int value) {
        this.x = value;
    }

    public int getY() {
        return y;
    }

    public void setY(int value) {
        this.y = value;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int value) {
        this.width = value;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int value) {
        this.height = value;
    }

    public AnnotationIcon getIcon() {
        return icon;
    }

    public void setIcon(AnnotationIcon value) {
        this.icon = value;
    }

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean value) {
        this.open = value;
    }

    public boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(boolean value) {
        this.readonly = value;
    }

    public boolean getPrintable() {
        return printable;
    }

    public void setPrintable(boolean value) {
        this.printable = value;
    }

    public String getRgbColor() {
        return rgbColor;
    }

    public void setRgbColor(String value) {
        this.rgbColor = value;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double value) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Opacity must be between 0 and 1");
        }
        this.opacity = value;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean value) {
        this.hidden = value;
    }

    public boolean getRotateable() {
        return rotateable;
    }

    public void setRotateable(boolean value) {
        this.rotateable = value;
    }
}
