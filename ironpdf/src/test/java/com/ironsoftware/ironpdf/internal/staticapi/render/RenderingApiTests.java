package com.ironsoftware.ironpdf.internal.staticapi.render;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Page_Api;
import com.ironsoftware.ironpdf.internal.staticapi.Render_Api;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class RenderingApiTests extends TestBase {

    @Test
    public final void RenderHtmlFileAsPdfTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Assertions.assertEquals(1, Page_Api.getPagesInfo(doc).size());
    }

    @Test
    public final void RenderUriAsPdfTest() {
        InternalPdfDocument doc = Render_Api.renderUrlAsPdf("https://www.google.com");
        Assertions.assertEquals(1, Page_Api.getPagesInfo(doc).size());
    }

    @Test
    public final void RenderHtmlAsPdfTest() {
        ChromePdfRenderOptions tempVar = new ChromePdfRenderOptions();
        tempVar.setPaperSize(PaperSize.A2);

        InternalPdfDocument doc = Render_Api.renderHtmlAsPdf("<body><h1>Hello World !</h1></body>", tempVar);
        List<PageInfo> info = Page_Api.getPagesInfo(doc);
        Assertions.assertEquals(1, info.size());
        Assertions.assertEquals(420, info.get(0).getWidth(), 1);
    }

}
