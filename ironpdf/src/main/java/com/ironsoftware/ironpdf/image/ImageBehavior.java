package com.ironsoftware.ironpdf.image;

import java.util.List;

/**
 * Defines layout behavior relative to the page size when creating a PDF from images.
 * <p>See: {@link com.ironsoftware.ironpdf.PdfDocument#fromImage(List, ImageBehavior)}</p>
 */
public enum ImageBehavior {
    /**
     * Image will be placed on center of the page
     */
    CENTERED_ON_PAGE,

    /**
     * Image will fit to the defined page size whilst maintaining aspect ratio.
     */
    FIT_TO_PAGE,

    /**
     * Image will fit to the page and keep aspect ratio
     */
    FIT_TO_PAGE_AND_MAINTAIN_ASPECT_RATIO,

     /**
     * Page will be scaled to exactly match the original image dimensions.
     */
    CROP_PAGE,

    /**
     * Image will be placed to the left top corner of the page
     */
    TOP_LEFT_CORNER_OF_PAGE,

    /**
     * Image will be placed to the left bottom corner of the page
     */
    BOTTOM_LEFT_CORNER_OF_PAGE,

    /**
     * Image will be placed to the right top corner of the page
     */
    TOP_RIGHT_CORNER_OF_PAGE,

    /**
     * Image will be placed to the right bottom corner of the page
     */
    BOTTOM_RIGHT_CORNER_OF_PAGE
}



