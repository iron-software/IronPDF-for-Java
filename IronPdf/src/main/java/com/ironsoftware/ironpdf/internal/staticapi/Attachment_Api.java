package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public final class Attachment_Api {

    /**
     * Gets collection of attachments contained within a pdf document
     */
    public static List<String> getPdfAttachmentCollection(InternalPdfDocument pdfDocument) {
        RpcClient client = Access.ensureConnection();

        GetPdfAttachmentCollectionRequest.Builder req = GetPdfAttachmentCollectionRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        GetPdfAttachmentCollectionResult res = client.blockingStub.pdfDocumentAttachmentGetPdfAttachmentCollection(
                req.build());

        if (res.getResultOrExceptionCase()
                == GetPdfAttachmentCollectionResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        return res.getResult().getNamesList();
    }

    /**
     * Gets attachment data from attachment name
     *
     * @param name Attachment name
     */
    public static byte[] getPdfAttachmentData(InternalPdfDocument pdfDocument, String name) {
        RpcClient client = Access.ensureConnection();

        GetPdfAttachmentDataRequest.Builder req = GetPdfAttachmentDataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setName(name);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<GetPdfAttachmentDataResultStream> msgChunks = new ArrayList<>();

        client.stub.pdfDocumentAttachmentGetPdfAttachmentData(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch,
                        msgChunks));

        Utils_Util.waitAndCheck(finishLatch, msgChunks);

        List<byte[]> bytesChunks = msgChunks.stream().map(res -> {
                    if (res.getResultOrExceptionCase()
                            == GetPdfAttachmentDataResultStream.ResultOrExceptionCase.EXCEPTION) {
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
     * @param name            Attachment name
     * @param attachmentBytes Attachment data
     */
    public static void addPdfAttachment(InternalPdfDocument pdfDocument, String name,
                                        byte[] attachmentBytes) {
        RpcClient client = Access.ensureConnection();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<EmptyResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<AddPdfAttachmentRequestStream> requestStream =
                client.stub.pdfDocumentAttachmentAddPdfAttachment(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        AddPdfAttachmentRequestStream.Info.Builder info = AddPdfAttachmentRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);
        info.setName(name);

        // sending request
        AddPdfAttachmentRequestStream.Builder infoMsg = AddPdfAttachmentRequestStream.newBuilder();

        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<byte[]> it = Utils_Util.chunk(attachmentBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            AddPdfAttachmentRequestStream.Builder attachmentDataMsg = AddPdfAttachmentRequestStream.newBuilder();

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
     * @param name Attachment name
     */
    public static void removePdfAttachment(InternalPdfDocument pdfDocument, String name) {
        RpcClient client = Access.ensureConnection();

        RemovePdfAttachmentRequest.Builder req = RemovePdfAttachmentRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setName(name);

        EmptyResult res = client.blockingStub.pdfDocumentAttachmentRemovePdfAttachment(
                req.build());

        Utils_Util.handleEmptyResult(res);
    }
}
