package com.ironsoftware;

import com.ironsoftware.ironpdf.PdfDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

public class BackgroundForegroundTests extends TestBase {

    @Test
    public final void AddBackgroundTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        // TODO replace toBufferedImage -> toBufferedImages after change
        byte[] imageBytesBefore = toByteArray(doc.toBufferedImage().get(0));

        Color avgColorBefore = GetAvgColor(imageBytesBefore);

        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        PdfDocument layerDoc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));
        doc.addBackgroundPdf(layerDoc);

        byte[] imageBytesAfter = toByteArray(doc.toBufferedImage().get(0));

        Color avgColorAfter = GetAvgColor(imageBytesAfter);

        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);
    }

    @Test
    public final void AddForegroundTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        byte[] imageBytesBefore = toByteArray(doc.toBufferedImage().get(0));

        Color avgColorBefore = GetAvgColor(imageBytesBefore);

        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        PdfDocument layerDoc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));
        // TODO after remove options will be add backgroundForegroundPdfPageIndex, PageSelection to which the background/foreground will be added.
        doc.addForegroundPdf(layerDoc);

        byte[] imageBytesAfter = toByteArray(doc.toBufferedImage().get(0));

        Color avgColorAfter = GetAvgColor(imageBytesAfter);

        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);
    }
}
