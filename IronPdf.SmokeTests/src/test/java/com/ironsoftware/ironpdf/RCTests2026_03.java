package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.form.ComboBoxField;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.form.FormFieldsSet;
import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;
import com.ironsoftware.ironpdf.signature.VerifiedSignature;
import com.ironsoftware.ironpdf.standard.PdfAVersions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * RC Tests for IronPdf Java 2026.3
 * Mapped from C# RC tests — 12 test methods covering 10 testable release note items.
 * Skipped: LinuxAndDockerDependenciesAutoConfig (.NET only).
 * Tests that fail indicate the fix is not available in Java and the corresponding
 * release note should be removed from the Java release notes.
 */
public class RCTests2026_03 extends TestBase {

    private static final String RC_DIR = "/Data/RC_2026_03";

    // ==================== Feature Tests ====================

    /**
     * Test 01: CompressPdfToBytes/Stream (Feature)
     * Release note: Implement CompressAndSaveAs() that returns memory stream/bytes
     * Uses new QPdf smart compression via compressPdfToBytes() — returns compressed bytes in memory
     */
    @Test
    public final void Test01_CompressInMemory() throws IOException {
        String html = "<!DOCTYPE html><html><head><style>"
                + "body{font-family:Arial;margin:40px;}"
                + "table{border-collapse:collapse;width:100%;}"
                + "th,td{border:1px solid #ddd;padding:8px;}"
                + "</style></head><body>"
                + "<h1>Compression Test</h1>"
                + "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>"
                + "<table><tr><th>Col 1</th><th>Col 2</th><th>Col 3</th></tr>"
                + "<tr><td>Data A</td><td>Data B</td><td>Data C</td></tr>"
                + "<tr><td>Data D</td><td>Data E</td><td>Data F</td></tr></table>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        byte[] originalBytes = pdf.getBinaryData();
        Assertions.assertTrue(originalBytes.length > 0, "Original PDF should have content");

        // Use new QPdf smart compression — returns compressed bytes in memory
        byte[] compressedBytes = pdf.compressPdfToBytes();

        Assertions.assertTrue(compressedBytes.length > 0, "Compressed PDF should have content");
        Assertions.assertTrue(compressedBytes.length <= originalBytes.length,
                "Compressed PDF (" + compressedBytes.length + ") should be <= original (" + originalBytes.length + ")");

        // Verify compressed bytes produce a valid PDF
        PdfDocument compressedPdf = new PdfDocument(compressedBytes);
        Assertions.assertEquals(pdf.getPagesInfo().size(), compressedPdf.getPagesInfo().size(),
                "Compressed PDF should have same page count");

        // Save for visual inspection
        Path outputFile = Paths.get("TestOutput/Test01_Compressed.pdf");
        Files.createDirectories(outputFile.getParent());
        Files.write(outputFile, compressedBytes);

        Assertions.assertTrue(Files.exists(outputFile), "Compressed output file should exist");
        System.out.println("Test01: Original=" + originalBytes.length + " bytes, Compressed=" + compressedBytes.length
                + " bytes (" + String.format("%.1f", (compressedBytes.length * 100.0 / originalBytes.length)) + "%)");
    }

    /**
     * Test 01b: CompressPdfToStream (Feature)
     * Verifies the InputStream variant of compress returns the same data as the byte[] variant.
     */
    @Test
    public final void Test01b_CompressToStream() throws IOException {
        String html = "<html><body><h1>Stream Compress Test</h1>"
                + "<p>Verifying compressPdfToStream returns valid compressed data.</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);
        byte[] originalBytes = pdf.getBinaryData();

        InputStream compressedStream = pdf.compressPdfToStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] tmp = new byte[8192];
        int bytesRead;
        while ((bytesRead = compressedStream.read(tmp)) != -1) {
            buffer.write(tmp, 0, bytesRead);
        }
        compressedStream.close();
        byte[] streamBytes = buffer.toByteArray();

