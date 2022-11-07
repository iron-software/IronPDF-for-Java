package com.ironsoftware.ironpdf.bookmark;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Bookmark_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Class used to add , edit and remove bookmarks from a {@link com.ironsoftware.ironpdf.PdfDocument} outline.
 * <p>Bookmarks are arranged and navigated in a parent/child node hierarchy, similar to an HTML DOM.</p>
 * <p> See: {@link com.ironsoftware.ironpdf.PdfDocument#getBookmark()} </p>
 * <p> See: {@link Bookmark} </p>
 */
public class BookmarkManager {

    private final InternalPdfDocument internalPdfDocument;

    /**
     * Please get BookmarkManager by {@link PdfDocument#getBookmark()} instead.
     *
     * @param internalPdfDocument the internal pdf document
     */
    public BookmarkManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Total number of bookmarks, including all nested bookmarks
     */
    public final int getCount() {
        return this.getBookmarks().size();
    }

    /**
     * Retrieve all bookmarks within this PDF, recursively retrieve all children of bookmarks within
     * this collection, and return as a flat list
     *
     * @return A flattened list of all bookmark ojects in this collection and all of their children
     */
    public final List<com.ironsoftware.ironpdf.bookmark.Bookmark> getBookmarks() {
        return Bookmark_Api.getBookmarks(internalPdfDocument)
                .stream().peek(x -> x.setBookmarkManager(this)).collect(Collectors.toList());
    }

    /**
     * Add a new bookmark at the end of the bookmark collection
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public final void addBookMarkAtEnd(String text, int pageIndex) {

        List<com.ironsoftware.ironpdf.bookmark.Bookmark> bookmarks = this.getBookmarks();
        com.ironsoftware.ironpdf.bookmark.Bookmark lastBookmarks =
                bookmarks.size() == 0 ? null : bookmarks.get(bookmarks.size() - 1);

        if (lastBookmarks == null) {
            Bookmark_Api.insertBookmarkAtStart(internalPdfDocument,
                    pageIndex, text);
        } else {
            Bookmark_Api.insertBookmark(internalPdfDocument, pageIndex,
                    text, lastBookmarks.getParentBookmarkText(), lastBookmarks.getText());
        }
    }

    /**
     * Add a new bookmark at the start of the document collection
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public final void addBookMarkAtStart(String text, int pageIndex) {

        List<com.ironsoftware.ironpdf.bookmark.Bookmark> bookmarks = this.getBookmarks();
        com.ironsoftware.ironpdf.bookmark.Bookmark firstBookmarks =
                bookmarks.size() == 0 ? null : bookmarks.get(0);

        Bookmark_Api.insertBookmarkAtStart(internalPdfDocument,
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
    public final void insertBookmark(String text, int pageIndex, String parentText,
                                     String previousText) {
        Bookmark_Api.insertBookmark(internalPdfDocument, pageIndex,
                text, parentText, previousText);
    }

    /**
     * Insert a new bookmark
     *
     * @param bookmark a bookmark object.
     */
    public final void insertBookmark(Bookmark bookmark) {
        Bookmark_Api.insertBookmark(internalPdfDocument,
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
    public final com.ironsoftware.ironpdf.bookmark.Bookmark addChildBookmark(String text,
                                                                             int pageIndex, String parentBookmarkText) {

        Bookmark_Api.insertBookmark(internalPdfDocument, pageIndex,
                text, parentBookmarkText, null);

        //hacks to get the new bookmark, as the static api doesn't return it.
        Optional<com.ironsoftware.ironpdf.bookmark.Bookmark> optNewBookmark = Bookmark_Api.getBookmarks(
                        internalPdfDocument).stream().filter(x -> x.getText().equals(text)
                        && x.getPageIndex() == pageIndex
                        && x.getParentBookmarkText().equals(parentBookmarkText))
                .findFirst();
        optNewBookmark.ifPresent(x -> x.setBookmarkManager(this));
        return optNewBookmark.orElse(null);
    }
}
