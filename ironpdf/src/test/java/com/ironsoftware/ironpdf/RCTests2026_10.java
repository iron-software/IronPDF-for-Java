package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.compression.AdvancedCompressionOptions;
import com.ironsoftware.ironpdf.compression.BitonalCompressionMode;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.TableOfContentsTypes;
import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.signature.VerifiedSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * RC smoke tests for IronPdf Java 2026.10: signing an existing named signature field (PDF-1690),
 * configurable table of contents titles (PDF-2192), and opt-in bitonal CCITT Group 4 compression
 * (PDF-659). The one-signature-per-document breaking change is covered in {@link SignatureTests}.
 */
public class RCTests2026_10 extends TestBase {

    // One page carrying two empty, named signature fields ("ApproverSignature", "PreparerSignature"),
    // the same setup the .NET PDF-1690 test seeds.
    private static final String SIGNATURE_FIELDS_PDF = "/Data/signature-fields.pdf";
    // One page holding an 8-bit grayscale, JPEG-encoded scan of black text (PDF-659 style input).
    private static final String GRAYSCALE_SCAN_PDF = "/Data/grayscale-scan.pdf";

    private static final String TOC_HTML =
            "<html><body><div>Cover page</div><div id='ironpdf-toc'></div>"
                    + "<h1>Chapter One</h1><p>Body</p><div style='page-break-after:always;'></div>"
                    + "<h1>Chapter Two</h1><p>Body</p></body></html>";
    private static final String TOC_TITLE = "Contents Heading 2026";

    private static int occurrences(String haystack, String needle) {
        int count = 0;
        for (int i = haystack.indexOf(needle); i != -1; i = haystack.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
    }

    private Signature testSignature() throws IOException {
        return new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456");
    }

    // ==================== PDF-1690: sign an existing named signature field ====================

    @Test
    public final void Test01_SignExistingNamedField_FillsInPlace() throws IOException {
        PdfDocument pdf = PdfDocument.fromFile(getTestPath(SIGNATURE_FIELDS_PDF));
        Signature signature = testSignature();
        // The second field, so a pass proves the name was honored rather than the first field filled.
        signature.setSignatureFieldName("PreparerSignature");

        pdf.getSignature().SignPdfWithSignature(signature);
        byte[] signed = pdf.getBinaryData();

        List<VerifiedSignature> verified = new PdfDocument(signed).getSignature().getVerifiedSignature();
        Assertions.assertEquals(1, verified.size());
        Assertions.assertEquals("PreparerSignature", verified.get(0).getSignatureName());

        // Filled in place: no extra signature field was appended.
        String raw = new String(signed, StandardCharsets.ISO_8859_1);
        Assertions.assertEquals(2, occurrences(raw, "/FT/Sig") + occurrences(raw, "/FT /Sig"),
                "Expected the two pre-placed signature fields and no appended one");
    }

    @Test
    public final void Test02_SignMissingNamedField_Throws() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h2>No signature field here</h2>");
        Signature signature = testSignature();
        signature.setSignatureFieldName("DoesNotExist");

        Exception ex = Assertions.assertThrows(Exception.class, () -> pdf.getSignature().SignPdfWithSignature(signature));
        // Assert it is the missing-field failure, not an unrelated cert/render/transport fault.
        Assertions.assertTrue(String.valueOf(ex.getMessage()).contains("DoesNotExist"), ex.getMessage());
    }

