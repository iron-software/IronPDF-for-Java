package com.ironsoftware.ironpdf.headerfooter;

import com.ironsoftware.ironpdf.font.FontTypes;

/**
 * Defines a text based PDF Header and Footer.
 * <p>{@link TextHeaderFooter} uses a fast and logical approach to rendering Headers and Footers for the most
 * common use cases. A more advanced option available to developers is {@link HtmlHeaderFooter}.</p>
 * <p>See:{@link com.ironsoftware.ironpdf.PdfDocument#addTextHeader(TextHeaderFooter)} &amp; {@link com.ironsoftware.ironpdf.PdfDocument#addTextFooter(TextHeaderFooter)}  </p>
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

    /**
     * Gets center text. The centered header text for the PDF document. <p>Merge meta-data into your header using
     * any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title} {pdf-title}</p>
     *
     * @return the center text
     */
    public String getCenterText() {
        return centerText;
    }

    /**
     * Sets center text. The centered header text for the PDF document. <p>Merge meta-data into your header using
     * any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title} {pdf-title}</p>
     *
     * @param value the value
     */
    public void setCenterText(String value) {
        centerText = value;
    }

    /**
     * Is draw divider line boolean. A horizontal line divider between the header / footer and the page content on every page
     * of the PDF document.
     *
     * @return the boolean
     */
    public boolean isDrawDividerLine() {
        return drawDividerLine;
    }

    /**
     * Sets draw divider line. A horizontal line divider between the header / footer and the page content on every page
     * of the PDF document.
     *
     * @param value the value
     */
    public void setDrawDividerLine(boolean value) {
        drawDividerLine = value;
    }

    /**
     * Gets font.
     *
     * @return the font
     */
    public FontTypes getFont() {
        return Font;
    }

    /**
     * Sets font.
     *
     * @param value the value
     */
    public void setFont(FontTypes value) {
        Font = value;
    }

    /**
     * Gets font size.
     *
     * @return the font size
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * Sets font size.
     *
     * @param value the value
     */
    public void setFontSize(double value) {
        fontSize = value;
    }

    /**
     * Gets left text. The left hand side header text for the PDF document. <p>Merge meta-data into your header
     * using any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title} {pdf-title}</p>
     *
     * @return the left text
     */
    public String getLeftText() {
        return leftText;
    }

    /**
     * Sets left text. The left hand side header text for the PDF document. <p>Merge meta-data into your header
     * using any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title} {pdf-title}</p>
     *
     * @param value the value
     */
    public void setLeftText(String value) {
        leftText = value;
    }

    /**
     * Gets right text. The right hand side header text for the PDF document. <p>Merge meta-data into your header
     * using any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title}
     * {pdf-title}</p>
     *
     * @return the right text
     */
    public String getRightText() {
        return rightText;
    }

    /**
     * Sets right text. The right hand side header text for the PDF document. <p>Merge meta-data into your header
     * using any of these placeholder strings: {page} {total-pages} {url} {date} {time} {html-title}
     * {pdf-title}</p>
     *
     * @param value the value
     */
    public void setRightText(String value) {
        rightText = value;
    }

    /**
     * Gets spacing.
     *
     * @return the spacing
     */
    public double getSpacing() {
        return spacing;
    }

    /**
     * Sets spacing.
     *
     * @param value the value
     */
    public void setSpacing(double value) {
        spacing = value;
    }

    /**
     * Clones this instance.
     *
     * @return System.Object of type SimpleHeaderFooter
     * @throws CloneNotSupportedException the clone not supported exception
     */
    public Object Clone() throws CloneNotSupportedException {
        return this.clone();
    }

    /**
     * Creates a text based PDF Header and Footer.
     * Please set any of {@link #setLeftText(String)}, {@link #setCenterText(String)} or {@link #setRightText(String)}
     */
    public TextHeaderFooter() {
    }


    /**
     * Creates a text based PDF Header and Footer.
     *
     * @param leftText   the left text. Default is null.
     * @param centerText the center text. Default is null.
     * @param rightText  the right text. Default is null.
     */
    public TextHeaderFooter(String leftText, String centerText, String rightText) {
        this.leftText = leftText;
        this.centerText = centerText;
        this.rightText = rightText;
    }
}
