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
    public final void SequentialSignatureFieldNamingTest() throws IOException {
        // Signing the same document twice must produce sequentially named fields
        // (Signature1, Signature2) without skipping a number.
        PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>Sequential signature field naming</h1>");

        SignatureManager signatureManager = pdf.getSignature();
        signatureManager.SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456"));
        signatureManager.SignPdfWithSignature(new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456"));

        String pdfText = new String(pdf.getBinaryData(), java.nio.charset.StandardCharsets.ISO_8859_1);

        Assertions.assertTrue(pdfText.contains("/T(Signature1)"),
                "First signature field must be named Signature1");
        Assertions.assertTrue(pdfText.contains("/T(Signature2)"),
                "Second signature field must be named Signature2 (sequential)");
        Assertions.assertFalse(pdfText.contains("/T(Signature3)"),
                "Signature numbering must not skip (no Signature3 after two signings)");
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
