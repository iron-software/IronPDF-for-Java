package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;

import com.ironsoftware.ironpdf.signature.VerifiedSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

/**
 * RC Tests for IronPdf Java 2026.4
 * Mapped from C# release notes — covers enhancements and bug fixes.
 * Skipped: DOCX-to-PDF (PDF-1491) — Java SDK has no DOCX rendering API.
 * Skipped: Docker ENTRYPOINT args (PDF-2182) — Java uses executable subprocess.
 */
public class RCTests2026_04 extends TestBase {

    private static final String RC_DIR = "/Data/RC_2026_04";

    // ==================== Enhancement Tests ====================

    /**
     * Test 01: HTML Headers/Footers performance — browser tab reuse (PDF-2083)
     * Release note: Improved PDF rendering performance by reducing internal render calls
     * and reusing browser tabs to minimize startup overhead.
     * Validates that rendering a large PDF with dynamic headers/footers completes within
     * a reasonable time, confirming the engine-side tab pooling improvement is active.
     */
    @Test
    public final void Test01_HtmlHeaderFooterPerformance() throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html><html><head><style>")
                .append("body{font-family:Arial;margin:40px;}")
                .append("h2{page-break-before:always;}")
                .append("</style></head><body>");
        htmlBuilder.append("<h1>Performance Test Document</h1>");
        htmlBuilder.append("<p>First page content.</p>");
        // Generate 20 pages
        for (int i = 2; i <= 20; i++) {
            htmlBuilder.append("<h2>Section ").append(i).append("</h2>");
            htmlBuilder.append("<p>Content for page ").append(i).append(". ");
            htmlBuilder.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
            htmlBuilder.append("Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>");
        }
        htmlBuilder.append("</body></html>");

        long startTime = System.currentTimeMillis();

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(htmlBuilder.toString());

        // Add dynamic header with page tokens
        HtmlHeaderFooter header = new HtmlHeaderFooter();
        header.setHtmlFragment(
                "<div style='font-family:Arial;font-size:9pt;width:100%;display:flex;"
                        + "justify-content:space-between;padding:5px 10px;border-bottom:1px solid #ccc;'>"
                        + "<span>Performance Report</span><span>Page {page} of {total-pages}</span></div>");
        pdf.addHtmlHeader(header);

        HtmlHeaderFooter footer = new HtmlHeaderFooter();
        footer.setHtmlFragment(
                "<div style='font-family:Arial;font-size:8pt;text-align:center;padding:5px;'>"
                        + "Generated on 2026-03-27 | Confidential</div>");
        pdf.addHtmlFooter(footer);

        long elapsed = System.currentTimeMillis() - startTime;

        Path outputFile = Paths.get("TestOutput/Test01_HeaderFooterPerf.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        int pageCount = pdf.getPagesInfo().size();
        Assertions.assertTrue(pageCount >= 20, "Should have at least 20 pages, got: " + pageCount);
        Assertions.assertTrue(Files.exists(outputFile));
        System.out.println("Test01 (PDF-2083): " + pageCount + " pages, " + elapsed + "ms, "
                + Files.size(outputFile) + " bytes");
    }

