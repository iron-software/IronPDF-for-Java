package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.stamp.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.*;

public final class Stamp_Api {

    public static void ApplyStamp(InternalPdfDocument pdfDocument, Stamper stamper,
                                  Iterable<Integer> pageIndexesToStamp) throws IOException {
        RpcClient client = Access.EnsureConnection();

        String stampValue = "";

        ApplyStampRequestStream.Info.Builder info = ApplyStampRequestStream.Info.newBuilder();

        info.setDocument(pdfDocument.remoteDocument);
        info.setOpacity(stamper.getOpacity());
        info.setRotation(stamper.getRotation());
        info.setHorizontalAlignment(Stamp_Converter.ToProto(stamper.getHorizontalAlignment()));
        info.setVerticalAlignment(Stamp_Converter.ToProto(stamper.getVerticalAlignment()));
        info.setHorizontalOffset(Stamp_Converter.ToProto(stamper.getHorizontalOffset()));
        info.setVerticalOffset(Stamp_Converter.ToProto(stamper.getVerticalOffset()));
        info.setScale(stamper.getScale());
        info.setIsStampBehindContent(stamper.isStampBehindContent());

        if (stamper.getMaxWidth() != null) {
            info.setMaxWidth(Stamp_Converter.ToProto(stamper.getMaxWidth()));
        }

        if (stamper.getMaxHeight() != null) {
            info.setMaxHeight(Stamp_Converter.ToProto(stamper.getMaxHeight()));
        }

        if (stamper.getMinWidth() != null) {
            info.setMinWidth(Stamp_Converter.ToProto(stamper.getMinWidth()));
        }

        if (stamper.getMinHeight() != null) {
            info.setMinHeight(Stamp_Converter.ToProto(stamper.getMinHeight()));
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
            htmlStamperInfo.setCssMediaType(Render_Converter.ToProto(htmlStamper.getCssMediaType()));
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
            textStamperInfo.setUseGoogleFont(((TextStamper) stamper).getUseGoogleFont());
            info.setTextStamper(textStamperInfo);
            stampValue = ((TextStamper) stamper).getText();
        } else if (stamper instanceof BarcodeStamper) {
            BarcodeStamperInfo.Builder barcodeStamperInfo = BarcodeStamperInfo.newBuilder();
            barcodeStamperInfo.setBarcodeType(
                    Stamp_Converter.ToProto(((BarcodeStamper) stamper).getBarcodeType()));
            barcodeStamperInfo.setWidthPx(((BarcodeStamper) stamper).getWidth());
            barcodeStamperInfo.setHeightPx(((BarcodeStamper) stamper).getHeight());
            info.setBarcodeStamper(barcodeStamperInfo);
            stampValue = (((BarcodeStamper) stamper).getValue());
        } else if (stamper instanceof ImageStamper) {
            ImageStamperInfo.Builder imageStamperInfo = ImageStamperInfo.newBuilder();
            info.setImageStamper(imageStamperInfo);
            stampValue = ((ImageStamper) stamper).getImageUri().toAbsolutePath().toString();
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<EmptyResult> resultChunks = new ArrayList<>();
        io.grpc.stub.StreamObserver<ApplyStampRequestStream> requestStream =
                client.Stub.pdfDocumentStampApplyStamp(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        ApplyStampRequestStream.Builder infoMsg =
                ApplyStampRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<char[]> it = Chunk(stampValue.toCharArray()); it.hasNext(); ) {
            char[] chars = it.next();
            ApplyStampRequestStream.Builder msg = ApplyStampRequestStream.newBuilder();
            msg.setStampValue(String.valueOf(chars));
            requestStream.onNext(msg.build());
        }

        requestStream.onCompleted();

        WaitAndCheck(finishLatch, resultChunks);

        HandleEmptyResultChunks(resultChunks);
    }
}
