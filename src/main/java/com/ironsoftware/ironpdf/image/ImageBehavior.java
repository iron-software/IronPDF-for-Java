package com.ironsoftware.ironpdf.image;

/**
 * E
 */
public enum ImageBehavior {
    /**
     * Image should be placed on center of the page
     */
    CENTERED_ON_PAGE,

    /**
     * Image should fit to the page
     */
    FIT_TO_PAGE,

    /**
     * Image should fit to the page and keep aspect ratio
     */
    FIT_TO_PAGE_AND_MAINTAIN_ASPECT_RATIO,

    /**
     * Page should fit to the image
     */
    CROP_PAGE,

    /**
     * Image should be placed to the left top corner of the page
     */
    TOP_LEFT_CORNER_OF_PAGE,

    /**
     * Image should be placed to the left bottom corner of the page
     */
    BOTTOM_LEFT_CORNER_OF_PAGE,

    /**
     * Image should be placed to the right top corner of the page
     */
    TOP_RIGHT_CORNER_OF_PAGE,

    /**
     * Image should be placed to the right bottom corner of the page
     */
    BOTTOM_RIGHT_CORNER_OF_PAGE;


    public static ImageBehavior forValue(int value) {
        return values()[value];
    }

    public int getValue() {
        return this.ordinal();
    }
}
