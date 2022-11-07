package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.FitToPaperModes;
import com.ironsoftware.ironpdf.render.*;

import java.util.HashMap;
import java.util.Map;


final class Render_Converter {

    static com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions toProto(
            ChromePdfRenderOptions Options) {
        if (Options == null) {
            return null;
        }

        com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions.Builder proto = com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions.newBuilder();
        proto.setCreatePdfFormsFromHtml(Options.isCreatePdfFormsFromHtml());
        proto.setCustomCssUrl(Utils_Util.nullGuard(Options.getCustomCssUrl()));
        proto.setEnableJavaScript(Options.isEnableJavaScript());
        proto.setFirstPageNumber(Options.getFirstPageNumber());
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
        proto.setRenderDelay(Options.getRenderDelay());
        proto.setTimeout(Options.getTimeout());
        proto.setTitle(Utils_Util.nullGuard(Options.getTitle()));
        proto.setViewPortHeight(Options.getViewPortHeight());
        proto.setViewPortWidth(Options.getViewPortWidth());
        proto.setZoom(Options.getZoom());
        proto.setCssMediaType(Render_Converter.toProto(Options.getCssMediaType()));

        if (Options.getPaperSize() != PaperSize.Custom) {
            return proto.build();
        }

        proto.setCustomPaperHeight(Options.getCustomPaperHeight());
        proto.setCustomPaperHeight(Options.getCustomPaperWidth());

        return proto.build();
    }

    static PdfPaperOrientation toProto(PaperOrientation Input) {
        PdfPaperOrientation.Builder tempVar = PdfPaperOrientation.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static PdfPaperSize toProto(PaperSize Input) {
        PdfPaperSize.Builder tempVar = PdfPaperSize.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static PdfCssMediaType toProto(CssMediaType Input) {
        PdfCssMediaType.Builder tempVar = PdfCssMediaType.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.FitToPaperModes toProto(FitToPaperModes Input) {
        com.ironsoftware.ironpdf.internal.proto.FitToPaperModes.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.FitToPaperModes.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials toProto(
            ChromeHttpLoginCredentials iron) {
        if (iron == null) {
            return null;
        }

        com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials.Builder proto = com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials.newBuilder();
        proto.setCustomCookies(Render_Converter.toProto(iron.getCustomCookies()));
        proto.setEnableCookies(iron.isEnableCookies());
        proto.setNetworkPassword(Utils_Util.nullGuard(iron.getNetworkPassword()));
        proto.setNetworkUsername(Utils_Util.nullGuard(iron.getNetworkUsername()));

        return proto.build();
    }

    static StringDictionary toProto(HashMap<String, String> dictionary) {
        dictionary = dictionary != null ? dictionary : new HashMap<>();
        StringDictionary.Builder proto = StringDictionary.newBuilder();
        for (Map.Entry<String, String> keyValuePair : dictionary.entrySet()) {
            StringDictionaryEntry.Builder tempVar = StringDictionaryEntry.newBuilder();
            tempVar.setKey(keyValuePair.getKey());
            tempVar.setValue(keyValuePair.getValue());
            proto.addItems(tempVar);
        }
        return proto.build();
    }
}
