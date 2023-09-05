package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.PdfiumAddBackgroundForegroundRequestP;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumLayerModesP;
import com.ironsoftware.ironpdf.page.PageInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Background foreground api.
 */
public final class BackgroundForeground_Api {

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param baseDocument       the base document
     * @param backgroundDocument The background PDF path.
     * @param basePdfPageIndexes A list of Indexes (zero-based page numbers) of pages in this PDF to                           which the background will be applied to.
     */
    public static void addBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument, List<Integer> basePdfPageIndexes) {
        addBackground(baseDocument, backgroundDocument, basePdfPageIndexes, 0);
    }

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param baseDocument           the base document
     * @param backgroundDocument     The background PDF path.
     * @param basePdfPageIndexes     A list of Indexes (zero-based page numbers) of pages in this PDF                               to which the background will be applied to.
     * @param backgroundPdfPageIndex Index (zero-based page number) to copy from the BackgroundPdf.
     */
    public static void addBackground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument backgroundDocument, List<Integer> basePdfPageIndexes,
                                     int backgroundPdfPageIndex) {
        RpcClient client = Access.ensureConnection();

        PdfiumAddBackgroundForegroundRequestP.Builder req = PdfiumAddBackgroundForegroundRequestP.newBuilder();
        req.setSourcePdf(backgroundDocument.remoteDocument);
        req.setDestinationPdf(baseDocument.remoteDocument);
        req.setSrcPageIndex(backgroundPdfPageIndex);

        if(basePdfPageIndexes == null || basePdfPageIndexes.isEmpty()){
            basePdfPageIndexes = Page_Api.getPagesInfo(baseDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        req.addAllDestPageIndices(basePdfPageIndexes);
        req.setLayerMode(PdfiumLayerModesP.newBuilder().setEnumValue(1).build()); // Background = 1

        EmptyResultP res = client.blockingStub.pdfiumBackgroundForegroundAddBackgroundForeground(
                req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Adds the background to specified pages of this PDF.  The background is copied from a page in
     * another PDF document.
     *
     * @param baseDocument       the base document
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
     * @param baseDocument       the base document
     * @param foregroundDocument The overlay PDF path.
     * @param basePdfPageIndexes A list of Indexes (zero-based page numbers) of pages in this PDF to                           which the overlay will be applied to.
     */
    public static void addForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument, List<Integer> basePdfPageIndexes) {
        addForeground(baseDocument, foregroundDocument, basePdfPageIndexes, 0);
    }

    /**
     * Adds an overlay to a range or pages of this PDF. The foreground overlay is copied from a page
     * in another PDF document.
     *
     * @param baseDocument           the base document
     * @param foregroundDocument     The overlay PDF path.
     * @param basePdfPageIndexes     A list of Indexes (zero-based page numbers) of pages in this PDF                               to which the overlay will be applied to.
     * @param foregroundPdfPageIndex Index (zero-based page number) to copy from the Overlay PDF.
     */
    public static void addForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument, List<Integer> basePdfPageIndexes,
                                     int foregroundPdfPageIndex) {
        RpcClient client = Access.ensureConnection();

        PdfiumAddBackgroundForegroundRequestP.Builder req = PdfiumAddBackgroundForegroundRequestP.newBuilder();
        req.setSourcePdf(foregroundDocument.remoteDocument);
        req.setDestinationPdf(baseDocument.remoteDocument);
        req.setSrcPageIndex(foregroundPdfPageIndex);

        if(basePdfPageIndexes == null || basePdfPageIndexes.isEmpty()){
            basePdfPageIndexes = Page_Api.getPagesInfo(baseDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }

        req.addAllDestPageIndices(basePdfPageIndexes);
        req.setLayerMode(PdfiumLayerModesP.newBuilder().setEnumValue(0).build()); // Foreground = 0

        EmptyResultP res = client.blockingStub.pdfiumBackgroundForegroundAddBackgroundForeground(
                req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Adds an overlay to a range or pages of this PDF. The foreground overlay is copied from a page
     * in another PDF document.
     *
     * @param baseDocument       the base document
     * @param foregroundDocument The overlay PDF path.
     */
    public static void addForeground(InternalPdfDocument baseDocument,
                                     InternalPdfDocument foregroundDocument) {
        addForeground(baseDocument, foregroundDocument, null, 0);
    }
}
