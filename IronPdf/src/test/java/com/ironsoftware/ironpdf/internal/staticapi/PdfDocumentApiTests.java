package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.edit.ChangeTrackingModes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfDocumentApiTests extends TestBase {

    @Test
    public final void FromFileTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
    }

    @Test
    public final void FromBytesTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromBytes(
                Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf"))));
    }


    @Test
    public final void GetBytesTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        AssertNotNullOrEmpty(PdfDocument_Api.getBytes(doc,false));
    }

    @Test
    public final void GetBytesIncrementalTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"),"","", ChangeTrackingModes.ENABLE_CHANGE_TRACKING);
        AssertNotNullOrEmpty(PdfDocument_Api.getBytes(doc,true));
    }

    @Test
    public final void SaveAsTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        PdfDocument_Api.saveAs(doc, "TestOutput/SaveAsTest.pdf");
    }
}
