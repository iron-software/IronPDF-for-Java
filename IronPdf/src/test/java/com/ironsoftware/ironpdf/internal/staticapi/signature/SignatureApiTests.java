package com.ironsoftware.ironpdf.internal.staticapi.signature;

import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Render_Api;
import com.ironsoftware.ironpdf.internal.staticapi.Signature_Api;
import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.signature.Signature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SignatureApiTests extends TestBase {

    @Test
    public final void AllPdfSignaturesTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlAsPdf("<body>A A AA</body>");
        Assertions.assertEquals(0, Signature_Api.getVerifiedSignatures(doc).size());
        Assertions.assertFalse(Signature_Api.verifyPdfSignatures(doc));
        Signature_Api.signPdfWithSignatureFile(doc, new Signature(getTestFile("/Data/IronTest.p12"), "123456"));
        Assertions.assertTrue(Signature_Api.verifyPdfSignatures(doc));
        Assertions.assertEquals(1, Signature_Api.getVerifiedSignatures(doc).size());
    }
}
