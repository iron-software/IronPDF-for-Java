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

}
