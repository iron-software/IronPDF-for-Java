package com.ironsoftware.ironpdf;
 
import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.LinearizationMode;
import com.ironsoftware.ironpdf.render.RenderedElementLocation;
 
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
 
/**
 * RC Tests for IronPdf Java 2026.5
 * Mapped from C# release notes — covers:
 *   - PDF-2132: linearization in-memory bytes (with LinearizationMode parity)
 *   - PDF-2172: auto-bookmarks from HTML headings + element query selectors
 *               + getElementLocations() retrieval
 */
public class RCTests2026_05 extends TestBase {
 
    private static final String RC_DIR = "/Data/RC_2026_05";
 
    // ==================== PDF-2132: Linearization in-memory ====================
 
    /**
     * Test 01: linearizePdfToBytes — instance method on a rendered PDF.
     * Validates that the returned bytes are a valid linearized PDF.
     */
    @Test
    public final void Test01_LinearizePdfToBytes_Instance() throws IOException {
        String html = "<!DOCTYPE html><html><body>"
                + "<h1>Linearization Test</h1>"
                + "<p>Document to be linearized in memory.</p>"
                + "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);
 
        byte[] linearized = pdf.linearizePdfToBytes();
 
        Assertions.assertNotNull(linearized, "Linearized bytes should not be null");
        Assertions.assertTrue(linearized.length > 0, "Linearized bytes should not be empty");
 
        // Verify the result is a valid PDF and is actually linearized.
        Path outputFile = Paths.get("TestOutput/Test01_LinearizePdfToBytes.pdf");
        Files.createDirectories(outputFile.getParent());
        Files.write(outputFile, linearized);
        Assertions.assertTrue(Files.exists(outputFile));
 
        boolean isLinearized = PdfDocument.isLinearized(outputFile);
        Assertions.assertTrue(isLinearized, "Output should be linearized");
 
        System.out.println("Test01 (PDF-2132): linearizePdfToBytes produced "
                + linearized.length + " bytes, isLinearized=" + isLinearized);
    }
 
    /**
     * Test 02: linearizePdfToStream — instance method.
     * Validates that the stream contains linearized PDF bytes.
     */
    @Test
    public final void Test02_LinearizePdfToStream_Instance() throws IOException {
        String html = "<html><body><h1>Stream Linearize</h1><p>Content.</p></body></html>";
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);
 
