package com.ironsoftware;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.image.DrawImageOptions;
import com.ironsoftware.ironpdf.image.ImageBehavior;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;

public class ImageTests extends TestBase {

    @Test
    public final void ImageToPdfTest() throws IOException {
        PdfDocument doc = PdfDocument.fromImage(Collections.singletonList(getTestPath("/Data/iron.jpg")), ImageBehavior.CENTERED_ON_PAGE);
        byte[] image = toByteArray(doc.extractAllImages().get(0));
        Assertions.assertNotEquals(Color.WHITE, GetAvgColor(image));
    }

    @Test
    public final void DrawBitmapTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        DrawImageOptions options = new DrawImageOptions(10, 10, 10, 10, PageSelection.firstPage());
        doc.drawImage(getTestPath("/Data/iron.jpg"), options);
        byte[] image = toByteArray(doc.extractAllImages().get(0));
        Assertions.assertNotEquals(Color.WHITE, GetAvgColor(image));
    }

    @Test
    public final void ExtractAllImagesTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));
        Assertions.assertEquals(1, doc.extractAllImages().size());
    }

}
