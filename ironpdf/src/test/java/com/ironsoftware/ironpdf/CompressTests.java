package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.compression.AdvancedCompressionOptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class CompressTests extends TestBase {

    private static final Path OUTPUT_DIR = Paths.get("TestOutput", "PDF-2232");
    private static String cachedHtml = null;

    /**
     * Applies a license from the {@code IRON_PDF_LICENSE_KEY} environment variable when
     * present. Image re-encoding is a licensed feature; without a license the engine
     * may watermark output and skew size comparisons.
     */
    @BeforeAll
    static void applyLicenseFromEnv() {
        String key = System.getenv("IRON_PDF_LICENSE_KEY");
        if (key != null && !key.isEmpty()) {
            License.setLicenseKey(key);
        }
    }

    @Test
    public final void CompressImages() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));
        int originalBytesSize = doc.getBinaryData().length;

        doc.compressImages(20, false);
        int compressedBytesSize = doc.getBinaryData().length;


        Assertions.assertTrue(originalBytesSize > compressedBytesSize);
    }

    /**
     * Core regression: with default options the instance overload produces a file
     * meaningfully smaller than the uncompressed original, and preserves the page count.
     */
    @Test
    public final void PDF2232_DefaultOptions_ShrinkImageHeavy_Instance() throws IOException {
        PdfDocument pdf = freshImageHeavyPdf();
        int pagesBefore = pdf.getPagesInfo().size();

        Path baseline = out("test01_baseline.pdf");
        Path compressed = out("test01_compressed.pdf");
        pdf.saveAs(baseline.toString());
        pdf.compressAndSaveAs(compressed.toString(), new AdvancedCompressionOptions());

        long baseSize = Files.size(baseline);
        long outSize = Files.size(compressed);
        int pagesAfter = PdfDocument.fromFile(compressed).getPagesInfo().size();

        System.out.printf("PDF2232 (instance default): baseline=%d, default-compressed=%d (%.1f%%)%n",
                baseSize, outSize, 100.0 * outSize / baseSize);

        Assertions.assertTrue(outSize < (long) (baseSize * 0.8),
                "Default AdvancedCompressionOptions should shrink image-heavy PDF by >=20%. "
                        + "baseline=" + baseSize + ", compressed=" + outSize);
        Assertions.assertEquals(pagesBefore, pagesAfter, "Page count must be preserved");
    }

    /**
     * The specific bug: leaving {@code JpegQuality} null (the default) must NOT disable
     * image re-encoding. A default options object should compress essentially the same
     * as one with {@code JpegQuality} explicitly set to the 85 fallback.
     */
    @Test
    public final void PDF2232_NullJpegQuality_BehavesLikeDefault85() throws IOException {
        Path defaults = out("test02_defaults.pdf");
        Path explicit85 = out("test02_explicit85.pdf");

        freshImageHeavyPdf().compressAndSaveAs(defaults.toString(), new AdvancedCompressionOptions());
        freshImageHeavyPdf().compressAndSaveAs(explicit85.toString(),
                new AdvancedCompressionOptions().setJpegQuality(85));

        long sDefault = Files.size(defaults);
        long sExplicit = Files.size(explicit85);
        long diff = Math.abs(sDefault - sExplicit);

        System.out.printf("PDF2232 (null==85): defaults=%d, explicit-jpeg85=%d, diff=%d%n",
                sDefault, sExplicit, diff);

        // Both paths downsample to 150 DPI and re-encode at quality 85, so the outputs
        // must be near-identical. A large gap would mean defaults skipped the image pass.
        Assertions.assertTrue(diff <= Math.max(4096, sExplicit / 20),
                "Default (null) JpegQuality should behave like JpegQuality=85. "
                        + "defaults=" + sDefault + ", explicit85=" + sExplicit);
    }

    /**
     * The image downsampling pass must be wired and effective: the defaults are never
     * larger than the no-image-pass path, and an aggressive DPI strictly beats it.
     */
    @Test
    public final void PDF2232_ImagePass_ReducesSize() throws IOException {
        Path defaults = out("test03_defaults.pdf");
        Path dpi50 = out("test03_dpi50.pdf");
        Path noImagePass = out("test03_no_image_pass.pdf");

        freshImageHeavyPdf().compressAndSaveAs(defaults.toString(), new AdvancedCompressionOptions());
        freshImageHeavyPdf().compressAndSaveAs(dpi50.toString(),
                new AdvancedCompressionOptions().setTargetImageDpi(50));
        freshImageHeavyPdf().compressAndSaveAs(noImagePass.toString(),
                new AdvancedCompressionOptions().setTargetImageDpi(null));

        long sDefault = Files.size(defaults);
        long sDpi50 = Files.size(dpi50);
        long sNoImg = Files.size(noImagePass);

        System.out.printf("PDF2232 (image pass): defaults=%d, dpi50=%d, no-image-pass(dpi=null)=%d%n",
                sDefault, sDpi50, sNoImg);

        Assertions.assertTrue(sDefault <= sNoImg,
                "Defaults (DPI 150) must never be larger than the dpi=null path. "
                        + "defaults=" + sDefault + ", dpi=null=" + sNoImg);
        Assertions.assertTrue(sDpi50 < sNoImg,
                "Aggressive downsampling (DPI 50) must beat the dpi=null path. "
                        + "dpi50=" + sDpi50 + ", dpi=null=" + sNoImg);
    }

    /**
     * The static byte[] overload must also shrink with default options (it previously
     * skipped the image pass entirely).
     */
    @Test
    public final void PDF2232_DefaultOptions_ShrinkImageHeavy_StaticBytes() throws IOException {
        PdfDocument pdf = freshImageHeavyPdf();
        byte[] bytes = pdf.getBinaryData();

        Path compressed = out("test04_static_compressed.pdf");
        PdfDocument.compressAndSaveAs(bytes, compressed.toString(), "", new AdvancedCompressionOptions());

        long baseSize = bytes.length;
        long outSize = Files.size(compressed);

        System.out.printf("PDF2232 (static default): baseline=%d, static-default-compressed=%d (%.1f%%)%n",
                baseSize, outSize, 100.0 * outSize / baseSize);

        Assertions.assertTrue(outSize < (long) (baseSize * 0.8),
                "Static byte[] overload with default options should shrink image-heavy PDF by >=20%. "
                        + "baseline=" + baseSize + ", compressed=" + outSize);
    }

    /**
     * Instance and static byte[] overloads must produce equivalent output for the same
     * (default) options on the same source bytes.
     */
    @Test
    public final void PDF2232_StaticVsInstanceParity_DefaultOptions() throws IOException {
        PdfDocument pdf = freshImageHeavyPdf();
        byte[] bytes = pdf.getBinaryData();

        Path instanceOut = out("test05_instance.pdf");
        Path staticOut = out("test05_static.pdf");

        pdf.compressAndSaveAs(instanceOut.toString(), new AdvancedCompressionOptions());
        PdfDocument.compressAndSaveAs(bytes, staticOut.toString(), "", new AdvancedCompressionOptions());

        long i = Files.size(instanceOut);
        long s = Files.size(staticOut);

        System.out.printf("PDF2232 (parity): instance=%d, static=%d%n", i, s);

        Assertions.assertTrue(Math.abs(i - s) <= Math.max(4096, i / 20),
                "instance vs static (default options) should produce ~same size. instance="
                        + i + ", static=" + s);
    }

    /**
     * TargetImageDpi monotonicity: lower DPI yields a smaller (or equal) file.
     * size(50) <= size(150) <= size(null).
     */
    @Test
    public final void PDF2232_DpiMonotonicity() throws IOException {
        Path dpi50 = out("test06_dpi50.pdf");
        Path dpi150 = out("test06_dpi150.pdf");
        Path dpiNull = out("test06_dpiNull.pdf");

        freshImageHeavyPdf().compressAndSaveAs(dpi50.toString(),
                new AdvancedCompressionOptions().setTargetImageDpi(50));
        freshImageHeavyPdf().compressAndSaveAs(dpi150.toString(),
                new AdvancedCompressionOptions().setTargetImageDpi(150));
        freshImageHeavyPdf().compressAndSaveAs(dpiNull.toString(),
                new AdvancedCompressionOptions().setTargetImageDpi(null));

        long s50 = Files.size(dpi50);
        long s150 = Files.size(dpi150);
        long sNull = Files.size(dpiNull);

        System.out.printf("PDF2232 (monotonicity): dpi50=%d, dpi150=%d, dpiNull=%d%n", s50, s150, sNull);

        Assertions.assertTrue(s50 <= s150, "size(dpi=50) should be <= size(dpi=150)");
        Assertions.assertTrue(s150 <= sNull, "size(dpi=150) should be <= size(dpi=null)");
    }

    /**
     * Text content must be preserved when image downsampling is disabled.
     */
    @Test
    public final void PDF2232_TextPreserved_WhenDpiNull() throws IOException {
        PdfDocument pdf = freshImageHeavyPdf();
        String original = pdf.extractAllText();

        Path output = out("test07_text_preserved.pdf");
        pdf.compressAndSaveAs(output.toString(),
                new AdvancedCompressionOptions().setTargetImageDpi(null).setJpegQuality(null));

        String after = PdfDocument.fromFile(output).extractAllText();
        Assertions.assertEquals(original, after,
                "Text must be byte-for-byte identical when TargetImageDpi=null");
        System.out.printf("PDF2232 (text preserved): text length before=%d, after=%d%n",
                original.length(), after.length());
    }

    /**
     * Public defaults must remain TargetImageDpi=150, JpegQuality=null,
     * RemoveStructureTree=false.
     */
    @Test
    public final void PDF2232_PublicDefaults() {
        AdvancedCompressionOptions opts = new AdvancedCompressionOptions();
        Assertions.assertEquals(Integer.valueOf(150), opts.getTargetImageDpi(), "default TargetImageDpi");
        Assertions.assertNull(opts.getJpegQuality(), "default JpegQuality must be null");
        Assertions.assertFalse(opts.isRemoveStructureTree(), "default RemoveStructureTree must be false");
        Assertions.assertTrue(opts.pdfiumWillReEncode(),
                "default options must engage the pdfium re-encode path (DPI 150 > 0)");
    }

    /**
     * Null options and null bytes must be rejected on both overloads.
     */
    @Test
    public final void PDF2232_NullOptions_Rejected() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<html><body><p>hi</p></body></html>");
        byte[] bytes = pdf.getBinaryData();

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> pdf.compressAndSaveAs(out("test09a.pdf").toString(), (AdvancedCompressionOptions) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PdfDocument.compressAndSaveAs(bytes, out("test09b.pdf").toString(), "",
                        (AdvancedCompressionOptions) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PdfDocument.compressAndSaveAs((byte[]) null, "out.pdf", "", new AdvancedCompressionOptions()));
    }

    /**
     * RemoveStructureTree must never increase the file size and must preserve pages.
     */
    @Test
    public final void PDF2232_RemoveStructureTree() throws IOException {
        Path keep = out("test10_keep.pdf");
        Path remove = out("test10_remove.pdf");

        PdfDocument keepPdf = freshImageHeavyPdf();
        int pagesBefore = keepPdf.getPagesInfo().size();
        keepPdf.compressAndSaveAs(keep.toString(),
                new AdvancedCompressionOptions().setRemoveStructureTree(false));
        freshImageHeavyPdf().compressAndSaveAs(remove.toString(),
                new AdvancedCompressionOptions().setRemoveStructureTree(true));

        long sKeep = Files.size(keep);
        long sRemove = Files.size(remove);
        int pagesAfter = PdfDocument.fromFile(remove).getPagesInfo().size();

        System.out.printf("PDF2232 (struct tree): keep=%d, remove=%d%n", sKeep, sRemove);

        Assertions.assertTrue(sRemove <= sKeep, "RemoveStructureTree=true must not increase size");
        Assertions.assertEquals(pagesBefore, pagesAfter, "Page count must be preserved");
    }

    /**
     * Exercises the byte[] fast path: with TargetImageDpi=null and no struct-tree removal,
     * {@code pdfiumWillReEncode()} is false, so the overload skips the loaded-document
     * delegate and streams the raw bytes straight through the from-bytes qpdf RPC. The
     * output must be a valid, no-larger PDF with the page count preserved.
     */
    @Test
    public final void PDF2232_StaticBytes_FastPath_NoImagePass() throws IOException {
        PdfDocument pdf = freshImageHeavyPdf();
        byte[] bytes = pdf.getBinaryData();
        int pagesBefore = pdf.getPagesInfo().size();

        Path compressed = out("test06_static_fastpath.pdf");
        PdfDocument.compressAndSaveAs(bytes, compressed.toString(), "",
                new AdvancedCompressionOptions().setTargetImageDpi(null));

        long outSize = Files.size(compressed);
        byte[] head = Files.readAllBytes(compressed);
        int pagesAfter = PdfDocument.fromFile(compressed).getPagesInfo().size();

        System.out.printf("PDF2232 (static fast path): baseline=%d, compressed=%d%n",
                bytes.length, outSize);

        Assertions.assertTrue(outSize > 0, "Fast-path output must be non-empty");
        Assertions.assertEquals("%PDF-", new String(head, 0, 5), "Fast-path output must be a valid PDF");
        Assertions.assertTrue(outSize <= bytes.length,
                "Structural-only compression must not enlarge the file: in=" + bytes.length + ", out=" + outSize);
        Assertions.assertEquals(pagesBefore, pagesAfter, "Page count must be preserved");
    }

    // ==================== Helpers ====================

    private static Path out(String name) throws IOException {
        Files.createDirectories(OUTPUT_DIR);
        return OUTPUT_DIR.resolve(name);
    }

    private PdfDocument freshImageHeavyPdf() throws IOException {
        return PdfDocument.fromFile(getTestPath("/Data/image-doc.pdf"));
    }
}
