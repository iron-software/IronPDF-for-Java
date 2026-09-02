package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.signature.SignatureHashAlgorithms;
import com.ironsoftware.ironpdf.signature.TimestampHashAlgorithms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RC smoke tests for IronPdf Java 2026.9: configurable signature hash algorithm (PDF-2257) and
 * {@link PdfDocument#setImageAltText(int, int, String)}.
 * <p>The signature digest (field 16) is verified end to end via its CMS OID in the signed output.
 * The timestamp imprint algorithm (field 17) has no offline coverage because the RC suite has no
 * RFC 3161 TSA; it is verified only by the enum-ordinal contract in
 * {@link #Test00_HashAlgorithmOrdinals_MatchWireContract()}.</p>
 */
public class RCTests2026_09 extends TestBase {

    // DER encodings of the CMS digestAlgorithm OIDs, as they appear (hex) inside the signature
    // /Contents blob of the saved PDF. These let the tests observe which digest the engine actually
    // used, which VerifiedSignature does not expose. Both are hex digits only, so case is moot.
    private static final String SHA256_OID_HEX = "0609608648016503040201";
    private static final String SHA512_OID_HEX = "0609608648016503040203";

    // The enum getValue() ordinals are the wire values sent straight to the engine, and the two
    // enums disagree on SHA-1 / SHA-256 (see pdfium_signature.proto). Pinning them catches an
    // off-by-one renumbering that would silently mis-select the digest or the timestamp imprint.
    @Test
    public final void Test00_HashAlgorithmOrdinals_MatchWireContract() {
        Assertions.assertEquals(0, SignatureHashAlgorithms.SHA256.getValue());
        Assertions.assertEquals(1, SignatureHashAlgorithms.SHA384.getValue());
        Assertions.assertEquals(2, SignatureHashAlgorithms.SHA512.getValue());
        Assertions.assertEquals(3, SignatureHashAlgorithms.SHA1.getValue());

        Assertions.assertEquals(0, TimestampHashAlgorithms.SHA1.getValue());
        Assertions.assertEquals(1, TimestampHashAlgorithms.SHA256.getValue());
        Assertions.assertEquals(2, TimestampHashAlgorithms.SHA512.getValue());
    }

    // ==================== PDF-2257: configurable signature hash algorithm ====================

    @Test
    public final void Test01_SignatureHashAlgorithm_Custom_UsesSha512() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>Signature hash algorithm</h1>");
        Signature signature = new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456");
        signature.setSignatureHashAlgorithm(SignatureHashAlgorithms.SHA512);

        pdf.getSignature().SignPdfWithSignature(signature);
        String raw = new String(pdf.getBinaryData(), StandardCharsets.ISO_8859_1);

        // Presence of the SHA-512 digest OID is the observable proof that field 16 reached the engine
        // (a signature made with the SHA-256 default would not carry it). Signature validity itself is
        // covered by SignatureTests. Verifying a re-opened copy here is avoided on purpose: the engine
        // re-saves non-incrementally on verify, which rewrites the signed byte range.
        Assertions.assertTrue(raw.contains(SHA512_OID_HEX),
                "Expected the SHA-512 digest OID in the signed output (field 16 ignored?)");
    }

    @Test
    public final void Test02_SignatureHashAlgorithm_Default_UsesSha256() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>Default hash algorithm</h1>");
        Signature signature = new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456");

        pdf.getSignature().SignPdfWithSignature(signature);
        String raw = new String(pdf.getBinaryData(), StandardCharsets.ISO_8859_1);

        Assertions.assertTrue(raw.contains(SHA256_OID_HEX),
                "Expected the SHA-256 digest OID in the signed output");
        Assertions.assertFalse(raw.contains(SHA512_OID_HEX),
                "Did not expect the SHA-512 digest OID when the default is used");
    }

    // ==================== SetImageAltText ====================

    @Test
    public final void Test03_SetImageAltText_TagsImageAndSurvivesSaveAndPdfUa() throws IOException {
        PdfDocument pdf = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        List<byte[]> before = pdf.extractAllRawImagesFromPages(PageSelection.allPages());
        Assertions.assertTrue(before.size() >= 1, "Expected google.pdf to contain at least one image");

        pdf.setImageAltText(0, 0, "Company logo");

        byte[] saved = pdf.getBinaryData();
        // Direct evidence the RPC did more than no-op: the alt text is observable in the saved bytes.
        Assertions.assertTrue(altTextPresent(saved, "Company logo"),
                "Expected the alt text to be present in the saved document");

        PdfDocument reopened = new PdfDocument(saved);
        List<byte[]> after = reopened.extractAllRawImagesFromPages(PageSelection.allPages());
        Assertions.assertEquals(before.size(), after.size());

        // The tag must survive the documented PDF/UA conversion, not just exist before it: convert,
        // then re-read the saved bytes and assert the alt text is still there.
        reopened.convertToPdfUA(NaturalLanguages.English);
        Assertions.assertTrue(altTextPresent(reopened.getBinaryData(), "Company logo"),
                "Expected the alt text to survive PDF/UA conversion");
    }

    // Matches the alt text against the saved bytes in either the literal (ISO-8859-1) or UTF-16BE
    // form, since a PDF text string may be written either way (UTF-16BE once it holds non-ASCII).
    private static boolean altTextPresent(byte[] pdf, String altText) {
        String raw = new String(pdf, StandardCharsets.ISO_8859_1);
        String utf16be = new String(altText.getBytes(StandardCharsets.UTF_16BE), StandardCharsets.ISO_8859_1);
        return raw.contains(altText) || raw.contains(utf16be);
    }

    @Test
    public final void Test04_SetImageAltText_RejectsInvalidArguments() throws IOException {
        PdfDocument pdf = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> pdf.setImageAltText(-1, 0, "alt"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> pdf.setImageAltText(0, -1, "alt"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> pdf.setImageAltText(0, 0, "   "));
        Assertions.assertThrows(IllegalArgumentException.class, () -> pdf.setImageAltText(0, 0, null));
    }
}
