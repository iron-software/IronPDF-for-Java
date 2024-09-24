package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.internal.staticapi.Text_Api;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class TextTests extends TestBase {

    @Test
    public final void ExtractAllTextTest() {
        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body>A A AA</body>");
        Assertions.assertTrue(doc.extractAllText().replace(" ","").contains("AAAA"));
    }

    @Test
    public final void ReplaceTextOnPageTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body><div>AA</div><div>BC</div></body>");
        doc.replaceText(PageSelection.firstPage(), "AA", "BB");
        String text = doc.extractAllText();
        Assertions.assertTrue(text.replaceAll("(\\r|\\n)", "").contains("BB"));
    }

}
