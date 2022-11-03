package com.ironsoftware.ironpdf.font;

import com.ironsoftware.ironpdf.internal.staticapi.Utils_CustomEnumeration;

import java.io.File;
import java.util.HashMap;

/**
 * Supported PDF Fonts
 */
public final class FontTypes extends Utils_CustomEnumeration {

    private static final HashMap<String, FontTypes> index = new HashMap<>();
    private static int customFontIndex = 99;
    private final String customFontFilePath;

    private FontTypes(int id, String name) {
        this(id, name, null);
    }

    private FontTypes(int id, String name, String fontFilePath) {
        super(id, name);
        if (fontFilePath != null) {
            index.put((new File(fontFilePath)).getAbsolutePath(), this);
        }
        index.put(name, this);
        index.put(forgivingName(name), this);
        this.customFontFilePath = fontFilePath;
    }

    private static String forgivingName(String fontName) {
        return "**" + fontName.trim().toLowerCase().replace("-", "");
    }

    /**
     * Create custom font types.
     *
     * @param Name         the name
     * @param FontFilePath the font file (.ttf, .otf, etc.) path
     * @return the font types
     */
    public static FontTypes createCustomFont(String Name, String FontFilePath) {
        try {
            return fromString(Name);
        } catch (Exception ignored) {
        }
        try {
            return fromString(FontFilePath);
        } catch (Exception ignored) {
        }

        return new FontTypes(customFontIndex++, Name, (new File(FontFilePath)).getAbsolutePath());
    }

    /**
     * Gets FontTypes from font name or font file path.
     *
     * @param FontNameOrFilePath the font name or file path
     * @return the font types
     */
    public static FontTypes fromString(String FontNameOrFilePath) {
        try {
            return index.get(FontNameOrFilePath);
        } catch (Exception ignored) {
        }

        try {
            return index.get((new File(FontNameOrFilePath)).getAbsolutePath());
        } catch (Exception ignored) {
        }

        try {
            return index.get(forgivingName(FontNameOrFilePath));
        } catch (Exception ignored) {
        }

        throw new RuntimeException(String.format(
                "You have set a non PDF standard FontType: %1$s, Please select one from IronPdf.Font.FontTypes or use the CreateCustomFont method.",
                FontNameOrFilePath));
    }

    /**
     * Gets arial.
     *
     * @return the arial
     */
    public static FontTypes getArial() {
        return new FontTypes(1, "Arial");
    }

    /**
     * Gets arial bold.
     *
     * @return the arial bold
     */
    public static FontTypes getArialBold() {
        return new FontTypes(2, "Arial-Bold");
    }

    /**
     * Gets arial bold italic.
     *
     * @return the arial bold italic
     */
    public static FontTypes getArialBoldItalic() {
        return new FontTypes(3, "Arial-BoldItalic");
    }

    /**
     * Gets arial italic.
     *
     * @return the arial italic
     */
    public static FontTypes getArialItalic() {
        return new FontTypes(4, "Arial-Italic");
    }

    /**
     * Gets courier.
     *
     * @return the courier
     */
    public static FontTypes getCourier() {
        return new FontTypes(5, "Courier");
    }

    /**
     * Gets courier bold oblique.
     *
     * @return the courier bold oblique
     */
    public static FontTypes getCourierBoldOblique() {
        return new FontTypes(6, "Courier-BoldOblique");
    }

    /**
     * Gets courier oblique.
     *
     * @return the courier oblique
     */
    public static FontTypes getCourierOblique() {
        return new FontTypes(7, "Courier-Oblique");
    }

    /**
     * Gets courier bold.
     *
     * @return the courier bold
     */
    public static FontTypes getCourierBold() {
        return new FontTypes(8, "Courier-Bold");
    }

    /**
     * Gets courier new.
     *
     * @return the courier new
     */
    public static FontTypes getCourierNew() {
        return new FontTypes(9, "CourierNew");
    }

    /**
     * Gets courier new bold.
     *
     * @return the courier new bold
     */
    public static FontTypes getCourierNewBold() {
        return new FontTypes(10, "CourierNew-Bold");
    }

    /**
     * Gets courier new bold italic.
     *
     * @return the courier new bold italic
     */
    public static FontTypes getCourierNewBoldItalic() {
        return new FontTypes(11, "CourierNew-BoldItalic");
    }

    /**
     * Gets courier new italic.
     *
     * @return the courier new italic
     */
    public static FontTypes getCourierNewItalic() {
        return new FontTypes(12, "CourierNew-Italic");
    }

    /**
     * Gets helvetica.
     *
     * @return the helvetica
     */
    public static FontTypes getHelvetica() {
        return new FontTypes(13, "Helvetica");
    }

    /**
     * Gets helvetica bold.
     *
     * @return the helvetica bold
     */
    public static FontTypes getHelveticaBold() {
        return new FontTypes(14, "Helvetica-Bold");
    }

    /**
     * Gets helvetica bold oblique.
     *
     * @return the helvetica bold oblique
     */
    public static FontTypes getHelveticaBoldOblique() {
        return new FontTypes(15, "Helvetica-BoldOblique");
    }

    /**
     * Gets helvetica oblique.
     *
     * @return the helvetica oblique
     */
    public static FontTypes getHelveticaOblique() {
        return new FontTypes(16, "Helvetica-Oblique");
    }

    /**
     * Gets symbol.
     *
     * @return the symbol
     */
    public static FontTypes getSymbol() {
        return new FontTypes(17, "Symbol");
    }

    /**
     * Gets times new roman.
     *
     * @return the times new roman
     */
    public static FontTypes getTimesNewRoman() {
        return new FontTypes(18, "TimesNewRoman");
    }

    /**
     * Gets times new roman bold.
     *
     * @return the times new roman bold
     */
    public static FontTypes getTimesNewRomanBold() {
        return new FontTypes(19, "TimesNewRoman-Bold");
    }

    /**
     * Gets times new roman bold italic.
     *
     * @return the times new roman bold italic
     */
    public static FontTypes getTimesNewRomanBoldItalic() {
        return new FontTypes(20, "TimesNewRoman-BoldItalic");
    }

    /**
     * Gets times new roman italic.
     *
     * @return the times new roman italic
     */
    public static FontTypes getTimesNewRomanItalic() {
        return new FontTypes(21, "TimesNewRoman-Italic");
    }

    /**
     * Gets zapf dingbats.
     *
     * @return the zapf dingbats
     */
    public static FontTypes getZapfDingbats() {
        return new FontTypes(22, "ZapfDingbats");
    }

    /**
     * Gets custom font file path. Only available when FontTypes object was created by {@link #createCustomFont(String, String)}
     *
     * @return the custom font file path
     */
    public String getCustomFontFilePath() {
        return customFontFilePath;
    }
}
