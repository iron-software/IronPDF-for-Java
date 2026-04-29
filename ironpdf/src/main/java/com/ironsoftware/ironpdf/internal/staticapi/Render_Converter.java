package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.FitToPaperModes;
import com.ironsoftware.ironpdf.render.*;

import java.util.HashMap;
import java.util.Map;


final class Render_Converter {

    /**
     * Separator used when joining string arrays into the single-string proto fields
     * consumed by {@code DictStringMarshaler} on the IronPdfEngine side
     * (e.g. {@code auto_bookmark_css_selectors}, {@code element_query_selectors}).
     *
     * <p><b>Must match</b> {@code IronSoftware.Pdfium.DictStringMarshaler.ELEM_SEP}
     * used by the C# engine. If cross-language rendering options stop being picked up by
     * the engine, this constant is the first place to double-check against the C# source.</p>
     */
    static final String DICT_ELEM_SEP = "(,IRON)";

    private static void validateAutoBookmarkHeadingLevels(ChromePdfRenderOptions options) {
        if (!options.isAutoBookmarksFromHeadings()) {
            return;
        }
        int min = options.getAutoBookmarkMinHeadingLevel();
        int max = options.getAutoBookmarkMaxHeadingLevel();
        if (min < 1 || min > 6) {
            throw new IllegalArgumentException(
                    "autoBookmarkMinHeadingLevel must be between 1 and 6 (inclusive). Got: " + min);
        }
        if (max < 1 || max > 6) {
            throw new IllegalArgumentException(
                    "autoBookmarkMaxHeadingLevel must be between 1 and 6 (inclusive). Got: " + max);
        }
        if (min > max) {
            throw new IllegalArgumentException(
                    "autoBookmarkMinHeadingLevel (" + min + ") must be less than or equal to autoBookmarkMaxHeadingLevel (" + max + ").");
        }
    }

    static com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptionsP toProto(
            ChromePdfRenderOptions Options) {

        if (Options == null) {
            Options = new ChromePdfRenderOptions();
        }

        validateAutoBookmarkHeadingLevels(Options);

        com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptionsP.Builder proto = com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptionsP.newBuilder();
        proto.setCreatePdfFormsFromHtml(Options.isCreatePdfFormsFromHtml());
        proto.setCustomCssUrl(Utils_Util.nullGuard(Options.getCustomCssUrl()));
        proto.setEnableJavaScript(Options.isEnableJavaScript());
        proto.setFitToPaperMode(Render_Converter.toProto(Options.getFitToPaperMode()));
        proto.setGrayScale(Options.isGrayScale());
        proto.setInputEncoding(Options.getInputEncoding());
        proto.setMarginBottom(Options.getMarginBottom());
        proto.setMarginTop(Options.getMarginTop());
        proto.setMarginLeft(Options.getMarginLeft());
        proto.setMarginRight(Options.getMarginRight());
        proto.setPaperOrientation(Render_Converter.toProto(Options.getPaperOrientation()));
        proto.setPaperSize(Render_Converter.toProto(Options.getPaperSize()));
        proto.setPrintHtmlBackgrounds(Options.isPrintHtmlBackgrounds());
        proto.setTitle(Utils_Util.nullGuard(Options.getTitle()));
        proto.setViewPortHeight(Options.getViewPortHeight());
        proto.setViewPortWidth(Options.getViewPortWidth());
        proto.setZoom(Options.getZoom());
        proto.setCssMediaType(Render_Converter.toProto(Options.getCssMediaType()));
        proto.setJavascript(Utils_Util.nullGuard(Options.getJavascript()));
        proto.setTimeout(Options.getTimeout());
        if (Options.getPaperSize() == PaperSize.Custom) {
            proto.setCustomPaperHeight(Options.getCustomPaperHeight());
            proto.setCustomPaperWidth(Options.getCustomPaperWidth());
        }

        proto.setWaitFor(toProto(Options.getWaitFor()));

        //let H/F be default, since in Java API we separate H/F from rendering

        proto.setTableOfContents(Render_Converter.toProto(Options.getTableOfContents()));

        // Auto-bookmark generation from HTML headings (optional proto fields).
        // Only emit when explicitly enabled so existing engine defaults remain untouched.
        if (Options.isAutoBookmarksFromHeadings()) {
            proto.setAutoBookmarksFromHeadings(true);
            proto.setAutoBookmarkMinHeadingLevel(Options.getAutoBookmarkMinHeadingLevel());
            proto.setAutoBookmarkMaxHeadingLevel(Options.getAutoBookmarkMaxHeadingLevel());
        }

        String joinedBookmarkSelectors = joinDictArray(Options.getAutoBookmarkCssSelectors());
        proto.setAutoBookmarkCssSelectors(joinedBookmarkSelectors == null ? "" : joinedBookmarkSelectors);
        String joinedElementSelectors = joinDictArray(Options.getElementQuerySelectors());
        proto.setElementQuerySelectors(joinedElementSelectors == null ? "" : joinedElementSelectors);

        return proto.build();
    }

