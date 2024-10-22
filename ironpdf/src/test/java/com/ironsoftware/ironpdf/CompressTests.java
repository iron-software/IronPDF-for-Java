package com.ironsoftware.ironpdf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CompressTests extends TestBase {

    @Test
    public final void CompressImages() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));
        int originalBytesSize = doc.getBinaryData().length;

        doc.compressImages(20, false);
        int compressedBytesSize = doc.getBinaryData().length;


        Assertions.assertTrue(originalBytesSize > compressedBytesSize);
    }
}
