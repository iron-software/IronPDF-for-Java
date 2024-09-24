package com.ironsoftware.ironpdf.page;

/**
 * A class which represents one page of a PDF Document.
 */
public class PageInfo {

    /**
     * The width of the pdf page in mm.
     */
    private final double width;
    /**
     * The height of the pdf page in mm.
     */
    private final double height;
    /**
     * The width of the pdf page in printer points.
     */
    private final double printWidth;
    /**
     * The height of the pdf page  in printer points.
     */
    private final double printHeight;
    /**
     * Page index
     */
    private final int pageIndex;
    /**
     * Page rotation.
     */
    private final com.ironsoftware.ironpdf.page.PageRotation pageRotation;

    /**
     * Instantiates a new Page info.
     *
     * @param width        the width
     * @param height       the height
     * @param printWidth   the print width
     * @param printHeight  the print height
     * @param pageRotation the page rotation
     */
    public PageInfo(int pageIndex, double width, double height, double printWidth, double printHeight,
                    com.ironsoftware.ironpdf.page.PageRotation pageRotation) {
        this.pageIndex = pageIndex;
        this.width = width;
        this.height = height;
        this.printWidth = printWidth;
        this.printHeight = printHeight;
        this.pageRotation = pageRotation;
    }

    /**
     * Gets width of the pdf page in mm.
     *
     * @return the width
     */
    public final double getWidth() {
        return width;
    }

    /**
     * Gets height of the pdf page in mm.
     *
     * @return the height
     */
    public final double getHeight() {
        return height;
    }

    /**
     * Gets print width of the pdf page in printer points.
     *
     * @return the print width.
     */
    public final double getPrintWidth() {
        return printWidth;
    }

    /**
     * Gets print height of the pdf page in printer points.
     *
     * @return the print height
     */
    public final double getPrintHeight() {
        return printHeight;
    }

    /**
     * Gets page index
     *
     * @return the page index
     */
    public final int getPageIndex() {
        return pageIndex;
    }

    /**
     * Gets page rotation.
     *
     * @return the page rotation
     */
    public final com.ironsoftware.ironpdf.page.PageRotation getPageRotation() {
        return pageRotation;
    }
}
