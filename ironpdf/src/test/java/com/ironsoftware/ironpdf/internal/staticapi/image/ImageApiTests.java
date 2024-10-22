package com.ironsoftware.ironpdf.internal.staticapi.image;

import com.ironsoftware.ironpdf.image.ImageBehavior;
import com.ironsoftware.ironpdf.internal.staticapi.Image_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.TestBase;
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
        InternalPdfDocument doc = Image_Api.imageToPdf(Collections.singletonList(new Image_Api.ImageData(srcImage,"jpg")), ImageBehavior.CENTERED_ON_PAGE, null);
        byte[] image = Image_Api.extractAllImages(doc, Collections.singletonList(0)).get(0);
        Assertions.assertNotEquals(Color.WHITE, GetAvgColor(image));
    }

    @Test
    public final void DrawBitmapTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        Image_Api.drawImage(doc, Files.readAllBytes(Paths.get(getTestFile("/Data/deer.bmp"))),
                Collections.singletonList(0), 10, 10, 10, 10);
        byte[] image = Image_Api.extractAllImages(doc, Collections.singletonList(0)).get(0);
        Assertions.assertNotEquals(Color.WHITE, GetAvgColor(image));
    }

    @Test
    public final void ExtractAllImagesTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));
        Assertions.assertEquals(1, Image_Api.extractAllImages(doc).size());
    }

    @Test
    public final void PdfToImageTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));
        for (byte[] bytes : Image_Api.pdfToImage(doc)) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            java.awt.image.BufferedImage bi = ImageIO.read(byteArrayInputStream);
            Assertions.assertNotNull(bi);
        }
    }

    @Test
    public final void MultiPageTiffTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));
        byte[] tiffData = Image_Api.toMultiPageTiff(doc, Collections.singletonList(0), 96, null, null);
        Assertions.assertNotNull(tiffData);
        Assertions.assertTrue(tiffData.length > 0);
    }


}
