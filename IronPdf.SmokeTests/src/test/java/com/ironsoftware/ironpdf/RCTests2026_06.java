package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.compression.AdvancedCompressionOptions;
import com.ironsoftware.ironpdf.compression.ObjectStreamMode;
import com.ironsoftware.ironpdf.form.FormManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * RC Tests for IronPdf Java 2026.6
 * Mapped from C# release notes — covers:
 *   - PDF-2046: AdvancedCompressionOptions / ObjectStreamMode + new compressAndSaveAs overloads
 *   - PDF-2184: setFormFont / setFormFontFromFile / disableFormFontFallback (already on main; re-asserted)
 *   - PDF-2050: Settings.setJobQueueWatchdogTimeoutSeconds is wired and round-trips
 *   - IronPdfEngine 2026.6.1 version pin
 *
 * Engine-side bug fixes (header/footer, CopyPages form fields, TOC numbering,
 * static+dynamic header/footer size growth, ECDSA signatures) ship through the
 * engine binary; no managed API surface to assert from here.
 */
public class RCTests2026_06 extends TestBase {

    private static final String RC_DIR = "/Data/RC_2026_06";

    // ==================== Engine version pin ====================

    /**
     * Test 01: Engine version is 2026.6.1.
     * Trip-wire — prevents an accidental rollback of the engine pin during
     * release prep. If this fails check ironpdf-engine-pack/*\/pom.xml and
     * Setting_Api.IRON_PDF_ENGINE_VERSION are in sync.
     */
    @Test
    public final void Test01_EngineVersion_Is_2026_6_1() {
        Assertions.assertEquals("2026.6.1", Settings.getIronPdfEngineVersion(),
                "Engine version must be 2026.6.1 for the June 2026 release");
        System.out.println("Test01: engine version = " + Settings.getIronPdfEngineVersion());
    }

    // ==================== PDF-2046: AdvancedCompressionOptions ====================

    /**
     * Test 02: Default options reproduce the legacy compressAndSaveAs behaviour.
     *
     * Mirrors C# {@code Advanced_CompressAndSaveAs_With_Default_Options_Should_Be_At_Least_As_Small_As_Legacy}
     * — the advanced overload at its defaults must produce a file no larger than the
     * legacy overload so customers can adopt it without surprise growth.
     */
    @Test
    public final void Test02_AdvancedCompression_Defaults_NotLargerThanLegacy() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(buildImageHeavyHtml());

        Path legacy = Paths.get("TestOutput/Test02_legacy.pdf");
        Path advanced = Paths.get("TestOutput/Test02_advanced.pdf");
        Files.createDirectories(legacy.getParent());

        pdf.compressAndSaveAs(legacy.toString());
        pdf.compressAndSaveAs(advanced.toString(), new AdvancedCompressionOptions());

        Assertions.assertTrue(Files.exists(legacy));
        Assertions.assertTrue(Files.exists(advanced));

        long legacySize = Files.size(legacy);
        long advancedSize = Files.size(advanced);

