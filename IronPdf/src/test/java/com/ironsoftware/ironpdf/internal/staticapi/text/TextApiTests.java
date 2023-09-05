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
        String text = Text_Api.extractAllText(doc);
        Assertions.assertTrue(text.replace(" ","").contains("AAAA"));
    }

    @Test
    public final void ReplaceTextOnPageTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlAsPdf("<body><div>AA</div><div>BC</div></body>");
        Text_Api.replaceTextOnPage(doc, 0, "AA", "BB");
        String text = Text_Api.extractAllText(doc);
        Assertions.assertTrue(text.replaceAll("(\\r|\\n)", "").contains("BB"));
    }
}
