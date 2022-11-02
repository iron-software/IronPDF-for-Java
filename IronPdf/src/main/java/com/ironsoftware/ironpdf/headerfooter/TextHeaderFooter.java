package com.ironsoftware.ironpdf.headerfooter;

import com.ironsoftware.ironpdf.font.FontTypes;

/**
 * Defines PDF Header and Footer display options.
 * <p>{@link TextHeaderFooter} uses a logical approach to rendering Headers and Footers for the most
 * common use cases.</p>
 * <p>
 * see also {@link HtmlHeaderFooter}
 */
public class TextHeaderFooter implements Cloneable {

    /**
     * Sets the centered header text for the PDF document. <p>Merge meta-data into your header using
     * any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title}
     * {pdf-title}</p>
     */
    private String centerText = null;
    /**
     * Adds a horizontal line divider between the header / footer and the page content on every page
     * of the PDF document.
     */
    private boolean drawDividerLine = false;
    private FontTypes Font = FontTypes.getArial();
    /**
     * Font size in px.
     */
    private double fontSize = 10;
    /**
     * Sets the left hand side header text for the PDF document. <p>Merge meta-data into your header
     * using any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title}
     * {pdf-title}</p>
     */
    private String leftText = null;
    /**
     * Sets the right hand side header text for the PDF document. <p>Merge meta-data into your header
     * using any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title}
     * {pdf-title}</p>
     */
    private String rightText = null;
    /**
     * Vertical spacing between the header and page content in millimeters.
     */
    private double spacing = 5;

    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String value) {
        centerText = value;
    }

    public boolean getDrawDividerLine() {
        return drawDividerLine;
    }

    public void setDrawDividerLine(boolean value) {
        drawDividerLine = value;
    }

    public FontTypes getFont() {
        return Font;
    }

    public void setFont(FontTypes value) {
        Font = value;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double value) {
        fontSize = value;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String value) {
        leftText = value;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String value) {
        rightText = value;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double value) {
        spacing = value;
    }

    /**
     * Clones this instance.
     *
     * @return System.Object of type SimpleHeaderFooter
     */
    public Object Clone() throws CloneNotSupportedException {
        return this.clone();
    }

    public TextHeaderFooter() {
    }

    public TextHeaderFooter(String leftText, String centerText, String rightText) {
        this.leftText = leftText;
        this.centerText = centerText;
        this.rightText = rightText;
    }
}
