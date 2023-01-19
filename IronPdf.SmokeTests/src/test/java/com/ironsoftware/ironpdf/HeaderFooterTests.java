package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;

public class HeaderFooterTests extends TestBase {

    @Test
    public final void AddTextHeaderTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        byte[] imageBytesBefore = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        TextHeaderFooter textHF = new TextHeaderFooter();
        textHF.setCenterText("CenterText");
        textHF.setLeftText("LeftText");
        textHF.setRightText("RightText");

        doc.addTextHeader(textHF);

        byte[] imageBytesAfter = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = doc.extractAllText();

        Assertions.assertTrue(allText.contains(textHF.getCenterText()));
        Assertions.assertTrue(allText.contains(textHF.getLeftText()));
        Assertions.assertTrue(allText.contains(textHF.getRightText()));
    }

    @Test
    public final void AddOnlyLeftTextHeaderTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        byte[] imageBytesBefore = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        TextHeaderFooter textHF = new TextHeaderFooter();
        textHF.setLeftText("LeftText");

        doc.addTextHeader(textHF);

        byte[] imageBytesAfter = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = doc.extractAllText();

        Assertions.assertTrue(allText.contains(textHF.getLeftText()));
    }
    @Test
    public final void AddTextFooterTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        byte[] imageBytesBefore = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        TextHeaderFooter textHF = new TextHeaderFooter();
        textHF.setCenterText("CenterText");
        textHF.setLeftText("LeftText");
        textHF.setRightText("RightText");

        doc.addTextFooter(textHF);

        byte[] imageBytesAfter = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = doc.extractAllText();

        Assertions.assertTrue(allText.contains(textHF.getCenterText()));
        Assertions.assertTrue(allText.contains(textHF.getLeftText()));
        Assertions.assertTrue(allText.contains(textHF.getRightText()));
    }

    @Test
    public final void AddHtmlHeaderTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        byte[] imageBytesBefore = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        HtmlHeaderFooter htmlHF = new HtmlHeaderFooter();
        htmlHF.setHtmlFragment(
                "<div style='width:100%;height:100%;background-color:yellow;color:yellow;'>A</div>");

        doc.addHtmlHeader(htmlHF);

        byte[] imageBytesAfter = toByteArray(doc.toBufferedImages().get(0));

        doc.saveAs(Paths.get("TestOutput/AddHtmlHeaderTest.pdf"));

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = doc.extractAllText();
        Assertions.assertTrue(allText.contains("A"));
    }

    @Test
    public final void AddHtmlFooterTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        byte[] imageBytesBefore = toByteArray(doc.toBufferedImages().get(0));

        Color avgColorBefore = GetAvgColor(imageBytesBefore);
        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        HtmlHeaderFooter htmlHF = new HtmlHeaderFooter();
        htmlHF.setHtmlFragment(
                "<div style='width:100%;height:100%;background-color:yellow;color:yellow;'>A</div>");

        doc.addHtmlFooter(htmlHF);

        byte[] imageBytesAfter = toByteArray(doc.toBufferedImages().get(0));

        doc.saveAs(Paths.get("TestOutput/AddHtmlHeaderTest.pdf"));

        Color avgColorAfter = GetAvgColor(imageBytesAfter);
        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);

        String allText = doc.extractAllText();
        Assertions.assertTrue(allText.contains("A"));
    }

}
