package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.signature.SignatureManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SignatureTests extends TestBase {

    @Test
    public final void SignDocumentTest() throws IOException {

        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>Testing 2048 bit digital security</h1>");
        Signature signature = new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456");

        SignatureManager signatureManager = pdf.getSignature();
        Assertions.assertEquals(0, signatureManager.getVerifiedSignature().size());
        signatureManager.SignPdfWithSignature(signature);

        Assertions.assertTrue(signatureManager.VerifyPdfSignatures());
    }

    @Test
    public final void RemoveSignedDocumentTest() throws IOException {

        PdfDocument pdf = PdfDocument.fromFile(getTestPath("/Data/signed_document.pdf"));
        SignatureManager signatureManager = pdf.getSignature();

        signatureManager.RemoveSignature();

        Assertions.assertEquals(0, signatureManager.getVerifiedSignature().size());
    }

    @Test
    public final void SignedPdfHasNonEmptyFieldNameTest() throws IOException {
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>Testing 2048 bit digital security</h1>");
        Signature signature = new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456");

        SignatureManager signatureManager = pdf.getSignature();
        signatureManager.SignPdfWithSignature(signature);

        byte[] bytes = pdf.getBinaryData();
        String pdfText = new String(bytes, java.nio.charset.StandardCharsets.ISO_8859_1);

        Assertions.assertFalse(pdfText.contains("/T()"),
                "Signature field name (/T) must not be empty");
        Assertions.assertTrue(pdfText.contains("/T(Signature1)"),
                "Signature field must have a non-empty name (expected /T(Signature1))");
    }

    @Test
    public final void SecondSignatureOnSameDocumentThrowsTest() throws IOException {
        // Breaking change (2026.10): a PdfDocument carries one new signature at a time. A second
        // signature on the same document throws, before or after a save; the supported multi-signature
        // flow is sign, save, re-open, sign (see ReSigningLoadedSignedDocumentDoesNotReuseFieldNameTest,
        // which also covers sequential Signature1 / Signature2 field naming).
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>One signature per document</h1>");

        SignatureManager signatureManager = pdf.getSignature();
        signatureManager.SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456"));

        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                signatureManager.SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456")));

        // Saving does not reset it: the same document still refuses a second signature.
        String pdfText = new String(pdf.getBinaryData(), java.nio.charset.StandardCharsets.ISO_8859_1);
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                signatureManager.SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456")));

        // The rejected attempts left only the first field behind. Re-read after the post-save rejection
        // so this covers both rejected attempts, not just the first.
        String afterRejected = new String(pdf.getBinaryData(), java.nio.charset.StandardCharsets.ISO_8859_1);
        Assertions.assertTrue(pdfText.contains("/T(Signature1)"), "First signature field must be named Signature1");
        Assertions.assertTrue(afterRejected.contains("/T(Signature1)"), "First signature field must survive the rejections");
        Assertions.assertFalse(afterRejected.contains("/T(Signature2)"), "A rejected second signature must not add a field");
    }

    @Test
    public final void SignSaveReopenSignKeepsBothSignaturesValidTest() throws IOException {
        // The supported multi-signature flow under the one-signature-per-document rule: sign, save,
        // re-open the saved bytes as a new document, sign again. Both signatures must verify.
        PdfDocument first = PdfDocument.renderHtmlAsPdf("<h1>Two signatures, two revisions</h1>");
        first.getSignature().SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456"));
        byte[] signedOnce = first.getBinaryData();

        PdfDocument reopened = new PdfDocument(signedOnce, (String) null);
        reopened.getSignature().SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456"));
        byte[] signedTwice = reopened.getBinaryData();

        java.util.List<com.ironsoftware.ironpdf.signature.VerifiedSignature> verified =
                new PdfDocument(signedTwice, (String) null).getSignature().getVerifiedSignature();
        Assertions.assertEquals(2, verified.size());
        Assertions.assertTrue(verified.stream().allMatch(com.ironsoftware.ironpdf.signature.VerifiedSignature::isValid),
                "Both signatures must verify after sign, save, re-open, sign");
    }

    @Test
    public final void ReSigningLoadedSignedDocumentDoesNotReuseFieldNameTest() throws IOException {
        // Sign once and finalize to bytes.
        PdfDocument first = PdfDocument.renderHtmlAsPdf("<h1>Re-sign a loaded, already-signed document</h1>");
        first.getSignature().SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456"));
        byte[] signedOnce = first.getBinaryData();

        // Reload as a brand-new document (so the in-session signature list is empty) and sign again.
        // The new field must be seeded from the document's existing signature count, not reuse Signature1.
        PdfDocument reloaded = new PdfDocument(signedOnce, (String) null);
        reloaded.getSignature().SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456"));

        String pdfText = new String(reloaded.getBinaryData(), java.nio.charset.StandardCharsets.ISO_8859_1);

        Assertions.assertTrue(pdfText.contains("/T(Signature2)"),
                "Re-signing an already-signed loaded document must name the new field Signature2");
        Assertions.assertEquals(1, countOccurrences(pdfText, "/T(Signature1)"),
                "The existing signature field name must remain unique (no duplicate Signature1)");
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0;
        for (int i = haystack.indexOf(needle); i != -1; i = haystack.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
    }
}
