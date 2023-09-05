package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetBookmarksDescriptorRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetBookmarksDescriptorResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumInsertBookmarkRequestP;

import java.util.List;

import static com.ironsoftware.ironpdf.internal.staticapi.Exception_Converter.fromProto;

/**
 * The type Bookmark api.
 */
public final class Bookmark_Api {

    /**
     * Retrieve all bookmarks within this PDF, recursively retrieve all children of bookmarks within
     * this collection, and return a flat list
     *
     * @param internalPdfDocument the internal pdf document
     * @return A flattened list of all bookmarks in this collection and all of their children
     */
    public static List<Bookmark> getBookmarks(InternalPdfDocument internalPdfDocument) {

        RpcClient client = Access.ensureConnection();

        PdfiumGetBookmarksDescriptorRequestP.Builder request = PdfiumGetBookmarksDescriptorRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        PdfiumGetBookmarksDescriptorResultP result = client.blockingStub.pdfiumBookmarkGetBookmarksDescriptor(
                request.build());

        if (result.getResultOrExceptionCase() == PdfiumGetBookmarksDescriptorResultP.ResultOrExceptionCase.EXCEPTION) {
            throw fromProto(result.getException());
        }

        return Bookmark_Converter.fromProto(result.getResult());
    }

    /**
     * Insert a new bookmark
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndex           The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @param text                The display text for the link.
     * @param parentText          parent bookmark text. set to null for insert at the top
     */
    public static void insertBookmarkAsFirstChild(InternalPdfDocument internalPdfDocument, int pageIndex,
                                                  String text, String parentText) {
        insertBookmark(internalPdfDocument, pageIndex, text, parentText, null);
    }

    /**
     * Insert a new bookmark
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndex           The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @param text                The display text for the link.
     * @param parentText          parent bookmark text. set to null for insert at the top
     * @param previousText        previous bookmark text. set to null for insert at first of its siblings
     */
    public static void insertBookmark(InternalPdfDocument internalPdfDocument, int pageIndex, String text,
                                      String parentText, String previousText) {
        RpcClient client = Access.ensureConnection();

        PdfiumInsertBookmarkRequestP.Builder request = PdfiumInsertBookmarkRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setPageIndex(pageIndex);
        request.setText(text);
        if (parentText != null) {
            request.setParentText(parentText);
        }

        if (previousText != null) {
            request.setPreviousText(previousText);
        }

        EmptyResultP res = client.blockingStub.pdfiumBookmarkInsertBookmark(
                request.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Insert a new bookmark
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndex           The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @param text                The display text for the link.
     */
    public static void insertBookmarkAtStart(InternalPdfDocument internalPdfDocument, int pageIndex,
                                             String text) {
        insertBookmark(internalPdfDocument, pageIndex, text, null, null);
    }
}
