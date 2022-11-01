package com.ironsoftware.ironpdf.render;

/**
 * Defines which style-sheet should be rendered.   'Print' or 'Screen'.  This matches the CSS3 Media
 * Queries standard.
 */
public enum CssMediaType {
    /**
     * Renders as expected for a web browser.
     */
    PRINT,

    /**
     * Ignores 'Print' styles and includes additional 'Screen' styles where available.
     */
    SCREEN
}
