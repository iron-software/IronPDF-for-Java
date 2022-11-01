package com.ironsoftware.ironpdf.page;

/**
 * A class which represents one page of a PDF Document.
 */
public class PageInfo {

    /**
     * Gets the width of the pdf page in mm.
     */
    private final double Width;
    /**
     * Gets the height of the pdf page in mm.
     */
    private final double Height;
    /**
     * Gets the width of the pdf page in printer points.
     */
    private final double PrintWidth;
    /**
     * Gets the height of the pdf page  in printer points.
     */
    private final double PrintHeight;
    /**
     * Gets the page orientation.
     */
    private final com.ironsoftware.ironpdf.page.PageRotation PageRotation;

    public PageInfo(double width, double height, double printWidth, double printHeight,
                    com.ironsoftware.ironpdf.page.PageRotation pageRotation) {
        Width = width;
        Height = height;
        PrintWidth = printWidth;
        PrintHeight = printHeight;
        PageRotation = pageRotation;
    }

    public final double getWidth() {
        return Width;
    }

    public final double getHeight() {
        return Height;
    }

    public final double getPrintWidth() {
        return PrintWidth;
    }

    public final double getPrintHeight() {
        return PrintHeight;
    }

    public final com.ironsoftware.ironpdf.page.PageRotation getPageRotation() {
        return PageRotation;
    }
}
