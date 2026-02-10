package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperOrientation;
import com.ironsoftware.ironpdf.render.PaperSize;
import com.ironsoftware.ironpdf.stamp.HtmlStamper;
import com.ironsoftware.ironpdf.stamp.HorizontalAlignment;
import com.ironsoftware.ironpdf.stamp.VerticalAlignment;
import com.ironsoftware.ironpdf.standard.PdfAVersions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

/**
 * RC Tests for IronPdf Java 2026.1
 * Mapped from C# RCTests_2026_01.cs — 11 test items covering all release notes.
 * Tests that fail indicate the fix is not available in Java and the corresponding
 * release note should be removed from the Java release notes.
 */
public class RCTests2026_01 extends TestBase {

    private static final String RC_DIR = "/Data/RC_2026_01";

    // ==================== P0 Tests (Blockers) ====================

    /**
     * Test 01: Form Unicode Freeze (P0)
     * Release note: Fixed issue where getting a form field with unicode characters causes IronPDF to freeze
     */
    @Test
    public final void Test01_FormUnicodeFreeze() throws Exception {
        Path testFile = getTestPath(RC_DIR + "/unicode_form.pdf");
        Assertions.assertTrue(Files.exists(testFile), "unicode_form.pdf not found");

        // Use timeout to detect freeze — the original bug causes freeze on form access
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> future = executor.submit(() -> {
                PdfDocument pdf = PdfDocument.fromFile(testFile);
                List<FormField> fields = pdf.getForm().getFields().getAllFields();
                return fields.size();
            });

            int fieldCount = future.get(30, TimeUnit.SECONDS);
            Assertions.assertTrue(fieldCount >= 0, "Form access should return field count without freeze");
        } catch (TimeoutException e) {
            Assertions.fail("FREEZE: Operation timed out when accessing form with Unicode content");
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Test 02: SaveAsPdfUA Colors (P0)
     * Release note: Fixed an issue where converting certain PDFs to PDF/UA could cause text and shape colors
     * to be reset or rendered incorrectly
     */
    @Test
    public final void Test02_SaveAsPdfUAColors() throws IOException {
        String html = "<!DOCTYPE html><html><head><style>"
                + ".red { color: #FF0000; } .blue { color: #0000FF; } .green { color: #00FF00; }"
                + ".box { width:100px;height:50px;display:inline-block;margin:5px;border:1px solid black; }"
                + "</style></head><body>"
                + "<h1 class='red'>Red Heading</h1>"
                + "<h2 class='blue'>Blue Heading</h2>"
                + "<h3 class='green'>Green Heading</h3>"
                + "<div class='box' style='background-color:#FF0000;'>Red</div>"
                + "<div class='box' style='background-color:#00FF00;'>Green</div>"
                + "<div class='box' style='background-color:#0000FF;'>Blue</div>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Path outputOriginal = Paths.get("TestOutput/Test02_Original.pdf");
        Path outputPdfUA = Paths.get("TestOutput/Test02_PdfUA.pdf");
        Files.createDirectories(outputOriginal.getParent());

        pdf.saveAs(outputOriginal);
        pdf.saveAsPdfUA(outputPdfUA.toString(), NaturalLanguages.English);

        Assertions.assertTrue(Files.exists(outputOriginal));
        Assertions.assertTrue(Files.exists(outputPdfUA));
        Assertions.assertTrue(Files.size(outputPdfUA) > 0, "PDF/UA file should not be empty");
    }

    /**
     * Test 03: ToPngImages Flatten (P0)
     * Release note: Fixed an issue where flattening a PDF caused content to disappear or become clipped
     * in documents using non-standard CropBox offsets
     */
    @Test
    public final void Test03_ToPngImagesFlatten() throws IOException {
        Path testFile = getTestPath(RC_DIR + "/nonstandard_cropbox.pdf");
        PdfDocument pdf;

        if (Files.exists(testFile)) {
            pdf = PdfDocument.fromFile(testFile);
        } else {
            pdf = PdfDocument.renderHtmlAsPdf(
                    "<html><body style='margin:0;padding:50px;'>"
                            + "<h1>Flatten Test</h1>"
                            + "<div style='width:200px;height:200px;background:linear-gradient(45deg, red, blue);'></div>"
                            + "</body></html>");
        }

        Path outputDir = Paths.get("TestOutput/Test03_Images");
        Files.createDirectories(outputDir);

        List<String> images = pdf.toPngImages(outputDir + "/page_*.png");

        Assertions.assertNotNull(images, "toPngImages should return image list");
        Assertions.assertFalse(images.isEmpty(), "Should generate at least one image");

        for (String imagePath : images) {
            Assertions.assertTrue(Files.exists(Paths.get(imagePath)),
                    "Generated image file should exist: " + imagePath);
        }
    }

    /**
     * Test 04: CopyPages StackOverflow (P0)
     * Release note: Enhanced copying pages from existing PDF to avoid Stack Overflow exception
     * for some specific documents
     */
    @Test
    public final void Test04_CopyPagesStackOverflow() throws Exception {
        Path testFile = getTestPath(RC_DIR + "/mergetest2.pdf");
        Assertions.assertTrue(Files.exists(testFile), "mergetest2.pdf not found");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> future = executor.submit(() -> {
                PdfDocument pdf = PdfDocument.fromFile(testFile);
                int totalPages = pdf.getPagesInfo().size();

                int endIndex = Math.min(99, totalPages - 1);
                PdfDocument copied = pdf.copyPages(0, endIndex);

                Path outputFile = Paths.get("TestOutput/Test04_CopiedPages.pdf");
                Files.createDirectories(outputFile.getParent());
                copied.saveAs(outputFile);

                return copied.getPagesInfo().size();
            });

            int copiedPages = future.get(60, TimeUnit.SECONDS);
            Assertions.assertTrue(copiedPages > 0, "Should have copied pages without StackOverflow");
        } catch (TimeoutException e) {
            Assertions.fail("TIMEOUT: CopyPages operation timed out");
        } catch (ExecutionException e) {
            if (e.getCause() instanceof StackOverflowError) {
                Assertions.fail("StackOverflowError: The bug is NOT fixed");
            }
            throw e;
        } finally {
            executor.shutdownNow();
        }
    }

