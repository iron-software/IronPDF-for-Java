package com.ironsoftware.ironpdf.backgroundforeground;

import com.ironsoftware.ironpdf.edit.PageSelection;

public class BackgroundForegroundOptions {

    /**
     * Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     */
    private int backgroundForegroundPdfPageIndex = 0;
    /**
     * PageSelection to which the background/foreground will be added. Default is
     * PageSelection.AllPages().
     */
    private PageSelection pageSelection = PageSelection.AllPages();

    public BackgroundForegroundOptions() {
    }

    public int getBackgroundForegroundPdfPageIndex() {
        return backgroundForegroundPdfPageIndex;
    }

    public void setBackgroundForegroundPdfPageIndex(int value) {
        this.backgroundForegroundPdfPageIndex = value;
    }

    public PageSelection getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(PageSelection pageIndexes) {
        pageSelection = pageIndexes;
    }

}
