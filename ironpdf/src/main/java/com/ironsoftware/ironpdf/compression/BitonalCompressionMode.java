package com.ironsoftware.ironpdf.compression;

/**
 * How the advanced compression pipeline handles bitonal (1-bit black/white) image encoding for
 * scanned documents. See {@link AdvancedCompressionOptions#setBitonalMode(BitonalCompressionMode)}.
 */
public enum BitonalCompressionMode {
    /**
     * Disabled (default). Images are compressed with the normal JPEG pipeline.
     */
    OFF(0),

    /**
     * Force every eligible 8-bit image to be thresholded to 1-bit and CCITT Group 4 encoded. The
     * result is kept only when it is smaller than the JPEG encoding, so it never enlarges an image,
     * but thresholding is lossy: use it only for black-and-white scans or line art, not colour or
     * photographic content.
     */
    FORCE(1);

    private final int value;

    BitonalCompressionMode(int value) {
        this.value = value;
    }

    /**
     * The wire value the engine expects on {@code PdfiumCompressImagesRequestP.bitonal_mode}.
     *
     * @return 0 for off, 1 for force
     */
    public int getValue() {
        return value;
    }
}
