package com.ironsoftware.ironpdf.bookmark;

import java.io.IOException;

/**
 * Represents a PDF bookmark as seen in the sidebar of PDF reader software to help user's navigate.
 */
public class Bookmark {

    /**
     * Next bookmark at the current level, if any
     */
    private final String NextBookmarkText;
    /**
     * Bookmark which contains this bookmark
     */
    private final String ParentBookmarkText;
    /**
     * The zero based page number that the bookmark links to.
     */
    private final int PageIndex;
    /**
     * Previous bookmark at the current level, if any
     */
    private final String PreviousBookmarkText;
    /**
     * Type of destination represented by the bookmark
     */
    private final BookmarkDestinations DestinationType;
    /**
     * The display text of the bookmark
     */
    private String Text;
    private BookmarkManager bookmarkManager;

    public Bookmark(String text, int pageIndex, String parentBookmarkText,
                    BookmarkDestinations destinationType,
                    String nextBookmarkText, String previousBookmarkText) {
        this.setText(text);
        this.PageIndex = pageIndex;
        this.ParentBookmarkText = parentBookmarkText;
        this.NextBookmarkText = nextBookmarkText;
        this.PreviousBookmarkText = previousBookmarkText;
        this.DestinationType = destinationType;
    }

    public final BookmarkDestinations getDestinationType() {
        return DestinationType;
    }

    public final String getNextBookmarkText() {
        return NextBookmarkText;
    }

    public final int getPageIndex() {
        return PageIndex;
    }

    public final BookmarkManager getBookmarkManager() {
        return bookmarkManager;
    }

    void setBookmarkManager(BookmarkManager value) {
        bookmarkManager = value;
    }

    /**
     * Add a new bookmark after this bookmark
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public final void AddNextBookmark(String text, int pageIndex) throws IOException {
        if (bookmarkManager == null) {
            throw new RuntimeException(
                    "not found a BookmarkManager, Please get this Bookmark object from PdfDocument.getBookmarkManager()");
        }
        bookmarkManager.InsertBookmark(text, pageIndex, this.getParentBookmarkText(), this.getText());
    }

//region API

    public final String getParentBookmarkText() {
        return ParentBookmarkText;
    }

    public final String getText() {
        return Text;
    }

    public final void setText(String value) {
        Text = value;
    }

    /**
     * Add a new bookmark before this bookmark
     *
     * @param text      The display text for the link.
     * @param pageIndex The zero based page number to link to.  E.g.  Page 1 has a PageIndex of 0
     */
    public final void AddPreviousBookmark(String text, int pageIndex) throws IOException {
        if (bookmarkManager == null) {
            throw new RuntimeException(
                    "not found a BookmarkManager, Please get this Bookmark object from PdfDocument.getBookmarkManager()");
        }
        bookmarkManager.InsertBookmark(text, pageIndex, this.getParentBookmarkText(),
                this.getPreviousBookmarkText());
    }

    public final String getPreviousBookmarkText() {
        return PreviousBookmarkText;
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
                                                                             int pageIndex) throws IOException {
        return bookmarkManager.AddChildBookmark(text, pageIndex, this.getText());
    }

    //endregion
}
