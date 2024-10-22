package com.ironsoftware.ironpdf.internal.staticapi.stamp;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.internal.staticapi.*;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;
import com.ironsoftware.ironpdf.stamp.HtmlStamper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class StampApiTests extends TestBase {

    @Test
    public final void StampHtmlTest() {
        InternalPdfDocument doc = Render_Api.renderHtmlAsPdf("ccc");
        Stamp_Api.applyStamp(doc, new HtmlStamper("<h>THIS IS STAMP<h>"), Collections.singletonList(new PageInfo(0,100,100,100,100, PageRotation.NONE)));
        Assertions.assertTrue(Text_Api.extractAllText(doc).contains("THIS IS STAMP"));
    }
}
