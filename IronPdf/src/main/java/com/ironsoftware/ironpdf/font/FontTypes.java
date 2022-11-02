package com.ironsoftware.ironpdf.font;

import com.ironsoftware.ironpdf.staticapi.Utils_CustomEnumeration;

import java.io.File;
import java.util.HashMap;

/**
 * Supported PDF Fonts
 */
public final class FontTypes extends Utils_CustomEnumeration {

    private static final HashMap<String, FontTypes> index = new HashMap<>();
    private static int customFontIndex = 99;
    private final String customFontFilePath;

    public FontTypes(int id, String name) {
        this(id, name, null);
    }

    public FontTypes(int id, String name, String fontFilePath) {
        super(id, name);
        if (fontFilePath != null) {
            index.put((new File(fontFilePath)).getAbsolutePath(), this);
        }
        index.put(name, this);
        index.put(ForgivingName(name), this);
        this.customFontFilePath = fontFilePath;
    }

    private static String ForgivingName(String fontName) {
        return "**" + fontName.trim().toLowerCase().replace("-", "");
    }

    public static FontTypes CreateCustomFont(String Name, String FontFilePath) {
        try {
            return FromString(Name);
        } catch (Exception ignored) {
        }
        try {
            return FromString(FontFilePath);
        } catch (Exception ignored) {
        }

        return new FontTypes(customFontIndex++, Name, (new File(FontFilePath)).getAbsolutePath());
    }

    public static FontTypes FromString(String FontNameOrFilePath) {
        try {
            return index.get(FontNameOrFilePath);
        } catch (Exception ignored) {
        }

        try {
            return index.get((new File(FontNameOrFilePath)).getAbsolutePath());
        } catch (Exception ignored) {
        }

        try {
            return index.get(ForgivingName(FontNameOrFilePath));
        } catch (Exception ignored) {
        }

        throw new RuntimeException(String.format(
                "You have set a non PDF standard FontType: %1$s, Please select one from IronPdf.Font.FontTypes or use the CreateCustomFont method.",
                FontNameOrFilePath));
    }

    public static FontTypes getArial() {
        return new FontTypes(1, "Arial");
    }

    public static FontTypes getArialBold() {
        return new FontTypes(2, "Arial-Bold");
    }

    public static FontTypes getArialBoldItalic() {
        return new FontTypes(3, "Arial-BoldItalic");
    }

    public static FontTypes getArialItalic() {
        return new FontTypes(4, "Arial-Italic");
    }

    public static FontTypes getCourier() {
        return new FontTypes(5, "Courier");
    }

    public static FontTypes getCourierBoldOblique() {
        return new FontTypes(6, "Courier-BoldOblique");
    }

    public static FontTypes getCourierOblique() {
        return new FontTypes(7, "Courier-Oblique");
    }

    public static FontTypes getCourierBold() {
        return new FontTypes(8, "Courier-Bold");
    }

    public static FontTypes getCourierNew() {
        return new FontTypes(9, "CourierNew");
    }

    public static FontTypes getCourierNewBold() {
        return new FontTypes(10, "CourierNew-Bold");
    }

    public static FontTypes getCourierNewBoldItalic() {
        return new FontTypes(11, "CourierNew-BoldItalic");
    }

    public static FontTypes getCourierNewItalic() {
        return new FontTypes(12, "CourierNew-Italic");
    }

    public static FontTypes getHelvetica() {
        return new FontTypes(13, "Helvetica");
    }

    public static FontTypes getHelveticaBold() {
        return new FontTypes(14, "Helvetica-Bold");
    }

    public static FontTypes getHelveticaBoldOblique() {
        return new FontTypes(15, "Helvetica-BoldOblique");
    }

    public static FontTypes getHelveticaOblique() {
        return new FontTypes(16, "Helvetica-Oblique");
    }

    public static FontTypes getSymbol() {
        return new FontTypes(17, "Symbol");
    }

    public static FontTypes getTimesNewRoman() {
        return new FontTypes(18, "TimesNewRoman");
    }

    public static FontTypes getTimesNewRomanBold() {
        return new FontTypes(19, "TimesNewRoman-Bold");
    }

    public static FontTypes getTimesNewRomanBoldItalic() {
        return new FontTypes(20, "TimesNewRoman-BoldItalic");
    }

    public static FontTypes getTimesNewRomanItalic() {
        return new FontTypes(21, "TimesNewRoman-Italic");
    }

    public static FontTypes getZapfDingbats() {
        return new FontTypes(22, "ZapfDingbats");
    }

    public String getCustomFontFilePath() {
        return customFontFilePath;
    }
}
