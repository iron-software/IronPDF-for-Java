package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.internal.proto.ExtractAllTextRequest;
import com.ironsoftware.ironpdf.internal.proto.ReplaceTextRequest;
import com.ironsoftware.ironpdf.internal.proto.StringResult;

import java.io.IOException;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;

public final class Text_Api {

    /**
     * Extracts the written text content from the PDF and returns it as a string. <p>Pages are
     * separated by 4 consecutive Environment.NewLines</p>
     *
     * @return All text in the PDF as a string.
     */
    public static String ExtractAllText(InternalPdfDocument pdfDocument) throws IOException {
        return ExtractAllText(pdfDocument, null);
    }


    /**
     * Extracts the written text content from the PDF and returns it as a string. <p>Pages are
     * separated by 4 consecutive Environment.NewLines</p>
     *
     * @param pageIndexes page indexes to extract text (defaults to all pages)
     * @return All text in the PDF as a string.
     */
    public static String ExtractAllText(InternalPdfDocument pdfDocument,
                                        Iterable<Integer> pageIndexes) throws IOException {
        RpcClient client = Access.EnsureConnection();

        ExtractAllTextRequest.Builder req = ExtractAllTextRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        if (pageIndexes != null) {
            req.addAllPageIndexes(pageIndexes);
        }

        StringResult res = client.BlockingStub.pdfDocumentTextExtractAllText(req.build());

        if (res.getResultOrExceptionCase() == StringResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(res.getException());
        }

        return res.getResult();
    }

    /**
     * Replace the specified old text with new text on a given page
     *
     * @param pageIndex Page index to search for old text to replace
     * @param oldText   Old text to remove
     * @param newText   New text to add
     */
    public static void ReplaceTextOnPage(InternalPdfDocument pdfDocument, int pageIndex,
                                         String oldText, String newText) throws IOException {
        RpcClient client = Access.EnsureConnection();

        ReplaceTextRequest.Builder req = ReplaceTextRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setPageIndex(pageIndex);
        req.setCurrentText(oldText);
        req.setNewText(newText);

        EmptyResult res = client.BlockingStub.pdfDocumentTextReplaceText(req.build());
        HandleEmptyResult(res);
    }
}