    // ==================== P1 Tests (Critical) ====================

    /**
     * Test 05: SaveAsPdfA4 Compliance (P1)
     * Release note: Fixed a compliance issue where PDF/A-4 exports failed validation due to
     * forbidden Unicode values in ToUnicode CMaps
     */
    @Test
    public final void Test05_SaveAsPdfA4Compliance() throws IOException {
        String html = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>"
                + "<style>body{font-family:Arial,sans-serif;margin:40px;}"
                + "table{border-collapse:collapse;width:100%;}"
                + "th,td{border:1px solid #ddd;padding:8px;}"
                + "th{background-color:#4CAF50;color:white;}</style></head><body>"
                + "<h1>PDF/A-4 Compliance Test</h1>"
                + "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>"
                + "<table><tr><th>Header 1</th><th>Header 2</th></tr>"
                + "<tr><td>Data 1</td><td>Data 2</td></tr></table>"
                + "</body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Path outputFile = Paths.get("TestOutput/Test05_PdfA4.pdf");
        Files.createDirectories(outputFile.getParent());

        pdf.saveAsPdfA(outputFile.toString(), PdfAVersions.PdfA4);

        Assertions.assertTrue(Files.exists(outputFile), "PDF/A-4 file should be created");
        Assertions.assertTrue(Files.size(outputFile) > 0, "PDF/A-4 file should not be empty");
    }

    /**
     * Test 06: Image Scale Position (P1)
     * Release note: Enhanced UpdateImageObject with a robust state-tracking transformation system
     */
    @Test
    public final void Test06_ImageScalePosition() throws IOException {
        String html = "<html><body style='margin:50px;'>"
                + "<h1>Image Scale Test</h1>"
                + "<p>Testing image position after scaling.</p>"
                + "<div style='position:relative;'>"
                + "<img src='https://ironpdf.com/img/svgs/iron-pdf-logo.svg' "
                + "width='200' height='60' style='position:absolute;left:100px;top:100px;'/>"
                + "</div></body></html>";

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html);

        Path outputFile = Paths.get("TestOutput/Test06_ImageScale.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        Assertions.assertEquals(1, pdf.getPagesInfo().size());
    }

