package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.PdfDocument;
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

}
