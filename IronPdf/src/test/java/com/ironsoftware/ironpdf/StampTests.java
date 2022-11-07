package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.stamp.HtmlStamper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StampTests extends TestBase {

    @Test
    public final void StampHtmlTest() {
        PdfDocument doc = PdfDocument.renderHtmlAsPdf("ccc");
        doc.applyStamp(new HtmlStamper("<h>THIS IS STAMP<h>"), PageSelection.firstPage());
        Assertions.assertTrue(doc.extractAllText().contains("THIS IS STAMP"));
    }

}
