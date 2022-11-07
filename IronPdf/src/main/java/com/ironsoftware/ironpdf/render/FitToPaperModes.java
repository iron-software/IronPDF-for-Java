package com.ironsoftware.ironpdf.render;

/**
 * Behaviors when fitting HTML content to a physical paper size
 * Can affect zoom level and css layout
 */
public enum FitToPaperModes {
    /**
     * Do nothing.
     * Default Chrome PDF printing behavior. Uses {@link ChromePdfRenderOptions#setZoom(int)} to specify zoom level.
     * {@link ChromePdfRenderOptions#setViewPortWidth(int)} has no effect.
     * Instead, Chrome will automatically set the view port based on {@link ChromePdfRenderOptions#setPaperSize(PaperSize)}.
     * Use {@link ChromePdfRenderOptions#setCssMediaType(CssMediaType)} to specify CSS media style.
     *
     * <p> Useful when using 'print' CSS media style or printing documents to match the Chrome browser print preview.
     */

    None,

    /**
     * Fit an exact number of pixels onto each PDF page.
     * Uses {@link ChromePdfRenderOptions#setViewPortWidth} to specify the pixel width to fit on each PDF page.
     * {@link ChromePdfRenderOptions#setZoom(int)} has no effect. Instead, IronPdf will calculate the zoom level based on
     * {@link ChromePdfRenderOptions#setViewPortWidth} and {@link ChromePdfRenderOptions#setPaperSize(PaperSize)}
     *
     * <p> Useful when an optimal pixel width is known or printing documents to match a Chrome browser window display
     */
    FixedPixelWidth,

    /**
     * Measures minimum HTML content width after it is rendered by the browser and calculates {@link ChromePdfRenderOptions#setZoom(int)}
     * based on the width of the content.
     * {@link ChromePdfRenderOptions#setZoom(int)} and {@link ChromePdfRenderOptions#setViewPortWidth} have no effect and are calculated automatically by IronPdf.
     *
     * <p> Useful when fitting a wide content or content of unknown width onto a PDF page
     */
    Automatic,
    /**
     * Measures minimum HTML content width after it is rendered by the browser using the smallest view port possible, and calculates
     * {@link ChromePdfRenderOptions#setZoom(int)} based on the width of the content.
     * Use {@link ChromePdfRenderOptions#setViewPortWidth} to specify the minimum number of pixels to be fit on each PDF page.
     * {@link ChromePdfRenderOptions#setZoom(int)} has no effect and is calculated automatically by IronPdf.
     *
     * <p> Useful when fitting smaller content onto a wide page
     */
    AutomaticFit
}
