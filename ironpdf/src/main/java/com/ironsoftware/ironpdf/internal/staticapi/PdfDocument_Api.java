package com.ironsoftware.ironpdf.internal.staticapi;


import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.ChangeTrackingModes;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.standard.PdfAVersions;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The type Pdf document api.
 */
public final class PdfDocument_Api {
    /**
     * The Logger.
     */
    static final Logger logger = LoggerFactory.getLogger(PdfDocument.class);

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException Exception thrown if it can not be opened.
     */
    public static InternalPdfDocument fromFile(String pdfFilePath) throws IOException {
        return fromFile(pdfFilePath, null, null, ChangeTrackingModes.AUTO_CHANGE_TRACKING);
    }


    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @param password    Optional user password if the PDF document is encrypted.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException Exception thrown if it can not be opened.
     */
    public static InternalPdfDocument fromFile(String pdfFilePath, String password)
            throws IOException {
        return fromFile(pdfFilePath, password, null, ChangeTrackingModes.AUTO_CHANGE_TRACKING);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath   The PDF file path.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,                      modifying restrictions etc..)
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException Exception thrown if it can not be opened.
     */
    public static InternalPdfDocument fromFile(String pdfFilePath, String password,
                                               String ownerPassword, ChangeTrackingModes trackChanges) throws IOException {
        if (Utils_StringHelper.isNullOrWhiteSpace(pdfFilePath)) {
            throw new IllegalArgumentException("Value 'pdfFilePath' cannot be null or empty.");
        }

        String absoluteFilePath = (new File(pdfFilePath)).getAbsolutePath();
        if (!(new File(absoluteFilePath)).isFile()) {
            throw new IOException(
                    String.format("%1$s is not a valid PDF file path. That file does not exist.",
                            absoluteFilePath));
        }

        byte[] pdfData = Files.readAllBytes(Paths.get(pdfFilePath));
        if (pdfData.length == 0) {
            throw new IOException(String.format(
                    "PdfData Stream is null or empty.  Can not create a PDF document from invalid data. %1$s",
                    pdfFilePath));
        }

        return fromBytes(pdfData, password, ownerPassword, trackChanges);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFileBytes  The PDF file data as byte array.
     * @param userPassword  Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,                      modifying restrictions etc..)
     * @return the internal pdf document
     */
    public static InternalPdfDocument fromBytes(byte[] pdfFileBytes, String userPassword,
                                                String ownerPassword, ChangeTrackingModes trackChanges) {
        RpcClient client = Access.ensureConnection();
        logger.info("open PDF");
        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResultP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<PdfiumPdfDocumentConstructorStreamP> requestStream =
                client.GetStub("fromBytes").pdfiumFromBytes(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        // sending request
        PdfiumPdfDocumentConstructorStreamP.Builder pdfDocumentConstructor_info = PdfiumPdfDocumentConstructorStreamP.newBuilder();

        PdfiumPdfDocumentConstructorStreamP.InfoP.Builder info = PdfiumPdfDocumentConstructorStreamP.InfoP.newBuilder();

        if (!Utils_StringHelper.isNullOrWhiteSpace(userPassword) || !Utils_StringHelper.isNullOrWhiteSpace(ownerPassword)) {

            info.setUserPassword(!Utils_StringHelper.isNullOrWhiteSpace(userPassword) ? userPassword : "");

            info.setOwnerPassword(!Utils_StringHelper.isNullOrWhiteSpace(ownerPassword) ? ownerPassword : "");
        }
        info.setTrackChanges(trackChanges.ordinal());
        pdfDocumentConstructor_info.setInfo(info);
        requestStream.onNext(pdfDocumentConstructor_info.build());

        for (Iterator<byte[]> it = Utils_Util.chunk(pdfFileBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            PdfiumPdfDocumentConstructorStreamP.Builder pdfDocumentConstructor_data = PdfiumPdfDocumentConstructorStreamP.newBuilder();

            pdfDocumentConstructor_data.setPdfBytesChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(pdfDocumentConstructor_data.build());
        }

        //finish sending
        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        InternalPdfDocument doc = Utils_Util.handlePdfDocumentChunks(resultChunks);
        doc.userPassword = userPassword;
        doc.ownerPassword = ownerPassword;
        return doc;
    }

    /**
     * Save as.
     *
     * @param internalPdfDocument the internal pdf document
     * @param filePath            the file path
     * @throws IOException the io exception
     */
    public static void saveAs(InternalPdfDocument internalPdfDocument, String filePath) throws IOException {
        byte[] data = PdfDocument_Api.getBytes(internalPdfDocument, false);
        saveAs(data, filePath);
    }

    /**
     * Save as.
     * Saves current changes as a revision and returns the revised the document, optionally also saving the document to disk
     *
     * @param internalPdfDocument the internal pdf document
     * @param filePath            the file path
     * @throws IOException the io exception
     */
    public static void saveAsRevision(InternalPdfDocument internalPdfDocument, String filePath) throws IOException {
        byte[] data = PdfDocument_Api.getBytes(internalPdfDocument, true);
        saveAs(data, filePath);
    }

    /**
     * Gets the binary data for the full PDF file as a byte array.
     *
     * @param internalPdfDocument the internal pdf document
     * @param isIncremental       isIncremental
     * @return the pdf byte array
     */
    public static byte[] getBytes(InternalPdfDocument internalPdfDocument, boolean isIncremental) {
        RpcClient client = Access.ensureConnection();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        List<BytesResultStreamP> resultChunks = new ArrayList<>();

        PdfiumGetBinaryDataRequestStreamP.InfoP.Builder infoP = PdfiumGetBinaryDataRequestStreamP.InfoP.newBuilder();
        infoP.setDocument(internalPdfDocument.remoteDocument);
        infoP.setIsIncremental(isIncremental);

        StreamObserver<PdfiumGetBinaryDataRequestStreamP> requestStream = client.GetStub("getBytes").pdfiumGetBinaryData(
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );

        requestStream.onNext(PdfiumGetBinaryDataRequestStreamP.newBuilder().setInfo(infoP).build());
        PdfiumPdfSignatureCollectionP.Builder collectionP = PdfiumPdfSignatureCollectionP.newBuilder();

        // sigObjIndex != Signature.InternalIndex
        for (int sigObjIndex : IntStream.range(0, internalPdfDocument.signatures.size()).toArray()) {
            Signature sigObj = internalPdfDocument.signatures.get(sigObjIndex);

            for (Iterator<byte[]> it = Utils_Util.chunk(sigObj.getCertificateRawData()); it.hasNext(); ) {
                byte[] chunk = it.next();
                PdfiumGetBinaryDataRequestStreamP.Builder msg = PdfiumGetBinaryDataRequestStreamP.newBuilder();
                msg.setRawSignaturesChunk(PdfiumRawSignatureChunkWithIndexP.newBuilder()
                        .setRawSignatureChunk(ByteString.copyFrom(chunk))
                        .setSignatureIndex(sigObjIndex).build());
                requestStream.onNext(msg.build());
            }

            PdfiumPdfSignatureP signatureP = Signature_Converter.toProto(sigObj);
            collectionP.addSignature(signatureP);
        }

        requestStream.onNext(PdfiumGetBinaryDataRequestStreamP.newBuilder().setSignatures(collectionP).build());

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handleByteChunks(resultChunks);
    }

    /**
     * Gets the binary data for the full PDF file as a byte array.
     *
     * @param internalPdfDocument the internal pdf document
     * @param index               revision index
     * @return the internal pdf document
     */
    public static byte[] getRevision(InternalPdfDocument internalPdfDocument, int index) {
        RpcClient client = Access.ensureConnection();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<BytesResultStreamP> resultChunks = new ArrayList<>();

        PdfiumGetRevisionRequestP.Builder req = PdfiumGetRevisionRequestP.newBuilder();
        req.setIndex(index);
        req.setDocument(internalPdfDocument.remoteDocument);

        client.GetStub("getRevision").pdfiumGetRevision(req.build(),
                //response handler
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handleByteChunks(resultChunks);
    }

    /**
     * Save as.
     *
     * @param pdfData  the pdf data
     * @param filePath the file path
     * @throws IOException the io exception
     */
    public static void saveAs(byte[] pdfData, String filePath) throws IOException {
        logger.info("save PDF to file: " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null) {
                Files.createDirectories(parent.toPath());
            }
        }
        try (FileOutputStream stream = new FileOutputStream(filePath)) {
            stream.write(pdfData);
        }
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFileBytes The PDF file data as byte array.
     * @param userPassword Optional user password if the PDF document is encrypted.
     * @return the internal pdf document
     */
    public static InternalPdfDocument fromBytes(byte[] pdfFileBytes, String userPassword) {
        return fromBytes(pdfFileBytes, userPassword, null, ChangeTrackingModes.AUTO_CHANGE_TRACKING);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFileBytes The PDF file data as byte array.
     * @return the internal pdf document
     */
    public static InternalPdfDocument fromBytes(byte[] pdfFileBytes) {
        return fromBytes(pdfFileBytes, null, null, ChangeTrackingModes.AUTO_CHANGE_TRACKING);
    }

    public static InternalPdfDocument toPdfA(InternalPdfDocument internalPdfDocument, byte[] customICCFileBytes, PdfAVersions pdfAVersion) {
        RpcClient client = Access.ensureConnection();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResultP> resultChunks = new ArrayList<>();

        int convtVer = 3; // default
        boolean isAVariant = true; // default
        switch (pdfAVersion){
            case PdfA1b:
                convtVer = 1;
                isAVariant = false;
                break;
            case PdfA2b:
                convtVer = 2;
                isAVariant = false;
                break;
            case PdfA3b:
                convtVer = 3;
                isAVariant = false;
                break;
            case PdfA1a:
                convtVer = 1;
                isAVariant = true;
                break;
            case PdfA2a:
                convtVer = 2;
                isAVariant = true;
                break;
            case PdfA3a:
                convtVer = 3;
                isAVariant = true;
                break;
        }

        PdfiumConvertToPdfARequestStreamP.InfoP.Builder infoP = PdfiumConvertToPdfARequestStreamP.InfoP.newBuilder();
        infoP.setDocument(internalPdfDocument.remoteDocument);
        infoP.setConvtVer(convtVer);
        infoP.setIsAVariant(isAVariant);
        PdfiumConvertToPdfARequestStreamP.Builder req = PdfiumConvertToPdfARequestStreamP.newBuilder();
        req.setInfo(infoP);


        StreamObserver<PdfiumConvertToPdfARequestStreamP> requestStream = client.GetStub("toPdfA").pdfiumConvertToPdfA(
                //response handler
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );

        requestStream.onNext(req.build());

        for (Iterator<byte[]> it = Utils_Util.chunk(customICCFileBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            PdfiumConvertToPdfARequestStreamP.Builder pdfDocumentConstructor_data = PdfiumConvertToPdfARequestStreamP.newBuilder();

            pdfDocumentConstructor_data.setIccBytesChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(pdfDocumentConstructor_data.build());
        }

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        InternalPdfDocument doc = Utils_Util.handlePdfDocumentChunks(resultChunks);
        doc.userPassword = internalPdfDocument.userPassword;
        doc.ownerPassword = internalPdfDocument.ownerPassword;
        return doc;
    }

    public static InternalPdfDocument toPdfUA(InternalPdfDocument internalPdfDocument, int naturalLanguages) {
        RpcClient client = Access.ensureConnection();

        PdfiumConvertToPdfUARequestP.Builder req = PdfiumConvertToPdfUARequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setLang(naturalLanguages);

        EmptyResultP res = client.GetBlockingStub("toPdfUA").pdfiumConvertToPdfUA(req.build());

        Utils_Util.handleEmptyResult(res);

        return internalPdfDocument;
    }
}
