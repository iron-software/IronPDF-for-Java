package com.ironsoftware.ironpdf.image;

/**
 * Defines options when rasterizing (converting) an PDf to image objects and files.
 * <p>See: {@link com.ironsoftware.ironpdf.PdfDocument#toImages(String, String, ToImageOptions)} and {@link com.ironsoftware.ironpdf.PdfDocument#toBufferedImages(ToImageOptions options)}
 */
public class ToImageOptions {

    /**
     * The desired resolution of the output Images. Default is 96dpi.
     */
    private int dpi = 96;
    /**
     * The target maximum height of the output images. Default is null.
     */
    private Integer imageMaxHeight = null;
    /**
     * The target maximum width of the output images. Default is null.
     */
    private Integer imageMaxWidth = null;

    /**
     * Gets dpi. The desired resolution of the output Images. Default is 96dpi.
     *
     * @return the dpi
     */
    public int getDpi() {
        return dpi;
    }

    /**
     * Sets dpi. The desired resolution of the output Images. Default is 96dpi.
     *
     * @param dpi the dpi
     */
    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    /**
     * Gets image max height. The target maximum height of the output images. Default is null.
     *
     * @return the image max height
     */
    public Integer getImageMaxHeight() {
        return imageMaxHeight;
    }

    /**
     * Sets image max height. The target maximum height of the output images. Default is null.
     *
     * @param imageMaxHeight the image max height
     */
    public void setImageMaxHeight(Integer imageMaxHeight) {
        this.imageMaxHeight = imageMaxHeight;
    }

    /**
     * Gets image max width. The target maximum width of the output images. Default is null.
     *
     * @return the image max width
     */
    public Integer getImageMaxWidth() {
        return imageMaxWidth;
    }

    /**
     * Sets image max width. The target maximum width of the output images. Default is null.
     *
     * @param imageMaxWidth the image max width
     */
    public void setImageMaxWidth(Integer imageMaxWidth) {
        this.imageMaxWidth = imageMaxWidth;
    }
}



