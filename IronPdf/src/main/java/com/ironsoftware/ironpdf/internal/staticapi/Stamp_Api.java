package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.stamp.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public final class Stamp_Api {

    public static void applyStamp(InternalPdfDocument internalPdfDocument, Stamper stamper,
                                  Iterable<Integer> pageIndexesToStamp) {
        RpcClient client = Access.ensureConnection();

        String stampValue = "";
        byte[] stampImageBytes = {};

        ApplyStampRequestStream.Info.Builder info = ApplyStampRequestStream.Info.newBuilder();

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
        if (pageIndexesToStamp != null) {
            info.addAllTargetPagesIndexes(pageIndexesToStamp);
        }

        if (stamper instanceof HtmlStamper) {
            HtmlStamper htmlStamper = (HtmlStamper) stamper;
            HtmlStamperInfo.Builder htmlStamperInfo = HtmlStamperInfo.newBuilder();
            htmlStamperInfo.setCssMediaType(Render_Converter.toProto(htmlStamper.getCssMediaType()));
            htmlStamperInfo.setTimeout(htmlStamper.getTimeout());
            htmlStamperInfo.setRenderDelay(htmlStamper.getRenderDelay());
            if (htmlStamper.getHtmlBaseUrl() != null) {
                htmlStamperInfo.setBaseUrl(htmlStamper.getHtmlBaseUrl());
            }
            info.setHtmlStamper(htmlStamperInfo);
            stampValue = htmlStamper.getHtml();
        } else if (stamper instanceof TextStamper) {
            TextStamperInfo.Builder textStamperInfo = TextStamperInfo.newBuilder();
            textStamperInfo.setFontFamily(((TextStamper) stamper).getFontFamily());
            textStamperInfo.setFontSize(((TextStamper) stamper).getFontSize());
            textStamperInfo.setIsBold(((TextStamper) stamper).isBold());
            textStamperInfo.setIsItalic(((TextStamper) stamper).isItalic());
            textStamperInfo.setIsUnderline(((TextStamper) stamper).isUnderline());
            textStamperInfo.setIsStrikethrough(((TextStamper) stamper).isStrikethrough());
            textStamperInfo.setUseGoogleFont(((TextStamper) stamper).isUseGoogleFont());
            textStamperInfo.setTextColor(((TextStamper) stamper).getFontColorCode());
            textStamperInfo.setBackgroundColor(((TextStamper) stamper).getBackgroundColorCode());

            info.setTextStamper(textStamperInfo);
            stampValue = ((TextStamper) stamper).getText();
        } else if (stamper instanceof BarcodeStamper) {
            BarcodeStamperInfo.Builder barcodeStamperInfo = BarcodeStamperInfo.newBuilder();
            barcodeStamperInfo.setBarcodeType(
                    Stamp_Converter.toProto(((BarcodeStamper) stamper).getBarcodeType()));
            barcodeStamperInfo.setWidthPx(((BarcodeStamper) stamper).getWidth());
            barcodeStamperInfo.setHeightPx(((BarcodeStamper) stamper).getHeight());
            info.setBarcodeStamper(barcodeStamperInfo);
            stampValue = (((BarcodeStamper) stamper).getValue());
        } else if (stamper instanceof ImageStamper) {
            ImageStamperInfo.Builder imageStamperInfo = ImageStamperInfo.newBuilder();
            info.setImageStamper(imageStamperInfo);
            stampImageBytes = ((ImageStamper) stamper).getImageData();
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<EmptyResult> resultChunks = new ArrayList<>();
        io.grpc.stub.StreamObserver<ApplyStampRequestStream> requestStream =
                client.stub.pdfDocumentStampApplyStamp(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        ApplyStampRequestStream.Builder infoMsg =
                ApplyStampRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<char[]> it = Utils_Util.chunk(stampValue.toCharArray()); it.hasNext(); ) {
            char[] chars = it.next();
            ApplyStampRequestStream.Builder msg = ApplyStampRequestStream.newBuilder();
            msg.setStampValue(String.valueOf(chars));
            requestStream.onNext(msg.build());
        }

        for (Iterator<byte[]> it = Utils_Util.chunk(stampImageBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            ApplyStampRequestStream.Builder msg = ApplyStampRequestStream.newBuilder();
            msg.setStampImageBytes(ByteString.copyFrom(bytes));
            requestStream.onNext(msg.build());
        }

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        Utils_Util.handleEmptyResultChunks(resultChunks);
    }
}
