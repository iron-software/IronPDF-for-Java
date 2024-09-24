package com.ironsoftware.ironpdf.bookmark;

import javax.annotation.Nullable;

/**
 * Represents a PDF bookmark as seen in the sidebar of PDF reader software to help users navigate.
 * <p>Bookmarks are arranged and navigated in a parent/child node hierarchy, similar to an HTML DOM.</p>
 */
public class Bookmark {

    /**
     * The text of next bookmark at the current level, if any. Null if it is last bookmark.
     */
    private final String nextBookmarkText;
    /**
     * The text of parent bookmark which contains this bookmark. Null if it is root bookmark.
     */
    private final String parentBookmarkText;
    /**
     * The zero based page number that the bookmark links to.
     */
    private final int pageIndex;
    /**
     * Previous bookmark at the current level, if any. Null if it is first bookmark.
     */
    private final String previousBookmarkText;
    /**
     * Type of destination represented by the bookmark
     */
    private final BookmarkDestinations destinationType;
    /**
     * The display text of the bookmark
     */
    private String text;
    private BookmarkManager bookmarkManager;


    /**
     * Instantiates a new Bookmark.
     *
     * @param text                 the display text of the bookmark
     * @param pageIndex            the zero based page number that the bookmark links to.
     * @param parentBookmarkText   the text of parent bookmark which contains this bookmark. Null if it is root bookmark.
     * @param destinationType      the type of destination represented by the bookmark
     * @param nextBookmarkText     the text of next bookmark at the current level, if any. Null if it is last bookmark.
     * @param previousBookmarkText the previous bookmark at the current level, if any. Null if it is first bookmark.
     */
    public Bookmark(@javax.annotation.Nonnull String text, int pageIndex, @Nullable String parentBookmarkText,
                    BookmarkDestinations destinationType,
                    @Nullable String nextBookmarkText, @Nullable String previousBookmarkText) {
        this.setText(text);
        this.pageIndex = pageIndex;
        this.parentBookmarkText = parentBookmarkText;
        this.nextBookmarkText = nextBookmarkText;
        this.previousBookmarkText = previousBookmarkText;
        this.destinationType = destinationType;
    }

    /**
     * Gets destination type. Type of destination represented by the bookmark.
     *
     * @return the destination type
     */
    public final BookmarkDestinations getDestinationType() {
        return destinationType;
    }

    /**
     * Gets next bookmark text. The text of next bookmark at the current level, if any. Null if it is last bookmark.
     *
     * @return the next bookmark text
     */
    public final String getNextBookmarkText() {
        return nextBookmarkText;
    }

    /**
     * Gets page index. The zero based page number that the bookmark links to.
     *
     * @return the page index
     */
    public final int getPageIndex() {
        return pageIndex;
    }

    /**
     * Gets bookmark manager. {@link BookmarkManager}
     *
     * @return the bookmark manager
     */
    public final BookmarkManager getBookmarkManager() {
        return bookmarkManager;
    }

    /**
     * Sets bookmark manager.
     *
     * @param value the BookmarkManager
     */
    void setBookmarkManager(BookmarkManager value) {
        bookmarkManager = value;
    }

    /**
     * Add a new bookmark after this bookmark
     *
     * @param text      the display text of the bookmark
     * @param pageIndex the zero based page number that the bookmark links to.
     */
    public final void addNextBookmark(String text, int pageIndex) {
        if (bookmarkManager == null) {
            throw new RuntimeException(
                    "not found a BookmarkManager, Please get this Bookmark object from PdfDocument.getBookmarkManager()");
        }
        bookmarkManager.insertBookmark(text, pageIndex, this.getParentBookmarkText(), this.getText());
    }

//region API

    /**
     * Gets parent bookmark text. The text of parent bookmark which contains this bookmark. Null if it is root bookmark.
     *
     * @return the parent bookmark text
     */
    public final String getParentBookmarkText() {
        return parentBookmarkText;
    }

    /**
     * Gets text. The display text of the bookmark.
     *
     * @return the text
     */
    public final String getText() {
        return text;
    }

    /**
     * Sets text. The display text of the bookmark
     *
     * @param value the value
     */
    public final void setText(String value) {
        text = value;
    }

    /**
     * Add a new bookmark before this bookmark
     *
     * @param text      the display text of the bookmark
     * @param pageIndex the zero based page number that the bookmark links to.
     */
    public final void AddPreviousBookmark(String text, int pageIndex) {
        if (bookmarkManager == null) {
            throw new RuntimeException(
                    "not found a BookmarkManager, Please get this Bookmark object from PdfDocument.getBookmarkManager()");
        }
        bookmarkManager.insertBookmark(text, pageIndex, this.getParentBookmarkText(),
                this.getPreviousBookmarkText());
    }

    /**
     * Gets previous bookmark text. Previous bookmark at the current level, if any. Null if it is first bookmark.
     *
     * @return the previous bookmark text.
     */
    public final String getPreviousBookmarkText() {
        return previousBookmarkText;
    }

    /**
     * Add a new bookmark as a first child of this bookmark.
     * <p>To add a bookmark as a second child, please navigate to the childBookmark object and call AddNextBookmark.</p>
     *
     * @param text      the display text of the bookmark
     * @param pageIndex the zero based page number that the bookmark links to.
     * @return a new child bookmark
     */
    public final com.ironsoftware.ironpdf.bookmark.Bookmark AddChildBookmark(String text,
                                                                             int pageIndex) {
        return bookmarkManager.addChildBookmark(text, pageIndex, this.getText());
    }

    //endregion
}
