package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.FitToPaperModes;
import com.ironsoftware.ironpdf.render.*;

import java.util.HashMap;
import java.util.Map;


final class Render_Converter {

    static com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptionsP toProto(
            ChromePdfRenderOptions Options) {

        if (Options == null) {
            Options = new ChromePdfRenderOptions();
        }

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
        if (Options.getPaperSize() != PaperSize.Custom) {
            return proto.build();
        }

        proto.setCustomPaperHeight(Options.getCustomPaperHeight());
        proto.setCustomPaperWidth(Options.getCustomPaperWidth());
        proto.setWaitFor(toProto(Options.getWaitFor()));

        proto.setHtmlHeader(ChromeHtmlHeaderFooterP.newBuilder().build());
        proto.setHtmlFooter(ChromeHtmlHeaderFooterP.newBuilder().build());

        return proto.build();
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
}
