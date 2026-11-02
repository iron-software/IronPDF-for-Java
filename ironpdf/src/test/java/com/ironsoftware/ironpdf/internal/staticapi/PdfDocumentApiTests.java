package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.edit.ChangeTrackingModes;
import com.ironsoftware.ironpdf.internal.proto.PdfDocumentP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumConvertToPdfUAForScreenReaderRequestP;
import org.junit.jupiter.api.Assertions;
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

    @Test
    public final void ScreenReaderRequestFlagsSourceHtmlAsRawHtmlTest() {
        // PDF-2302: the engine tags renderHtmlAsPdfUA output from a structure-tree DOM. Java has no port of
        // the .NET HtmlStructTreeDOM transform, so it must send the source HTML flagged as raw HTML for the
        // engine to build the DOM. Unflagged, the engine reads the HTML as a DOM and tags all text as /Artifact.
        String html = "<html><body><h1>Title</h1><p>Body</p></body></html>";
        PdfDocumentP document = PdfDocumentP.newBuilder().setDocumentId("doc-1").build();

        PdfiumConvertToPdfUAForScreenReaderRequestP request =
                PdfDocument_Api.buildScreenReaderRequest(document, html, 0);

        Assertions.assertEquals("doc-1", request.getDocument().getDocumentId());
        Assertions.assertEquals(html, request.getHtmlDom());
        Assertions.assertEquals(0, request.getLang());
        Assertions.assertTrue(request.getHtmlDomIsRawHtml());
    }
}
