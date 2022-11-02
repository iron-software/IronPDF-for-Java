package com.ironsoftware.ironpdf.bookmark;

import com.ironsoftware.ironpdf.staticapi.Bookmark_Api;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookmarkManager {

    private final InternalPdfDocument internalPdfDocument;


    public BookmarkManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Total number of bookmarks, including all nested bookmarks
     */
    public final int getCount() throws IOException {
        return this.getBookmarks().size();
    }

    /**
     * Retrieve all bookmarks within this PDF, recursively retrieve all children of bookmarks within
     * this collection, and return a flat list
     *
     * @return A flattened list of all bookmarks in this collection and all of their children
     */
    public final List<com.ironsoftware.ironpdf.bookmark.Bookmark> getBookmarks() throws IOException {
        return com.ironsoftware.ironpdf.staticapi.Bookmark_Api.GetBookmarks(internalPdfDocument)
                .stream().peek(x -> x.setBookmarkManager(this)).collect(Collectors.toList());
    }

    /**
     * Add a new bookmark at the end of the bookmark collection
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public final void AddBookMarkAtEnd(String text, int pageIndex) throws IOException {

        List<com.ironsoftware.ironpdf.bookmark.Bookmark> bookmarks = this.getBookmarks();
        com.ironsoftware.ironpdf.bookmark.Bookmark lastBookmarks =
                bookmarks.size() == 0 ? null : bookmarks.get(bookmarks.size() - 1);

        if (lastBookmarks == null) {
            com.ironsoftware.ironpdf.staticapi.Bookmark_Api.InsertBookmarkAtStart(internalPdfDocument,
                    pageIndex, text);
        } else {
            com.ironsoftware.ironpdf.staticapi.Bookmark_Api.InsertBookmark(internalPdfDocument, pageIndex,
                    text, lastBookmarks.getText(), null);
        }
    }

    /**
     * Add a new bookmark at the start of the document collection
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public final void AddBookMarkAtStart(String text, int pageIndex) throws IOException {

        List<com.ironsoftware.ironpdf.bookmark.Bookmark> bookmarks = this.getBookmarks();
        com.ironsoftware.ironpdf.bookmark.Bookmark firstBookmarks =
                bookmarks.size() == 0 ? null : bookmarks.get(0);

        com.ironsoftware.ironpdf.staticapi.Bookmark_Api.InsertBookmarkAtStart(internalPdfDocument,
                pageIndex, text);
    }


    /**
     * Insert a new bookmark
     *
     * @param text                 The display text for the link.
     * @param pageIndex            The zero based page number to link to.  E.g.  Page 1 has a
     *                             PageIndex of 0
     * @param parentText   parent bookmark text. set to null for insert at the top
     * @param previousText previous bookmark text. set to null for insert at first of its
     *                             siblings
     */
    public final void InsertBookmark(String text, int pageIndex, String parentText,
                                     String previousText) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Bookmark_Api.InsertBookmark(internalPdfDocument, pageIndex,
                text, parentText, previousText);
    }

    /**
     * Insert a new bookmark
     *
     * @param bookmark a bookmark object.
     */
    public final void InsertBookmark(Bookmark bookmark) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Bookmark_Api.InsertBookmark(internalPdfDocument,
                bookmark.getPageIndex(), bookmark.getText(), bookmark.getParentBookmarkText(),
                bookmark.getPreviousBookmarkText());
    }

    /**
     * Add a new bookmark as a first child of this bookmark. To add a bookmark as a second child,
     * please navigate to the childBookmark object and call AddNextBookmark.
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     * @return a new child bookmark
     */
    public final com.ironsoftware.ironpdf.bookmark.Bookmark AddChildBookmark(String text,
                                                                             int pageIndex, String parentBookmarkText) throws IOException {

        com.ironsoftware.ironpdf.staticapi.Bookmark_Api.InsertBookmark(internalPdfDocument, pageIndex,
                text, parentBookmarkText, null);

        //hacks to get the new bookmark, as the static api doesn't return it.
        Optional<com.ironsoftware.ironpdf.bookmark.Bookmark> optNewBookmark = Bookmark_Api.GetBookmarks(
                        internalPdfDocument).stream().filter(x -> x.getText().equals(text)
                        && x.getPageIndex() == pageIndex
                        && x.getParentBookmarkText().equals(parentBookmarkText))
                .findFirst();
        optNewBookmark.ifPresent(x -> x.setBookmarkManager(this));
        return optNewBookmark.orElse(null);
    }
}
