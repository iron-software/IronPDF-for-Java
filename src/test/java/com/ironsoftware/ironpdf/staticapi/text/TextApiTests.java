package com.ironsoftware.ironpdf.staticapi.text;

import com.ironsoftware.ironpdf.staticapi.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TextApiTests extends TestBase {

    @Test
    public final void ExtractAllTextTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlAsPdf("<body>A A AA</body>");
        Assertions.assertTrue(Text_Api.ExtractAllText(doc).contains("A A AA"));
    }

    @Test
    public final void ReplaceTextOnPageTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlAsPdf("<body><div>AA</div><div>BC</div></body>");
        PdfDocument_Api.SaveAs(doc, "C:/tmp/ReplaceTextOnPageTest.pdf");
        Text_Api.ReplaceTextOnPage(doc, 0, "AA", "BB");
        Assertions.assertTrue(Text_Api.ExtractAllText(doc).contains("BB"));
    }
}