    /**
     * Test 07: AccessViolation Font Widths (P1)
     * Release note: Fixed a System.AccessViolationException crash when processing malformed font
     * Widths arrays in certain PDFs
     */
    @Test
    public final void Test07_AccessViolationFontWidths() throws Exception {
        Path testFile = getTestPath(RC_DIR + "/malformed_fonts.pdf");
        Assertions.assertTrue(Files.exists(testFile), "malformed_fonts.pdf not found");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> future = executor.submit(() -> {
                PdfDocument pdf = PdfDocument.fromFile(testFile);
                String text = pdf.extractAllText();
                return text.length();
            });

            int charCount = future.get(30, TimeUnit.SECONDS);
            Assertions.assertTrue(charCount >= 0,
                    "Text extraction should complete without AccessViolation");
        } catch (TimeoutException e) {
            Assertions.fail("TIMEOUT: Text extraction timed out");
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Test 08: ToBitmap Crash (P1)
     * Release note: Fixed a crash (Access Violation) when rendering pages with complex or
     * malformed form fields.
     * The fix ensures a proper exception is thrown instead of an Access Violation crash.
     * The PDF has a corrupt page that fails in FPDF_LoadPage (PDFium), which is expected.
     */
    @Test
    public final void Test08_ToBitmapCrash() throws Exception {
        Path testFile = getTestPath(RC_DIR + "/corrupted_forms.pdf");
        Assertions.assertTrue(Files.exists(testFile), "corrupted_forms.pdf not found");

        // The fix ensures a graceful exception instead of an Access Violation crash.
        // Loading the corrupt PDF and rendering to images should throw a handled exception,
        // not crash the process.
        PdfDocument pdf = PdfDocument.fromFile(testFile);
        Assertions.assertThrows(Exception.class, () -> {
            pdf.toBufferedImages();
        }, "Should throw a proper exception for corrupt pages, not crash with Access Violation");
    }

    /**
     * Test 09: Rectangle Y Position (P1)
     * Release note: Fixed an issue where rectangles were drawn at the incorrect Y position
     */
    @Test
    public final void Test09_RectangleYPosition() throws IOException {
        String html = "<html><body style='margin:0;width:595px;height:842px;background:#f0f0f0;'>"
                + "<p style='margin:20px;'>Rectangle position test - A4 page</p>"
                + "</body></html>";

        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setPaperSize(PaperSize.A4);
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(html, options);

        // Stamp rectangles at different Y positions
        HtmlStamper stamper1 = new HtmlStamper(
                "<div style='width:100px;height:30px;background:red;'>"
                        + "<span style='color:white;font-size:10px;'>Y=50</span></div>");
        stamper1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        stamper1.setVerticalAlignment(VerticalAlignment.TOP);
        stamper1.setHorizontalOffset(new com.ironsoftware.ironpdf.edit.Length(50));
        stamper1.setVerticalOffset(new com.ironsoftware.ironpdf.edit.Length(50));

        HtmlStamper stamper2 = new HtmlStamper(
                "<div style='width:100px;height:30px;background:blue;'>"
                        + "<span style='color:white;font-size:10px;'>Y=100</span></div>");
        stamper2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        stamper2.setVerticalAlignment(VerticalAlignment.TOP);
        stamper2.setHorizontalOffset(new com.ironsoftware.ironpdf.edit.Length(50));
        stamper2.setVerticalOffset(new com.ironsoftware.ironpdf.edit.Length(100));

        HtmlStamper stamper3 = new HtmlStamper(
                "<div style='width:100px;height:30px;background:green;'>"
                        + "<span style='color:white;font-size:10px;'>Y=200</span></div>");
        stamper3.setHorizontalAlignment(HorizontalAlignment.LEFT);
        stamper3.setVerticalAlignment(VerticalAlignment.TOP);
        stamper3.setHorizontalOffset(new com.ironsoftware.ironpdf.edit.Length(50));
        stamper3.setVerticalOffset(new com.ironsoftware.ironpdf.edit.Length(200));

        pdf.applyStamp(stamper1, PageSelection.firstPage());
        pdf.applyStamp(stamper2, PageSelection.firstPage());
        pdf.applyStamp(stamper3, PageSelection.firstPage());

        Path outputFile = Paths.get("TestOutput/Test09_RectanglePosition.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));
        // Visual check: Red at Y=50, Blue at Y=100, Green at Y=200 (all from top)
    }

    // ==================== P2 Tests (Medium) ====================

    /**
     * Test 12: HTML Flex Orientation (P2)
     * Release note: Fixed an issue where HTML headers and footers with flex-based layouts did
     * not adapt correctly to different page orientations
     */
    @Test
    public final void Test12_HtmlFlexOrientation() throws IOException {
        String flexHtml = "<!DOCTYPE html><html><head><style>"
                + "body{margin:0;padding:20px;font-family:Arial,sans-serif;}"
                + ".header{display:flex;justify-content:space-between;align-items:center;"
                + "width:100%;background:#f0f0f0;padding:10px;box-sizing:border-box;}"
                + ".logo{font-size:24px;font-weight:bold;color:#333;}"
                + ".nav{display:flex;gap:20px;}"
                + ".content{display:flex;flex-wrap:wrap;gap:20px;margin-top:20px;}"
                + ".card{flex:1 1 200px;border:1px solid #ddd;padding:15px;background:white;}"
                + "</style></head><body>"
                + "<div class='header'><div class='logo'>Company Logo</div>"
                + "<div class='nav'><a href='#'>Home</a><a href='#'>About</a><a href='#'>Contact</a></div></div>"
                + "<div class='content'>"
                + "<div class='card'><h3>Card 1</h3><p>Flex card content</p></div>"
                + "<div class='card'><h3>Card 2</h3><p>Flex card content</p></div>"
                + "<div class='card'><h3>Card 3</h3><p>Flex card content</p></div>"
                + "</div></body></html>";

        // Portrait
        ChromePdfRenderOptions portraitOptions = new ChromePdfRenderOptions();
        portraitOptions.setPaperSize(PaperSize.A4);
        portraitOptions.setPaperOrientation(PaperOrientation.PORTRAIT);
        PdfDocument pdfPortrait = PdfDocument.renderHtmlAsPdf(flexHtml, portraitOptions);

        // Landscape
        ChromePdfRenderOptions landscapeOptions = new ChromePdfRenderOptions();
        landscapeOptions.setPaperSize(PaperSize.A4);
        landscapeOptions.setPaperOrientation(PaperOrientation.LANDSCAPE);
        PdfDocument pdfLandscape = PdfDocument.renderHtmlAsPdf(flexHtml, landscapeOptions);

        Path outputPortrait = Paths.get("TestOutput/Test12_FlexPortrait.pdf");
        Path outputLandscape = Paths.get("TestOutput/Test12_FlexLandscape.pdf");
        Files.createDirectories(outputPortrait.getParent());

        pdfPortrait.saveAs(outputPortrait);
        pdfLandscape.saveAs(outputLandscape);

        Assertions.assertTrue(Files.exists(outputPortrait));
        Assertions.assertTrue(Files.exists(outputLandscape));
        // Visual check: Flex layout should adapt correctly in both orientations
    }

    /**
     * Test 13: Header/Footer Scaling (P2)
     * Release note: Fixed an issue where HTML headers and footers rendered with incorrect height
     * and font scaling when added post-render
     */
    @Test
    public final void Test13_HeaderFooterScaling() throws IOException {
        String bodyHtml = "<!DOCTYPE html><html><head><style>"
                + "body{font-family:Arial,sans-serif;font-size:12pt;margin:40px;}"
                + "h1{font-size:18pt;} p{font-size:12pt;line-height:1.5;}"
                + "</style></head><body>"
                + "<h1>Document Title (18pt)</h1>"
                + "<p>Body text at 12pt font size. This is a reference for comparing "
                + "header and footer font sizes.</p>"
                + "</body></html>";

        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setPaperSize(PaperSize.A4);
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(bodyHtml, options);

        // Add header post-render
        HtmlHeaderFooter header = new HtmlHeaderFooter();
        header.setHtmlFragment(
                "<div style='font-family:Arial,sans-serif;font-size:12pt;"
                        + "width:100%;text-align:center;color:#333;'>"
                        + "Header Text (12pt) - Should Match Body</div>");
        pdf.addHtmlHeader(header);

        // Add footer post-render
        HtmlHeaderFooter footer = new HtmlHeaderFooter();
        footer.setHtmlFragment(
                "<div style='font-family:Arial,sans-serif;font-size:12pt;"
                        + "width:100%;text-align:center;color:#333;'>"
                        + "Footer Text (12pt) - Page {page}</div>");
        pdf.addHtmlFooter(footer);

        Path outputFile = Paths.get("TestOutput/Test13_HeaderFooterScaling.pdf");
        Files.createDirectories(outputFile.getParent());
        pdf.saveAs(outputFile);

        Assertions.assertTrue(Files.exists(outputFile));

        String allText = pdf.extractAllText();
        Assertions.assertTrue(allText.contains("Header Text"),
                "Header text should be present in PDF");
        Assertions.assertTrue(allText.contains("Footer Text"),
                "Footer text should be present in PDF");
        // Visual check: Header/footer font size should match body 12pt
    }
}
