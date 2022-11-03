package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.signature.VerifiedSignature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public final class Signature_Api {

    public static List<VerifiedSignature> getVerifiedSignatures(InternalPdfDocument pdfDocument) {
        RpcClient client = Access.ensureConnection();

        GetVerifiedSignatureRequest.Builder req = GetVerifiedSignatureRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<GetVerifySignatureResult> resultChunks = new ArrayList<>();

        client.stub.pdfDocumentSignatureGetVerifiedSignature(req.build(), new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));


        if (resultChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdf.");
        }

        GetVerifySignatureResult res = resultChunks.stream().findFirst().get();

        return Signature_Converter.fromProto(res);
    }

    public static void signPdfWithSignatureFile(InternalPdfDocument pdfDocument, Signature signature) {
        RpcClient client = Access.ensureConnection();

        SignPdfRequestStream.Info.Builder info = SignPdfRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);

        if (signature.getSigningContact() != null) {
            info.setSigningContact(signature.getSigningContact());
        }

        if (signature.getPassword() != null) {
            info.setPassword(signature.getPassword());
        }

        if (signature.getSigningLocation() != null) {
            info.setSigningLocation(signature.getSigningLocation());
        }

        if (signature.getSigningReason() != null) {
            info.setSigningReason(signature.getSigningReason());
        }

        if (signature.getSignatureDate() != null) {
            info.setSignatureDate(Timestamp.newBuilder().setSeconds(signature.getSignatureDate().getEpochSecond())
                    .setNanos(signature.getSignatureDate().getNano()));
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<EmptyResult> resultChunks = new ArrayList<>();
        io.grpc.stub.StreamObserver<SignPdfRequestStream> requestStream =
                client.stub.pdfDocumentSignatureSignPdf(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        SignPdfRequestStream.Builder infoMsg =
                SignPdfRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<byte[]> it = Utils_Util.chunk(signature.getCertificateRawData()); it.hasNext(); ) {
            byte[] bytes = it.next();
            SignPdfRequestStream.Builder msg = SignPdfRequestStream.newBuilder();
            msg.setCertificateFileBytesChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(msg.build());
        }

        for (Iterator<byte[]> it = Utils_Util.chunk(signature.getSignatureImage()); it.hasNext(); ) {
            byte[] bytes = it.next();
            SignPdfRequestStream.Builder msg = SignPdfRequestStream.newBuilder();
            msg.setSignatureImageChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(msg.build());
        }

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        Utils_Util.handleEmptyResultChunks(resultChunks);
    }

    public static boolean verifyPdfSignatures(InternalPdfDocument pdfDocument){
        RpcClient client = Access.ensureConnection();

        VerifyPdfSignaturesRequest.Builder req = VerifyPdfSignaturesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<BooleanResult> resultChunks = new ArrayList<>();

        client.stub.pdfDocumentSignatureVerifiedSignatures(req.build(), new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));


        if (resultChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdf.");
        }

        BooleanResult res = resultChunks.stream().findFirst().get();

        return Utils_Util.handleBooleanResult(res);
    }

}
