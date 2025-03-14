package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.PdfiumCompressImagesRequestP;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumRemoveStructTreeRequestP;

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

    public static void compressStructTree(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();

        PdfiumRemoveStructTreeRequestP.Builder req = PdfiumRemoveStructTreeRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        EmptyResultP res = client.GetBlockingStub("compressStructTree").pdfiumCompressRemoveStructTree(req.build());

        Utils_Util.handleEmptyResult(res);
    }


}
