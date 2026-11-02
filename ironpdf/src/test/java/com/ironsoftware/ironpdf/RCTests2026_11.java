package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.Setting_Api;
import com.ironsoftware.ironpdf.standard.PdfUAVersions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RC smoke tests for IronPdf Java 2026.11: the engine pairing, the PDF/UA-2 revision identifier, inserting
 * a PDF into itself, and the PDF 2.0 header. The 2026.11 features of the other clients (external signing,
 * text blocks, region redaction) have no Java API, so there is nothing of them to test here.
 */
public class RCTests2026_11 extends TestBase {

    // Every module that names an IronPdfEngine build. They must all name the same one, otherwise a platform
    // would download a different engine than the client expects.
    private static final List<String> ENGINE_VERSION_FILES = Arrays.asList(
            "ironpdf/src/main/java/com/ironsoftware/ironpdf/internal/staticapi/Setting_Api.java",
            "ironpdf-cloud/patch/src/main/java/com/ironsoftware/ironpdf/internal/staticapi/Setting_Api.java",
            "ironpdf-engine-pack/ironpdf-engine-linux-x64/pom.xml",
            "ironpdf-engine-pack/ironpdf-engine-macos-arm64/pom.xml",
            "ironpdf-engine-pack/ironpdf-engine-macos-x64/pom.xml",
            "ironpdf-engine-pack/ironpdf-engine-windows-x64/pom.xml",
            "ironpdf-engine-pack/ironpdf-engine-windows-x86/pom.xml");

    private static final Pattern ENGINE_VERSION_PATTERN = Pattern.compile(
            "(?:IRON_PDF_ENGINE_VERSION\\s*=\\s*\"([^\"]+)\")|(?:<ironpdf\\.engine\\.version>([^<]+)</ironpdf\\.engine\\.version>)");

    // The IronPdfJava directory: found by walking up from the working directory, which is the module directory
    // under Maven and may be IronPdfJava or the repository root when run from an IDE.
    private static Path javaRoot() {
        for (Path dir = Paths.get("").toAbsolutePath(); dir != null; dir = dir.getParent()) {
            if (Files.isDirectory(dir.resolve("ironpdf-engine-pack"))) return dir;
            if (Files.isDirectory(dir.resolve("IronPdfJava").resolve("ironpdf-engine-pack"))) return dir.resolve("IronPdfJava");
        }
        throw new IllegalStateException("IronPdfJava directory not found from " + Paths.get("").toAbsolutePath());
    }

    private static String engineVersionIn(String relativePath) throws IOException {
        String text = new String(Files.readAllBytes(javaRoot().resolve(relativePath)), StandardCharsets.UTF_8);
        Matcher matcher = ENGINE_VERSION_PATTERN.matcher(text);
        Assertions.assertTrue(matcher.find(), "No engine version found in " + relativePath);
        return matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
    }

    private static byte[] pdf20Bytes(Path emptyPdf) throws IOException {
        // The one-page empty.pdf with its header changed from %PDF-1.x to %PDF-2.0. Both are eight bytes, so no
        // offset in the file moves.
        byte[] bytes = Files.readAllBytes(emptyPdf);
        byte[] header = "%PDF-2.0".getBytes(StandardCharsets.ISO_8859_1);
        System.arraycopy(header, 0, bytes, 0, header.length);
        return bytes;
    }

    private static String headerOf(byte[] pdf) {
        return new String(pdf, 0, 8, StandardCharsets.ISO_8859_1);
    }

    // ==================== engine pairing ====================

    @Test
    public final void Test01_EveryModulePinsTheSameEngineVersion() throws IOException {
        String expected = Setting_Api.IRON_PDF_ENGINE_VERSION;
        Assertions.assertTrue(expected.matches("\\d{4}\\.\\d+\\.\\d+"), expected);

        for (String file : ENGINE_VERSION_FILES) {
            Assertions.assertEquals(expected, engineVersionIn(file), file);
        }
    }

    // ==================== PDF/UA-2 revision ====================

    @Test
    public final void Test02_PdfUa2_WritesRevision2024() throws IOException {
        PdfDocument pdf = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));
        pdf.convertToPdfUA(NaturalLanguages.English, PdfUAVersions.PdfUA2);

        // The engine wrote pdfuaid:rev 2020; the revision is the four-digit year of the standard, 2024.
        String xmp = new String(pdf.getBinaryData(), StandardCharsets.ISO_8859_1);
        // The XMP is read as plain text, so it must be stored uncompressed; fail clearly if it is not.
        Assertions.assertTrue(xmp.contains("<x:xmpmeta"), "No uncompressed XMP packet in the saved PDF");
        Assertions.assertTrue(Pattern.compile("<pdfuaid:part>\\s*2\\s*</pdfuaid:part>").matcher(xmp).find(),
                "PDF/UA-2 output has no pdfuaid:part 2");
        Assertions.assertTrue(Pattern.compile("<pdfuaid:rev>\\s*2024\\s*</pdfuaid:rev>").matcher(xmp).find(),
                "PDF/UA-2 output has no pdfuaid:rev 2024");
        Assertions.assertFalse(Pattern.compile("<pdfuaid:rev>\\s*2020\\s*</pdfuaid:rev>").matcher(xmp).find(),
                "PDF/UA-2 output still writes pdfuaid:rev 2020");
    }

    // ==================== insert a PDF into itself ====================

    @Test
    public final void Test03_InsertPdf_AcceptsTheDocumentItself() throws IOException {
        // Inserting a PDF into itself used to terminate the engine process.
        PdfDocument pdf = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));
        int pagesBefore = pdf.getPagesInfo().size();

        pdf.insertPdf(pdf);

        Assertions.assertEquals(pagesBefore * 2, pdf.getPagesInfo().size());
    }

    // ==================== PDF 2.0 header ====================

    @Test
    public final void Test04_Pdf20_KeepsItsHeaderOnSaveAppendAndInsert() throws IOException {
        // A PDF 2.0 document was written back with a %PDF-1.0 header after a save, append or insert.
        Path empty = getTestPath("/Data/empty.pdf");

        PdfDocument saved = new PdfDocument(pdf20Bytes(empty));
        Assertions.assertEquals("%PDF-2.0", headerOf(saved.getBinaryData()));

        PdfDocument appended = new PdfDocument(pdf20Bytes(empty));
        appended.appendPdf(new PdfDocument(pdf20Bytes(empty)));
        Assertions.assertEquals("%PDF-2.0", headerOf(appended.getBinaryData()));

        PdfDocument inserted = new PdfDocument(pdf20Bytes(empty));
        inserted.insertPdf(new PdfDocument(pdf20Bytes(empty)), 0);
        Assertions.assertEquals("%PDF-2.0", headerOf(inserted.getBinaryData()));
    }
}
