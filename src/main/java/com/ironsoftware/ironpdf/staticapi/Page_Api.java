package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;
import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandlePdfDocumentResult;

public final class Page_Api {

    /**
     * Removes a range of pages from the PDF
     *
     * @param pageIndexes A list of pages indexes to remove.
     */
    public static void RemovePage(InternalPdfDocument pdfDocument, Iterable<Integer> pageIndexes)
            throws IOException {
        RpcClient client = Access.EnsureConnection();
        RemovePagesRequest.Builder req = RemovePagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.addAllPageIndexes(pageIndexes);

        EmptyResult res = client.BlockingStub.pdfDocumentPageRemovePages(req.build());
        HandleEmptyResult(res);
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
    public static InternalPdfDocument MergePage(List<InternalPdfDocument> pdfDocuments)
            throws IOException {
        RpcClient client = Access.EnsureConnection();
        PdfDocumentMergeRequest.Builder req = PdfDocumentMergeRequest.newBuilder();

        req.addAllDocuments(
                pdfDocuments.stream().map(x -> x.remoteDocument).collect(Collectors.toList()));

        PdfDocumentResult res = client.BlockingStub.pdfDocumentPageMerge(req.build());

        return HandlePdfDocumentResult(res);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param anotherPdf PdfDocument to insert.
     */

    public static void InsertPage(InternalPdfDocument mainPdfDocument, InternalPdfDocument anotherPdf)
            throws IOException {
        InsertPage(mainPdfDocument, anotherPdf, 0);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param anotherPdf PdfDocument to insert.
     * @param atIndex    Index at which to insert the new content.  Note: Page 1 has index 0...
     */
    public static void InsertPage(InternalPdfDocument mainPdfDocument, InternalPdfDocument anotherPdf,
                                  int atIndex) throws IOException {
        RpcClient client = Access.EnsureConnection();
        PdfDocumentInsertRequest.Builder req = PdfDocumentInsertRequest.newBuilder();
        req.setMainDocument(mainPdfDocument.remoteDocument);
        req.setInsertionIndex(atIndex);
        req.setInsertedDocument(anotherPdf.remoteDocument);

        EmptyResult res = client.BlockingStub.pdfDocumentPageInsertPdf(req.build());
        HandleEmptyResult(res);
    }

    /**
     * Appends another PDF to the end of the current {@link InternalPdfDocument} If AnotherPdfFile
     * contains form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name'
     * will be 'Name_'
     *
     * @param anotherPdf PdfDocument to append.
     */
    public static void AppendPdf(InternalPdfDocument mainPdfDocument, InternalPdfDocument anotherPdf)
            throws IOException {
        RpcClient client = Access.EnsureConnection();
        PdfDocumentInsertRequest.Builder req = PdfDocumentInsertRequest.newBuilder();
        req.setMainDocument(mainPdfDocument.remoteDocument);
        req.setInsertionIndex(GetPagesInfo(mainPdfDocument).size());
        req.setInsertedDocument(anotherPdf.remoteDocument);

        EmptyResult res = client.BlockingStub.pdfDocumentPageInsertPdf(req.build());
        HandleEmptyResult(res);
    }

    /**
     * Gets the list of pages with information in the PDF document.
     */
    public static List<PageInfo> GetPagesInfo(InternalPdfDocument pdfDocument) throws IOException {
        RpcClient client = Access.EnsureConnection();
        GetPagesRequest.Builder req = GetPagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        GetPagesResult res = client.BlockingStub.pdfDocumentPageGetPages(req.build());

        if (res.getResultOrExceptionCase() == GetPagesResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(res.getException());
        }
        return res.getResult().getPagesList().stream()
                .map(Page_Converter::FromProto).collect(Collectors.toList());
    }

    /**
     * Rotates all page of the PdfDocument by a specified number of degrees.
     *
     * @param pageRotation Degrees of rotation
     */
    public static void RotatePage(InternalPdfDocument pdfDocument, PageRotation pageRotation)
            throws IOException {
        RpcClient client = Access.EnsureConnection();
        RotatePagesRequest.Builder req = RotatePagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setPageRotation(Page_Converter.ToProto(pageRotation));

        EmptyResult res = client.BlockingStub.pdfDocumentPageRotatePages(req.build());
        HandleEmptyResult(res);
    }

    /**
     * Rotates pages of the PdfDocument by a specified number of degrees.
     *
     * @param pageIndexes  Indexes of the pages to rotate in an IEnumerable, list or array. PageIndex
     *                     is a 'Zero based' page number, the first page being 0
     * @param pageRotation Degrees of rotation
     */
    public static void RotatePage(InternalPdfDocument pdfDocument, PageRotation pageRotation,
                                  Iterable<Integer> pageIndexes) throws IOException {
        RpcClient client = Access.EnsureConnection();
        RotatePagesRequest.Builder req = RotatePagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setPageRotation(Page_Converter.ToProto(pageRotation));
        if (pageIndexes != null) {
            req.addAllPageIndexes(pageIndexes);
        }

        EmptyResult res = client.BlockingStub.pdfDocumentPageRotatePages(req.build());
        HandleEmptyResult(res);
    }

    /**
     * Creates a new PDF by copying a range of pages from this PdfDocument.
     *
     * @param pageIndexes An IEnumerable of page indexes to copy into the new PDF.
     * @return A new {@link InternalPdfDocument}
     */
    public static InternalPdfDocument CopyPage(InternalPdfDocument pdfDocument,
                                               Iterable<Integer> pageIndexes) throws IOException {
        RpcClient client = Access.EnsureConnection();
        CopyPagesRequest.Builder req = CopyPagesRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        req.addAllPageIndexes(pageIndexes);

        PdfDocumentResult res = client.BlockingStub.pdfDocumentPageCopyPages(req.build());

        return HandlePdfDocumentResult(res);
    }
}
