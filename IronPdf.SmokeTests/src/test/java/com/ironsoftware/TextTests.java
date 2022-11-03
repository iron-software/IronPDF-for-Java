package com.ironsoftware;

import com.ironsoftware.ironpdf.PdfDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class TextTests extends TestBase {

    @Test
    public final void ExtractAllTextTest() {
        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body>A A AA</body>");
        Assertions.assertTrue(doc.extractAllText().contains("A A AA"));
    }

    @Test
    public final void ReplaceTextOnPageTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlAsPdf("<body><div>AA</div><div>BC</div></body>");
        doc.saveAs(Paths.get("C:/tmp/ReplaceTextOnPageTest.pdf"));
        doc.replaceTextOnPage(0, "AA", "BB");
        Assertions.assertTrue(doc.extractAllText().contains("BB"));
    }

}
