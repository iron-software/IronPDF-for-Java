package com.ironsoftware.ironpdf.staticapi.image;

import com.ironsoftware.ironpdf.staticapi.Image_Api;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class ImageApiTests extends TestBase {

    @Test
    public final void ImageToPdfTest() throws IOException {
        byte[] srcImage = Files.readAllBytes(Paths.get(getTestFile("/Data/iron.jpg")));
        InternalPdfDocument doc = Image_Api.ImageToPdf(Collections.singletonList(srcImage).iterator());
        byte[] image = Image_Api.ExtractAllImages(doc, Collections.singletonList(0)).get(0);
        Assertions.assertNotEquals(Color.WHITE, GetAvgColor(image));
    }

    @Test
    public final void DrawBitmapTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        Image_Api.DrawBitmap(doc, Files.readAllBytes(Paths.get(getTestFile("/Data/iron.jpg"))),
                Collections.singletonList(0), 10, 10, 10, 10);
        byte[] image = Image_Api.ExtractAllImages(doc, Collections.singletonList(0)).get(0);
        Assertions.assertNotEquals(Color.WHITE, GetAvgColor(image));
    }

    @Test
    public final void ExtractAllImagesTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/google.pdf"));
        Assertions.assertEquals(1, Image_Api.ExtractAllImages(doc).size());
    }

    @Test
    public final void PdfToImageTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/google.pdf"));
        for (byte[] bytes : Image_Api.PdfToImage(doc)) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            java.awt.image.BufferedImage bi = ImageIO.read(byteArrayInputStream);
            Assertions.assertNotNull(bi);
        }
    }
}
