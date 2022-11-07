package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.bookmark.BookmarkDestinations;
import com.ironsoftware.ironpdf.bookmark.BookmarkManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class BookmarkTests extends TestBase {

    @Test
    public final void GetBookmarksTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        BookmarkManager bookmarkManager = doc.getBookmark();
        Assertions.assertTrue(bookmarkManager.getBookmarks().isEmpty());
    }

    @Test
    public final void InsertBookmarkTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        BookmarkManager bookmarkManager = doc.getBookmark();
        bookmarkManager.addBookMarkAtStart("B1", 0);
        bookmarkManager.addChildBookmark("B2", 0, "B1");
        bookmarkManager.addChildBookmark("B3", 0, "B1");
        bookmarkManager.insertBookmark("B4", 0, "B1", "B3");
        bookmarkManager.addBookMarkAtStart("B5", 0);
        //todo check addBookMarkAtEnd

        List<Bookmark> bookmarkCollection = bookmarkManager.getBookmarks();

        Assertions.assertEquals("B5", bookmarkCollection.get(0).getText());
        AssertNullOrEmpty(bookmarkCollection.get(0).getParentBookmarkText());
        Assertions.assertEquals(BookmarkDestinations.PAGE,
                bookmarkCollection.get(0).getDestinationType());
        Assertions.assertEquals("B1", bookmarkCollection.get(0).getNextBookmarkText());
        AssertNullOrEmpty(bookmarkCollection.get(0).getPreviousBookmarkText());
        Assertions.assertEquals(0, bookmarkCollection.get(0).getPageIndex());

        Assertions.assertEquals("B1", bookmarkCollection.get(1).getText());
        AssertNullOrEmpty(bookmarkCollection.get(1).getParentBookmarkText());
        Assertions.assertEquals(BookmarkDestinations.PAGE,
                bookmarkCollection.get(1).getDestinationType());
        AssertNullOrEmpty(bookmarkCollection.get(1).getNextBookmarkText());
        Assertions.assertEquals("B5", bookmarkCollection.get(1).getPreviousBookmarkText());
        Assertions.assertEquals(0, bookmarkCollection.get(1).getPageIndex());

        Assertions.assertEquals("B3", bookmarkCollection.get(2).getText());
        Assertions.assertEquals("B1", bookmarkCollection.get(2).getParentBookmarkText());
        Assertions.assertEquals(BookmarkDestinations.PAGE,
                bookmarkCollection.get(2).getDestinationType());
        Assertions.assertEquals("B4", bookmarkCollection.get(2).getNextBookmarkText());
        AssertNullOrEmpty(bookmarkCollection.get(2).getPreviousBookmarkText());
        Assertions.assertEquals(0, bookmarkCollection.get(2).getPageIndex());

        Assertions.assertEquals("B4", bookmarkCollection.get(3).getText());
        Assertions.assertEquals("B1", bookmarkCollection.get(3).getParentBookmarkText());
        Assertions.assertEquals(BookmarkDestinations.PAGE,
                bookmarkCollection.get(3).getDestinationType());
        Assertions.assertEquals("B2", bookmarkCollection.get(3).getNextBookmarkText());
        Assertions.assertEquals("B3", bookmarkCollection.get(3).getPreviousBookmarkText());
        Assertions.assertEquals(0, bookmarkCollection.get(3).getPageIndex());

        Assertions.assertEquals("B2", bookmarkCollection.get(4).getText());
        Assertions.assertEquals("B1", bookmarkCollection.get(4).getParentBookmarkText());
        Assertions.assertEquals(BookmarkDestinations.PAGE,
                bookmarkCollection.get(4).getDestinationType());
        AssertNullOrEmpty(bookmarkCollection.get(4).getNextBookmarkText());
        Assertions.assertEquals("B4", bookmarkCollection.get(4).getPreviousBookmarkText());
        Assertions.assertEquals(0, bookmarkCollection.get(4).getPageIndex());

    }

}
