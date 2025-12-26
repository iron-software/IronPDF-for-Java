package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.standard.PdfUAVersions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfATests extends TestBase {

    @Test
    public final void PdfATest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        int originalBytesSize = doc.getBinaryData().length;

        int pdfAByteSize = doc.convertToPdfA().getBinaryData().length;

        Assertions.assertTrue(originalBytesSize != pdfAByteSize);
    }

    @Test
    public final void PdfATest_PDF_1825() throws IOException {
        //PDF-1825
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        byte[] pdfAByte = doc.convertToPdfA().getBinaryData();
        String rawString = new String(pdfAByte, StandardCharsets.UTF_8);
        Assertions.assertTrue(rawString.toString().contains("<pdfaid:part>3</pdfaid:part>"));
    }


    @Test
    public final void PdfUATest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        int originalBytesSize = doc.getBinaryData().length;

        int pdfUAByteSize = doc.convertToPdfUA(NaturalLanguages.English).getBinaryData().length;

        Assertions.assertTrue(originalBytesSize != pdfUAByteSize);
    }

    @Test
    public final void PdfUA1WithVersionTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        int originalBytesSize = doc.getBinaryData().length;

        int pdfUAByteSize = doc.convertToPdfUA(NaturalLanguages.English, PdfUAVersions.PdfUA1).getBinaryData().length;

        Assertions.assertTrue(originalBytesSize != pdfUAByteSize);
    }

    @Test
    public final void PdfUA2WithVersionTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        int originalBytesSize = doc.getBinaryData().length;

        int pdfUAByteSize = doc.convertToPdfUA(NaturalLanguages.English, PdfUAVersions.PdfUA2).getBinaryData().length;

        Assertions.assertTrue(originalBytesSize != pdfUAByteSize);
    }
}
