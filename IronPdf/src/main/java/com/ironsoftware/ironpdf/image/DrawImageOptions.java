package com.ironsoftware.ironpdf.image;

import com.ironsoftware.ironpdf.edit.PageSelection;

public class DrawImageOptions {

    /**
     * PageSelection to which the Bitmap will be drawn. Default is PageSelection.FirstPage().
     */
    private PageSelection pageSelection = PageSelection.FirstPage();
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
     * Draw a bitmap options
     *
     * @param x              X coordinate
     * @param y              Y coordinate
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
     * Draw a bitmap options
     *
     * @param x              X coordinate
     * @param y              Y coordinate
     * @param desired_width  Desired width
     * @param desired_height Desired Height
     * @param pageSelection  PageSelection to which the Bitmap will be drawn. Default is
     *                       PageSelection.FirstPage().
     */
    public DrawImageOptions(int x, int y, int desired_width, int desired_height,
                            PageSelection pageSelection) {
        this.x = x;
        this.y = y;
        this.width = desired_width;
        this.height = desired_height;
        this.pageSelection = pageSelection;
    }

    public PageSelection getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(PageSelection pageIndexes) {
        pageSelection = pageIndexes;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