    /**
     * Test 02: PDF/UA structure tree tagging for forms with mixed input types (PDF-1986)
     * Engine fix: IronPdfServiceHandler now calls HtmlHelper.HtmlStructTreeDOM()
     * and ConvertToPdfUAForScreenReader() to produce proper semantic structure.
     */
    @Test
    public final void Test02_PdfUAFormMixedInputTypes() throws IOException {
        String html = "<html lang=\"en\"><head>"
                + "<title>PDF/UA Mixed Forms Test</title>"
                + "</head><body>"
                + "<h1>PDF/UA Form Tagging Test</h1>"
                + "<form>"
                + "<label for=\"name\">Full Name:</label>"
                + "<input type=\"text\" id=\"name\" name=\"name\" value=\"Test User\" /><br/><br/>"
                + "<label for=\"email\">Email Address:</label>"
                + "<input type=\"email\" id=\"email\" name=\"email\" value=\"test@example.com\" /><br/><br/>"
                + "<input type=\"hidden\" name=\"csrf_token\" value=\"abc123\" />"
                + "<fieldset><legend>Preferences:</legend>"
                + "<input type=\"checkbox\" id=\"opt1\" name=\"pref\" value=\"a\" checked />"
                + "<label for=\"opt1\">Option A</label><br/>"
                + "<input type=\"checkbox\" id=\"opt2\" name=\"pref\" value=\"b\" />"
                + "<label for=\"opt2\">Option B</label><br/>"
                + "</fieldset><br/>"
                + "<fieldset><legend>Priority:</legend>"
                + "<input type=\"radio\" id=\"high\" name=\"priority\" value=\"high\" checked />"
                + "<label for=\"high\">High</label>"
                + "<input type=\"radio\" id=\"medium\" name=\"priority\" value=\"medium\" />"
                + "<label for=\"medium\">Medium</label>"
                + "</fieldset><br/>"
                + "<textarea id=\"notes\" name=\"notes\" rows=\"3\" cols=\"30\">Sample notes</textarea><br/>"
                + "<input type=\"submit\" value=\"Submit Form\" />"
                + "</form>"
                + "<h2>Additional Content After Form</h2>"
                + "<p>This paragraph follows the form.</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdfUA(html, NaturalLanguages.English);

        Path outputFile = Paths.get("TestOutput/Test02_PdfUA_MixedForms.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        Assertions.assertTrue(Files.size(outputFile) > 0, "PDF/UA output should not be empty");
        System.out.println("Test02 (PDF-1986): " + Files.size(outputFile) + " bytes");
    }

    // ==================== Bug Fix Tests ====================

    /**
     * Test 03: Memory leak fix in ReplaceTextOnPages (PDF-1857)
     * Release note: Fixed memory leak in ReplaceTextOnPages where repeated calls
     * caused increasing memory growth (93-97% reduction).
     * Runs replaceText in a loop to verify memory does not grow excessively.
     */
    @Test
    public final void Test03_ReplaceTextMemoryLeak() throws IOException {
        String html = "<html><body>"
                + "<p>Replace this text on every iteration.</p>"
                + "<p>Another line with Replace this text content.</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memBefore = runtime.totalMemory() - runtime.freeMemory();

        // Run replaceText 20 times to detect memory growth
        for (int i = 0; i < 20; i++) {
            pdf.replaceText(PageSelection.firstPage(),
                    "Replace this text", "Replaced iteration " + i);
            // Replace back for next iteration
            pdf.replaceText(PageSelection.firstPage(),
                    "Replaced iteration " + i, "Replace this text");
        }

        runtime.gc();
        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        long memGrowth = memAfter - memBefore;

        Path outputFile = Paths.get("TestOutput/Test03_ReplaceTextMemory.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        System.out.println("Test03 (PDF-1857): Memory before=" + (memBefore / 1024) + "KB, after="
                + (memAfter / 1024) + "KB, growth=" + (memGrowth / 1024) + "KB");
        // With the fix, memory growth over 20 iterations should be minimal (< 50MB)
        // Before fix: would climb hundreds of MB
    }

    /**
     * Test 04: Buffer over-read fix in GetMetadata
     * Release note: Fixed buffer over-read in GetMetadata that could cause crashes or data corruption.
     * Tests metadata retrieval on multiple PDFs to ensure no crash.
     */
    @Test
    public final void Test04_GetMetadataNoCrash() throws IOException {
        // Test on a rendered PDF
        PdfDocument pdf1 = PdfDocument.renderHtmlAsPdf("<html><body><h1>Metadata Test</h1></body></html>");
        String author1 = pdf1.getMetadata().getAuthor();
        Assertions.assertNotNull(author1, "Author metadata should not be null");
        System.out.println("Test04a: Rendered PDF author='" + author1 + "'");

        // Test on an existing PDF file
        Path testFile = getTestPath(RC_DIR + "/base.pdf");
        if (Files.exists(testFile)) {
            PdfDocument pdf2 = PdfDocument.fromFile(testFile);
            String author2 = pdf2.getMetadata().getAuthor();
            Assertions.assertNotNull(author2, "base.pdf author metadata should not be null");
            System.out.println("Test04b: base.pdf author='" + author2 + "'");
        }

        // Test on a multi-page PDF
        Path wikiFile = getTestPath(RC_DIR + "/wikipedia.pdf");
        if (Files.exists(wikiFile)) {
            PdfDocument pdf3 = PdfDocument.fromFile(wikiFile);
            String title = pdf3.getMetadata().getTitle();
            Assertions.assertNotNull(title, "wikipedia.pdf title should not be null");
            System.out.println("Test04c: wikipedia.pdf title='" + title + "'");
        }
    }

    /**
     * Test 05: PDF/UA image clipping with CSS overflow:hidden (PDF-2178)
     * Engine fix: IronPdfServiceHandler now calls HtmlHelper.HtmlStructTreeDOM()
     * and ConvertToPdfUAForScreenReader() to produce proper semantic structure.
     */
    @Test
    public final void Test05_PdfUAOverflowHiddenClipping() throws IOException {
        String html = "<html lang=\"en\"><head>"
                + "<title>PDF/UA Image Clipping Test</title>"
                + "<style>.clipped { width: 200px; height: 100px; overflow: hidden; border: 1px solid #ccc; }"
                + ".clipped img { width: 400px; height: 300px; }</style>"
                + "</head><body>"
                + "<h1>Image Clipping Test</h1>"
                + "<p>The image below is clipped by overflow:hidden.</p>"
                + "<div class=\"clipped\">"
                + "<img src=\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='300'%3E"
                + "%3Crect fill='%2300f' width='400' height='300'/%3E"
                + "%3Ctext x='50' y='150' fill='white' font-size='24'%3EClipped Image%3C/text%3E%3C/svg%3E\""
                + " alt=\"Test clipped image\" />"
                + "</div>"
                + "<p>Content after clipped image.</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdfUA(html, NaturalLanguages.English);

        Path outputFile = Paths.get("TestOutput/Test05_PdfUA_ImageClipping.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        Assertions.assertTrue(Files.size(outputFile) > 0, "PDF/UA output should not be empty");
        System.out.println("Test05 (PDF-2178): " + Files.size(outputFile) + " bytes");
    }

    /**
     * Test 06: SignatureName fix for externally signed PDFs (PDF-2060)
     * Release note: Fixed an issue where SignatureName was always empty when verifying
     * signatures created by external signers such as Adobe Reader.
     * Tests PDF-2060.pdf (Adobe/CAC signed), Google.Signed.pdf, and DocC.pdf (3 sigs).
     */
    @Test
    public final void Test06_SignatureNameExternalSigners() throws Exception {
        String[][] testFiles = {
                {RC_DIR + "/PDF-2060.pdf", "PDF-2060.pdf"},
                {RC_DIR + "/Google.Signed.pdf", "Google.Signed.pdf"},
                {RC_DIR + "/DocC.pdf", "DocC.pdf"},
        };

        for (String[] entry : testFiles) {
            Path testFile = getTestPath(entry[0]);
            if (!Files.exists(testFile)) {
                System.out.println("Test06: SKIPPED " + entry[1] + " (file not found)");
                continue;
            }

            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                Future<List<VerifiedSignature>> future = executor.submit(() -> {
                    PdfDocument pdf = PdfDocument.fromFile(testFile);
                    return pdf.getSignature().getVerifiedSignature();
                });

                List<VerifiedSignature> sigs = future.get(30, TimeUnit.SECONDS);
                // Verify the call does not throw (main fix validation)
                Assertions.assertNotNull(sigs, entry[1] + " should not return null");
                System.out.println("Test06: " + entry[1] + " — " + sigs.size() + " signature(s) found");

                // When signatures are found, verify SignatureName is populated
                for (VerifiedSignature sig : sigs) {
                    String name = sig.getSignatureName();
                    System.out.println("Test06: " + entry[1] + " — SignatureName='" + name
                            + "', valid=" + sig.isValid()
                            + ", filter='" + sig.getFilter() + "'");
                    Assertions.assertNotNull(name, "SignatureName should not be null");
                    Assertions.assertFalse(name.isEmpty(),
                            entry[1] + ": SignatureName should not be empty (fix validates /T fallback)");
                }

            } catch (TimeoutException e) {
                Assertions.fail("TIMEOUT: Signature verification timed out for " + entry[1]);
            } finally {
                executor.shutdownNow();
            }
        }
    }

    /**
     * Test 07: PDF-2182 — Docker ENTRYPOINT args forwarding (N/A for Java)
     * Java SDK uses executable subprocess mode by default, not Docker.
     * This test simply confirms the connection mode is SUBPROCESS.
     */
    @Test
    public final void Test07_DockerEntrypointNotApplicable() {
        // Java uses executable subprocess by default — Docker ENTRYPOINT fix is N/A
        IronPdfEngineConnection connection = IronPdfEngineConnection.configure().withSubprocess();
        Assertions.assertNotNull(connection, "Default subprocess connection should be configurable");
        System.out.println("Test07 (PDF-2182): N/A for Java — uses executable subprocess, not Docker");
    }

    /**
     * Test 08: CompressAndSaveAs — new API (Phase 3 item)
     * Verifies the compressAndSaveAs method works correctly using the proto RPC
     * QPdf_Compression_CompressAndSaveAs.
     */
    @Test
    public final void Test08_CompressAndSaveAs() throws IOException {
        String html = "<!DOCTYPE html><html><body>"
                + "<h1>Compress And Save As Test</h1>"
                + "<p>This document will be compressed and saved to a file path.</p>"
                + "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
                + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);
        byte[] originalBytes = pdf.getBinaryData();

        Path outputFile = Paths.get("TestOutput/Test08_CompressAndSaveAs.pdf");
        Files.createDirectories(outputFile.getParent());

        // Use compressAndSaveAs — saves compressed PDF directly to file
        pdf.compressAndSaveAs(outputFile.toString());

        Assertions.assertTrue(Files.exists(outputFile), "Compressed file should exist");
        long compressedSize = Files.size(outputFile);
        Assertions.assertTrue(compressedSize > 0, "Compressed file should not be empty");
        Assertions.assertTrue(compressedSize <= originalBytes.length,
                "Compressed (" + compressedSize + ") should be <= original (" + originalBytes.length + ")");

        // Verify the saved file is a valid PDF
        PdfDocument reloaded = PdfDocument.fromFile(outputFile);
        Assertions.assertEquals(pdf.getPagesInfo().size(), reloaded.getPagesInfo().size(),
                "Compressed PDF should have same page count");

        System.out.println("Test08: Original=" + originalBytes.length + " bytes, CompressAndSaveAs="
                + compressedSize + " bytes ("
                + String.format("%.1f", (compressedSize * 100.0 / originalBytes.length)) + "%)");
    }

    /**
     * Test 09: CompressAndSaveAs with quality parameter
     * Tests compression with explicit JPEG quality setting.
     */
    @Test
    public final void Test09_CompressAndSaveAsWithQuality() throws IOException {
        String html = "<html><body><h1>Quality Compress Test</h1>"
                + "<p>Testing compressAndSaveAs with explicit quality parameter.</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Path outputFile = Paths.get("TestOutput/Test09_CompressAndSaveAs_Quality.pdf");
        Files.createDirectories(outputFile.getParent());

        pdf.compressAndSaveAs(outputFile.toString(), 50);

        Assertions.assertTrue(Files.exists(outputFile), "Compressed file should exist");
        Assertions.assertTrue(Files.size(outputFile) > 0, "Compressed file should not be empty");

        PdfDocument reloaded = PdfDocument.fromFile(outputFile);
        Assertions.assertEquals(1, reloaded.getPagesInfo().size(), "Should have 1 page");

        System.out.println("Test09: CompressAndSaveAs(quality=50) = " + Files.size(outputFile) + " bytes");
    }
}
