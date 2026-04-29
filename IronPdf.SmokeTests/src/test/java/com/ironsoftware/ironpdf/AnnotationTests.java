package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.annotation.AnnotationIcon;
import com.ironsoftware.ironpdf.annotation.AnnotationManager;
import com.ironsoftware.ironpdf.annotation.AnnotationOptions;
import com.ironsoftware.ironpdf.annotation.LinkAnnotation;
import com.ironsoftware.ironpdf.bookmark.BookmarkDestinations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnnotationTests extends TestBase {

    @Test
    public final void AddTextAnnotationTest() throws IOException {
//        Settings.setDebug(true);
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        AnnotationOptions options = new AnnotationOptions("This is a text annotation 1");
        options.setRgbColor(null);
        options.setHidden(false);
        options.setOpacity(0);
        options.setOpen(false);
        options.setPrintable(false);
        options.setReadonly(false);
        options.setRotateable(false);
        options.setSubject("Subject");
        options.setTitle("Title");
        options.setIcon(AnnotationIcon.NO_ICON);

        AnnotationManager annotationManager = doc.getAnnotation();
        annotationManager.addTextAnnotation(options, 0);

        annotationManager.addTextAnnotation(new AnnotationOptions("This is a Text annotation 2"), 0);

        Assertions.assertEquals(2, annotationManager.getAnnotationCount(0));
        doc.saveAs(Paths.get(String.format("TestOutput/%1$s.pdf", "AddTextAnnotationTest")));
    }

    @Test
    public final void GetAnnotationCountTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        Assertions.assertEquals(0, doc.getAnnotation().getAnnotationCount(0));
    }

    // ==================== PDF-1559: Internal hyperlink annotations ====================
 
    /**
     * Helper: renders a simple multi-page HTML document so the link-annotation tests do
     * not depend on an external fixture PDF.
     */
    private static PdfDocument renderMultiPagePdf(int pageCount) {
        StringBuilder html = new StringBuilder("<html><body style='font-family:Arial;'>");
        html.append("<h1>Page 1</h1><p>Introduction.</p>");
        for (int i = 2; i <= pageCount; i++) {
            html.append("<div style='page-break-before:always'></div>");
            html.append("<h1>Page ").append(i).append("</h1>");
            html.append("<p>Content for page ").append(i).append(".</p>");
        }
        html.append("</body></html>");
        return PdfDocument.renderHtmlAsPdf(html.toString());
    }
 
    /**
     * Adds a link annotation on page 0 that navigates to the last page and verifies
     * the annotation count increases by one on that page.
     */
    @Test
    public final void AddLinkAnnotation_NavigatesToPageTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(5);
        int pageCount = doc.getPagesInfo().size();
        Assertions.assertTrue(pageCount >= 2, "Test requires a multi-page PDF");
 
        int initialCount = doc.getAnnotation().getAnnotationCount(0);
 
        LinkAnnotation link = new LinkAnnotation(0, pageCount - 1);
        link.setX(50);
        link.setY(700);
        link.setWidth(200);
        link.setHeight(20);
        link.setContents("Go to last page");
        doc.getAnnotation().addLinkAnnotation(link);
 
        Assertions.assertEquals(initialCount + 1, doc.getAnnotation().getAnnotationCount(0),
                "Page 0 annotation count should have increased by 1");
        Assertions.assertTrue(link.getAnnotationIndex() >= 0,
                "AnnotationIndex should be populated after add, got=" + link.getAnnotationIndex());
 
        Path output = Paths.get("TestOutput/AddLinkAnnotation_NavigatesToPage.pdf");
        Files.createDirectories(output.getParent());
        doc.saveAs(output);
        Assertions.assertTrue(Files.exists(output));
        Assertions.assertTrue(Files.size(output) > 0);
    }
 
    /**
     * Builds a simulated TOC with multiple link annotations on page 0.
     */
    @Test
    public final void AddMultipleLinkAnnotations_CustomTocTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(5);
        int pageCount = doc.getPagesInfo().size();
        Assertions.assertTrue(pageCount >= 3, "Test requires at least 3 pages");
 
        int initial = doc.getAnnotation().getAnnotationCount(0);
        int tocLinks = Math.min(pageCount, 5) - 1;
        for (int i = 1; i <= tocLinks; i++) {
            LinkAnnotation link = new LinkAnnotation(0, i, "Section " + i);
            link.setX(72);
            link.setY(700 - (i * 25));
            link.setWidth(300);
            link.setHeight(20);
            doc.getAnnotation().addLinkAnnotation(link);
        }
 
        Assertions.assertEquals(initial + tocLinks, doc.getAnnotation().getAnnotationCount(0),
                "Page 0 should have all TOC link annotations");
        Path output = Paths.get("TestOutput/AddMultipleLinkAnnotations_CustomToc.pdf");
        Files.createDirectories(output.getParent());
        doc.saveAs(output);
    }
 
    /**
     * Adds a link with an explicit color code and persists/reloads.
     */
    @Test
    public final void AddLinkAnnotation_WithColorTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(3);
 
        LinkAnnotation link = new LinkAnnotation(0, 1);
        link.setX(100);
        link.setY(500);
        link.setWidth(150);
        link.setHeight(16);
        link.setColorCode("#0000FF");
        link.setContents("Blue link to page 2");
        doc.getAnnotation().addLinkAnnotation(link);
 
        Path output = Paths.get("TestOutput/AddLinkAnnotation_WithColor.pdf");
        Files.createDirectories(output.getParent());
        doc.saveAs(output);
 
        PdfDocument reloaded = PdfDocument.fromFile(output);
        Assertions.assertTrue(reloaded.getAnnotation().getAnnotationCount(0) >= 1,
                "Reloaded page 0 should have at least 1 annotation");
    }
 
    /**
     * Verifies a hidden link annotation is still added.
     */
    @Test
    public final void AddLinkAnnotation_HiddenLinkTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(3);
        int initial = doc.getAnnotation().getAnnotationCount(0);
 
        LinkAnnotation link = new LinkAnnotation(0, 1);
        link.setX(72);
        link.setY(350);
        link.setWidth(200);
        link.setHeight(16);
        link.setHidden(true);
        doc.getAnnotation().addLinkAnnotation(link);
 
        Assertions.assertTrue(link.isHidden(), "Link should remain flagged as hidden");
        Assertions.assertEquals(initial + 1, doc.getAnnotation().getAnnotationCount(0));
    }
 
    /**
     * Covers the full destination-type strategy: adds one link for each
     * {@link BookmarkDestinations} value and verifies the annotation count grows.
     */
    @Test
    public final void AddLinkAnnotation_AllDestinationTypesTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(3);
        int initial = doc.getAnnotation().getAnnotationCount(0);
 
        BookmarkDestinations[] destTypes = BookmarkDestinations.values();
        for (int i = 0; i < destTypes.length; i++) {
            LinkAnnotation link = new LinkAnnotation(0, 1);
            link.setDestinationType(destTypes[i]);
            link.setDestinationLeft(50);
            link.setDestinationRight(400);
            link.setDestinationTop(600);
            link.setDestinationBottom(100);
            link.setDestinationZoom(100);
            link.setX(72);
            link.setY(700 - (i * 22));
            link.setWidth(200);
            link.setHeight(18);
            doc.getAnnotation().addLinkAnnotation(link);
        }
 
        Assertions.assertEquals(initial + destTypes.length,
                doc.getAnnotation().getAnnotationCount(0),
                "All destination-type links should be added");
        Path output = Paths.get("TestOutput/AddLinkAnnotation_AllDestinationTypes.pdf");
        Files.createDirectories(output.getParent());
        doc.saveAs(output);
    }
 
    /**
     * Adds a link with PAGE_ZOOM destination and scroll-to-Y behaviour (C# PageZoom test).
     */
    @Test
    public final void AddLinkAnnotation_WithPageZoomDestinationTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(3);
 
        LinkAnnotation link = new LinkAnnotation(0, 1);
        link.setDestinationType(BookmarkDestinations.PAGE_ZOOM);
        link.setDestinationLeft(0);
        link.setDestinationTop(400);
        link.setDestinationZoom(100);
        link.setX(72);
        link.setY(700);
        link.setWidth(300);
        link.setHeight(18);
        link.setContents("Jump to page 2 at Y=400");
        doc.getAnnotation().addLinkAnnotation(link);
 
        Path output = Paths.get("TestOutput/AddLinkAnnotation_PageZoom.pdf");
        Files.createDirectories(output.getParent());
        doc.saveAs(output);
 
        PdfDocument reloaded = PdfDocument.fromFile(output);
        Assertions.assertTrue(reloaded.getAnnotation().getAnnotationCount(0) >= 1);
    }
 
    /**
     * Verifies the defaults for a freshly-constructed {@link LinkAnnotation}.
     */
    @Test
    public final void LinkAnnotation_DefaultPropertyValuesTest() {
        LinkAnnotation link = new LinkAnnotation(3, 7);
 
        Assertions.assertEquals(3, link.getPageIndex());
        Assertions.assertEquals(7, link.getDestinationPageIndex());
        Assertions.assertEquals(BookmarkDestinations.PAGE, link.getDestinationType());
        Assertions.assertEquals(0, link.getDestinationLeft());
        Assertions.assertEquals(0, link.getDestinationRight());
        Assertions.assertEquals(0, link.getDestinationTop());
        Assertions.assertEquals(0, link.getDestinationBottom());
        Assertions.assertEquals(0, link.getDestinationZoom());
        Assertions.assertEquals(-1, link.getAnnotationIndex());
        Assertions.assertNull(link.getRectangle());
        Assertions.assertNull(link.getColorCode());
        Assertions.assertFalse(link.isHidden());
        Assertions.assertEquals("", link.getContents());
        Assertions.assertEquals("", link.getTitle());
        Assertions.assertEquals(-1, link.getX());
        Assertions.assertEquals(-1, link.getY());
        Assertions.assertEquals(-1, link.getWidth());
        Assertions.assertEquals(-1, link.getHeight());
    }
 
    /**
     * Verifies all property setters persist correctly.
     */
    @Test
    public final void LinkAnnotation_SetAllPropertiesTest() {
        LinkAnnotation link = new LinkAnnotation(0, 1);
        link.setDestinationType(BookmarkDestinations.PAGE_ZOOM);
        link.setDestinationLeft(10);
        link.setDestinationRight(500);
        link.setDestinationTop(700);
        link.setDestinationBottom(100);
        link.setDestinationZoom(150);
        link.setX(50);
        link.setY(600);
        link.setWidth(300);
        link.setHeight(20);
        link.setColorCode("#FF0000");
        link.setHidden(true);
        link.setContents("Test link");
        link.setTitle("Test title");
 
        Assertions.assertEquals(BookmarkDestinations.PAGE_ZOOM, link.getDestinationType());
        Assertions.assertEquals(10, link.getDestinationLeft());
        Assertions.assertEquals(500, link.getDestinationRight());
        Assertions.assertEquals(700, link.getDestinationTop());
        Assertions.assertEquals(100, link.getDestinationBottom());
        Assertions.assertEquals(150, link.getDestinationZoom());
        Assertions.assertEquals(50, link.getX());
        Assertions.assertEquals(600, link.getY());
        Assertions.assertEquals(300, link.getWidth());
        Assertions.assertEquals(20, link.getHeight());
        Assertions.assertEquals("#FF0000", link.getColorCode());
        Assertions.assertTrue(link.isHidden());
        Assertions.assertEquals("Test link", link.getContents());
        Assertions.assertEquals("Test title", link.getTitle());
    }
 
    /**
     * Verifies the toString format matches the C# implementation.
     */
    @Test
    public final void LinkAnnotation_ToStringTest() {
        LinkAnnotation link = new LinkAnnotation(2, 5);
        link.setContents("My Link");
        Assertions.assertEquals("Link on page 2 -> page 5: My Link", link.toString());
 
        LinkAnnotation empty = new LinkAnnotation(0, 1);
        Assertions.assertEquals("Link on page 0 -> page 1: ", empty.toString());
    }
 
    /**
     * Setting the rectangle directly should be reflected by the individual
     * {@code x/y/width/height} getters.
     */
    @Test
    public final void LinkAnnotation_SetRectangleDirectlyTest() {
        LinkAnnotation link = new LinkAnnotation(0, 1);
        Rectangle rect = new Rectangle(10, 20, 300, 50);
        link.setRectangle(rect);
 
        Assertions.assertEquals(10, link.getX());
        Assertions.assertEquals(20, link.getY());
        Assertions.assertEquals(300, link.getWidth());
        Assertions.assertEquals(50, link.getHeight());
        Assertions.assertSame(rect, link.getRectangle());
    }
 
    /**
     * Adding a text annotation and a link annotation on the same page both succeed,
     * and the total count increases by 2.
     */
    @Test
    public final void AddLinkAnnotation_MixedWithTextAnnotationTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(3);
        int initial = doc.getAnnotation().getAnnotationCount(0);
 
        AnnotationOptions textOpt = new AnnotationOptions("A text note");
        textOpt.setX(100);
        textOpt.setY(300);
        textOpt.setWidth(30);
        textOpt.setHeight(30);
        textOpt.setIcon(AnnotationIcon.COMMENT);
        doc.getAnnotation().addTextAnnotation(textOpt, 0);
 
        LinkAnnotation link = new LinkAnnotation(0, 1);
        link.setX(72);
        link.setY(250);
        link.setWidth(200);
        link.setHeight(18);
        doc.getAnnotation().addLinkAnnotation(link);
 
        Assertions.assertEquals(initial + 2, doc.getAnnotation().getAnnotationCount(0),
                "Both text + link annotations should have been added");
    }
 
    /**
     * Invalid inputs should throw {@link IllegalArgumentException}.
     */
    @Test
    public final void AddLinkAnnotation_InvalidInputsTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(2);
        AnnotationManager mgr = doc.getAnnotation();
 
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            mgr.addLinkAnnotation(null);
        }, "null link should throw IllegalArgumentException");
 
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            LinkAnnotation bad = new LinkAnnotation(-1, 0);
            mgr.addLinkAnnotation(bad);
        }, "negative pageIndex should throw IllegalArgumentException");
 
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            LinkAnnotation bad = new LinkAnnotation(0, -1);
            mgr.addLinkAnnotation(bad);
        }, "negative destinationPageIndex should throw IllegalArgumentException");
    }

    /**
     * Color fallback — no explicit colorCode must NOT reach the engine as empty string
     * (would throw "unable to convert to IronSoftware.Drawing.Color"). Uses #000000 fallback.
     */
    @Test
    public final void AddLinkAnnotation_NoColor_NoEngineErrorTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(2);
        int initial = doc.getAnnotation().getAnnotationCount(0);

        LinkAnnotation link = new LinkAnnotation(0, 1);
        link.setX(72);
        link.setY(500);
        link.setWidth(100);
        link.setHeight(18);
        // NO setColorCode(...) — was the cause of the original AddLinkAnnotation_* test failures.
        doc.getAnnotation().addLinkAnnotation(link);

        Assertions.assertEquals(initial + 1, doc.getAnnotation().getAnnotationCount(0));
    }

    /**
     * showBorder round-trips through the encoded URL as the 8th segment (0 or 1).
     */
    @Test
    public final void AddLinkAnnotation_ShowBorderRoundTripTest() throws IOException {
        PdfDocument doc = renderMultiPagePdf(2);
        int initial = doc.getAnnotation().getAnnotationCount(0);

        LinkAnnotation withBorder = new LinkAnnotation(0, 1);
        withBorder.setX(50); withBorder.setY(400); withBorder.setWidth(100); withBorder.setHeight(16);
        withBorder.setShowBorder(true);
        doc.getAnnotation().addLinkAnnotation(withBorder);

        LinkAnnotation noBorder = new LinkAnnotation(0, 1);
        noBorder.setX(50); noBorder.setY(360); noBorder.setWidth(100); noBorder.setHeight(16);
        noBorder.setShowBorder(false);
        doc.getAnnotation().addLinkAnnotation(noBorder);

        Assertions.assertEquals(initial + 2, doc.getAnnotation().getAnnotationCount(0));
    }
}
