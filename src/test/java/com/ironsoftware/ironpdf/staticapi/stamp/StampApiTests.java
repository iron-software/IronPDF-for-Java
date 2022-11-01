package com.ironsoftware.ironpdf.staticapi.stamp;

import com.ironsoftware.ironpdf.stamp.HtmlStamper;
import com.ironsoftware.ironpdf.staticapi.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

public class StampApiTests extends TestBase {

    @Test
    public final void StampHtmlTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlAsPdf("ccc");
        Stamp_Api.ApplyStamp(doc, new HtmlStamper("<h>THIS IS STAMP<h>"), Collections.singletonList(0));
        Assertions.assertTrue(Text_Api.ExtractAllText(doc).contains("THIS IS STAMP"));
    }
}
