package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.internal.proto.GetBookmarksRequest;
import com.ironsoftware.ironpdf.internal.proto.GetBookmarksResult;
import com.ironsoftware.ironpdf.internal.proto.InsertBookmarkRequest;

import java.io.IOException;
import java.util.List;

import static com.ironsoftware.ironpdf.staticapi.Exception_Converter.FromProto;
import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;

public final class Bookmark_Api {

    /**
     * Retrieve all bookmarks within this PDF, recursively retrieve all children of bookmarks within
     * this collection, and return a flat list
     *
     * @return A flattened list of all bookmarks in this collection and all of their children
     */
    public static List<Bookmark> GetBookmarks(InternalPdfDocument pdfDocument) throws IOException {

        RpcClient client = Access.EnsureConnection();

        GetBookmarksRequest.Builder request = GetBookmarksRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        GetBookmarksResult result = client.BlockingStub.pdfDocumentBookmarkGetBookmarks(
                request.build());

        if (result.getResultOrExceptionCase() == GetBookmarksResult.ResultOrExceptionCase.EXCEPTION) {
            throw FromProto(result.getException());
        }

        return Bookmark_Converter.FromProto(result.getResult());
    }

    /**
     * Insert a new bookmark
     *
     * @param text       The display text for the link.
     * @param pageIndex  The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @param parentText parent bookmark text. set to null for insert at the top
     */
    public static void InsertBookmarkAsFirstChild(InternalPdfDocument pdfDocument, int pageIndex,
                                                  String text, String parentText) throws IOException {
        InsertBookmark(pdfDocument, pageIndex, text, parentText, null);
    }

    /**
     * Insert a new bookmark
     *
     * @param text         The display text for the link.
     * @param pageIndex    The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @param parentText   parent bookmark text. set to null for insert at the top
     * @param previousText previous bookmark text. set to null for insert at first of its siblings
     */
    public static void InsertBookmark(InternalPdfDocument pdfDocument, int pageIndex, String text,
                                      String parentText, String previousText) throws IOException {
        RpcClient client = Access.EnsureConnection();

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

        EmptyResult res = client.BlockingStub.pdfDocumentBookmarkInsertBookmark(
                request.build());
        HandleEmptyResult(res);
    }

    /**
     * Insert a new bookmark
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public static void InsertBookmarkAtStart(InternalPdfDocument pdfDocument, int pageIndex,
                                             String text) throws IOException {
        InsertBookmark(pdfDocument, pageIndex, text, null, null);
    }
}
