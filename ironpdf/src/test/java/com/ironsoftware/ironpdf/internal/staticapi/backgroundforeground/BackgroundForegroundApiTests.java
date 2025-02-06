package com.ironsoftware.ironpdf.internal.staticapi.backgroundforeground;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.internal.staticapi.BackgroundForeground_Api;
import com.ironsoftware.ironpdf.internal.staticapi.Image_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;

public class BackgroundForegroundApiTests extends TestBase {

    @Test
    public final void AddBackgroundTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/emptyA4.pdf"));

        byte[] imageBytesBefore = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);

        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        InternalPdfDocument layerDoc = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));
        BackgroundForeground_Api.addBackground(doc, layerDoc);

        byte[] imageBytesAfter = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorAfter = GetAvgColor(imageBytesAfter);

        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);
    }

    @Test
    public final void AddForegroundTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/emptyA4.pdf"));

        byte[] imageBytesBefore = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorBefore = GetAvgColor(imageBytesBefore);

        Assertions.assertEquals(Color.WHITE, avgColorBefore);

        InternalPdfDocument layerDoc = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));
        BackgroundForeground_Api.addForeground(doc, layerDoc);

        byte[] imageBytesAfter = Image_Api.pdfToImage(doc, Collections.singletonList(0)).get(0);

        Color avgColorAfter = GetAvgColor(imageBytesAfter);

        Assertions.assertNotEquals(Color.WHITE, avgColorAfter);
    }
}