    /**
     * Joins a string array into the single-string format the engine expects (elements
     * separated by {@link #DICT_ELEM_SEP}). Returns {@code null} when the input is null
     * or effectively empty, so callers can skip setting the optional proto field.
     */
    private static String joinDictArray(String[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean hasAny = false;
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                continue;
            }
            if (hasAny) {
                sb.append(DICT_ELEM_SEP);
            }
            sb.append(value);
            hasAny = true;
        }
        return hasAny ? sb.toString() : null;
    }

    static RenderOptionWaitForP toProto(WaitFor waitFor){
        RenderOptionWaitForP.Builder proto =RenderOptionWaitForP.newBuilder()
                .setType(waitFor.getType().getValue())
                .setTimeout(waitFor.getTimeout())
                .setNetworkIdleDuration(waitFor.getNetworkIdleDuration())
                .setNumAllowedInFlight(waitFor.getNumAllowedInFlight())
                .setRenderDelayDuration(waitFor.getRenderDelayDuration());
        if(!Utils_StringHelper.isNullOrEmpty(waitFor.getHtmlElementQueryStr())){
            proto.setHtmlElementQueryStr(waitFor.getHtmlElementQueryStr());
        }
        return proto.build();
    }

    static ChromePdfPaperOrientationP toProto(PaperOrientation Input) {
        ChromePdfPaperOrientationP.Builder tempVar = ChromePdfPaperOrientationP.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static ChromePdfPaperSizeP toProto(PaperSize Input) {
        ChromePdfPaperSizeP.Builder tempVar = ChromePdfPaperSizeP.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static ChromePdfCssMediaTypeP toProto(CssMediaType Input) {
        ChromePdfCssMediaTypeP.Builder tempVar = ChromePdfCssMediaTypeP.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.ChromeFitToPaperModesP toProto(FitToPaperModes Input) {
        com.ironsoftware.ironpdf.internal.proto.ChromeFitToPaperModesP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.ChromeFitToPaperModesP.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentialsP toProto(
            ChromeHttpLoginCredentials iron) {
        if (iron == null) {
            iron = new ChromeHttpLoginCredentials();
        }

        com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentialsP.Builder proto = com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentialsP.newBuilder();
        proto.setCustomCookies(Render_Converter.toProto(iron.getCustomCookies()));
        proto.setEnableCookies(iron.isEnableCookies());
        proto.setNetworkPassword(Utils_Util.nullGuard(iron.getNetworkPassword()));
        proto.setNetworkUsername(Utils_Util.nullGuard(iron.getNetworkUsername()));

        return proto.build();
    }

    static StringDictionaryP toProto(HashMap<String, String> dictionary) {
        dictionary = dictionary != null ? dictionary : new HashMap<>();
        StringDictionaryP.Builder proto = StringDictionaryP.newBuilder();
        for (Map.Entry<String, String> keyValuePair : dictionary.entrySet()) {
            StringDictionaryEntryP.Builder tempVar = StringDictionaryEntryP.newBuilder();
            tempVar.setKey(keyValuePair.getKey());
            tempVar.setValue(keyValuePair.getValue());
            proto.addItems(tempVar);
        }
        return proto.build();
    }

    static ChromeTableOfContentsTypesP toProto(TableOfContentsTypes Input) {
        ChromeTableOfContentsTypesP.Builder tempVar = ChromeTableOfContentsTypesP.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }
}
