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

        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body><h1>Hello World !</h1></body>", tempVar);
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertEquals(1, info.size());
        Assertions.assertEquals(420, info.get(0).getWidth(), 1);
    }

    @Test
    public final void InputEncodingTest() {
        ChromePdfRenderOptions tempVar = new ChromePdfRenderOptions();
        tempVar.setInputEncoding("utf-16");

        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body><h1>Hello World !</h1></body>", tempVar);
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertEquals(1, doc.getPagesInfo().size());
    }

    @Test
    final void RenderRtfAsPdfTest() throws IOException {
        PdfDocument doc = PdfDocument.renderRtfFileAsPdf(getTestPath("Data/Sample_RTF.rtf"));
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertTrue(info.size() > 1);
        Assertions.assertTrue(doc.extractTextFromPage(PageSelection.firstPage()).contains("Lorem"));
    }

    @Test
    final void RenderZipAsPdfTest() throws IOException {
        PdfDocument doc = PdfDocument.renderZipAsPdf(getTestPath("Data/ZipTest.zip"), "Basic.html");
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertEquals(4, info.size());
        Assertions.assertFalse(doc.extractAllImages().isEmpty());
    }

    @Test
    public final void RenderHtmlAsPdfWithJavascriptTest() {
        ChromePdfRenderOptions tempVar = new ChromePdfRenderOptions();
        tempVar.setPaperSize(PaperSize.A2);
        tempVar.setJavascript("console.log('ExecutePostProcessingJavascript test'); document.querySelectorAll('h1').forEach(function(el){el.style.color='red';})");
        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body><h1>Hello World !</h1></body>", tempVar);
        List<PageInfo> info = doc.getPagesInfo();
        Assertions.assertEquals(1, info.size());
        Assertions.assertEquals(420, info.get(0).getWidth(), 1);
    }

    @Test
    public final void RenderHtmlFileWithLocalCssTest() throws IOException {
        // This test verifies that local CSS files are loaded when rendering HTML files
        // The HTML file references a local CSS file that sets a red background
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/localCssTest.html"));
        Assertions.assertEquals(1, doc.getPagesInfo().size());

        // Convert PDF to image and check the average color
        // If CSS is loaded correctly, the background should be red
        List<java.awt.image.BufferedImage> images = doc.toBufferedImages();
        Assertions.assertFalse(images.isEmpty());

        byte[] imageBytes = toByteArray(images.get(0));
        java.awt.Color avgColor = GetAvgColor(imageBytes);

        // The red component should be significantly higher than others if CSS was applied
        // Red background (#FF0000) should give high red value
        Assertions.assertTrue(avgColor.getRed() > 150,
                "Red component should be high if CSS is applied. Actual red: " + avgColor.getRed());
        Assertions.assertTrue(avgColor.getRed() > avgColor.getBlue(),
                "Red should be greater than blue if CSS is applied");
    }

}
