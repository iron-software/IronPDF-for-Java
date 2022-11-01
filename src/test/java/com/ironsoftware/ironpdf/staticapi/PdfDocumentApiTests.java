package com.ironsoftware.ironpdf.staticapi;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfDocumentApiTests extends TestBase {

    @Test
    public final void FromFileTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
    }

    @Test
    public final void FromBytesTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromBytes(
                Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf"))));
    }


    @Test
    public final void GetBytesTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        AssertNotNullOrEmpty(PdfDocument_Api.GetBytes(doc));
    }

    @Test
    public final void SaveAsTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        PdfDocument_Api.SaveAs(doc, "TestOutput/SaveAsTest.pdf");
    }
}
