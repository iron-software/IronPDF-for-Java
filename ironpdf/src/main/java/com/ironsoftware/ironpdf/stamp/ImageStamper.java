package com.ironsoftware.ironpdf.stamp;

import com.ironsoftware.ironpdf.edit.PageSelection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This allows the user to edit an existing PDF by adding an image. A subclass of
 * {@link Stamper}. Defines an Image PDF Stamper.
 * {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper, PageSelection)}
 */
public class ImageStamper extends Stamper {


    private byte[] imageData;

    /**
     * Initializes a new instance of the {@link ImageStamper} class.
     *
     * @param imagePath The path of the image to be stamped by this stamper
     * @throws IOException the io exception
     */
    public ImageStamper(String imagePath) throws IOException {

        imageData = Files.readAllBytes(Paths.get(imagePath));
    }

    /**
     * Initializes a new instance of the {@link ImageStamper} class.
     *
     * @param imagePath The uri of the image to be stamped by this stamper
     * @throws IOException the io exception
     */
    public ImageStamper(Path imagePath) throws IOException {
        imageData = Files.readAllBytes(imagePath);
    }

    /**
     * Initializes a new instance of the {@link ImageStamper} class.
     *
     * @param imageBytes The uri of the image to be stamped by this stamper
     */
    public ImageStamper(byte[] imageBytes) {
        imageData = imageBytes;
    }

    /**
     * Initializes a new instance of the {@link ImageStamper} class.
     *
     * @param bufferedImage The {@link BufferedImage} object
     * @throws IOException the io exception
     */
    public ImageStamper(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        imageData = byteArrayOutputStream.toByteArray();
    }

    /**
     * Gets image data (byte[]).
     *
     * @return the image byte[]
     */
    public final byte[] getImageData() {
        return imageData;
    }
}
