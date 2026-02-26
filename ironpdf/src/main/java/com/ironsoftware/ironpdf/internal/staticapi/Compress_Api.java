package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.BytesResultStreamP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumCompressImagesRequestP;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumRemoveStructTreeRequestP;
import com.ironsoftware.ironpdf.internal.proto.QPdfCompressInMemoryRequestIdP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * The type Compress api.
 */
public final class Compress_Api {

    /**
     * Compress existing images using JPG encoding and the specified settings
     *
     * @param internalPdfDocument the internal pdf document
     * @param quality             (1 - 100) to use during compression
     * @param scaleToVisibleSize  Scale down the image resolution according to its visible size in the PDF document; may cause distortion with some image configurations. Default is false.
     */
    public static void compressImages(InternalPdfDocument internalPdfDocument, int quality,
                                      boolean scaleToVisibleSize) {
        RpcClient client = Access.ensureConnection();

        PdfiumCompressImagesRequestP.Builder req = PdfiumCompressImagesRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setQuality(quality);
        req.setScaleToVisibleSize(scaleToVisibleSize);

        EmptyResultP res = client.GetBlockingStub("compressImages").pdfiumCompressCompressImages(req.build());

        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Compress the PDF in memory using QPdf smart compression and return the compressed bytes.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the compressed pdf byte array
     */
    public static byte[] compressInMemory(InternalPdfDocument internalPdfDocument) {
        return compressInMemory(internalPdfDocument, "");
    }

    /**
     * Compress the PDF in memory using QPdf smart compression and return the compressed bytes.
     *
     * @param internalPdfDocument the internal pdf document
     * @param password            the pdf password (empty string if none)
     * @return the compressed pdf byte array
     */
    public static byte[] compressInMemory(InternalPdfDocument internalPdfDocument, String password) {
        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();

        QPdfCompressInMemoryRequestIdP.Builder req = QPdfCompressInMemoryRequestIdP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPassword(password);

        client.GetStub("compressInMemory").qPdfCompressionCompressInMemoryFromId(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handleByteChunks(resultChunks);
    }

    public static void compressStructTree(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();

        PdfiumRemoveStructTreeRequestP.Builder req = PdfiumRemoveStructTreeRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        EmptyResultP res = client.GetBlockingStub("compressStructTree").pdfiumCompressRemoveStructTree(req.build());

        Utils_Util.handleEmptyResult(res);
    }


}
