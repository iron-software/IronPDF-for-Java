package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class RenderingTests extends TestBase {

    @Test
    public final void RenderHtmlFileAsPdfTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Assertions.assertEquals(1, doc.getPagesInfo().size());
    }

    @Test
    public final void RenderUriAsPdfTest() {
        PdfDocument doc = PdfDocument.renderUrlAsPdf("https://www.google.com");
        Assertions.assertEquals(1, doc.getPagesInfo().size());
    }

    @Test
    public final void RenderHtmlAsPdfTest() {
        ChromePdfRenderOptions tempVar = new ChromePdfRenderOptions();
        tempVar.setPaperSize(PaperSize.A2);

        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body><h1>Hello World !</h1></body>", "",
                tempVar);
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertEquals(1, info.size());
        Assertions.assertEquals(420, info.get(0).getWidth(), 1);
    }

    @Test
    final void RenderRtfAsPdfTest() throws IOException {
        PdfDocument doc = PdfDocument.renderRtfFileAsPdf(getTestPath("Data/Sample_RTF.rtf"));
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertEquals(4, info.size());
        Assertions.assertTrue(doc.extractTextFromPage(PageSelection.firstPage()).contains("Lorem"));
    }

    @Test
    final void RenderZipAsPdfTest() throws IOException {
        PdfDocument doc = PdfDocument.renderZipAsPdf(getTestPath("Data/ZipTest.zip"),"Basic.html");
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertEquals(4, info.size());
        Assertions.assertFalse(doc.extractAllImages().isEmpty());
    }

}
