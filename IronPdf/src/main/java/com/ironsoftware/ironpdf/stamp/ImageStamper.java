package com.ironsoftware.ironpdf.stamp;

import com.ironsoftware.ironpdf.edit.PageSelection;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This allows the user to edit an existing PDF by adding an image. A subclass of
 * {@link Stamper}. Defines an Image PDF Stamper.
 * {@link com.ironsoftware.ironpdf.PdfDocument#ApplyStamp(Stamper, PageSelection)}
 */
public class ImageStamper extends Stamper {

    /**
     * The path of the image to be stamped by this stamper
     */
    private Path ImageUri;

    /**
     * Initializes a new instance of the {@link ImageStamper} class.
     *
     * @param imagePath The path of the image to be stamped by this stamper
     */
    public ImageStamper(String imagePath) {
        setImageUri(Paths.get(imagePath));
    }

    /**
     * Initializes a new instance of the {@link ImageStamper} class.
     *
     * @param imagePath The uri of the image to be stamped by this stamper
     */
    public ImageStamper(Path imagePath) {
        setImageUri(imagePath);
    }

//    /**
//     * Initializes a new instance of the {@link ImageStamper} class.
//     *
//     * @param bitmap The uri of the image to be stamped by this stamper
//     */
//    public ImageStamper(byte[] bitmap) {
//        //todo implement this
//    }

    public final Path getImageUri() {
        return ImageUri;
    }

    public final void setImageUri(Path value) {
        ImageUri = value;
    }
}
