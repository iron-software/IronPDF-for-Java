package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.internal.proto.ExtractAllTextRequest;
import com.ironsoftware.ironpdf.internal.proto.ReplaceTextRequest;
import com.ironsoftware.ironpdf.internal.proto.StringResult;

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
        return extractAllText(internalPdfDocument, null);
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
                                        Iterable<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();

        ExtractAllTextRequest.Builder req = ExtractAllTextRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        if (pageIndexes != null) {
            req.addAllPageIndexes(pageIndexes);
        }

        StringResult res = client.blockingStub.pdfDocumentTextExtractAllText(req.build());

        if (res.getResultOrExceptionCase() == StringResult.ResultOrExceptionCase.EXCEPTION) {
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

        ReplaceTextRequest.Builder req = ReplaceTextRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPageIndex(pageIndex);
        req.setCurrentText(oldText);
        req.setNewText(newText);

        EmptyResult res = client.blockingStub.pdfDocumentTextReplaceText(req.build());
        Utils_Util.handleEmptyResult(res);
    }
}