        try (InputStream stream = pdf.linearizePdfToStream()) {
            Assertions.assertNotNull(stream);
            Assertions.assertTrue(stream instanceof ByteArrayInputStream,
                    "linearizePdfToStream should return a ByteArrayInputStream");
            int available = stream.available();
            Assertions.assertTrue(available > 0, "Stream should have content");
            System.out.println("Test02 (PDF-2132): linearizePdfToStream produced " + available + " bytes");
        }
    }
 
    /**
     * Test 03: Static linearizePdfToBytes(byte[]) — bytes in, bytes out.
     */
    @Test
    public final void Test03_LinearizePdfToBytes_StaticFromBytes() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>Static From Bytes</h1></body></html>");
        byte[] inputBytes = pdf.getBinaryData();
 
        byte[] linearized = PdfDocument.linearizePdfToBytes(inputBytes);
        Assertions.assertNotNull(linearized);
        Assertions.assertTrue(linearized.length > 0);
 
        Path outputFile = Paths.get("TestOutput/Test03_LinearizeFromBytes.pdf");
        Files.createDirectories(outputFile.getParent());
        Files.write(outputFile, linearized);
        Assertions.assertTrue(PdfDocument.isLinearized(outputFile),
                "Output should be linearized (static bytes overload)");
        System.out.println("Test03 (PDF-2132): static linearizePdfToBytes produced "
                + linearized.length + " bytes");
    }
 
    /**
     * Test 04: Static linearizePdfToBytes(InputStream) — stream in, bytes out.
     */
    @Test
    public final void Test04_LinearizePdfToBytes_StaticFromStream() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>Static From Stream</h1></body></html>");
        byte[] inputBytes = pdf.getBinaryData();
 
        byte[] linearized;
        try (InputStream input = new ByteArrayInputStream(inputBytes)) {
            linearized = PdfDocument.linearizePdfToBytes(input);
        }
 
        Assertions.assertNotNull(linearized);
        Assertions.assertTrue(linearized.length > 0);
        System.out.println("Test04 (PDF-2132): static linearizePdfToBytes(InputStream) produced "
                + linearized.length + " bytes");
    }
 
    /**
     * Test 05: saveAsLinearized — instance method writes a linearized file to disk.
     */
    @Test
    public final void Test05_SaveAsLinearized_Instance() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>SaveAsLinearized</h1><p>Disk write.</p></body></html>");
 
        Path outputFile = Paths.get("TestOutput/Test05_SaveAsLinearized.pdf");
        Files.createDirectories(outputFile.getParent());
 
        pdf.saveAsLinearized(outputFile.toString());
 
        Assertions.assertTrue(Files.exists(outputFile), "Linearized file should exist");
        Assertions.assertTrue(Files.size(outputFile) > 0, "File should not be empty");
        Assertions.assertTrue(PdfDocument.isLinearized(outputFile),
                "Saved file should report as linearized");
        System.out.println("Test05 (PDF-2132): saveAsLinearized produced "
                + Files.size(outputFile) + " bytes");
    }
 
    /**
     * Test 06: Static saveAsLinearized(byte[], path) — bytes in, file out.
     */
    @Test
    public final void Test06_SaveAsLinearized_StaticFromBytes() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>Static Save</h1></body></html>");
        byte[] inputBytes = pdf.getBinaryData();
 
        Path outputFile = Paths.get("TestOutput/Test06_SaveAsLinearized_Static.pdf");
        Files.createDirectories(outputFile.getParent());
 
        PdfDocument.saveAsLinearized(inputBytes, outputFile.toString());
 
        Assertions.assertTrue(Files.exists(outputFile));
        Assertions.assertTrue(Files.size(outputFile) > 0);
        Assertions.assertTrue(PdfDocument.isLinearized(outputFile));
        System.out.println("Test06 (PDF-2132): static saveAsLinearized produced "
                + Files.size(outputFile) + " bytes");
    }
 
    /**
     * Test 07: linearizePdfToBytes edge cases — null and empty input should throw.
     */
    @Test
    public final void Test07_LinearizePdfToBytes_InvalidInputs() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            PdfDocument.linearizePdfToBytes((byte[]) null);
        }, "null byte[] input should throw IllegalArgumentException");
 
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            PdfDocument.linearizePdfToBytes(new byte[0]);
        }, "empty byte[] input should throw IllegalArgumentException");
 
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            PdfDocument.linearizePdfToBytes((InputStream) null);
        }, "null InputStream input should throw IllegalArgumentException");
 
        System.out.println("Test07 (PDF-2132): invalid input validation OK");
    }
 
    /**
     * Test 08: Pre-existing non-linearized fixture round-trip.
     * If a fixture PDF is available under RC_DIR/unlinearized.pdf this verifies:
     *   1) isLinearized returns false for the source
     *   2) linearizePdfToBytes succeeds
     *   3) the resulting bytes write out to a file that isLinearized returns true for
     * When the fixture is absent the test is skipped with a note.
     */
    @Test
    public final void Test08_LinearizeExistingFile() throws IOException {
        Path source = getTestPath(RC_DIR + "/unlinearized.pdf");
        if (!Files.exists(source)) {
            System.out.println("Test08 (PDF-2132): SKIPPED — fixture " + source + " not present");
            return;
        }
 
        Assertions.assertFalse(PdfDocument.isLinearized(source),
                "Fixture should not be linearized to start with");
 
        byte[] sourceBytes = Files.readAllBytes(source);
        byte[] linearized = PdfDocument.linearizePdfToBytes(sourceBytes);
 
        Path outputFile = Paths.get("TestOutput/Test08_LinearizeExisting.pdf");
        Files.createDirectories(outputFile.getParent());
        Files.write(outputFile, linearized);
 
        Assertions.assertTrue(PdfDocument.isLinearized(outputFile),
                "Linearized output should report as linearized");
        System.out.println("Test08 (PDF-2132): fixture linearized from "
                + sourceBytes.length + " -> " + linearized.length + " bytes");
    }
 
    // ==================== PDF-2172: Auto-bookmarks from HTML headings ====================
 
    /**
     * Test 09: Auto-bookmarks generated from &lt;h1&gt;…&lt;h6&gt; when enabled.
     * Verifies that the resulting PdfDocument has at least one auto-generated bookmark.
     */
    @Test
    public final void Test09_AutoBookmarksFromHeadings_Enabled() throws IOException {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setAutoBookmarksFromHeadings(true);
        options.setAutoBookmarkMinHeadingLevel(1);
        options.setAutoBookmarkMaxHeadingLevel(3);
 
        String html = "<html><body>"
                + "<h1>Chapter 1</h1><p>Intro paragraph.</p>"
                + "<h2>Section 1.1</h2><p>Details.</p>"
                + "<h3>Sub-section 1.1.1</h3><p>More details.</p>"
                + "<h1>Chapter 2</h1><p>Another chapter.</p>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);
 
        Path outputFile = Paths.get("TestOutput/Test09_AutoBookmarks_Enabled.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);
 
        List<Bookmark> bookmarks = pdf.getBookmark().getBookmarks();
        Assertions.assertNotNull(bookmarks, "Bookmarks list should not be null");
        Assertions.assertFalse(bookmarks.isEmpty(),
                "Auto-bookmarks should be generated when enabled. Count=" + bookmarks.size());
        System.out.println("Test09 (PDF-2172): auto-bookmarks enabled -> "
                + bookmarks.size() + " bookmarks generated");
    }
 
    /**
     * Test 10: Baseline — rendering the SAME headings twice should yield strictly fewer
     * bookmarks when {@code autoBookmarksFromHeadings} is left at its default (false) than
     * when it is enabled. This verifies the option actually drives engine behavior rather
     * than asserting an exact count, which would be brittle against engine defaults.
     */
    @Test
    public final void Test10_AutoBookmarksFromHeadings_DefaultVsEnabled() throws IOException {
        String html = "<html><body>"
                + "<h1>Chapter 1</h1><p>Intro paragraph.</p>"
                + "<h2>Section 1.1</h2><p>Details.</p>"
                + "</body></html>";
 
        // Default (option unset / disabled)
        PdfDocument defaultPdf = PdfDocument.renderHtmlAsPdf(html);
        List<Bookmark> defaultBookmarks = defaultPdf.getBookmark().getBookmarks();
        Assertions.assertNotNull(defaultBookmarks);
 
        // Enabled
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setAutoBookmarksFromHeadings(true);
        options.setAutoBookmarkMinHeadingLevel(1);
        options.setAutoBookmarkMaxHeadingLevel(6);
        PdfDocument enabledPdf = PdfDocument.renderHtmlAsPdf(html, options);
        List<Bookmark> enabledBookmarks = enabledPdf.getBookmark().getBookmarks();
        Assertions.assertNotNull(enabledBookmarks);
 
        Assertions.assertTrue(enabledBookmarks.size() > defaultBookmarks.size(),
                "Enabled should yield strictly more bookmarks than default. default="
                        + defaultBookmarks.size() + ", enabled=" + enabledBookmarks.size());
        System.out.println("Test10 (PDF-2172): default=" + defaultBookmarks.size()
                + ", enabled=" + enabledBookmarks.size());
    }
 
    /**
     * Test 11: autoBookmarkMaxHeadingLevel restricts which heading levels are bookmarked.
     * With max level 1 only &lt;h1&gt; elements should become bookmarks.
     */
    @Test
    public final void Test11_AutoBookmarks_MaxHeadingLevel() throws IOException {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setAutoBookmarksFromHeadings(true);
        options.setAutoBookmarkMinHeadingLevel(1);
        options.setAutoBookmarkMaxHeadingLevel(1); // only h1
 
        String html = "<html><body>"
                + "<h1>Chapter 1</h1>"
                + "<h2>Section 1.1</h2>"
                + "<h3>Sub 1.1.1</h3>"
                + "<h1>Chapter 2</h1>"
                + "<h2>Section 2.1</h2>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);
 
        List<Bookmark> bookmarks = pdf.getBookmark().getBookmarks();
        Assertions.assertEquals(2, bookmarks.size(),
                "With maxHeadingLevel=1, only 2 h1 elements should be bookmarked. Got=" + bookmarks.size());
        System.out.println("Test11 (PDF-2172): maxHeadingLevel=1 -> "
                + bookmarks.size() + " bookmarks");
    }
 
    /**
     * Test 12: autoBookmarkCssSelectors picks up additional non-heading elements.
     * With just the CSS selector set (no heading level change) the elements matched by
     * the selector should appear as bookmarks alongside the standard heading range.
     */
    @Test
    public final void Test12_AutoBookmarks_CssSelectors() throws IOException {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setAutoBookmarksFromHeadings(true);
        options.setAutoBookmarkCssSelectors(new String[]{"h1", ".chapter-title", "[data-bookmark]"});

        String html = "<html><body>"
                + "<h1>Main Title</h1>"
                + "<div class='chapter-title'>Chapter A</div>"
                + "<p>Content.</p>"
                + "<div data-bookmark='marked'>Marked Section</div>"
                + "<p>More.</p>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);
 
        List<Bookmark> bookmarks = pdf.getBookmark().getBookmarks();
        Assertions.assertNotNull(bookmarks);
        // Expect at least 3 entries: h1 + .chapter-title + [data-bookmark]
        Assertions.assertTrue(bookmarks.size() >= 3,
                "Expected at least 3 bookmarks (h1 + 2 css matches). Got=" + bookmarks.size());
        System.out.println("Test12 (PDF-2172): css selector auto-bookmarks -> "
                + bookmarks.size() + " bookmarks");
    }
 
    /**
     * Test 13: elementQuerySelectors can be set on the render options.
     * We can't retrieve per-element locations from the Java client today
     * (no link annotation API), but the engine must accept the option and
     * the PDF must render successfully.
     */
    @Test
    public final void Test13_ElementQuerySelectors_AcceptedByEngine() throws IOException {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setElementQuerySelectors(new String[]{"h1", "h2", ".section-title"});
 
        String html = "<html><body>"
                + "<h1>Chapter 1</h1>"
                + "<h2>Section 1.1</h2>"
                + "<div class='section-title'>Extra</div>"
                + "<p>Body text.</p>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);
 
        Path outputFile = Paths.get("TestOutput/Test13_ElementQuerySelectors.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);
 
        Assertions.assertTrue(Files.exists(outputFile));
        Assertions.assertTrue(Files.size(outputFile) > 0,
                "PDF with elementQuerySelectors should render successfully");
        System.out.println("Test13 (PDF-2172): element query selectors render OK, "
                + Files.size(outputFile) + " bytes");
    }
 
    // ==================== PDF-2132: LinearizationMode tests ====================
 
    /**
     * Test 14: All three LinearizationMode values produce a linearized PDF when given
     * the same input. Verifies the strategy pattern works end-to-end.
     */
    @Test
    public final void Test14_LinearizationMode_AllModesProduceLinearized() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>Linearization Modes</h1><p>All three modes.</p></body></html>");
        byte[] inputBytes = pdf.getBinaryData();
 
        for (LinearizationMode mode : new LinearizationMode[]{
                LinearizationMode.Automatic,
                LinearizationMode.InMemory,
                LinearizationMode.FileBased}) {
            byte[] result = PdfDocument.linearizePdfToBytes(inputBytes, "", mode);
            Assertions.assertNotNull(result, mode + " should not return null");
            Assertions.assertTrue(result.length > 0, mode + " should not return empty");
 
            Path outFile = Paths.get("TestOutput/Test14_LinearizationMode_" + mode + ".pdf");
            Files.createDirectories(outFile.getParent());
            Files.write(outFile, result);
            Assertions.assertTrue(PdfDocument.isLinearized(outFile),
                    mode + " mode should produce a linearized PDF");
            System.out.println("Test14 (PDF-2132): mode=" + mode
                    + " produced " + result.length + " bytes (linearized=true)");
        }
    }
 
    /**
     * Test 15: Instance linearizePdfToBytes(LinearizationMode) — instance overload accepts the mode.
     */
    @Test
    public final void Test15_LinearizePdfToBytes_Instance_WithMode() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>Instance Mode</h1></body></html>");
 
        byte[] inMemory = pdf.linearizePdfToBytes(LinearizationMode.InMemory);
        byte[] fileBased = pdf.linearizePdfToBytes(LinearizationMode.FileBased);
 
        Assertions.assertTrue(inMemory.length > 0, "InMemory result should not be empty");
        Assertions.assertTrue(fileBased.length > 0, "FileBased result should not be empty");
 
        Path inMemoryOut = Paths.get("TestOutput/Test15_Instance_InMemory.pdf");
        Path fileBasedOut = Paths.get("TestOutput/Test15_Instance_FileBased.pdf");
        Files.createDirectories(inMemoryOut.getParent());
        Files.write(inMemoryOut, inMemory);
        Files.write(fileBasedOut, fileBased);
 
        Assertions.assertTrue(PdfDocument.isLinearized(inMemoryOut), "InMemory output should be linearized");
        Assertions.assertTrue(PdfDocument.isLinearized(fileBasedOut), "FileBased output should be linearized");
 
        System.out.println("Test15 (PDF-2132): instance modes OK — InMemory=" + inMemory.length
                + " bytes, FileBased=" + fileBased.length + " bytes");
    }
 
    // ==================== PDF-2172: getElementLocations() retrieval ====================
 
    /**
     * Test 16: getElementLocations() returns the rendered location of every element matched by
     * the configured element query selectors, sorted in document order. Mirrors C# test
     * {@code GetElementLocations_WithHeadingSelectors_ReturnsLocations}.
     */
    @Test
    public final void Test16_GetElementLocations_WithHeadingSelectors() throws IOException {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setElementQuerySelectors(new String[]{"h1", "h2"});
 
        String html = "<html><body>"
                + "<h1>Chapter 1</h1><p>Content</p>"
                + "<h2>Section 1.1</h2><p>More content</p>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);
        List<RenderedElementLocation> locations = pdf.getElementLocations();
 
        Assertions.assertNotNull(locations);
        Assertions.assertEquals(2, locations.size(),
                "Expected 2 element locations (h1 + h2), got=" + locations.size());
        Assertions.assertEquals("Chapter 1", locations.get(0).getText());
        Assertions.assertEquals("Section 1.1", locations.get(1).getText());
        Assertions.assertEquals(0, locations.get(0).getPageIndex());
        Assertions.assertEquals(0, locations.get(0).getElementIndex());
        Assertions.assertEquals(1, locations.get(1).getElementIndex());
        Assertions.assertNotNull(locations.get(0).getRectangle(),
                "Element location should carry a rectangle");
        System.out.println("Test16 (PDF-2172): " + locations.size() + " locations -> "
                + locations.get(0).getText() + ", " + locations.get(1).getText());
    }
 
    /**
     * Test 17: getElementLocations() spans multiple pages and reports the correct page index
     * for each element. Mirrors C# {@code GetElementLocations_MultiPage_CorrectPageIndices}.
     */
    @Test
    public final void Test17_GetElementLocations_MultiPage() throws IOException {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setElementQuerySelectors(new String[]{"h1"});
 
        String html = "<html><body>"
                + "<h1>Page 1</h1><div style='page-break-before: always'></div>"
                + "<h1>Page 2</h1><div style='page-break-before: always'></div>"
                + "<h1>Page 3</h1>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);
        List<RenderedElementLocation> locations = pdf.getElementLocations();
 
        Assertions.assertEquals(3, locations.size(),
                "Expected 3 h1 locations across 3 pages, got=" + locations.size());
        Assertions.assertEquals(0, locations.get(0).getPageIndex());
        Assertions.assertEquals(1, locations.get(1).getPageIndex());
        Assertions.assertEquals(2, locations.get(2).getPageIndex());
        System.out.println("Test17 (PDF-2172): page indices = "
                + locations.get(0).getPageIndex() + ", "
                + locations.get(1).getPageIndex() + ", "
                + locations.get(2).getPageIndex());
    }
 
    /**
     * Test 18: getElementLocations() returns an empty list when no element query selectors
     * are configured (baseline case). Mirrors C# {@code GetElementLocations_NoSelectors_ReturnsEmpty}.
     */
    @Test
    public final void Test18_GetElementLocations_NoSelectors_ReturnsEmpty() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<html><body><h1>Title</h1></body></html>");
        List<RenderedElementLocation> locations = pdf.getElementLocations();
        Assertions.assertNotNull(locations);
        Assertions.assertTrue(locations.isEmpty(),
                "Expected no locations when no element query selectors configured, got=" + locations.size());
        System.out.println("Test18 (PDF-2172): no-selectors baseline -> " + locations.size() + " locations");
    }
 
    /**
     * Test 19: getElementLocations() also matches non-heading elements via attribute selectors.
     * Mirrors C# {@code GetElementLocations_DataAttributes_MatchesNonHeadings}.
     */
    @Test
    public final void Test19_GetElementLocations_DataAttributes() throws IOException {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setElementQuerySelectors(new String[]{"[data-track]"});
 
        String html = "<html><body>"
                + "<p data-track='intro'>Introduction</p>"
                + "<p>Regular</p>"
                + "<div data-track='footer'>Footer</div>"
                + "</body></html>";
 
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);
        List<RenderedElementLocation> locations = pdf.getElementLocations();
 
        Assertions.assertEquals(2, locations.size(),
                "Expected 2 [data-track] matches, got=" + locations.size());
        Assertions.assertEquals("Introduction", locations.get(0).getText());
        Assertions.assertEquals("Footer", locations.get(1).getText());
        System.out.println("Test19 (PDF-2172): data-attribute matches = "
                + locations.get(0).getText() + ", " + locations.get(1).getText());
    }

    /**
	 * Test20: heading-level validation rejects out-of-range and inverted ranges.
	 * Mirrors C# ValidateAutoBookmarkHeadingLevels guard added in PR review.
	 */
	@Test
	public final void Test20_AutoBookmark_HeadingLevels_ValidationRejects() {
		ChromePdfRenderOptions badMin = new ChromePdfRenderOptions();
		badMin.setAutoBookmarksFromHeadings(true);
		badMin.setAutoBookmarkMinHeadingLevel(0);
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> PdfDocument.renderHtmlAsPdf("<h1>x</h1>", badMin));

		ChromePdfRenderOptions badMax = new ChromePdfRenderOptions();
		badMax.setAutoBookmarksFromHeadings(true);
		badMax.setAutoBookmarkMaxHeadingLevel(7);
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> PdfDocument.renderHtmlAsPdf("<h1>x</h1>", badMax));

		ChromePdfRenderOptions inverted = new ChromePdfRenderOptions();
		inverted.setAutoBookmarksFromHeadings(true);
		inverted.setAutoBookmarkMinHeadingLevel(4);
		inverted.setAutoBookmarkMaxHeadingLevel(2);
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> PdfDocument.renderHtmlAsPdf("<h1>x</h1>", inverted));
	}

	/**
	 * Test21: heading-level validation is skipped when autoBookmarksFromHeadings is false.
	 */
	@Test
	public final void Test21_AutoBookmark_HeadingLevels_NotValidatedWhenDisabled() {
		ChromePdfRenderOptions opt = new ChromePdfRenderOptions();
		// autoBookmarksFromHeadings defaults to false — out-of-range should NOT throw.
		opt.setAutoBookmarkMinHeadingLevel(0);
		PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>hi</h1>", opt);
		Assertions.assertTrue(pdf.getBinaryData().length > 0);
	}

	/**
	 * Test22: getElementLocations() caches results across calls.
	 */
	@Test
	public final void Test22_GetElementLocations_CachesResult() {
		ChromePdfRenderOptions opt = new ChromePdfRenderOptions();
		opt.setElementQuerySelectors(new String[]{"h1", "h2"});
		PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
				"<html><body><h1>Cached</h1><h2>Also</h2></body></html>", opt);

		List<RenderedElementLocation> first = pdf.getElementLocations();
		List<RenderedElementLocation> second = pdf.getElementLocations();
		// Same instance means the cache returned the populated list, not a rebuild.
		Assertions.assertSame(first, second, "second call should return cached list");
	}

	/**
	 * Test23: resetElementLocationCache() forces a rebuild on next access.
	 */
	@Test
	public final void Test23_ResetElementLocationCache_ForcesRebuild() {
		ChromePdfRenderOptions opt = new ChromePdfRenderOptions();
		opt.setElementQuerySelectors(new String[]{"h1"});
		PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
				"<html><body><h1>One</h1></body></html>", opt);

		List<RenderedElementLocation> first = pdf.getElementLocations();
		pdf.resetElementLocationCache();
		List<RenderedElementLocation> rebuilt = pdf.getElementLocations();
		Assertions.assertNotSame(first, rebuilt, "reset should produce a new list instance");
		Assertions.assertEquals(first.size(), rebuilt.size());
	}

	/**
	 * Test24: ELEMENT_QUERY_PREFIX contract — guards against drift between the engine
	 * and this client. Failure here = client/engine prefix mismatch, not a feature bug.
	 */
	@Test
	public final void Test24_GetElementLocations_PrefixContract_RoundTrips() {
		ChromePdfRenderOptions opt = new ChromePdfRenderOptions();
		opt.setElementQuerySelectors(new String[]{"h1"});
		PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
				"<html><body><h1>Unique Sentinel</h1></body></html>", opt);

		List<RenderedElementLocation> locations = pdf.getElementLocations();
		Assertions.assertEquals(1, locations.size());
		Assertions.assertEquals("Unique Sentinel", locations.get(0).getText());
	}

	@Test
	public final void Test25_HeaderFooter_WithScaledRendering_NonDefaultZoom() throws IOException {
		StringBuilder body = new StringBuilder("<html><body><h1>Scaled Body</h1>");
		for (int i = 0; i < 80; i++) body.append("<p>Scaled content. </p>");
		body.append("</body></html>");

		ChromePdfRenderOptions opts = new ChromePdfRenderOptions();
		opts.UseScaledRendering(150); // non-default zoom

		PdfDocument pdf = PdfDocument.renderHtmlAsPdf(body.toString(), opts);

		HtmlHeaderFooter header = new HtmlHeaderFooter();
		header.setHtmlFragment("<div>HEADER_MARKER_ZOOM150</div>");
		header.setMaxHeight(15);
		pdf.addHtmlHeader(header);

		HtmlHeaderFooter footer = new HtmlHeaderFooter();
		footer.setHtmlFragment("<div>FOOTER_MARKER_ZOOM150</div>");
		footer.setMaxHeight(15);
		pdf.addHtmlFooter(footer);

		String text = pdf.extractAllText();
		Assertions.assertTrue(text.contains("HEADER_MARKER_ZOOM150"),
				"Header marker missing under 150% scaled rendering");
		Assertions.assertTrue(text.contains("FOOTER_MARKER_ZOOM150"),
				"Footer marker missing under 150% scaled rendering");
	}

	@Test
	public final void Test26_HeaderFooter_WithScaledRendering_ZoomBelow100() throws IOException {
		StringBuilder body = new StringBuilder("<html><body><h1>Small Zoom Body</h1>");
		for (int i = 0; i < 40; i++) body.append("<p>Tiny content. </p>");
		body.append("</body></html>");

		ChromePdfRenderOptions opts = new ChromePdfRenderOptions();
		opts.UseScaledRendering(75); // scaled below default

		PdfDocument pdf = PdfDocument.renderHtmlAsPdf(body.toString(), opts);

		HtmlHeaderFooter header = new HtmlHeaderFooter();
		header.setHtmlFragment("<div>HEADER_MARKER_ZOOM75</div>");
		header.setMaxHeight(15);
		pdf.addHtmlHeader(header);

		HtmlHeaderFooter footer = new HtmlHeaderFooter();
		footer.setHtmlFragment("<div>FOOTER_MARKER_ZOOM75</div>");
		footer.setMaxHeight(15);
		pdf.addHtmlFooter(footer);

		String text = pdf.extractAllText();
		Assertions.assertTrue(text.contains("HEADER_MARKER_ZOOM75"),
				"Header marker missing under 75% scaled rendering");
		Assertions.assertTrue(text.contains("FOOTER_MARKER_ZOOM75"),
				"Footer marker missing under 75% scaled rendering");
	}

	// ---------------------------------------------------------------------------
	// PDF/UA rendering with accessible form elements.
	// Verifies that renderHtmlAsPdfUA produces a valid, non-empty PDF with
	// form inputs, labels, and semantic structure for screen readers.
	// Mirrors Node Test25_PdfUA_AccessibleForm in IronPdf-for-Node.
	// ---------------------------------------------------------------------------
	@Test
	public final void Test27_PdfUA_AccessibleForm() throws IOException {
		String html = "<html lang=\"en\"><body>"
				+ "<h1>Accessible Form</h1>"
				+ "<form>"
				+ "<label for=\"name\">Name:</label>"
				+ "<input type=\"text\" id=\"name\" name=\"name\" />"
				+ "<label for=\"email\">Email:</label>"
				+ "<input type=\"email\" id=\"email\" name=\"email\" />"
				+ "</form>"
				+ "<p>Content after form.</p>"
				+ "</body></html>";

		PdfDocument pdf = PdfDocument.renderHtmlAsPdfUA(html, NaturalLanguages.English);
		byte[] bytes = pdf.getBinaryData();

		Assertions.assertTrue(bytes.length > 0, "PDF/UA buffer should not be empty");
		String magic = new String(bytes, 0, 5);
		Assertions.assertEquals("%PDF-", magic, "Output should be a valid PDF");

		Path outputFile = Paths.get("TestOutput/Test27_pdfua_form.pdf");
		Files.createDirectories(outputFile.getParent());
		Files.write(outputFile, bytes);
		Assertions.assertTrue(Files.exists(outputFile));

		System.out.println("Test28 (PDF/UA): accessible form rendered OK, size="
				+ bytes.length + " bytes");
	}
}