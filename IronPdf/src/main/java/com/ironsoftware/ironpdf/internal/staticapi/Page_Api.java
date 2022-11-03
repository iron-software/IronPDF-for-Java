package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;

import java.util.List;
import java.util.stream.Collectors;

public final class Page_Api {

    /**
     * Removes a range of pages from the PDF
     *
     * @param pageIndexes A list of pages indexes to remove.
     */
    public static void removePage(InternalPdfDocument pdfDocument, Iterable<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();
        RemovePagesRequest.Builder req = RemovePagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.addAllPageIndexes(pageIndexes);

        EmptyResult res = client.blockingStub.pdfDocumentPageRemovePages(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Static method that joins (concatenates) multiple PDF documents together into one compiled PDF
     * document. If the PDF contains form fields the form field in the resulting PDF's name will be
     * appended with '_{index}' e.g. 'Name' will be 'Name_0'
     *
     * @param pdfDocuments A IEnumerable of PdfDocument.  To merge existing PDF files you may use the
     *                     PdfDocument.FromFile static method in conjunction with Merge.
     * @return A new, merged {@link InternalPdfDocument}
     */
    public static InternalPdfDocument mergePage(List<InternalPdfDocument> pdfDocuments) {
        RpcClient client = Access.ensureConnection();
        PdfDocumentMergeRequest.Builder req = PdfDocumentMergeRequest.newBuilder();

        req.addAllDocuments(
                pdfDocuments.stream().map(x -> x.remoteDocument).collect(Collectors.toList()));

        PdfDocumentResult res = client.blockingStub.pdfDocumentPageMerge(req.build());

        return Utils_Util.handlePdfDocumentResult(res);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param anotherPdf PdfDocument to insert.
     */

    public static void insertPage(InternalPdfDocument mainPdfDocument, InternalPdfDocument anotherPdf) {
        insertPage(mainPdfDocument, anotherPdf, 0);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param anotherPdf PdfDocument to insert.
     * @param atIndex    Index at which to insert the new content.  Note: Page 1 has index 0...
     */
    public static void insertPage(InternalPdfDocument mainPdfDocument, InternalPdfDocument anotherPdf,
                                  int atIndex) {
        RpcClient client = Access.ensureConnection();
        PdfDocumentInsertRequest.Builder req = PdfDocumentInsertRequest.newBuilder();
        req.setMainDocument(mainPdfDocument.remoteDocument);
        req.setInsertionIndex(atIndex);
        req.setInsertedDocument(anotherPdf.remoteDocument);

        EmptyResult res = client.blockingStub.pdfDocumentPageInsertPdf(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Appends another PDF to the end of the current {@link InternalPdfDocument} If AnotherPdfFile
     * contains form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name'
     * will be 'Name_'
     *
     * @param anotherPdf PdfDocument to append.
     */
    public static void appendPdf(InternalPdfDocument mainPdfDocument, InternalPdfDocument anotherPdf) {
        RpcClient client = Access.ensureConnection();
        PdfDocumentInsertRequest.Builder req = PdfDocumentInsertRequest.newBuilder();
        req.setMainDocument(mainPdfDocument.remoteDocument);
        req.setInsertionIndex(getPagesInfo(mainPdfDocument).size());
        req.setInsertedDocument(anotherPdf.remoteDocument);

        EmptyResult res = client.blockingStub.pdfDocumentPageInsertPdf(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Gets the list of pages with information in the PDF document.
     */
    public static List<PageInfo> getPagesInfo(InternalPdfDocument pdfDocument) {
        RpcClient client = Access.ensureConnection();
        GetPagesRequest.Builder req = GetPagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        GetPagesResult res = client.blockingStub.pdfDocumentPageGetPages(req.build());

        if (res.getResultOrExceptionCase() == GetPagesResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }
        return res.getResult().getPagesList().stream()
                .map(Page_Converter::fromProto).collect(Collectors.toList());
    }

    /**
     * Rotates all page of the PdfDocument by a specified number of degrees.
     *
     * @param pageRotation Degrees of rotation
     */
    public static void rotatePage(InternalPdfDocument pdfDocument, PageRotation pageRotation) {
        RpcClient client = Access.ensureConnection();
        RotatePagesRequest.Builder req = RotatePagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setPageRotation(Page_Converter.toProto(pageRotation));

        EmptyResult res = client.blockingStub.pdfDocumentPageRotatePages(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Rotates pages of the PdfDocument by a specified number of degrees.
     *
     * @param pageIndexes  Indexes of the pages to rotate in an IEnumerable, list or array. PageIndex
     *                     is a 'Zero based' page number, the first page being 0
     * @param pageRotation Degrees of rotation
     */
    public static void rotatePage(InternalPdfDocument pdfDocument, PageRotation pageRotation,
                                  Iterable<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();
        RotatePagesRequest.Builder req = RotatePagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setPageRotation(Page_Converter.toProto(pageRotation));
        if (pageIndexes != null) {
            req.addAllPageIndexes(pageIndexes);
        }

        EmptyResult res = client.blockingStub.pdfDocumentPageRotatePages(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Creates a new PDF by copying a range of pages from this PdfDocument.
     *
     * @param pageIndexes An IEnumerable of page indexes to copy into the new PDF.
     * @return A new {@link InternalPdfDocument}
     */
    public static InternalPdfDocument copyPage(InternalPdfDocument pdfDocument,
                                               Iterable<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();
        CopyPagesRequest.Builder req = CopyPagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        req.addAllPageIndexes(pageIndexes);

        PdfDocumentResult res = client.blockingStub.pdfDocumentPageCopyPages(req.build());

        return Utils_Util.handlePdfDocumentResult(res);
    }
}
