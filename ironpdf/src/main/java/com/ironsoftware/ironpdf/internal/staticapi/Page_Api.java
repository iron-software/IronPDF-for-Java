package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Page api.
 */
public final class Page_Api {

    /**
     * Removes a range of pages from the PDF
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         A list of pages indexes to remove.
     */
    public static void removePage(InternalPdfDocument internalPdfDocument, List<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();
        PdfiumRemovePagesRequestP.Builder req = PdfiumRemovePagesRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.addAllPageIndexes(pageIndexes);

        PdfiumRemovePagesResultP res = client.GetBlockingStub("removePage").pdfiumPageRemovePages(req.build());

        if (res.getResultOrExceptionCase() == PdfiumRemovePagesResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }
    }

    /**
     * Static method that joins (concatenates) multiple PDF documents together into one compiled PDF
     * document. If the PDF contains form fields the form field in the resulting PDF's name will be
     * appended with '_{index}' e.g. 'Name' will be 'Name_0'
     *
     * @param pdfDocuments A List of PdfDocument.  To merge existing PDF files you may use the                     PdfDocument.FromFile static method in conjunction with Merge.
     * @return A new, merged {@link InternalPdfDocument}
     */
    public static InternalPdfDocument mergePage(List<InternalPdfDocument> pdfDocuments) {
        RpcClient client = Access.ensureConnection();
        PdfiumPdfDocumentMergeRequestP.Builder req = PdfiumPdfDocumentMergeRequestP.newBuilder();

        req.addAllDocuments(
                pdfDocuments.stream().map(x -> x.remoteDocument).collect(Collectors.toList()));

        PdfDocumentResultP res = client.GetBlockingStub("mergePage").pdfiumPageMerge(req.build());

        return Utils_Util.handlePdfDocumentResult(res);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param internalPdfDocument the internal pdf document
     * @param anotherPdf          internal pdf document to append.
     */
    public static void insertPage(InternalPdfDocument internalPdfDocument, InternalPdfDocument anotherPdf) {
        insertPage(internalPdfDocument, anotherPdf, 0);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param internalPdfDocument the internal pdf document
     * @param anotherPdf          internal pdf document to append.
     * @param atIndex             Index at which to insert the new content.  Note: Page 1 has index 0...
     */
    public static void insertPage(InternalPdfDocument internalPdfDocument, InternalPdfDocument anotherPdf,
                                  int atIndex) {
        RpcClient client = Access.ensureConnection();
        PdfiumPdfDocumentInsertRequestP.Builder req = PdfiumPdfDocumentInsertRequestP.newBuilder();
        req.setMainDocument(internalPdfDocument.remoteDocument);
        req.setInsertionIndex(atIndex);
        req.setInsertedDocument(anotherPdf.remoteDocument);

        EmptyResultP res = client.GetBlockingStub("insertPage").pdfiumPageInsertPdf(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Appends another PDF to the end of the current {@link InternalPdfDocument} If AnotherPdfFile
     * contains form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name'
     * will be 'Name_'
     *
     * @param mainPdfDocument the main internal pdf document
     * @param anotherPdf      internal pdf document to append.
     */
    public static void appendPdf(InternalPdfDocument mainPdfDocument, InternalPdfDocument anotherPdf) {
        RpcClient client = Access.ensureConnection();
        PdfiumPdfDocumentInsertRequestP.Builder req = PdfiumPdfDocumentInsertRequestP.newBuilder();
        req.setMainDocument(mainPdfDocument.remoteDocument);
        req.setInsertionIndex(getPagesInfo(mainPdfDocument).size());
        req.setInsertedDocument(anotherPdf.remoteDocument);

        EmptyResultP res = client.GetBlockingStub("appendPdf").pdfiumPageInsertPdf(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Gets the list of pages with information in the PDF document.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the pages info
     */
    public static List<PageInfo> getPagesInfo(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();
        PdfiumGetPagesRequestP.Builder req = PdfiumGetPagesRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        PdfiumGetPagesResultP res = client.GetBlockingStub("getPagesInfo").pdfiumPageGetPages(req.build());

        if (res.getResultOrExceptionCase() == PdfiumGetPagesResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }
        return res.getResult().getPagesList().stream()
                .map(Page_Converter::fromProto).collect(Collectors.toList());
    }

    /**
     * Rotates all page of the PdfDocument by a specified number of degrees.
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageRotation        Degrees of rotation
     */
    public static void setPageRotation(InternalPdfDocument internalPdfDocument, PageRotation pageRotation) {
        setPageRotation(internalPdfDocument, pageRotation, null);
    }

    /**
     * Rotates pages of the PdfDocument by a specified number of degrees.
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageRotation        Degrees of rotation
     * @param pageIndexes         Indexes of the pages to rotate in an IEnumerable, list or array. PageIndex                     is a 'Zero based' page number, the first page being 0
     */
    public static void setPageRotation(InternalPdfDocument internalPdfDocument, PageRotation pageRotation,
                                       List<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();
        PdfiumSetPagesRotationRequestP.Builder req = PdfiumSetPagesRotationRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPageRotation(Page_Converter.toProto(pageRotation));

        if(pageIndexes == null || pageIndexes.isEmpty()){
            pageIndexes = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        req.addAllPageIndexes(pageIndexes);

        EmptyResultP res = client.GetBlockingStub("setPageRotation").pdfiumPageSetPagesRotation(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Creates a new PDF by copying a range of pages from this PdfDocument.
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         An Iterable of page indexes to copy into the new PDF.
     * @return A new {@link InternalPdfDocument}
     */
    public static InternalPdfDocument copyPage(InternalPdfDocument internalPdfDocument,
                                               List<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();
        PdfiumCopyPagesRequestP.Builder req = PdfiumCopyPagesRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        req.addAllPageIndexes(pageIndexes);

        PdfDocumentResultP res = client.GetBlockingStub("copyPage").pdfiumPageCopyPages(req.build());

        return Utils_Util.handlePdfDocumentResult(res);
    }

    /**
     Resize a page to the specified dimensions (in millimeters)
     @param internalPdfDocument the internal pdf document
     @param pageWidth Desired page width, in millimeters
     @param pageHeight Desired page height, in millimeters
     @param pageIndex Selected page indexes.
     */
    public static void resizePage(InternalPdfDocument internalPdfDocument, double pageWidth, double pageHeight,
                                       Integer pageIndex) {
        RpcClient client = Access.ensureConnection();
        PdfiumResizePageRequestP.Builder req = PdfiumResizePageRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPageHeight(pageHeight);
        req.setPageWidth(pageWidth);
        req.setPageIndex(pageIndex);

        EmptyResultP res = client.GetBlockingStub("resizePage").pdfiumPageResizePage(req.build());
        Utils_Util.handleEmptyResult(res);
    }
}
