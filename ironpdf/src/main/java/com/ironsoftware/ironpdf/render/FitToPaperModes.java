package com.ironsoftware.ironpdf.render;

/**
 * Behaviors when fitting HTML content to a physical paper size
 * Can affect zoom level and css layout
 */
public enum FitToPaperModes {
    /**
     * Do nothing.
    */
    Zoom,

    /**
     * Fit an exact number of pixels onto each PDF page.
     * Useful when an optimal pixel width is known or printing documents to match a Chrome browser window display
     */
    FixedPixelWidth,

    /**
     * Measures minimum HTML content width after it is rendered by the browser and calculates zoom
     * based on the width of the content.
     *
     * Useful when fitting a wide content or content of unknown width onto a PDF page
     */
    Automatic,
    /**
     * Measures minimum HTML content width after it is rendered by the browser using the smallest view port possible
     * Useful when fitting smaller content onto a wide page
     */
    AutomaticFit,
    /**
     * Creates a single page PDF which will force its entire content's width and height to fit into one page. Can be used for a consumer bill or receipt.
     * Useful when printing bill or receipt
     */
    ContinuousFeed
}
