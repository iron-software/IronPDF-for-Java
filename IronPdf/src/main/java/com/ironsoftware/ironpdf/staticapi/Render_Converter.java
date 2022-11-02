package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.*;

import java.util.HashMap;
import java.util.Map;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.NullGuard;


final class Render_Converter {

    static com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions ToProto(
            ChromePdfRenderOptions Options) {
        if (Options == null) {
            return null;
        }

        com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions.Builder proto = com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions.newBuilder();
        proto.setCreatePdfFormsFromHtml(Options.getCreatePdfFormsFromHtml());
        proto.setCustomCssUrl(NullGuard(Options.getCustomCssUrl()));
        proto.setEnableJavaScript(Options.getEnableJavaScript());
        proto.setFirstPageNumber(Options.getFirstPageNumber());
        proto.setFitToPaperWidth(Options.getFitToPaperWidth());
        proto.setGrayScale(Options.getGrayScale());
        proto.setInputEncoding(Options.getInputEncoding());
        proto.setMarginBottom(Options.getMarginBottom());
        proto.setMarginTop(Options.getMarginTop());
        proto.setMarginLeft(Options.getMarginLeft());
        proto.setMarginRight(Options.getMarginRight());
        proto.setPaperOrientation(Render_Converter.ToProto(Options.getPaperOrientation()));
        proto.setPaperSize(Render_Converter.ToProto(Options.getPaperSize()));
        proto.setPrintHtmlBackgrounds(Options.getPrintHtmlBackgrounds());
        proto.setRenderDelay(Options.getRenderDelay());
        proto.setTimeout(Options.getTimeout());
        proto.setTitle(NullGuard(Options.getTitle()));
        proto.setViewPortHeight(Options.getViewPortHeight());
        proto.setViewPortWidth(Options.getViewPortWidth());
        proto.setZoom(Options.getZoom());
        proto.setCssMediaType(Render_Converter.ToProto(Options.getCssMediaType()));

        if (Options.getPaperSize() != PaperSize.Custom) {
            return proto.build();
        }

        proto.setCustomPaperHeight(Options.getCustomPaperHeight());
        proto.setCustomPaperHeight(Options.getCustomPaperWidth());

        return proto.build();
    }

    static PdfPaperOrientation ToProto(PaperOrientation Input) {
        PdfPaperOrientation.Builder tempVar = PdfPaperOrientation.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static PdfPaperSize ToProto(PaperSize Input) {
        PdfPaperSize.Builder tempVar = PdfPaperSize.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static PdfCssMediaType ToProto(CssMediaType Input) {
        PdfCssMediaType.Builder tempVar = PdfCssMediaType.newBuilder();
        tempVar.setEnumValue(Input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials ToProto(
            ChromeHttpLoginCredentials iron) {
        if (iron == null) {
            return null;
        }

        com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials.Builder proto = com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials.newBuilder();
        proto.setCustomCookies(Render_Converter.ToProto(iron.getCustomCookies()));
        proto.setEnableCookies(iron.isEnableCookies());
        proto.setNetworkPassword(NullGuard(iron.getNetworkPassword()));
        proto.setNetworkUsername(NullGuard(iron.getNetworkUsername()));

        return proto.build();
    }

    static StringDictionary ToProto(HashMap<String, String> dictionary) {
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
