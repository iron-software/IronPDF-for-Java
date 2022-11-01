package com.ironsoftware.ironpdf.staticapi.render;

import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.Page_Api;
import com.ironsoftware.ironpdf.staticapi.Render_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class RenderingApiTests extends TestBase {

    @Test
    public final void RenderHtmlFileAsPdfTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Assertions.assertEquals(1, Page_Api.GetPagesInfo(doc).size());
    }

    @Test
    public final void RenderUriAsPdfTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderUriAsPdf("https://www.google.com");
        Assertions.assertEquals(1, Page_Api.GetPagesInfo(doc).size());
    }

    @Test
    public final void RenderHtmlAsPdfTest() throws IOException {
        ChromePdfRenderOptions tempVar = new ChromePdfRenderOptions();
        tempVar.setPaperSize(PaperSize.A2);

        InternalPdfDocument doc = Render_Api.RenderHtmlAsPdf("<body><h1>Hello World !</h1></body>", "",
                tempVar);
        List<PageInfo> info = Page_Api.GetPagesInfo(doc);
        Assertions.assertEquals(1, info.size());
        Assertions.assertEquals(420, info.get(0).getWidth(), 1);
    }

}
