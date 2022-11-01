package com.ironsoftware.ironpdf.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.*;

public final class Attachment_Api {

    /**
     * Gets collection of attachments contained within a pdf document
     */
    public static Iterator<String> GetPdfAttachmentCollection(InternalPdfDocument pdfDocument)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        GetPdfAttachmentCollectionRequest.Builder req = GetPdfAttachmentCollectionRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        GetPdfAttachmentCollectionResult res = client.BlockingStub.pdfDocumentAttachmentGetPdfAttachmentCollection(
                req.build());

        if (res.getResultOrExceptionCase()
                == GetPdfAttachmentCollectionResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(res.getException());
        }

        return res.getResult().getNamesList().listIterator();
    }

    /**
     * Gets attachment data from attachment name
     *
     * @param name Attachment name
     */
    public static byte[] GetPdfAttachmentData(InternalPdfDocument pdfDocument, String name)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        GetPdfAttachmentDataRequest.Builder req = GetPdfAttachmentDataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setName(name);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<GetPdfAttachmentDataResultStream> msgChunks = new ArrayList<>();

        client.Stub.pdfDocumentAttachmentGetPdfAttachmentData(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch,
                        msgChunks));

        WaitAndCheck(finishLatch, msgChunks);

        List<byte[]> bytesChunks = msgChunks.stream().map(res -> {
                    if (res.getResultOrExceptionCase()
                            == GetPdfAttachmentDataResultStream.ResultOrExceptionCase.EXCEPTION) {
                        throw Exception_Converter.FromProto(res.getException());
                    }
                    return res.getResultChunk().toByteArray();
                }
        ).collect(Collectors.toList());

        return CombineChunk(bytesChunks);
    }

    /**
     * Add new attachment
     *
     * @param name            Attachment name
     * @param attachmentBytes Attachment data
     */
    public static void AddPdfAttachment(InternalPdfDocument pdfDocument, String name,
                                        byte[] attachmentBytes) throws IOException {
        RpcClient client = Access.EnsureConnection();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<EmptyResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<AddPdfAttachmentRequestStream> requestStream =
                client.Stub.pdfDocumentAttachmentAddPdfAttachment(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        AddPdfAttachmentRequestStream.Info.Builder info = AddPdfAttachmentRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);
        info.setName(name);

        // sending request
        AddPdfAttachmentRequestStream.Builder infoMsg = AddPdfAttachmentRequestStream.newBuilder();

        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<byte[]> it = Chunk(attachmentBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            AddPdfAttachmentRequestStream.Builder attachmentDataMsg = AddPdfAttachmentRequestStream.newBuilder();

            attachmentDataMsg.setAttachmentChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(attachmentDataMsg.build());
        }

        //finish sending
        requestStream.onCompleted();

        WaitAndCheck(finishLatch, resultChunks);

        HandleEmptyResultChunks(resultChunks);
    }

    /**
     * Remove attachment by attachment name
     *
     * @param name Attachment name
     */
    public static void RemovePdfAttachment(InternalPdfDocument pdfDocument, String name)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        RemovePdfAttachmentRequest.Builder req = RemovePdfAttachmentRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setName(name);

        EmptyResult res = client.BlockingStub.pdfDocumentAttachmentRemovePdfAttachment(
                req.build());

        HandleEmptyResult(res);
    }
}
