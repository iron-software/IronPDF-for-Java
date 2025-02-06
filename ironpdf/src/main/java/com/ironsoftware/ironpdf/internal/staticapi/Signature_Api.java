package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.signature.SignaturePermissions;
import com.ironsoftware.ironpdf.signature.VerifiedSignature;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public final class Signature_Api {

    public static List<VerifiedSignature> getVerifiedSignatures(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<PdfiumGetVerifySignatureResultP> resultChunks = new ArrayList<>();

        PdfiumGetVerifiedSignatureRequestStreamP.InfoP.Builder infoP = PdfiumGetVerifiedSignatureRequestStreamP.InfoP.newBuilder();
        infoP.setDocument(internalPdfDocument.remoteDocument);
        StreamObserver<PdfiumGetVerifiedSignatureRequestStreamP> requestStream = client.GetStub("getVerifiedSignatures").pdfiumSignatureGetVerifiedSignature(new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        requestStream.onNext(PdfiumGetVerifiedSignatureRequestStreamP.newBuilder().setInfo(infoP).build());

        //don't send DataChunk (pdf byte[]) and let Server get the pdf byte[] inside the server to prevent too much grpc call

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        if (resultChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdf.");
        }

        PdfiumGetVerifySignatureResultP res = resultChunks.stream().findFirst().get();

        return Signature_Converter.fromProto(res);
    }

    public static int signPdfWithSignatureFile(InternalPdfDocument internalPdfDocument, Signature signature, SignaturePermissions permissions) {
        RpcClient client = Access.ensureConnection();

        PdfiumSignRequestStreamP.InfoP.Builder info = PdfiumSignRequestStreamP.InfoP.newBuilder();
        info.setDocument(internalPdfDocument.remoteDocument);

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

        info.setSignaturePermission(Signature_Converter.toProto(permissions));

        if(signature.getSignatureImageRectangle() != null){
            info.setSignatureImageW(signature.getSignatureImageRectangle().width);
            info.setSignatureImageH(signature.getSignatureImageRectangle().height);
            info.setSignatureImageX(signature.getSignatureImageRectangle().x);
            info.setSignatureImageY(signature.getSignatureImageRectangle().y);
        }

        if (signature.getTimeStampUrl() != null) {
            info.setTimeStampUrl(signature.getTimeStampUrl());
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<PdfiumSignResultP> resultChunks = new ArrayList<>();
        io.grpc.stub.StreamObserver<PdfiumSignRequestStreamP> requestStream =
                client.GetStub("signPdfWithSignatureFile").pdfiumSignatureSign(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        PdfiumSignRequestStreamP.Builder infoMsg =
                PdfiumSignRequestStreamP.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<byte[]> it = Utils_Util.chunk(signature.getCertificateRawData()); it.hasNext(); ) {
            byte[] bytes = it.next();
            PdfiumSignRequestStreamP.Builder msg = PdfiumSignRequestStreamP.newBuilder();
            msg.setCertificateFileBytesChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(msg.build());
        }

        if(signature.getSignatureImage() != null){
            for (Iterator<byte[]> it = Utils_Util.chunk(signature.getSignatureImage()); it.hasNext(); ) {
                byte[] bytes = it.next();
                PdfiumSignRequestStreamP.Builder msg = PdfiumSignRequestStreamP.newBuilder();
                msg.setSignatureImageChunk(ByteString.copyFrom(bytes));
                requestStream.onNext(msg.build());
            }
        }

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);



        if (resultChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdf.");
        }
        PdfiumSignResultP res = resultChunks.stream().findFirst().get();

        if (res.getResultOrExceptionCase() == PdfiumSignResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }
        return res.getResult();
    }

    public static boolean verifyPdfSignatures(InternalPdfDocument internalPdfDocument){
        List<VerifiedSignature> sig = getVerifiedSignatures(internalPdfDocument);
        boolean result = sig.stream().allMatch(VerifiedSignature::isValid);
        return result;
    }

    public static void removeSignature(InternalPdfDocument internalPdfDocument){
        RpcClient client = Access.ensureConnection();

        PdfiumRemoveSignaturesRequestP.Builder req = PdfiumRemoveSignaturesRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<EmptyResultP> resultChunks = new ArrayList<>();

        client.GetStub("removeSignature").pdfiumSignatureRemoveSignatures(req.build(), new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        if (resultChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdf.");
        }

        EmptyResultP res = resultChunks.stream().findFirst().get();

        Utils_Util.handleEmptyResultChunks(resultChunks);
    }

}
