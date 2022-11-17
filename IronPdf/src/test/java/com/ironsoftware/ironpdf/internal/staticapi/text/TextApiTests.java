package com.ironsoftware.ironpdf.internal.staticapi.text;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.internal.staticapi.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TextApiTests extends TestBase {

    @Test
    public final void ExtractAllTextTest() {
        InternalPdfDocument doc = Render_Api.renderHtmlAsPdf("<body>A A AA</body>");
        Assertions.assertTrue(Text_Api.extractAllText(doc).contains("A A AA"));
    }

    @Test
    public final void ReplaceTextOnPageTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlAsPdf("<body><div>AA</div><div>BC</div></body>");
        PdfDocument_Api.saveAs(doc, "C:/tmp/ReplaceTextOnPageTest.pdf");
        Text_Api.replaceTextOnPage(doc, 0, "AA", "BB");
        Assertions.assertTrue(Text_Api.extractAllText(doc).contains("BB"));
    }
}
