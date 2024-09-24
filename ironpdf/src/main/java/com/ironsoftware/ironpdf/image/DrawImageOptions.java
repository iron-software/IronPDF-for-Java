package com.ironsoftware.ironpdf.image;

import com.ironsoftware.ironpdf.edit.PageSelection;

/**
 * Defines options available to the developers when drawing images onto a {@link com.ironsoftware.ironpdf.PdfDocument}.
 */
public class DrawImageOptions {

    /**
     * PageSelection to which the image will be drawn. Default is PageSelection.FirstPage().
     */
    private PageSelection pageSelection = PageSelection.firstPage();
    /**
     * X coordinate. Default is 0.
     */
    private int x;
    /**
     * Y coordinate. Default is 0.
     */
    private int y;
    /**
     * Width (Pixel).
     */
    private int width;
    /**
     * Height (Pixel).
     */
    private int height;

    /**
     * Draw image options
     *
     * @param x              X coordinate in px
     * @param y              Y coordinate in px
     * @param desired_width  Desired width
     * @param desired_height Desired Height
     */
    public DrawImageOptions(int x, int y, int desired_width, int desired_height) {
        this.x = x;
        this.y = y;
        this.width = desired_width;
        this.height = desired_height;
    }

    /**
     * Draw image options
     *
     * @param x              X coordinate in px
     * @param y              Y coordinate in px
     * @param desired_width  Desired width
     * @param desired_height Desired Height
     * @param pageSelection  PageSelection to which the image will be drawn. Default is                       PageSelection.FirstPage().
     */
    public DrawImageOptions(int x, int y, int desired_width, int desired_height,
                            PageSelection pageSelection) {
        this.x = x;
        this.y = y;
        this.width = desired_width;
        this.height = desired_height;
        this.pageSelection = pageSelection;
    }

    /**
     * Gets page selection. PageSelection to which the image will be drawn. Default is PageSelection.FirstPage().
     *
     * @return the page selection
     */
    public PageSelection getPageSelection() {
        return pageSelection;
    }

    /**
     * Sets page selection. PageSelection to which the image will be drawn. Default is PageSelection.FirstPage().
     *
     * @param pageIndexes the page indexes
     */
    public void setPageSelection(PageSelection pageIndexes) {
        pageSelection = pageIndexes;
    }

    /**
     * Gets X coordinate. Default is 0.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Sets X coordinate. Default is 0.
     *
     * @param x the x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets Y coordinate. Default is 0.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets Y coordinate. Default is 0.
     *
     * @param y the y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets width (Pixel).
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width (Pixel).
     *
     * @param width the width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets height (Pixel).
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height (Pixel).
     *
     * @param height the height
     */
    public void setHeight(int height) {
        this.height = height;
    }

}