    @Test
    public final void Test03_NamedFieldWithSignatureImage_IsRejected() throws IOException {
        PdfDocument pdf = PdfDocument.fromFile(getTestPath(SIGNATURE_FIELDS_PDF));
        Signature signature = testSignature();
        signature.setSignatureFieldName("ApproverSignature");
        signature.setSignatureImage(Files.readAllBytes(getTestPath("/Data/iron.jpg")), new Rectangle(0, 0, 100, 50));

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> pdf.getSignature().SignPdfWithSignature(signature));
    }

    @Test
    public final void Test04_BlankSignatureFieldName_AppendsNewField() throws IOException {
        // Whitespace-only is treated as unset, so a new field is appended as before (matches .NET).
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h2>Blank field name</h2>");
        Signature signature = testSignature();
        signature.setSignatureFieldName("   ");

        pdf.getSignature().SignPdfWithSignature(signature);
        String raw = new String(pdf.getBinaryData(), StandardCharsets.ISO_8859_1);
        Assertions.assertTrue(raw.contains("/T(Signature1)"), "Expected a newly appended Signature1 field");
    }

    // ==================== PDF-2192: table of contents title ====================

    private String renderTocText(String title, boolean includeEntry) {
        ChromePdfRenderOptions options = new ChromePdfRenderOptions();
        options.setTableOfContents(TableOfContentsTypes.WithPageNumbers);
        options.setTableOfContentsTitle(title);
        options.setTableOfContentsIncludesTitleEntry(includeEntry);
        return PdfDocument.renderHtmlAsPdf(TOC_HTML, options).extractAllText();
    }

    @Test
    public final void Test05_TableOfContentsTitle_RendersHeadingAndOptionalEntry() {
        Assertions.assertEquals(0, occurrences(renderTocText(null, false), TOC_TITLE));
        Assertions.assertEquals(1, occurrences(renderTocText(TOC_TITLE, false), TOC_TITLE),
                "The title renders once, as the heading");
        // With the title entry the title is drawn twice: once as the heading, once as the first entry.
        Assertions.assertEquals(2, occurrences(renderTocText(TOC_TITLE, true), TOC_TITLE),
                "The title renders as the heading and as the first TOC entry");
    }

    // ==================== PDF-659: bitonal (CCITT Group 4) compression ====================

    @Test
    public final void Test06_BitonalDefaults_AreOff() {
        AdvancedCompressionOptions options = new AdvancedCompressionOptions();
        Assertions.assertEquals(BitonalCompressionMode.OFF, options.getBitonalMode());
        Assertions.assertNull(options.getBitonalResolutionDpi());
        Assertions.assertNull(options.getBitonalThreshold());
        Assertions.assertEquals(0, BitonalCompressionMode.OFF.getValue());
        Assertions.assertEquals(1, BitonalCompressionMode.FORCE.getValue());

        // The pdfium pass runs for a positive target DPI, or for bitonal alone.
        Assertions.assertFalse(new AdvancedCompressionOptions().setTargetImageDpi(null).pdfiumImagePassWillRun());
        Assertions.assertTrue(new AdvancedCompressionOptions().setTargetImageDpi(null)
                .setBitonalMode(BitonalCompressionMode.FORCE).pdfiumImagePassWillRun());
        Assertions.assertThrows(IllegalArgumentException.class, () -> options.setBitonalMode(null));
    }

    @Test
    public final void Test07_BitonalForce_OnGrayscaleScan_WritesCcittG4() throws IOException {
        Path input = getTestPath(GRAYSCALE_SCAN_PDF);
        long inputSize = Files.size(input);
        Path output = Files.createTempFile("rc2026_10_bitonal_", ".pdf");
        try {
            PdfDocument.fromFile(input).compressAndSaveAs(output.toString(),
                    new AdvancedCompressionOptions().setBitonalMode(BitonalCompressionMode.FORCE));

            byte[] bytes = Files.readAllBytes(output);
            String raw = new String(bytes, StandardCharsets.ISO_8859_1);
            Assertions.assertTrue(raw.contains("CCITTFaxDecode"),
                    "Force mode must store the scanned page as CCITT Group 4, not JPEG");
            Assertions.assertTrue(Pattern.compile("/BitsPerComponent\\s+1\\b").matcher(raw).find(),
                    "A CCITT Group 4 stream is 1 bit per component");
            Assertions.assertTrue(bytes.length < inputSize, "CCITT Group 4 must be smaller than the JPEG scan");

            // The re-encoded page stays decodable.
            Assertions.assertEquals(1, new PdfDocument(bytes).getPagesInfo().size());
        } finally {
            Files.deleteIfExists(output);
        }
    }

    @Test
    public final void Test09_BitonalOnly_NoTargetDpi_FromBytes_WritesCcittG4() throws IOException {
        // Bitonal alone (targetImageDpi null) is the only case where the widened "bitonal runs the
        // pdfium pass" condition decides the outcome, on both the byte-input branch (load into a
        // document rather than the qpdf-only fast path) and the instance branch it delegates to.
        byte[] input = Files.readAllBytes(getTestPath(GRAYSCALE_SCAN_PDF));
        Path output = Files.createTempFile("rc2026_10_bitonal_bytes_", ".pdf");
        try {
            PdfDocument.compressAndSaveAs(input, output.toString(), "",
                    new AdvancedCompressionOptions()
                            .setTargetImageDpi(null)
                            .setBitonalMode(BitonalCompressionMode.FORCE));

            byte[] bytes = Files.readAllBytes(output);
            String raw = new String(bytes, StandardCharsets.ISO_8859_1);
            Assertions.assertTrue(raw.contains("CCITTFaxDecode"),
                    "Bitonal alone must still reach the pdfium pass and write CCITT Group 4");
            Assertions.assertTrue(Pattern.compile("/BitsPerComponent\\s+1\\b").matcher(raw).find());
            Assertions.assertTrue(bytes.length < input.length, "CCITT Group 4 must be smaller than the JPEG scan");
        } finally {
            Files.deleteIfExists(output);
        }
    }

    @Test
    public final void Test08_BitonalOff_KeepsJpeg() throws IOException {
        Path output = Files.createTempFile("rc2026_10_jpeg_", ".pdf");
        try {
            PdfDocument.fromFile(getTestPath(GRAYSCALE_SCAN_PDF))
                    .compressAndSaveAs(output.toString(), new AdvancedCompressionOptions());
            String raw = new String(Files.readAllBytes(output), StandardCharsets.ISO_8859_1);
            Assertions.assertFalse(raw.contains("CCITTFaxDecode"), "Bitonal is opt-in; the default must not use CCITT");
        } finally {
            Files.deleteIfExists(output);
        }
    }
}
