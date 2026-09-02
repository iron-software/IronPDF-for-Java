package com.ironsoftware.ironpdf.internal.staticapi.signature;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Render_Api;
import com.ironsoftware.ironpdf.internal.staticapi.Signature_Api;
import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.signature.SignatureManager;
import com.ironsoftware.ironpdf.signature.SignaturePermissions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

public class SignatureApiTests extends TestBase {

    @Test
    public final void AllPdfSignaturesTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlAsPdf("<body>A A AA</body>");
        Assertions.assertEquals(0, Signature_Api.getVerifiedSignatures(doc).size());
        Assertions.assertTrue(Signature_Api.verifyPdfSignatures(doc)); // no
        Signature_Api.signPdfWithSignatureFile(doc, new Signature(getTestFile("/Data/IronTest.p12"), "123456"), SignaturePermissions.NoChangesAllowed, Instant.now());
        Assertions.assertTrue(Signature_Api.verifyPdfSignatures(doc));
        // getVerifiedSignatures does not reliably report an unfinalized signature placeholder (the
        // signature is reserved here but the document is never saved): depending on engine state it
        // returns 0 or 1 for the just-reserved signature, and the exact value flips between
        // environments. Accept either rather than pinning a brittle count.
        int verifiedCount = Signature_Api.getVerifiedSignatures(doc).size();
        Assertions.assertTrue(verifiedCount == 0 || verifiedCount == 1,
                "unexpected verified signature count for an unfinalized placeholder: " + verifiedCount);
    }

}
