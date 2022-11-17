package com.ironsoftware.ironpdf.internal.staticapi.headerfooter;


import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import com.ironsoftware.ironpdf.internal.staticapi.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;

public class HeaderFooterApiTests extends TestBase {

    @Test
    public final void AddTextHeaderTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        byte[] imageBytesBefore = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        TextHeaderFooter textHF = new TextHeaderFooter();
        textHF.setCenterText("CenterText");
        textHF.setLeftText("LeftText");
        textHF.setRightText("RightText");

        HeaderFooter_Api.addTextHeader(doc, textHF);

        byte[] imageBytesAfter = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = Text_Api.extractAllText(doc);

        Assertions.assertTrue(allText.contains(textHF.getCenterText()));
        Assertions.assertTrue(allText.contains(textHF.getLeftText()));
        Assertions.assertTrue(allText.contains(textHF.getRightText()));
    }

    @Test
    public final void AddTextFooterTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        byte[] imageBytesBefore = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        TextHeaderFooter textHF = new TextHeaderFooter();
        textHF.setCenterText("CenterText");
        textHF.setLeftText("LeftText");
        textHF.setRightText("RightText");

        HeaderFooter_Api.addTextFooter(doc, textHF);

        byte[] imageBytesAfter = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = Text_Api.extractAllText(doc);

        Assertions.assertTrue(allText.contains(textHF.getCenterText()));
        Assertions.assertTrue(allText.contains(textHF.getLeftText()));
        Assertions.assertTrue(allText.contains(textHF.getRightText()));
    }

    @Test
    public final void AddHtmlHeaderTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        byte[] imageBytesBefore = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        HtmlHeaderFooter htmlHF = new HtmlHeaderFooter();
        htmlHF.setHtmlFragment(
                "<div style='width:100%;height:100%;background-color:yellow;color:yellow;'>A</div>");

        HeaderFooter_Api.addHtmlHeader(doc, htmlHF);

        byte[] imageBytesAfter = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        PdfDocument_Api.saveAs(doc, "TestOutput/AddHtmlHeaderTest.pdf");

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = Text_Api.extractAllText(doc);
        Assertions.assertTrue(allText.contains("A"));
    }

    @Test
    public final void AddHtmlFooterTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        byte[] imageBytesBefore = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        HtmlHeaderFooter htmlHF = new HtmlHeaderFooter();
        htmlHF.setHtmlFragment(
                "<div style='width:100%;height:100%;background-color:yellow;color:yellow;'>A</div>");

        HeaderFooter_Api.addHtmlFooter(doc, htmlHF);

        byte[] imageBytesAfter = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        PdfDocument_Api.saveAs(doc, "TestOutput/AddHtmlHeaderTest.pdf");

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = Text_Api.extractAllText(doc);
        Assertions.assertTrue(allText.contains("A"));
    }
}
