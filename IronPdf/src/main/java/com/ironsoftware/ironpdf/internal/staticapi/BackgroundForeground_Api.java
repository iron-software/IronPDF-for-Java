package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.AddBackgroundForegroundRequest;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;

public final class BackgroundForeground_Api {

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param basePdfPageIndexes A list of Indexes (zero-based page numbers) of pages in this PDF to
     *                           which the background will be applied to.
     * @param backgroundDocument The background PDF path.
     */

    public static void addBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument, Iterable<Integer> basePdfPageIndexes) {
        addBackground(baseDocument, backgroundDocument, basePdfPageIndexes, 0);
    }

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param basePdfPageIndexes     A list of Indexes (zero-based page numbers) of pages in this PDF
     *                               to which the background will be applied to.
     * @param backgroundDocument     The background PDF path.
     * @param backgroundPdfPageIndex Index (zero-based page number) to copy from the BackgroundPdf.
     */
    public static void addBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument, Iterable<Integer> basePdfPageIndexes,
                                     int backgroundPdfPageIndex) {
        RpcClient client = Access.ensureConnection();

        AddBackgroundForegroundRequest.Builder req = AddBackgroundForegroundRequest.newBuilder();
        req.setBackgroundPdf(backgroundDocument.remoteDocument);
        req.setBasePdf(baseDocument.remoteDocument);
        req.setLayerPdfPageIndex(backgroundPdfPageIndex);
        if (basePdfPageIndexes != null) {
            req.addAllBasePdfPages(basePdfPageIndexes);
        }

        EmptyResult res = client.blockingStub.pdfDocumentBackgroundForegroundAddBackgroundForeground(
                req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param backgroundDocument The background PDF path.
     */
    public static void addBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument) {
        addBackground(baseDocument, backgroundDocument, null, 0);
    }

    /**
     * Adds an overlay to a range or pages of this PDF. The foreground overlay is copied from a page
     * in another PDF document.
     *
     * @param basePdfPageIndexes A list of Indexes (zero-based page numbers) of pages in this PDF to
     *                           which the overlay will be applied to.
     * @param foregroundDocument The overlay PDF path.
     */

    public static void addForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument, Iterable<Integer> basePdfPageIndexes) {
        addForeground(baseDocument, foregroundDocument, basePdfPageIndexes, 0);
    }

    /**
     * Adds an overlay to a range or pages of this PDF. The foreground overlay is copied from a page
     * in another PDF document.
     *
     * @param basePdfPageIndexes     A list of Indexes (zero-based page numbers) of pages in this PDF
     *                               to which the overlay will be applied to.
     * @param foregroundDocument     The overlay PDF path.
     * @param foregroundPdfPageIndex Index (zero-based page number) to copy from the Overlay PDF.
     */
    public static void addForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument, Iterable<Integer> basePdfPageIndexes,
                                     int foregroundPdfPageIndex) {
        RpcClient client = Access.ensureConnection();

        AddBackgroundForegroundRequest.Builder req = AddBackgroundForegroundRequest.newBuilder();
        req.setForegroundPdf(foregroundDocument.remoteDocument);
        req.setBasePdf(baseDocument.remoteDocument);
        req.setLayerPdfPageIndex(foregroundPdfPageIndex);

        if (basePdfPageIndexes != null) {
            req.addAllBasePdfPages(basePdfPageIndexes);
        }

        EmptyResult res = client.blockingStub.pdfDocumentBackgroundForegroundAddBackgroundForeground(
                req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Adds an overlay to a range or pages of this PDF. The foreground overlay is copied from a page
     * in another PDF document.
     *
     * @param foregroundDocument The overlay PDF path.
     */
    public static void addForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument) {
        addForeground(baseDocument, foregroundDocument, null, 0);
    }
}
