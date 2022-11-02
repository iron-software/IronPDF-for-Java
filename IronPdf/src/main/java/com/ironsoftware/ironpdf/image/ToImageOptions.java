package com.ironsoftware.ironpdf.image;

import com.ironsoftware.ironpdf.edit.PageSelection;

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
     * PageSelection to which  will be rendered to image. Default is PageSelection.AllPages().
     */
    private PageSelection pageSelection = PageSelection.AllPages();

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public Integer getImageMaxHeight() {
        return imageMaxHeight;
    }

    public void setImageMaxHeight(Integer imageMaxHeight) {
        this.imageMaxHeight = imageMaxHeight;
    }

    public Integer getImageMaxWidth() {
        return imageMaxWidth;
    }

    public void setImageMaxWidth(Integer imageMaxWidth) {
        this.imageMaxWidth = imageMaxWidth;
    }

    public PageSelection getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(PageSelection pageIndexes) {
        pageSelection = pageIndexes;
    }

}
