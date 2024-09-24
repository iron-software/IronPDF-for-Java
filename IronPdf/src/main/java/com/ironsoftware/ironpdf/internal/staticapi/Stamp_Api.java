package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.stamp.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public final class Stamp_Api {

    public static void applyStamp(InternalPdfDocument internalPdfDocument, Stamper stamper,
                                  List<PageInfo> pageInfosToStamp) {
        RpcClient client = Access.ensureConnection();

        String stampValue = "";
        byte[] stampImageBytes = {};

        ChromeApplyStampRequestStreamP.InfoP.Builder info = ChromeApplyStampRequestStreamP.InfoP.newBuilder();

        info.setDocument(internalPdfDocument.remoteDocument);
        info.setOpacity(stamper.getOpacity());
        info.setRotation(stamper.getRotation());
        info.setHorizontalAlignment(Stamp_Converter.toProto(stamper.getHorizontalAlignment()));
        info.setVerticalAlignment(Stamp_Converter.toProto(stamper.getVerticalAlignment()));
        info.setHorizontalOffset(Stamp_Converter.toProto(stamper.getHorizontalOffset()));
        info.setVerticalOffset(Stamp_Converter.toProto(stamper.getVerticalOffset()));
        info.setScale(stamper.getScale());
        info.setIsStampBehindContent(stamper.isStampBehindContent());

        if (stamper.getMaxWidth() != null) {
            info.setMaxWidth(Stamp_Converter.toProto(stamper.getMaxWidth()));
        }

        if (stamper.getMaxHeight() != null) {
            info.setMaxHeight(Stamp_Converter.toProto(stamper.getMaxHeight()));
        }

        if (stamper.getMinWidth() != null) {
            info.setMinWidth(Stamp_Converter.toProto(stamper.getMinWidth()));
        }

        if (stamper.getMinHeight() != null) {
            info.setMinHeight(Stamp_Converter.toProto(stamper.getMinHeight()));
        }

        if (stamper.getHyperlink() != null) {
            info.setHyperlink(stamper.getHyperlink());
        }

        if (pageInfosToStamp != null) {
            info.addAllTargetPages(pageInfosToStamp.stream().map(Page_Converter::toProto).collect(Collectors.toList()));
        }

        info.setTimeout(stamper.getWaitFor().getTimeout());
        info.setRenderDelay(stamper.getWaitFor().getRenderDelayDuration());

        if (stamper instanceof HtmlStamper) {
            HtmlStamper htmlStamper = (HtmlStamper) stamper;
            ChromeHtmlStamperInfoP.Builder chromeHtmlStamperInfoP = ChromeHtmlStamperInfoP.newBuilder();
            chromeHtmlStamperInfoP.setCssMediaType(Render_Converter.toProto(htmlStamper.getCssMediaType()));
            if (htmlStamper.getHtmlBaseUrl() != null) {
                chromeHtmlStamperInfoP.setBaseUrl(htmlStamper.getHtmlBaseUrl());
            }
            info.setHtmlStamper(chromeHtmlStamperInfoP);
            stampValue = htmlStamper.getHtml();
        } else if (stamper instanceof TextStamper) {
            ChromeTextStamperInfoP.Builder textStamperInfoP = ChromeTextStamperInfoP.newBuilder();
            textStamperInfoP.setFontFamily(((TextStamper) stamper).getFontFamily());
            textStamperInfoP.setFontSize(((TextStamper) stamper).getFontSize());
            textStamperInfoP.setIsBold(((TextStamper) stamper).isBold());
            textStamperInfoP.setIsItalic(((TextStamper) stamper).isItalic());
            textStamperInfoP.setIsUnderline(((TextStamper) stamper).isUnderline());
            textStamperInfoP.setIsStrikethrough(((TextStamper) stamper).isStrikethrough());
            textStamperInfoP.setUseGoogleFont(((TextStamper) stamper).isUseGoogleFont());
            textStamperInfoP.setTextColor(((TextStamper) stamper).getFontColorCode());
            textStamperInfoP.setBackgroundColor(((TextStamper) stamper).getBackgroundColorCode());

            info.setTextStamper(textStamperInfoP);
            stampValue = ((TextStamper) stamper).getText();
        } else if (stamper instanceof BarcodeStamper) {
            ChromeBarcodeStamperInfoP.Builder barcodeStamperInfo = ChromeBarcodeStamperInfoP.newBuilder();
            barcodeStamperInfo.setBarcodeType(
                    Stamp_Converter.toProto(((BarcodeStamper) stamper).getBarcodeType()));
            barcodeStamperInfo.setWidthPx(((BarcodeStamper) stamper).getWidth());
            barcodeStamperInfo.setHeightPx(((BarcodeStamper) stamper).getHeight());
            info.setBarcodeStamper(barcodeStamperInfo);
            stampValue = (((BarcodeStamper) stamper).getValue());
        } else if (stamper instanceof ImageStamper) {
            ChromeImageStamperInfoP.Builder imageStamperInfoP = ChromeImageStamperInfoP.newBuilder();
            info.setImageStamper(imageStamperInfoP);
            stampImageBytes = ((ImageStamper) stamper).getImageData();
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<EmptyResultP> resultChunks = new ArrayList<>();
        io.grpc.stub.StreamObserver<ChromeApplyStampRequestStreamP> requestStream =
                client.GetStub("applyStamp").chromeStampApplyStamp(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        ChromeApplyStampRequestStreamP.Builder infoMsg =
                ChromeApplyStampRequestStreamP.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<char[]> it = Utils_Util.chunk(stampValue.toCharArray()); it.hasNext(); ) {
            char[] chars = it.next();
            ChromeApplyStampRequestStreamP.Builder msg = ChromeApplyStampRequestStreamP.newBuilder();
            msg.setStampValue(String.valueOf(chars));
            requestStream.onNext(msg.build());
        }

        for (Iterator<byte[]> it = Utils_Util.chunk(stampImageBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            ChromeApplyStampRequestStreamP.Builder msg = ChromeApplyStampRequestStreamP.newBuilder();
            msg.setStampImageBytes(ByteString.copyFrom(bytes));
            requestStream.onNext(msg.build());
        }

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        Utils_Util.handleEmptyResultChunks(resultChunks);
    }
}
