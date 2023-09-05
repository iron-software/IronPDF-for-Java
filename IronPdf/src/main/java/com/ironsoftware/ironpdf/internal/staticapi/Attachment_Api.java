package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The type Attachment api.
 */
public final class Attachment_Api {

    /**
     * Gets collection of attachments contained within a pdf document
     *
     * @param internalPdfDocument the internal pdf document
     * @return the pdf attachment collection
     */
    public static List<String> getPdfAttachmentCollection(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();

        PdfiumGetPdfAttachmentCountResultP countResultP = client.blockingStub.pdfiumAttachmentGetPdfAttachmentCount(PdfiumGetPdfAttachmentCountRequestP.newBuilder().setDocument(internalPdfDocument.remoteDocument).build());

        if (countResultP.hasException()) {
            throw Exception_Converter.fromProto(countResultP.getException());
        }

        return IntStream.range(0, countResultP.getResult()).mapToObj(attachmentIndex -> {
            PdfiumGetPdfAttachmentNameRequestP.Builder req = PdfiumGetPdfAttachmentNameRequestP.newBuilder();
            req.setDocument(internalPdfDocument.remoteDocument);
            req.setIndex(attachmentIndex);
            PdfiumGetPdfAttachmentNameResultP res = client.blockingStub.pdfiumAttachmentGetPdfAttachmentName(
                    req.build());

            if (res.getResultOrExceptionCase()
                    == PdfiumGetPdfAttachmentNameResultP.ResultOrExceptionCase.EXCEPTION) {
                throw Exception_Converter.fromProto(res.getException());
            }
            return res.getResult();
        }).collect(Collectors.toList());
    }

    /**
     * Gets attachment data from attachment name
     *
     * @param internalPdfDocument the internal pdf document
     * @param name                Attachment name
     * @return the byte [ ]
     */
    public static byte[] getPdfAttachmentData(InternalPdfDocument internalPdfDocument, String name) {

        int index = getPdfAttachmentCollection(internalPdfDocument).indexOf(name);

        if (index == -1) {
            throw new RuntimeException(String.format("not found Attachment name: %s", name));
        }

        return getPdfAttachmentData(internalPdfDocument, index);
    }

    /**
     * Gets attachment data from attachment index
     *
     * @param internalPdfDocument the internal pdf document
     * @param index               Attachment index
     * @return the byte [ ]
     */
    public static byte[] getPdfAttachmentData(InternalPdfDocument internalPdfDocument, int index) {
        RpcClient client = Access.ensureConnection();

        PdfiumGetPdfAttachmentDataRequestP.Builder req = PdfiumGetPdfAttachmentDataRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setIndex(index);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<PdfiumGetPdfAttachmentDataResultStreamP> msgChunks = new ArrayList<>();

        client.stub.pdfiumAttachmentGetPdfAttachmentData(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch,
                        msgChunks));

        Utils_Util.waitAndCheck(finishLatch, msgChunks);

        List<byte[]> bytesChunks = msgChunks.stream().map(res -> {
                    if (res.getResultOrExceptionCase()
                            == PdfiumGetPdfAttachmentDataResultStreamP.ResultOrExceptionCase.EXCEPTION) {
                        throw Exception_Converter.fromProto(res.getException());
                    }
                    return res.getResultChunk().toByteArray();
                }
        ).collect(Collectors.toList());

        return Utils_Util.combineChunk(bytesChunks);
    }

    /**
     * Add a new attachment
     *
     * @param internalPdfDocument the internal pdf document
     * @param name                Attachment name
     * @param attachmentBytes     Attachment data
     */
    public static void addPdfAttachment(InternalPdfDocument internalPdfDocument, String name,
                                        byte[] attachmentBytes) {
        RpcClient client = Access.ensureConnection();

        PdfiumAddPdfAttachmentResultP addPdfAttachmentResultP = client.blockingStub.pdfiumAttachmentAddPdfAttachment(PdfiumAddPdfAttachmentRequestP.newBuilder().setDocument(internalPdfDocument.remoteDocument).setName(name).build());

        if (addPdfAttachmentResultP.hasException()) {
            throw Exception_Converter.fromProto(addPdfAttachmentResultP.getException());
        }

        int attachmentIndex = addPdfAttachmentResultP.getResult();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<EmptyResultP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<PdfiumSetPdfAttachmentDataRequestStreamP> requestStream =
                client.stub.pdfiumAttachmentSetPdfAttachmentData(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        PdfiumSetPdfAttachmentDataRequestStreamP.InfoP.Builder info = PdfiumSetPdfAttachmentDataRequestStreamP.InfoP.newBuilder();
        info.setDocument(internalPdfDocument.remoteDocument);
        info.setIndex(attachmentIndex);

        // sending request
        PdfiumSetPdfAttachmentDataRequestStreamP.Builder infoMsg = PdfiumSetPdfAttachmentDataRequestStreamP.newBuilder();

        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<byte[]> it = Utils_Util.chunk(attachmentBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            PdfiumSetPdfAttachmentDataRequestStreamP.Builder attachmentDataMsg = PdfiumSetPdfAttachmentDataRequestStreamP.newBuilder();

            attachmentDataMsg.setAttachmentChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(attachmentDataMsg.build());
        }

        //finish sending
        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        Utils_Util.handleEmptyResultChunks(resultChunks);
    }


    /**
     * Remove attachment by attachment name
     *
     * @param internalPdfDocument the internal pdf document
     * @param name                Attachment name
     */
    public static void removePdfAttachment(InternalPdfDocument internalPdfDocument, String name) {
        int index = getPdfAttachmentCollection(internalPdfDocument).indexOf(name);

        if (index == -1) {
            throw new RuntimeException(String.format("not found Attachment name: %s", name));
        }

        removePdfAttachment(internalPdfDocument, index);
    }

    /**
     * Remove attachment by attachment index
     *
     * @param internalPdfDocument the internal pdf document
     * @param index               Attachment index
     */
    public static void removePdfAttachment(InternalPdfDocument internalPdfDocument, int index) {
        RpcClient client = Access.ensureConnection();

        PdfiumRemovePdfAttachmentRequestP.Builder req = PdfiumRemovePdfAttachmentRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setIndex(index);

        EmptyResultP res = client.blockingStub.pdfiumAttachmentRemovePdfAttachment(
                req.build());

        Utils_Util.handleEmptyResult(res);
    }
}
