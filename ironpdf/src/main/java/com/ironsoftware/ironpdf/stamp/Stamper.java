package com.ironsoftware.ironpdf.stamp;

import com.ironsoftware.ironpdf.edit.Length;
import com.ironsoftware.ironpdf.edit.MeasurementUnit;
import com.ironsoftware.ironpdf.render.WaitFor;

/**
 * Defines a PDF Stamper. {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper)}
 */
public abstract class Stamper {

    /**
     * A convenient wrapper to wait for various events, or just wait for amount of time.
     */
    private WaitFor waitFor = new WaitFor();

    private String html = "";
    /**
     * Allows the stamp to be transparent. 0 is fully invisible, 100 if fully opaque.
     */
    private int opacity = 100;
    /**
     * Rotates the stamp clockwise from 0 to 360 degrees as specified.
     */
    private int rotation = 0;
    /**
     * The horizontal alignment of the stamp relative to the page.
     */
    private com.ironsoftware.ironpdf.stamp.HorizontalAlignment horizontalAlignment = com.ironsoftware.ironpdf.stamp.HorizontalAlignment.CENTER;
    /**
     * The vertical alignment of the stamp relative to the page.
     */
    private com.ironsoftware.ironpdf.stamp.VerticalAlignment verticalAlignment = com.ironsoftware.ironpdf.stamp.VerticalAlignment.MIDDLE;
    /**
     * The horizontal offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset to the right direction. Negative indicates an offset to the left direction.
     */
    private Length horizontalOffset = new Length();
    /**
     * The vertical offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset in the downward direction. Negative indicates an offset in the upward direction.
     */
    private Length verticalOffset = new Length();
    /**
     * The maximum width of the output stamp.
     */
    private Length maxWidth = null;
    /**
     * The maximum height of the output stamp.
     */
    private Length maxHeight = null;
    /**
     * The minimum width of the output stamp.
     */
    private Length minWidth = null;
    /**
     * The minimum height of the output stamp.
     */
    private Length minHeight = null;
    /**
     * The HTML base URL for which references to external CSS, Javascript and Image files will be
     * relative.
     */
    private String innerHtmlBaseUrl = null;
    /**
     * Makes stamped elements of this Stamper have an on-click hyperlink. Note: HTML links
     * created by &lt;a href=''&gt; tags are not reserved by stamping.
     */
    private String hyperlink = null;
    /**
     * Applies a percentage scale to the stamps to be larger or smaller.  Default is 100
     * (Percent) which has no effect.
     */
    private double scale = 100.0;
    /**
     * Set to true for apply stamp behind the content. If the content is opaque, the stamp may be
     * invisible.
     */
    private boolean isStampBehindContent = false;

    /**
     * Instantiates a new Stamper. Defines a PDF Stamper. {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper)}
     */
    Stamper() {
    }

    /**
     * Instantiates a new Stamper. Defines a PDF Stamper. {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper)}
     *
     * @param html the html
     */
    Stamper(String html) {
        setHtml(html);
    }

    /**
     * Gets html. The HTML fragment which will be stamped onto your PDF.  All external references to JavaScript,
     * CSS, and image files will be relative to HtmlBaseUrl.
     *
     * @return the html
     */
    public final String getHtml() {
        return html;
    }

    /**
     * Sets html. The HTML fragment which will be stamped onto your PDF.  All external references to JavaScript,
     * CSS, and image files will be relative to HtmlBaseUrl.
     *
     * @param value the value
     */
    public final void setHtml(String value) {
        html = value;
    }

    /**
     * Gets opacity. Allows the stamp to be transparent. 0 is fully invisible, 100 if fully opaque.
     *
     * @return the opacity
     */
    public final int getOpacity() {
        return opacity;
    }

    /**
     * Sets opacity. Allows the stamp to be transparent. 0 is fully invisible, 100 if fully opaque.
     *
     * @param value the value
     */
    public final void setOpacity(int value) {
        opacity = value;
    }

    /**
     * Gets rotation. Rotates the stamp clockwise from 0 to 360 degrees as specified.
     *
     * @return the rotation
     */
    public final int getRotation() {
        return rotation;
    }

    /**
     * Sets rotation. Rotates the stamp clockwise from 0 to 360 degrees as specified.
     *
     * @param value the value
     */
    public final void setRotation(int value) {
        rotation = value;
    }

    /**
     * Gets horizontal alignment. The horizontal alignment of the stamp relative to the page.
     *
     * @return the horizontal alignment
     */
    public final com.ironsoftware.ironpdf.stamp.HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Sets horizontal alignment. The horizontal alignment of the stamp relative to the page.
     *
     * @param value the value
     */
    public final void setHorizontalAlignment(
            com.ironsoftware.ironpdf.stamp.HorizontalAlignment value) {
        horizontalAlignment = value;
    }

    /**
     * Gets vertical alignment. The vertical alignment of the stamp relative to the page.
     *
     * @return the vertical alignment
     */
    public final com.ironsoftware.ironpdf.stamp.VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Sets vertical alignment. The vertical alignment of the stamp relative to the page.
     *
     * @param value the value
     */
    public final void setVerticalAlignment(com.ironsoftware.ironpdf.stamp.VerticalAlignment value) {
        verticalAlignment = value;
    }

