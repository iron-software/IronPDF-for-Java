package com.ironsoftware.ironpdf.stamp;

import com.ironsoftware.ironpdf.edit.PageSelection;

import java.awt.*;

/**
 * This allows the user to edit an existing PDF by adding some stamped text. A subclass of
 * {@link Stamper}. Defines a Text PDF Stamper. Can be applied with:
 * {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper, PageSelection)}
 */
public class TextStamper extends Stamper {

    /**
     * The text to be stamped by the Stamper
     */
    private String text;
    /**
     * Determines if the bold font weight is applied.
     */
    private boolean isBold = false;
    /**
     * Determines if the text has the italic font style applied.
     */
    private boolean isItalic = false;
    /**
     * Determines if the text has an underline font style applied.
     */
    private boolean isUnderline = false;
    /**
     * Determines if the text has a strike-through applied.
     */
    private boolean isStrikethrough = false;
    /**
     * Font size in px.
     */
    private int fontSize = 12;
    /**
     * Font family name for the text.
     * Note: If using a web font from <a href="https://fonts.google.com/">font.google</a> then you must set {@link #setUseGoogleFont} property of this TextStamper to true.
     */
    private String fontFamily = "Arial";

    /**
     Font Color. Default is '#000000' (Black).
     */
    private String fontColorCode = "#000000";

    /**
     Background Color. Default is '#00FFFFFF' (Transparent).
     */
    private String backgroundColorCode = "#00FFFFFF" ;

    /**
     * Must be set to true to when using {@link #fontFamily} from <a href="https://fonts.google.com/">font.google</a> as a web
     * font
     */
    private boolean useGoogleFont = false;

    /**
     * Instantiates a new Text stamper. This allows the user to edit an existing PDF by adding some stamped text. A subclass of
     * {@link Stamper}. Defines a Text PDF Stamper. Can be applied with:
     * {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper, PageSelection)}
     */
    public TextStamper() {
        setText("");
    }

    /**
     * Instantiates a new Text stamper.  This allows the user to edit an existing PDF by adding some stamped text. A subclass of
     * {@link Stamper}. Defines a Text PDF Stamper. Can be applied with:
     * {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper, PageSelection)}
     *
     * @param text the text to be stamped by the Stamper
     */
    public TextStamper(String text) {
        setText(text);
    }

    /**
     * Gets text to be stamped by the Stamper
     *
     * @return the text
     */
    public final String getText() {
        return text;
    }

    /**
     * Sets text to be stamped by the Stamper
     *
     * @param value the value
     */
    public final void setText(String value) {
        text = value;
    }

    /**
     * Is bold boolean. Determines if the bold font weight is applied.
     *
     * @return the boolean
     */
    public final boolean isBold() {
        return isBold;
    }

    /**
     * Sets bold. Determines if the bold font weight is applied.
     *
     * @param value the value
     */
    public final void setBold(boolean value) {
        isBold = value;
    }

    /**
     * Is italic boolean. Determines if the text has the italic font style applied.
     *
     * @return the boolean
     */
    public final boolean isItalic() {
        return isItalic;
    }

    /**
     * Sets italic. Determines if the text has the italic font style applied.
     *
     * @param value the value
     */
    public final void setItalic(boolean value) {
        isItalic = value;
    }

    /**
     * Is underline boolean. Determines if the text has an underline font style applied.
     *
     * @return the boolean
     */
    public final boolean isUnderline() {
        return isUnderline;
    }

    /**
     * Sets underline. Determines if the text has an underline font style applied.
     *
     * @param value the value
     */
    public final void setUnderline(boolean value) {
        isUnderline = value;
    }

    /**
     * Is strikethrough boolean. Determines if the text has a strike-through applied.
     *
     * @return the boolean
     */
    public final boolean isStrikethrough() {
        return isStrikethrough;
    }

    /**
     * Sets strikethrough. Determines if the text has a strike-through applied.
     *
     * @param value the value
     */
    public final void setStrikethrough(boolean value) {
        isStrikethrough = value;
    }

    /**
     * Gets font size. Font size in px.
     *
     * @return the font size
     */
    public final int getFontSize() {
        return fontSize;
    }

    /**
     * Sets font size. Font size in px.
     *
     * @param value the value
     */
    public final void setFontSize(int value) {
        fontSize = value;
    }

    /**
     * Gets font family. Font family name for the text.
     * <div></div>
     * Note: If using a web font from <a href="https://fonts.google.com/">font.google</a> then you must set {@link #setUseGoogleFont} property of this TextStamper to true.
     *
     * @return the font family
     */
    public final String getFontFamily() {
        return fontFamily;
    }

    /**
     * Sets font family. Font family name for the text.
     * Note: If using a web font from <a href="https://fonts.google.com/">font.google</a> then you must set {@link #setUseGoogleFont} property of this TextStamper to true.
     *
     * @param value the value
     */
    public final void setFontFamily(String value) {
        fontFamily = value;
    }

    /**
     * Is use google font boolean. Working with {@link #setFontFamily(String)}
     *
     * @return the boolean
     */
    public final boolean isUseGoogleFont() {
        return useGoogleFont;
    }

    /**
     * Sets use google font. {@link #setFontFamily(String)}
     *
     * @param value the value
     */
    public final void setUseGoogleFont(boolean value) {
        useGoogleFont = value;
    }

    /**
     * Gets Font Color. Default is '#000000' (Black).
     *
     * @return the font color
     */
    public final String getFontColorCode() {
        return fontColorCode;
    }

    /**
     * Sets Font Color. Default is '#000000' (Black).
     *
     * @param value font color
     */
    public final void setFontColor(String value) {
        fontColorCode = value;
    }

    /**
     * Gets Background Color. Default is '#00FFFFFF' (Transparent).
     *
     * @return the font color
     */
    public final String getBackgroundColorCode() {
        return backgroundColorCode;
    }

    /**
     * Sets Background Color. Default is '#00FFFFFF' (Transparent).
     *
     * @param value font color
     */
    public final void setBackgroundColorCode(String value) {
        backgroundColorCode = value;
    }

}
