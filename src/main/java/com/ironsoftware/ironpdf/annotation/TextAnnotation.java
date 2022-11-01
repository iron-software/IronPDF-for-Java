package com.ironsoftware.ironpdf.annotation;

import com.ironsoftware.ironpdf.staticapi.Annotation_Api;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;

/**
 * Defines a Sticky-Note style PDF annotation.
 * {@link Annotation_Api#AddTextAnnotation(InternalPdfDocument, TextAnnotation, int, int, int)}
 */
public final class TextAnnotation {

    /**
     * The color of the annotation's 'Sticky Note'
     */
    private String colorCode = null;
    /**
     * The contents of the 'sticky note' annotation
     */
    private String contents = "";
    /**
     * Hides the annotation from users
     */
    private boolean hidden = false;
    /**
     * The opacity of the annotation (valid values are from 0.0 to 1.0)
     */
    private double opacity = 1.0;
    /**
     * Sets the annotation to be opened and readable by default, without user interaction
     */
    private boolean openByDefault = true;
    /**
     * Allows the annotation to be printed when users print the PDF
     */
    private boolean printable = false;
    /**
     * Allows the annotation to be printed when users print the PDF
     */
    private boolean readOnly = false;

    /**
     * Allows the annotation to be rotated.  E.g. when the containing page os rotated
     */
    private boolean rotateable = false;
    /**
     * The subject of the annotation as displayed in the header of the 'sticky note'
     */
    private String subject = "";
    /**
     * The main title of the annotation as displayed in the header of the 'sticky note'
     */
    private String title = "";
    /**
     * An icon to visually represent the 'sticky note' annotation
     */
    private AnnotationIcon icon = AnnotationIcon.COMMENT;

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String value) {
        colorCode = value;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String value) {
        contents = value;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean value) {
        hidden = value;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double value) {
        opacity = value;
    }

    public boolean getOpenByDefault() {
        return openByDefault;
    }

    public void setOpenByDefault(boolean value) {
        openByDefault = value;
    }

    public boolean getPrintable() {
        return printable;
    }

    public void setPrintable(boolean value) {
        printable = value;
    }

    public boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean value) {
        readOnly = value;
    }

    public boolean getRotateable() {
        return rotateable;
    }

    public void setRotateable(boolean value) {
        rotateable = value;
    }

    /**
     * Returns a {@link String} that represents this annotation.
     *
     * @return A {@link String} that represents this instance.
     */
    @Override
    public String toString() {
        return String.format("%1$s\n%2$s\n%3$s", getTitle(), getSubject(), getContents());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String value) {
        subject = value;
    }

    public AnnotationIcon getIcon() {
        return icon;
    }

    public void setIcon(AnnotationIcon value) {
        icon = value;
    }
}
