package com.ironsoftware.ironpdf.staticapi.compress;


import com.ironsoftware.ironpdf.staticapi.Compress_Api;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CompressApiTests extends TestBase {

    @Test
    public final void CompressImages() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/google.pdf"));
        int originalBytesSize = PdfDocument_Api.GetBytes(doc).length;
        Compress_Api.CompressImages(doc, 20, false);
        int compressedBytesSize = PdfDocument_Api.GetBytes(doc).length;

        Assertions.assertTrue(originalBytesSize > compressedBytesSize);
    }
}
