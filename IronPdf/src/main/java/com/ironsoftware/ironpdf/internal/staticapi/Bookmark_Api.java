package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.internal.proto.GetBookmarksRequest;
import com.ironsoftware.ironpdf.internal.proto.GetBookmarksResult;
import com.ironsoftware.ironpdf.internal.proto.InsertBookmarkRequest;

import java.util.List;

import static com.ironsoftware.ironpdf.internal.staticapi.Exception_Converter.fromProto;

public final class Bookmark_Api {

    /**
     * Retrieve all bookmarks within this PDF, recursively retrieve all children of bookmarks within
     * this collection, and return a flat list
     *
     * @return A flattened list of all bookmarks in this collection and all of their children
     */
    public static List<Bookmark> getBookmarks(InternalPdfDocument pdfDocument) {

        RpcClient client = Access.ensureConnection();

        GetBookmarksRequest.Builder request = GetBookmarksRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        GetBookmarksResult result = client.blockingStub.pdfDocumentBookmarkGetBookmarks(
                request.build());

        if (result.getResultOrExceptionCase() == GetBookmarksResult.ResultOrExceptionCase.EXCEPTION) {
            throw fromProto(result.getException());
        }

        return Bookmark_Converter.fromProto(result.getResult());
    }

    /**
     * Insert a new bookmark
     *
     * @param text       The display text for the link.
     * @param pageIndex  The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @param parentText parent bookmark text. set to null for insert at the top
     */
    public static void insertBookmarkAsFirstChild(InternalPdfDocument pdfDocument, int pageIndex,
                                                  String text, String parentText) {
        insertBookmark(pdfDocument, pageIndex, text, parentText, null);
    }

    /**
     * Insert a new bookmark
     *
     * @param text         The display text for the link.
     * @param pageIndex    The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @param parentText   parent bookmark text. set to null for insert at the top
     * @param previousText previous bookmark text. set to null for insert at first of its siblings
     */
    public static void insertBookmark(InternalPdfDocument pdfDocument, int pageIndex, String text,
                                      String parentText, String previousText) {
        RpcClient client = Access.ensureConnection();

        InsertBookmarkRequest.Builder request = InsertBookmarkRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        request.setPageIndex(pageIndex);
        request.setText(text);
        if (parentText != null) {
            request.setParentText(parentText);
        }

        if (previousText != null) {
            request.setPreviousText(previousText);
        }

        EmptyResult res = client.blockingStub.pdfDocumentBookmarkInsertBookmark(
                request.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Insert a new bookmark
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public static void insertBookmarkAtStart(InternalPdfDocument pdfDocument, int pageIndex,
                                             String text) {
        insertBookmark(pdfDocument, pageIndex, text, null, null);
    }
}
