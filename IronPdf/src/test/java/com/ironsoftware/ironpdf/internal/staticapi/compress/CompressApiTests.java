package com.ironsoftware.ironpdf.internal.staticapi.compress;


import com.ironsoftware.ironpdf.internal.staticapi.Compress_Api;
import com.ironsoftware.ironpdf.internal.staticapi.TestBase;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CompressApiTests extends TestBase {

    @Test
    public final void CompressImages() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));
        int originalBytesSize = PdfDocument_Api.getBytes(doc).length;
        Compress_Api.compressImages(doc, 20, false);
        int compressedBytesSize = PdfDocument_Api.getBytes(doc).length;

        Assertions.assertTrue(originalBytesSize > compressedBytesSize);
    }
}