        Assertions.assertTrue(streamBytes.length > 0, "Compressed stream should have content");
        Assertions.assertTrue(streamBytes.length <= originalBytes.length,
                "Compressed (" + streamBytes.length + ") should be <= original (" + originalBytes.length + ")");

        // Verify the stream produces a valid PDF
        PdfDocument fromStream = new PdfDocument(streamBytes);
        Assertions.assertEquals(1, fromStream.getPagesInfo().size(),
                "Compressed PDF from stream should have 1 page");

        System.out.println("Test01b: Original=" + originalBytes.length + " bytes, Stream compress=" + streamBytes.length + " bytes");
    }

    /**
     * Test 02: Internal URL Scheme (Feature)
     * Release note: Replace fake HTTP domain with custom CEF scheme to eliminate DNS leaks
     * Verify rendering works correctly (ironpdf:// is internal, just test rendering succeeds)
     */
    @Test
    public final void Test02_InternalUrlScheme() throws IOException {
        String html = "<!DOCTYPE html><html><head><style>"
                + "body{font-family:Arial;margin:20px;}"
                + ".box{width:200px;height:100px;background:#4CAF50;color:white;"
                + "display:flex;align-items:center;justify-content:center;margin:10px;}"
                + "</style></head><body>"
                + "<h1>URL Scheme Test</h1>"
                + "<p>This test verifies rendering works with the new ironpdf:// scheme.</p>"
                + "<div class='box'>Rendered OK</div>"
                + "<p>Special chars: &amp; &lt; &gt; &quot;</p>"
                + "<p>Unicode: cafe\u0301 na\u00EFve r\u00E9sum\u00E9</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Path outputFile = Paths.get("TestOutput/Test02_UrlScheme.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        Assertions.assertTrue(Files.size(outputFile) > 0, "Rendered PDF should not be empty");

        String text = pdf.extractAllText();
        Assertions.assertTrue(text.contains("URL Scheme Test"), "Text should be extractable");
    }

    // ==================== P0 Tests (Blockers) ====================

    /**
     * Test 03: Bookmark Malformed Outline (P0)
     * Release note: Adding bookmarks to specific PDF causes System.AccessViolationException
     * Jira: PDF-2128
     */
    @Test
    public final void Test03_BookmarkMalformedOutline() throws Exception {
        Path testFile = getTestPath(RC_DIR + "/malformed_outline.pdf");
        Assertions.assertTrue(Files.exists(testFile), "malformed_outline.pdf not found");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> future = executor.submit(() -> {
                PdfDocument pdf = PdfDocument.fromFile(testFile);

                // Add bookmark — this should not throw AccessViolationException
                pdf.getBookmark().addBookMarkAtStart("Test Bookmark", 0);

                Path outputFile = Paths.get("TestOutput/Test03_BookmarkAdded.pdf");
                Files.createDirectories(outputFile.getParent());
                pdf.saveAs(outputFile);

                return pdf.getBookmark().getBookmarks().size();
            });

            int bookmarkCount = future.get(30, TimeUnit.SECONDS);
            Assertions.assertTrue(bookmarkCount > 0, "Should have at least one bookmark after adding");
            System.out.println("Test03: " + bookmarkCount + " bookmark(s) in output");
        } catch (TimeoutException e) {
            Assertions.fail("TIMEOUT: Bookmark operation timed out — possible crash");
        } finally {
            executor.shutdownNow();
        }
    }

    // ==================== P1 Tests (Critical) ====================

    /**
     * Test 04: PDF/UA Accessibility (P1)
     * Release note: RenderHtmlAsPdfUA() doesn't render with the correct accessibility tag tree
     * Jira: PDF-2154
     * Note: Avoid <table> and <select> elements — known SIGSEGV crash
     */
    @Test
    public final void Test04_PdfUAAccessibility() throws IOException {
        // Use simple HTML without <table> or <select> (those still crash)
        String html = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>"
                + "<style>body{font-family:Arial,sans-serif;margin:40px;}</style></head><body>"
                + "<h1>PDF/UA Accessibility Test</h1>"
                + "<p>This document tests semantic tag preservation in PDF/UA output.</p>"
                + "<h2>Section One</h2>"
                + "<p>Content for section one with proper heading hierarchy.</p>"
                + "<ul><li>List item 1</li><li>List item 2</li><li>List item 3</li></ul>"
                + "<h2>Section Two</h2>"
                + "<p>Content for section two.</p>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Path outputFile = Paths.get("TestOutput/Test04_PdfUA.pdf");
        Files.createDirectories(outputFile.getParent());

        pdf.saveAsPdfUA(outputFile.toString(), NaturalLanguages.English);

        Assertions.assertTrue(Files.exists(outputFile), "PDF/UA file should be created");
        Assertions.assertTrue(Files.size(outputFile) > 0, "PDF/UA file should not be empty");
        System.out.println("Test04: PDF/UA output " + Files.size(outputFile) + " bytes");
        // Visual check: Verify tag tree uses H1/H2/P tags (not Sect) in Adobe Acrobat or PAC
    }

    /**
     * Test 05: PDF/A Validation (P1)
     * Release note: Invalid PDF/A file produced for specific input file (XMP metadata + Type1 font)
     * Jira: PDF-2102
     * Tests 4 input files: pdfa_input.pdf, F21_2024-12-15.pdf, Verkoopfactuur.pdf, Iron-PDFA.pdf
     */
    @Test
    public final void Test05_PdfAValidation() throws IOException {
        String[][] testFiles = {
                {RC_DIR + "/pdfa_input.pdf", "Test05_PdfA_pdfa_input.pdf"},
                {RC_DIR + "/F21_2024-12-15.pdf", "Test05_PdfA_F21.pdf"},
                {RC_DIR + "/PDF-2102_attachments/Verkoopfactuur.pdf", "Test05_PdfA_Verkoopfactuur.pdf"},
                {RC_DIR + "/PDF-2102_attachments/Iron-PDFA.pdf", "Test05_PdfA_IronPDFA.pdf"},
        };

        Files.createDirectories(Paths.get("TestOutput"));

        for (String[] entry : testFiles) {
            Path testFile = getTestPath(entry[0]);
            Assertions.assertTrue(Files.exists(testFile), entry[0] + " not found");

            PdfDocument pdf = PdfDocument.fromFile(testFile);
            Path outputFile = Paths.get("TestOutput/" + entry[1]);

            pdf.saveAsPdfA(outputFile.toString());

            Assertions.assertTrue(Files.exists(outputFile), entry[1] + " should be created");
            Assertions.assertTrue(Files.size(outputFile) > 0, entry[1] + " should not be empty");
            System.out.println("Test05: " + entry[1] + " = " + Files.size(outputFile) + " bytes");
        }
    }

    /**
     * Test 06: Signature Verification (P1)
     * Release note: GetVerifiedSignatures throws exceptions on PDF with valid signatures
     * Jira: PDF-2155
     */
    @Test
    public final void Test06_SignatureVerification() throws Exception {
        Path testFile = getTestPath(RC_DIR + "/signed_valid.pdf");
        Assertions.assertTrue(Files.exists(testFile), "signed_valid.pdf not found");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> future = executor.submit(() -> {
                PdfDocument pdf = PdfDocument.fromFile(testFile);
                List<VerifiedSignature> sigs = pdf.getSignature().getVerifiedSignature();
                return sigs.size();
            });

            int sigCount = future.get(30, TimeUnit.SECONDS);
            Assertions.assertTrue(sigCount >= 0, "Should not throw when reading verified signatures");
            System.out.println("Test06: " + sigCount + " verified signature(s) found");
        } catch (TimeoutException e) {
            Assertions.fail("TIMEOUT: Signature verification timed out");
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Test 07: Signature Timestamp (P1)
     * Release note: Parsing signature timestamps in certain PDFs throws unhandled exception
     * Jira: PDF-2155 / PDF-1353
     * Tests DocC.pdf and Qualif_AFAQ_valid_2024_2027.pdf — both had timestamp parse issues
     */
    @Test
    public final void Test07_SignatureTimestamp() throws Exception {
        String[][] testFiles = {
                {RC_DIR + "/DocC.pdf", "DocC.pdf"},
                {RC_DIR + "/Qualif_AFAQ_valid_2024_2027.pdf", "Qualif_AFAQ.pdf"},
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            for (String[] entry : testFiles) {
                Path testFile = getTestPath(entry[0]);
                Assertions.assertTrue(Files.exists(testFile), entry[1] + " not found");

                Future<Integer> future = executor.submit(() -> {
                    PdfDocument pdf = PdfDocument.fromFile(testFile);
                    // The fix is that this call should NOT throw an exception
                    // when parsing timestamps — signatures may or may not pass verification
                    List<VerifiedSignature> sigs = pdf.getSignature().getVerifiedSignature();
                    return sigs.size();
                });

                int sigCount = future.get(30, TimeUnit.SECONDS);
                Assertions.assertTrue(sigCount >= 0, entry[1] + " should not throw when reading signatures");
                System.out.println("Test07: " + entry[1] + " = " + sigCount + " verified signature(s)");
            }
        } catch (TimeoutException e) {
            Assertions.fail("TIMEOUT: Signature timestamp parsing timed out");
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Test 08: Resize Rotated Pages (P1)
     * Release note: Resize() physically rotates pages when PageRotation value is present
     * Jira: PDF-2139
     */
    @Test
    public final void Test08_ResizeRotatedPages() throws IOException {
        Path testFile = getTestPath(RC_DIR + "/rotated_270.pdf");
        Assertions.assertTrue(Files.exists(testFile), "rotated_270.pdf not found");

        PdfDocument pdf = PdfDocument.fromFile(testFile);

        PageInfo originalPage = pdf.getPagesInfo().get(0);
        System.out.println("Test08: Original dimensions: " + originalPage.getWidth() + "x" + originalPage.getHeight() + "mm"
                + ", rotation=" + originalPage.getPageRotation());

        // Resize to A5 (148mm x 210mm)
        double a5Width = 148.0;
        double a5Height = 210.0;
        pdf.resizePage(a5Width, a5Height, PageSelection.firstPage());

        PageInfo resizedPage = pdf.getPagesInfo().get(0);
        System.out.println("Test08: Resized dimensions: " + resizedPage.getWidth() + "x" + resizedPage.getHeight() + "mm");

        Path outputFile = Paths.get("TestOutput/Test08_ResizedRotated.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        Assertions.assertTrue(Files.size(outputFile) > 0, "Resized PDF should not be empty");
        // Visual check: Content should not be rotated or cut off after resize
    }

    // ==================== P2 Tests (Medium) ====================

    /**
     * Test 09: ComboBox After Merge (P2)
     * Release note: Merge and AppendPdf breaks ComboBox Form Field
     * Jira: PDF-2047
     */
    @Test
    public final void Test09_ComboBoxAfterMerge() throws IOException {
        Path testFile = getTestPath(RC_DIR + "/combobox_form.pdf");
        Assertions.assertTrue(Files.exists(testFile), "combobox_form.pdf not found");

        PdfDocument pdfA = PdfDocument.fromFile(testFile);

        // Verify ComboBox choices exist before merge
        FormFieldsSet fieldsBeforeMerge = pdfA.getForm().getFields();
        List<ComboBoxField> combosBefore = fieldsBeforeMerge.getComboBoxFields();
        Assertions.assertFalse(combosBefore.isEmpty(), "Should have ComboBox fields before merge");

        int choicesBeforeCount = 0;
        for (ComboBoxField combo : combosBefore) {
            choicesBeforeCount += combo.getOptions().size();
            System.out.println("Test09 Before: " + combo.getName() + " options=" + combo.getOptions());
        }

        // Create a simple second PDF to merge with
        PdfDocument pdfB = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>Second Document</h1><p>Merge target</p></body></html>");

        // Merge
        PdfDocument merged = PdfDocument.merge(Arrays.asList(pdfA, pdfB));

        // Verify ComboBox choices preserved after merge
        FormFieldsSet fieldsAfterMerge = merged.getForm().getFields();
        List<ComboBoxField> combosAfter = fieldsAfterMerge.getComboBoxFields();
        Assertions.assertFalse(combosAfter.isEmpty(), "Should have ComboBox fields after merge");

        int choicesAfterCount = 0;
        for (ComboBoxField combo : combosAfter) {
            choicesAfterCount += combo.getOptions().size();
            System.out.println("Test09 After: " + combo.getName() + " options=" + combo.getOptions());
        }

        Assertions.assertEquals(choicesBeforeCount, choicesAfterCount,
                "ComboBox choices count should be preserved after merge");

        Path outputFile = Paths.get("TestOutput/Test09_MergedComboBox.pdf");
        Files.createDirectories(outputFile.getParent());
        merged.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        System.out.println("Test09: " + choicesAfterCount + " choices preserved after merge");
    }

    // ==================== P3 Tests (Low) ====================

    /**
     * Test 10: RTF Multi-Column (P3)
     * Release note: RTF to PDF Conversion Issue with Landscape Multi-Column Layout
     * Jira: PDF-2072
     * Tests 3 RTF files: multicolumn_landscape.rtf, complex_layout.rtf, mixed_orientation.rtf
     */
    @Test
    public final void Test10_RtfMultiColumn() throws IOException {
        String[][] testFiles = {
                {RC_DIR + "/multicolumn_landscape.rtf", "Test10_RtfMultiColumn.pdf"},
                {RC_DIR + "/complex_layout.rtf", "Test10_RtfComplexLayout.pdf"},
                {RC_DIR + "/mixed_orientation.rtf", "Test10_RtfMixedOrientation.pdf"},
        };

        Files.createDirectories(Paths.get("TestOutput"));

        for (String[] entry : testFiles) {
            Path testFile = getTestPath(entry[0]);
            Assertions.assertTrue(Files.exists(testFile), entry[0] + " not found");

            PdfDocument pdf = PdfDocument.renderRtfFileAsPdf(testFile);

            Path outputFile = Paths.get("TestOutput/" + entry[1]);
            pdf.saveAs(outputFile);

            Assertions.assertTrue(Files.exists(outputFile), entry[1] + " should be created");
            Assertions.assertTrue(Files.size(outputFile) > 0, entry[1] + " should not be empty");

            int pageCount = pdf.getPagesInfo().size();
            System.out.println("Test10: " + entry[1] + " = " + Files.size(outputFile) + " bytes, " + pageCount + " page(s)");
        }
        // Visual check: Columns should render side-by-side, content not split across pages
    }

    /**
     * Test 11: Header/Footer Responsive CSS (P3)
     * Release note: HTML header/footer font scaling and width incorrect with UseResponsiveCssRendering(1280)
     * Jira: PDF-2141
     */
    @Test
    public final void Test11_HeaderFooterResponsive() throws IOException {
        String bodyHtml = "<!DOCTYPE html><html><head><style>"
                + "body{font-family:Arial,sans-serif;font-size:12pt;margin:40px;}"
                + ".grid{display:grid;grid-template-columns:repeat(3,1fr);gap:10px;}"
                + ".card{border:1px solid #ddd;padding:15px;background:#f9f9f9;}"
                + "</style></head><body>"
                + "<h1>Responsive Header/Footer Test</h1>"
                + "<p>This page tests header and footer width with responsive CSS rendering.</p>"
                + "<div class='grid'>"
                + "<div class='card'>Column 1</div>"
                + "<div class='card'>Column 2</div>"
                + "<div class='card'>Column 3</div>"
                + "</div></body></html>";

        // Render with responsive CSS
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setPaperSize(PaperSize.A4);
        options.UseResponsiveCssRendering(1280);

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(bodyHtml, options);

        // Add header with responsive layout
        HtmlHeaderFooter header = new HtmlHeaderFooter();
        header.setHtmlFragment(
                "<div style='font-family:Arial;font-size:10pt;width:100%;display:flex;"
                        + "justify-content:space-between;background:#333;color:white;padding:5px 10px;'>"
                        + "<span>Company Report</span><span>Page {page} of {total-pages}</span></div>");
        pdf.addHtmlHeader(header);

        // Add footer
        HtmlHeaderFooter footer = new HtmlHeaderFooter();
        footer.setHtmlFragment(
                "<div style='font-family:Arial;font-size:10pt;width:100%;text-align:center;"
                        + "border-top:1px solid #ccc;padding:5px 0;'>"
                        + "Confidential - Generated 2026-02-26</div>");
        pdf.addHtmlFooter(footer);

        Path outputResponsive = Paths.get("TestOutput/Test11_Responsive.pdf");
        Files.createDirectories(outputResponsive.getParent());
        pdf.saveAs(outputResponsive);

        // Also render without responsive for comparison
        ChromePdfRenderOptions noResponsiveOptions = new ChromePdfRenderOptions();
        noResponsiveOptions.setPaperSize(PaperSize.A4);
        PdfDocument pdfNoResponsive = PdfDocument.renderHtmlAsPdf(bodyHtml, noResponsiveOptions);
        pdfNoResponsive.addHtmlHeader(header);
        pdfNoResponsive.addHtmlFooter(footer);

        Path outputNoResponsive = Paths.get("TestOutput/Test11_NoResponsive.pdf");
        pdfNoResponsive.saveAs(outputNoResponsive);

        Assertions.assertTrue(Files.exists(outputResponsive));
        Assertions.assertTrue(Files.exists(outputNoResponsive));
        System.out.println("Test11: Responsive=" + Files.size(outputResponsive) + " bytes, "
                + "NoResponsive=" + Files.size(outputNoResponsive) + " bytes");
        // Visual check: Header dark background should span full page width in both versions
    }

    /**
     * Test 12: PDF/UA With Forms (P1)
     * Release note: PDF/UA duplicate tags + form crash fix (input elements)
     * Jira: PDF-2154 / PDF-1986
     * Note: Avoid <table> and <select> — known SIGSEGV crash
     */
    @Test
    public final void Test12_PdfUAWithForms() throws IOException {
        // HTML with input elements (no <table> or <select> — those crash)
        String html = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>"
                + "<style>body{font-family:Arial,sans-serif;margin:40px;}"
                + "label{display:block;margin:10px 0 5px;font-weight:bold;}"
                + "input{padding:5px;margin-bottom:10px;}</style></head><body>"
                + "<h1>PDF/UA Form Test</h1>"
                + "<p>Testing input elements do not cause AccessViolationException.</p>"
                + "<h2>Contact Form</h2>"
                + "<label for='name'>Name:</label>"
                + "<input type='text' id='name' name='name' value='John Doe'/>"
                + "<label for='email'>Email:</label>"
                + "<input type='email' id='email' name='email' value='john@example.com'/>"
                + "<label for='phone'>Phone:</label>"
                + "<input type='text' id='phone' name='phone' value='+1-555-0123'/>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Path outputFile = Paths.get("TestOutput/Test12_PdfUAForms.pdf");
        Files.createDirectories(outputFile.getParent());

        // This should NOT throw AccessViolationException
        pdf.saveAsPdfUA(outputFile.toString(), NaturalLanguages.English);

        Assertions.assertTrue(Files.exists(outputFile), "PDF/UA with forms should be created");
        Assertions.assertTrue(Files.size(outputFile) > 0, "PDF/UA with forms should not be empty");
        System.out.println("Test12: PDF/UA with forms output " + Files.size(outputFile) + " bytes");
        // Visual check: Verify no duplicate accessibility tags in Adobe Acrobat or PAC
    }
}
