package com.ironsoftware.ironpdf.stamp;

import com.ironsoftware.ironpdf.edit.PageSelection;

/**
 * This allows the user to edit an existing PDF by adding some stamped text. A subclass of
 * {@link Stamper}. Defines a Text PDF Stamper. Can be applied with:
 * {@link com.ironsoftware.ironpdf.PdfDocument#ApplyStamp(Stamper, PageSelection)}
 * <p>To see usage and an example of TextStamper, visit:
 * <a href="https://ironpdf.com/tutorials/csharp-edit-pdf-complete-tutorial/#stamp-text-onto-a-pdf">text stamping tutorial</a>
 */
public class TextStamper extends Stamper {

    /**
     * The text to be stamped by the Stamper
     */
    private String Text;
    /**
     * Determines if the bold font weight is applied
     */
    private boolean IsBold = false;
    /**
     * Determines if the text has the italic font style applied
     */
    private boolean IsItalic = false;
    /**
     * Determines if the text has an underline font style applied
     */
    private boolean IsUnderline = false;
    /**
     * Determines if the text has a strike-through applied
     */
    private boolean IsStrikethrough = false;
    /**
     * Font size in px
     */
    private int FontSize = 12;
    /**
     * Font family name for the text.
     * <div></div>
     * Note: If using a web font from <a href="https://fonts.google.com/">font.google</a> then you must set {@link #UseGoogleFont} property of this TextStamper to true.
     */
    private String FontFamily = "Arial";
    /**
     * Must be set to true to when using {@link #FontFamily} from <a href="https://fonts.google.com/">font.google</a> as a web
     * font
     */
    private boolean UseGoogleFont = false;

    public TextStamper() {
        setText("");
    }

    public TextStamper(String text) {
        setText(text);
    }

    public final String getText() {
        return Text;
    }

    public final void setText(String value) {
        Text = value;
    }

    public final boolean isBold() {
        return IsBold;
    }

    public final void setBold(boolean value) {
        IsBold = value;
    }

    public final boolean isItalic() {
        return IsItalic;
    }

    public final void setItalic(boolean value) {
        IsItalic = value;
    }

    public final boolean isUnderline() {
        return IsUnderline;
    }

    public final void setUnderline(boolean value) {
        IsUnderline = value;
    }

    public final boolean isStrikethrough() {
        return IsStrikethrough;
    }

    public final void setStrikethrough(boolean value) {
        IsStrikethrough = value;
    }

    public final int getFontSize() {
        return FontSize;
    }

    public final void setFontSize(int value) {
        FontSize = value;
    }

    public final String getFontFamily() {
        return FontFamily;
    }

    public final void setFontFamily(String value) {
        FontFamily = value;
    }

    public final boolean getUseGoogleFont() {
        return UseGoogleFont;
    }

    public final void setUseGoogleFont(boolean value) {
        UseGoogleFont = value;
    }

}
