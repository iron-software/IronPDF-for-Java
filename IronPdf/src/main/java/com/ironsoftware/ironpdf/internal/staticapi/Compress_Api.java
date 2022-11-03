package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.CompressImagesRequest;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;

public final class Compress_Api {

    /**
     * Compress existing images using JPG encoding and the specified settings
     *
     * @param quality            (1 - 100) to use during compression
     * @param scaleToVisibleSize Scale down the image resolution according to its visible size in the
     *                           PDF document; may cause distortion with some image configurations.
     *                           Default is false.
     */
    public static void compressImages(InternalPdfDocument pdfDocument, int quality,
                                      boolean scaleToVisibleSize) {
        RpcClient client = Access.ensureConnection();

        CompressImagesRequest.Builder req = CompressImagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setQuality(quality);
        req.setScaleToVisibleSize(scaleToVisibleSize);

        EmptyResult res = client.blockingStub.pdfDocumentCompressCompressImages(req.build());

        Utils_Util.handleEmptyResult(res);
    }
}
