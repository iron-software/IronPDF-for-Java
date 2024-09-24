package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumExtractAllTextRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumReplaceTextRequestP;
import com.ironsoftware.ironpdf.internal.proto.StringResultP;
import com.ironsoftware.ironpdf.page.PageInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Text api.
 */
public final class Text_Api {

    /**
     * Extracts the written text content from the PDF and returns it as a string. <p>Pages are
     * separated by 4 consecutive Environment.NewLines</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @return All text in the PDF as a string.
     */
    public static String extractAllText(InternalPdfDocument internalPdfDocument) {
        return extractAllText(internalPdfDocument, internalPdfDocument.getPageList(PageSelection.allPages()));
    }


    /**
     * Extracts the written text content from the PDF and returns it as a string. <p>Pages are
     * separated by 4 consecutive Environment.NewLines</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         page indexes to extract text (defaults to all pages)
     * @return All text in the PDF as a string.
     */
    public static String extractAllText(InternalPdfDocument internalPdfDocument,
                                        List<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();

        PdfiumExtractAllTextRequestP.Builder req = PdfiumExtractAllTextRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        if(pageIndexes == null || pageIndexes.isEmpty()){
            pageIndexes = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        req.addAllPageIndexes(pageIndexes);

        StringResultP res = client.GetBlockingStub("extractAllText").pdfiumTextExtractAllText(req.build());

        if (res.getResultOrExceptionCase() == StringResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        return res.getResult();
    }

    /**
     * Replace the specified old text with new text on a given page
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndex           Page index to search for old text to replace
     * @param oldText             Old text to remove
     * @param newText             New text to add
     */
    public static void replaceTextOnPage(InternalPdfDocument internalPdfDocument, int pageIndex,
                                         String oldText, String newText) {
        RpcClient client = Access.ensureConnection();

        PdfiumReplaceTextRequestP.Builder req = PdfiumReplaceTextRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPageIndex(pageIndex);
        req.setCurrentText(oldText);
        req.setNewText(newText);

        EmptyResultP res = client.GetBlockingStub("replaceTextOnPage").pdfiumTextReplaceText(req.build());
        Utils_Util.handleEmptyResult(res);
    }
}
