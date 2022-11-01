package com.ironsoftware.ironpdf.stamp;

import com.ironsoftware.ironpdf.edit.Length;
import com.ironsoftware.ironpdf.edit.MeasurementUnit;

/**
 * Defines a PDF Stamper. {@link com.ironsoftware.ironpdf.PdfDocument#ApplyStamp(Stamper)}
 * <p> To see a full class walkthrough with diagrams and examples
 * visit:
 * <a href="https://ironpdf.com/tutorials/csharp-edit-pdf-complete-tutorial/#stamping-and-watermarking">stamping tutorial</a>
 */
public abstract class Stamper {

    private String html = "";
    /**
     * Allows the stamp to be transparent. 0 is fully invisible, 100 if fully opaque.
     */
    private int Opacity = 100;
    /**
     * Rotates the stamp clockwise from 0 to 360 degrees as specified.
     */
    private int Rotation = 0;
    /**
     * The horizontal alignment of the stamp relative to the page.
     */
    private com.ironsoftware.ironpdf.stamp.HorizontalAlignment HorizontalAlignment = com.ironsoftware.ironpdf.stamp.HorizontalAlignment.CENTER;
    /**
     * The vertical alignment of the stamp relative to the page.
     */
    private com.ironsoftware.ironpdf.stamp.VerticalAlignment VerticalAlignment = com.ironsoftware.ironpdf.stamp.VerticalAlignment.MIDDLE;
    /**
     * The horizontal offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset to the right direction. Negative indicates an offset to the left direction.
     */
    private Length HorizontalOffset = new Length();
    /**
     * The horizontal offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset in the downward direction. Negative indicates an offset in the upward direction.
     */
    private Length VerticalOffset = new Length();
    /**
     * The maximum width of the output stamp.
     */
    private Length MaxWidth = null;
    /**
     * The maximum height of the output stamp.
     */
    private Length MaxHeight = null;
    /**
     * The minimum width of the output stamp.
     */
    private Length MinWidth = null;
    /**
     * The minimum height of the output stamp.
     */
    private Length MinHeight = null;
    /**
     * The HTML base URL for which references to external CSS, Javascript and Image files will be
     * relative.
     */
    private String InnerHtmlBaseUrl = null;
    /**
     * Makes stamped elements of this Stamper have an on-click hyperlink. Note: HTML links
     * created by &lt;a href=''&gt; tags are not reserved by stamping.
     */
    private String Hyperlink = null;
    /**
     * Applies a percentage scale to the stamps to be larger or smaller.  Default is 100
     * (Percent) which has no effect.
     */
    private double Scale = 100.0;
    /**
     * Set to true for apply stamp behind the content. If the content is opaque, the stamp may be
     * invisible.
     */
    private boolean IsStampBehindContent = false;

    Stamper() {
    }

    Stamper(String html) {
        setHtml(html);
    }

    /**
     * The HTML fragment which will be stamped onto your PDF.  All external references to JavaScript,
     * CSS, and image files will be relative to HtmlBaseUrl.
     */
    public final String getHtml() {
        return html;
    }

    public final void setHtml(String value) {
        html = value;
    }

    public final int getOpacity() {
        return Opacity;
    }

    public final void setOpacity(int value) {
        Opacity = value;
    }

    public final int getRotation() {
        return Rotation;
    }

    public final void setRotation(int value) {
        Rotation = value;
    }

    public final com.ironsoftware.ironpdf.stamp.HorizontalAlignment getHorizontalAlignment() {
        return HorizontalAlignment;
    }

    public final void setHorizontalAlignment(
            com.ironsoftware.ironpdf.stamp.HorizontalAlignment value) {
        HorizontalAlignment = value;
    }

    public final com.ironsoftware.ironpdf.stamp.VerticalAlignment getVerticalAlignment() {
        return VerticalAlignment;
    }

    public final void setVerticalAlignment(com.ironsoftware.ironpdf.stamp.VerticalAlignment value) {
        VerticalAlignment = value;
    }

    public final Length getHorizontalOffset() {
        return HorizontalOffset;
    }

    public final void setHorizontalOffset(Length value) {
        HorizontalOffset = value;
    }

    public final Length getVerticalOffset() {
        return VerticalOffset;
    }

    public final void setVerticalOffset(Length value) {
        VerticalOffset = value;
    }

    public final Length getMaxWidth() {
        return MaxWidth;
    }

    public final void setMaxWidth(Length value) {
        MaxWidth = value;
    }

    public final Length getMaxHeight() {
        return MaxHeight;
    }

    public final void setMaxHeight(Length value) {
        MaxHeight = value;
    }

    public final Length getMinWidth() {
        return MinWidth;
    }

    public final void setMinWidth(Length value) {
        MinWidth = value;
    }

    public final Length getMinHeight() {
        return MinHeight;
    }

    public final void setMinHeight(Length value) {
        MinHeight = value;
    }

    final String getInnerHtmlBaseUrl() {
        return InnerHtmlBaseUrl;
    }

    final void setInnerHtmlBaseUrl(String value) {
        InnerHtmlBaseUrl = value;
    }

    public final String getHyperlink() {
        return Hyperlink;
    }

    public final void setHyperlink(String value) {
        Hyperlink = value;
    }

    public final double getScale() {
        return Scale;
    }

    public final void setScale(double value) {
        Scale = value;
    }

    public final boolean isStampBehindContent() {
        return IsStampBehindContent;
    }

    public final void setStampBehindContent(boolean value) {
        IsStampBehindContent = value;
    }

}
