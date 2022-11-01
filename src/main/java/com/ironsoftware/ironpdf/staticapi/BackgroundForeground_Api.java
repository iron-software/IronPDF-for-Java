package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.AddBackgroundForegroundRequest;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;

import java.io.IOException;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;

public final class BackgroundForeground_Api {

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param basePdfPageIndexes A list of Indexes (zero-based page numbers) of pages in this PDF to
     *                           which the background will be applied to.
     * @param backgroundDocument The background PDF path.
     */

    public static void AddBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument, Iterable<Integer> basePdfPageIndexes)
            throws IOException {
        AddBackground(baseDocument, backgroundDocument, basePdfPageIndexes, 0);
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
    public static void AddBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument, Iterable<Integer> basePdfPageIndexes,
                                     int backgroundPdfPageIndex) throws IOException {
        RpcClient client = Access.EnsureConnection();

        AddBackgroundForegroundRequest.Builder req = AddBackgroundForegroundRequest.newBuilder();
        req.setBackgroundPdf(backgroundDocument.remoteDocument);
        req.setBasePdf(baseDocument.remoteDocument);
        req.setLayerPdfPageIndex(backgroundPdfPageIndex);
        if (basePdfPageIndexes != null) {
            req.addAllBasePdfPages(basePdfPageIndexes);
        }

        EmptyResult res = client.BlockingStub.pdfDocumentBackgroundForegroundAddBackgroundForeground(
                req.build());
        HandleEmptyResult(res);
    }

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param backgroundDocument The background PDF path.
     */
    public static void AddBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument) throws IOException {
        AddBackground(baseDocument, backgroundDocument, null, 0);
    }

    /**
     * Adds an overlay to a range or pages of this PDF. The foreground overlay is copied from a page
     * in another PDF document.
     *
     * @param basePdfPageIndexes A list of Indexes (zero-based page numbers) of pages in this PDF to
     *                           which the overlay will be applied to.
     * @param foregroundDocument The overlay PDF path.
     */

    public static void AddForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument, Iterable<Integer> basePdfPageIndexes)
            throws IOException {
        AddForeground(baseDocument, foregroundDocument, basePdfPageIndexes, 0);
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
    public static void AddForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument, Iterable<Integer> basePdfPageIndexes,
                                     int foregroundPdfPageIndex) throws IOException {
        RpcClient client = Access.EnsureConnection();

        AddBackgroundForegroundRequest.Builder req = AddBackgroundForegroundRequest.newBuilder();
        req.setForegroundPdf(foregroundDocument.remoteDocument);
        req.setBasePdf(baseDocument.remoteDocument);
        req.setLayerPdfPageIndex(foregroundPdfPageIndex);

        if (basePdfPageIndexes != null) {
            req.addAllBasePdfPages(basePdfPageIndexes);
        }

        EmptyResult res = client.BlockingStub.pdfDocumentBackgroundForegroundAddBackgroundForeground(
                req.build());
        HandleEmptyResult(res);
    }

    /**
     * Adds an overlay to a range or pages of this PDF. The foreground overlay is copied from a page
     * in another PDF document.
     *
     * @param foregroundDocument The overlay PDF path.
     */
    public static void AddForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument) throws IOException {
        AddForeground(baseDocument, foregroundDocument, null, 0);
    }
}