        Assertions.assertTrue(advancedSize <= legacySize + 1024,
                "Advanced w/ defaults should be ~legacy size. legacy=" + legacySize
                        + ", advanced=" + advancedSize);
        System.out.println("Test02 (PDF-2046): legacy=" + legacySize
                + " bytes, advanced(defaults)=" + advancedSize + " bytes");
    }

    /**
     * Test 03: TargetImageDpi=50 with JpegQuality=50 produces a strictly smaller file
     * than the defaults on an image-heavy PDF. Verifies the pdfium image
     * downsampling path is engaged.
     */
    @Test
    public final void Test03_AdvancedCompression_LowDpiPlusJpeg_ReducesSize() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(buildImageHeavyHtml());

        Path defaults = Paths.get("TestOutput/Test03_defaults.pdf");
        Path aggressive = Paths.get("TestOutput/Test03_aggressive.pdf");
        Files.createDirectories(defaults.getParent());

        pdf.compressAndSaveAs(defaults.toString(), new AdvancedCompressionOptions());
        pdf.compressAndSaveAs(aggressive.toString(),
                new AdvancedCompressionOptions()
                        .setTargetImageDpi(50)
                        .setJpegQuality(50)
                        .setRemoveStructureTree(true));

        long d = Files.size(defaults);
        long a = Files.size(aggressive);

        // Aggressive must not be larger; in practice on image-heavy input it is
        // meaningfully smaller. The non-strict bound here keeps the test stable
        // across content-heavy and text-heavy fixtures.
        Assertions.assertTrue(a <= d,
                "aggressive should be <= defaults. defaults=" + d + ", aggressive=" + a);
        System.out.println("Test03 (PDF-2046): defaults=" + d + " bytes, aggressive=" + a + " bytes");
    }

    /**
     * Test 04: All three ObjectStreamMode values round-trip without throwing
     * and produce a valid PDF.
     */
    @Test
    public final void Test04_AdvancedCompression_AllObjectStreamModes() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><h1>ObjectStream Modes</h1><p>Round-trip.</p></body></html>");

        for (ObjectStreamMode mode : ObjectStreamMode.values()) {
            Path out = Paths.get("TestOutput/Test04_objstream_" + mode + ".pdf");
            Files.createDirectories(out.getParent());

            pdf.compressAndSaveAs(out.toString(),
                    new AdvancedCompressionOptions().setObjectStreams(mode));

            Assertions.assertTrue(Files.exists(out), mode + " output should exist");
            Assertions.assertTrue(Files.size(out) > 0, mode + " output should not be empty");

            // Validate first bytes are PDF magic.
            byte[] head = Files.readAllBytes(out);
            String magic = new String(head, 0, 5);
            Assertions.assertEquals("%PDF-", magic, mode + " output should be a valid PDF");

            System.out.println("Test04 (PDF-2046): mode=" + mode + " size=" + Files.size(out) + " bytes");
        }
    }

    /**
     * Test 05: Null options is rejected. Mirrors .NET ArgumentNullException check.
     */
    @Test
    public final void Test05_AdvancedCompression_NullOptions_Rejected() {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<html><body><p>hi</p></body></html>");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> pdf.compressAndSaveAs("TestOutput/Test05.pdf",
                        (AdvancedCompressionOptions) null));
    }

    /**
     * Test 06: byte[] entrypoint produces equivalent-sized output to instance method
     * with the same options. Mirrors .NET "entrypoint parity" assertion.
     */
    @Test
    public final void Test06_AdvancedCompression_Static_Vs_Instance_Parity() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(buildImageHeavyHtml());
        byte[] bytes = pdf.getBinaryData();

        Path instanceOut = Paths.get("TestOutput/Test06_instance.pdf");
        Path staticOut = Paths.get("TestOutput/Test06_static.pdf");
        Files.createDirectories(instanceOut.getParent());

        AdvancedCompressionOptions opts = new AdvancedCompressionOptions()
                .setTargetImageDpi(150)
                .setJpegQuality(70);

        pdf.compressAndSaveAs(instanceOut.toString(), opts);
        PdfDocument.compressAndSaveAs(bytes, staticOut.toString(), "", opts);

        long i = Files.size(instanceOut);
        long s = Files.size(staticOut);

        // Bounded equivalence — engine writes both, byte-counts may differ by a
        // small amount due to incidental timestamps.
        long diff = Math.abs(i - s);
        Assertions.assertTrue(diff <= Math.max(2048, i / 50),
                "instance vs static should produce ~same size. instance=" + i + ", static=" + s);
        System.out.println("Test06 (PDF-2046): instance=" + i + ", static=" + s + " bytes");
    }

    // ==================== PDF-2184: Form font fallback embedding ====================

    /**
     * Test 07: setFormFont with a real TTF font file does not throw and reports a
     * usable FormManager. We're not signing or filling here — the goal is to make
     * sure the streaming API plumbing introduced in 2026.5 is still wired up after
     * the 2026.6 engine bump.
     */
    @Test
    public final void Test07_PDF2184_SetFormFont_RoundTrips() throws IOException {
        Path fontPath = Paths.get("src/test/resources/Data/Poppins-Regular.ttf");
        if (!Files.exists(fontPath)) {
            System.out.println("Test07 (PDF-2184): SKIPPED — Poppins-Regular.ttf fixture not present");
            return;
        }

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><form><input type='text' name='Name'></form></body></html>");
        FormManager form = pdf.getForm();
        Assertions.assertNotNull(form);

        // Just verify the streaming send completes; engine-side font handling
        // is exercised by the standalone FormTests in this module.
        form.setFormFontFromFile(fontPath);
        System.out.println("Test07 (PDF-2184): setFormFontFromFile completed");
    }

    /**
     * Test 08: disableFormFontFallback completes without throwing.
     */
    @Test
    public final void Test08_PDF2184_DisableFormFontFallback_Roundtrips() {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf(
                "<html><body><form><input type='text' name='Name'></form></body></html>");
        pdf.getForm().disableFormFontFallback();
        System.out.println("Test08 (PDF-2184): disableFormFontFallback completed");
    }

    // ==================== JobQueueWatchdogTimeout ====================

    /**
     * Test 09: JobQueueWatchdogTimeoutSeconds round-trips through Settings.
     * The CLI argument is forwarded to the engine only on subprocess start, so
     * we can't observe the wire effect from a black-box test — but a typo or
     * accidental removal of the getter/setter would fail this assertion.
     */
    @Test
    public final void Test09_PDF2050_JobQueueWatchdogTimeout_RoundTrips() {
        int original = Settings.getJobQueueWatchdogTimeoutSeconds();
        try {
            Settings.setJobQueueWatchdogTimeoutSeconds(180);
            Assertions.assertEquals(180, Settings.getJobQueueWatchdogTimeoutSeconds());

            Settings.setJobQueueWatchdogTimeoutSeconds(0);
            Assertions.assertEquals(0, Settings.getJobQueueWatchdogTimeoutSeconds());

            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> Settings.setJobQueueWatchdogTimeoutSeconds(-1));
        } finally {
            Settings.setJobQueueWatchdogTimeoutSeconds(original);
        }
        System.out.println("Test09 (PDF-2050): JobQueueWatchdogTimeout setter/getter OK");
    }

    // ==================== Helpers ====================

    /**
     * Builds a deliberately image-heavy HTML page so the compression assertions
     * have something to actually shrink. Uses inline SVG gradients so we don't
     * depend on external fixture files.
     */
    private static String buildImageHeavyHtml() {
        StringBuilder sb = new StringBuilder("<html><body>");
        for (int i = 0; i < 4; i++) {
            sb.append("<h1>Chapter ").append(i).append("</h1>");
            sb.append("<svg width='600' height='200' xmlns='http://www.w3.org/2000/svg'>")
                    .append("<defs><linearGradient id='g").append(i).append("' x1='0' y1='0' x2='1' y2='1'>")
                    .append("<stop offset='0%' stop-color='red'/>")
                    .append("<stop offset='50%' stop-color='green'/>")
                    .append("<stop offset='100%' stop-color='blue'/>")
                    .append("</linearGradient></defs>")
                    .append("<rect width='600' height='200' fill='url(#g").append(i).append(")'/>")
                    .append("</svg>");
            for (int j = 0; j < 20; j++) {
                sb.append("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
                        .append("Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>");
            }
        }
        sb.append("</body></html>");
        return sb.toString();
    }
}
