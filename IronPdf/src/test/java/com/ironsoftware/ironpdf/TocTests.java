package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.TableOfContentsTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TocTests extends TestBase {

    @Test
    public final void TestBasicTableOfContents() throws IOException {

        ChromePdfRenderOptions renderOptions = new ChromePdfRenderOptions();
        renderOptions.setTableOfContents(TableOfContentsTypes.Basic);

        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/tableOfContent.html"),renderOptions);
        Assertions.assertEquals(5,doc.getPagesInfo().size());

        //doc.saveAs("C:/tmp/tocTest.pdf");
    }

    @Test
    public final void TestWithPageNumbersTableOfContents() throws IOException {

        ChromePdfRenderOptions renderOptions = new ChromePdfRenderOptions();
        renderOptions.setTableOfContents(TableOfContentsTypes.WithPageNumbers);

        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/tableOfContent.html"),renderOptions);
        Assertions.assertEquals(5,doc.getPagesInfo().size());


        //doc.saveAs("C:/tmp/tocTest3.pdf");
    }

}