package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetBookmarksDescriptorRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetBookmarksDescriptorResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumInsertBookmarkRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumBookmarkP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumBookmarkDescriptorP;

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
        PdfiumGetBookmarksDescriptorResultP dres = getBookmarkDescriptors(internalPdfDocument);
        return Bookmark_Converter.fromProto(dres.getResult());
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

        // Resolve parent by ID if we have a parentText
        String parentId = null;
        if (parentText != null && !parentText.isEmpty()) {
            PdfiumGetBookmarksDescriptorResultP dres = getBookmarkDescriptors(internalPdfDocument);
            parentId = dres.getResult().getBookmarkDescriptorsList().stream()
                .filter(p -> parentText.equals(p.getText()))
                .sorted((a,b) -> Integer.compare(depth(a.getHierarchy()), depth(b.getHierarchy())))
                .map(com.ironsoftware.ironpdf.internal.proto.PdfiumBookmarkDescriptorP::getItemId)
                .filter(id -> id != null && !id.isEmpty())
                .findFirst()
                .orElse(null);
        }

        PdfiumBookmarkP.Builder parent = PdfiumBookmarkP.newBuilder();

        if (parentId != null && !parentId.isEmpty()) {
            // Use ID path when available, but keep text as fallback
            parent.setItemId(parentId);
            if (parentText != null && !parentText.isEmpty()) {
                parent.setText(parentText);
            }
        } else if (parentText != null && !parentText.isEmpty()) {
            // Fallback to text when no id yet
            parent.setText(parentText);
            request.setParentText(parentText);
        } else {
            // Top-level insert: present but empty parent
            request.clearParentText();
        }

        request.setParent(parent.build());

        EmptyResultP res = client.GetBlockingStub("insertBookmark").pdfiumBookmarkInsertBookmark(
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

    /**
     * Fetches bookmark descriptors from the PDF document
     *
     * @param internalPdfDocument the internal pdf document
     * @return the bookmark descriptors result
     */
    private static PdfiumGetBookmarksDescriptorResultP getBookmarkDescriptors(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();

        PdfiumGetBookmarksDescriptorRequestP.Builder request = PdfiumGetBookmarksDescriptorRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        PdfiumGetBookmarksDescriptorResultP result = client.GetBlockingStub("getBookmarks")
                .pdfiumBookmarkGetBookmarksDescriptor(request.build());

        if (result.getResultOrExceptionCase() == PdfiumGetBookmarksDescriptorResultP.ResultOrExceptionCase.EXCEPTION) {
            throw fromProto(result.getException());
        }

        return result;
    }

    // Helper: count non-empty segments in \A\B\C (depth = 3)
    private static int depth(String h) {
        if (h == null || h.isEmpty()) return 0;
        int d = 0, i = 0, n = h.length();
        while (i < n) {
            while (i < n && h.charAt(i) == '\\') i++;
            int start = i;
            while (i < n && h.charAt(i) != '\\') i++;
            if (i > start) d++;
        }
        return d;
    }
}
