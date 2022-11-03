package com.ironsoftware.ironpdf.annotation;

import com.ironsoftware.ironpdf.internal.staticapi.Annotation_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;

/**
 * Defines a Sticky-Note style PDF annotation.
 * {@link Annotation_Api#addTextAnnotation(InternalPdfDocument, TextAnnotation, int, int, int)}
 */
public final class TextAnnotation {

    /**
     * The color of the annotation's 'Sticky Note'.  Uses CSS '#ff66BB' hex color style.
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
     * Sets the annotation to be read only
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


    /**
     * Instantiates a new text annotation.
     */
    public TextAnnotation(){
    }

    /**
     * Instantiates a new text annotation.
     *
     * @param contents the contents of the 'sticky note' annotation
     */
    public TextAnnotation(String contents){
        this.contents = contents;
    }

    /**
     * Gets the color of the annotation's 'Sticky Note'.  Uses CSS '#ff66BB' hex color style.
     *
     * @return the color code string.
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * Sets the color of the annotation's 'Sticky Note'.  Uses CSS '#ff66BB' hex color style.
     *
     * @param value the color code string.
     */
    public void setColorCode(String value) {
        colorCode = value;
    }

    /**
     * Gets the contents of the 'sticky note' annotation.
     *
     * @return the contents of the 'sticky note' annotation.
     */
    public String getContents() {
        return contents;
    }

    /**
     * Sets the contents of the 'sticky note' annotation.
     *
     * @param value the contents text.
     */
    public void setContents(String value) {
        contents = value;
    }

    /**
     * Hides the annotation from users.
     *
     * @return the isHidden value.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Hides the annotation from users.
     *
     * @param value the isHidden value.
     */
    public void setHidden(boolean value) {
        hidden = value;
    }

    /**
     * Gets the opacity of the annotation (valid values are from 0.0 to 1.0).
     *
     * @return the opacity of the annotation.
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
        opacity = value;
    }

    /**
     * The annotation to be opened and readable by default, without user interaction.
     *
     * @return the value of isOpenByDefault.
     */
    public boolean isOpenByDefault() {
        return openByDefault;
    }

    /**
     * Sets the annotation to be opened and readable by default, without user interaction.
     *
     * @param value the value of isOpenByDefault.
     */
    public void setOpenByDefault(boolean value) {
        openByDefault = value;
    }

    /**
     * Is allows the annotation to be printed when users print the PDF.
     *
     * @return the isPrintable value.
     */
    public boolean isPrintable() {
        return printable;
    }

    /**
     * Sets allows the annotation to be printed when users print the PDF.
     *
     * @param value the isPrintable value.
     */
    public void setPrintable(boolean value) {
        printable = value;
    }

    /**
     * Sets the annotation to be read only.
     *
     * @return the isReadOnly value.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the annotation to be read only.
     *
     * @param value the isReadOnly value.
     */
    public void setReadOnly(boolean value) {
        readOnly = value;
    }

    /**
     * Is rotateable boolean. Allows the annotation to be rotated.  E.g. when the containing page os rotated.
     *
     * @return the rotateable boolean.
     */
    public boolean isRotateable() {
        return rotateable;
    }

    /**
     * Sets rotateable. Allows the annotation to be rotated.  E.g. when the containing page os rotated.
     *
     * @param value the rotateable value.
     */
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

    /**
     * Gets title. The main title of the annotation as displayed in the header of the 'sticky note'.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title. The main title of the annotation as displayed in the header of the 'sticky note'.
     *
     * @param value the value
     */
    public void setTitle(String value) {
        title = value;
    }

    /**
     * Gets subject. The subject of the annotation as displayed in the header of the 'sticky note'.
     *
     * @return the subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject. The subject of the annotation as displayed in the header of the 'sticky note'.
     *
     * @param value the value.
     */
    public void setSubject(String value) {
        subject = value;
    }

    /**
     * Gets icon. An icon to visually represent the 'sticky note' annotation.
     *
     * @return the icon.
     */
    public AnnotationIcon getIcon() {
        return icon;
    }

    /**
     * Sets icon. An icon to visually represent the 'sticky note' annotation.
     *
     * @param value the value.
     */
    public void setIcon(AnnotationIcon value) {
        icon = value;
    }
}
