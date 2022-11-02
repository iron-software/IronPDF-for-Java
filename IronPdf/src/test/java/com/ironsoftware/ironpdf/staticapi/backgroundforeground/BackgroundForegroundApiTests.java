package com.ironsoftware.ironpdf.staticapi.backgroundforeground;

import com.ironsoftware.ironpdf.staticapi.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;

public class BackgroundForegroundApiTests extends TestBase {

    @Test
    public final void AddBackgroundTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        byte[] imageBytesBefore = Image_Api.PdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);

        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        InternalPdfDocument layerDoc = PdfDocument_Api.FromFile(getTestFile("/Data/google.pdf"));
        BackgroundForeground_Api.AddBackground(doc, layerDoc);

        byte[] imageBytesAfter = Image_Api.PdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorAfter = GetAvgColor(imageBytesAfter);

        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);
    }

    @Test
    public final void AddForegroundTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        byte[] imageBytesBefore = Image_Api.PdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);

        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        InternalPdfDocument layerDoc = PdfDocument_Api.FromFile(getTestFile("/Data/google.pdf"));
        BackgroundForeground_Api.AddForeground(doc, layerDoc);

        byte[] imageBytesAfter = Image_Api.PdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorAfter = GetAvgColor(imageBytesAfter);

        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);
    }
}