    /**
     * Gets horizontal offset. The horizontal offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset to the right direction. Negative indicates an offset to the left direction.
     *
     * @return the horizontal offset
     */
    public final Length getHorizontalOffset() {
        return horizontalOffset;
    }

    /**
     * Sets horizontal offset. The horizontal offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset to the right direction. Negative indicates an offset to the left direction.
     *
     * @param value the value
     */
    public final void setHorizontalOffset(Length value) {
        horizontalOffset = value;
    }

    /**
     * Gets vertical offset. The horizontal offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset in the downward direction. Negative indicates an offset in the upward direction.
     *
     * @return the vertical offset
     */
    public final Length getVerticalOffset() {
        return verticalOffset;
    }

    /**
     * Sets vertical offset. The horizontal offset. Default value is 0, and default unit is
     * {@link MeasurementUnit#PERCENTAGE}. Value of 0 has no effect. Positive indicates an
     * offset in the downward direction. Negative indicates an offset in the upward direction.
     *
     * @param value the value
     */
    public final void setVerticalOffset(Length value) {
        verticalOffset = value;
    }

    /**
     * Gets the maximum width of the output stamp.
     *
     * @return the max width
     */
    public final Length getMaxWidth() {
        return maxWidth;
    }

    /**
     * Sets the maximum width of the output stamp.
     *
     * @param value the value
     */
    public final void setMaxWidth(Length value) {
        maxWidth = value;
    }

    /**
     * Gets the maximum height of the output stamp.
     *
     * @return the max height
     */
    public final Length getMaxHeight() {
        return maxHeight;
    }

    /**
     * Sets the maximum height of the output stamp.
     *
     * @param value the value
     */
    public final void setMaxHeight(Length value) {
        maxHeight = value;
    }

    /**
     * Gets the minimum width of the output stamp.
     *
     * @return the min width
     */
    public final Length getMinWidth() {
        return minWidth;
    }

    /**
     * Sets the minimum width of the output stamp.
     *
     * @param value the value
     */
    public final void setMinWidth(Length value) {
        minWidth = value;
    }

    /**
     * Gets the minimum height of the output stamp.
     *
     * @return the min height
     */
    public final Length getMinHeight() {
        return minHeight;
    }

    /**
     * Sets the minimum height of the output stamp.
     *
     * @param value the value
     */
    public final void setMinHeight(Length value) {
        minHeight = value;
    }

    /**
     * Gets inner html base url. The HTML base URL for which references to external CSS, Javascript and Image files will be
     * relative.
     *
     * @return the inner html base url
     */
    final String getInnerHtmlBaseUrl() {
        return innerHtmlBaseUrl;
    }

    /**
     * Sets inner html base url. The HTML base URL for which references to external CSS, Javascript and Image files will be
     * relative.
     *
     * @param value the value
     */
    final void setInnerHtmlBaseUrl(String value) {
        innerHtmlBaseUrl = value;
    }

    /**
     * Gets hyperlink. Makes stamped elements of this Stamper have an on-click hyperlink. Note: HTML links
     * created by &lt;a href=''&gt; tags are not reserved by stamping.
     *
     * @return the hyperlink
     */
    public final String getHyperlink() {
        return hyperlink;
    }

    /**
     * Sets hyperlink. Makes stamped elements of this Stamper have an on-click hyperlink. Note: HTML links
     * created by &lt;a href=''&gt; tags are not reserved by stamping.
     *
     * @param value the value
     */
    public final void setHyperlink(String value) {
        hyperlink = value;
    }

    /**
     * Gets scale. Applies a percentage scale to the stamps to be larger or smaller.  Default is 100
     * (Percent) which has no effect.
     *
     * @return the scale
     */
    public final double getScale() {
        return scale;
    }

    /**
     * Sets scale. Applies a percentage scale to the stamps to be larger or smaller.  Default is 100
     * (Percent) which has no effect.
     *
     * @param value the value
     */
    public final void setScale(double value) {
        scale = value;
    }

    /**
     * Is stamp behind content boolean. Set to true for apply stamp behind the content. If the content is opaque, the stamp may be
     * invisible.
     *
     * @return the boolean
     */
    public final boolean isStampBehindContent() {
        return isStampBehindContent;
    }

    /**
     * Sets stamp behind content. Set to true for apply stamp behind the content. If the content is opaque, the stamp may be
     * invisible.
     *
     * @param value the value
     */
    public final void setStampBehindContent(boolean value) {
        isStampBehindContent = value;
    }

    /**
     * Get A convenient wrapper to wait for various events, or just wait for amount of time.
     */
    public WaitFor getWaitFor() {
        return waitFor;
    }

    /**
     * Set A convenient wrapper to wait for various events, or just wait for amount of time.
     */
    public void setWaitFor(WaitFor waitFor) {
        this.waitFor = waitFor;
    }
}
