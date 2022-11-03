package com.ironsoftware.ironpdf.backgroundforeground;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.PageSelection;

/**
 * Class allowing configuration of {@link com.ironsoftware.ironpdf.PdfDocument} background and foreground overlays.
 * <p>See : {@link com.ironsoftware.ironpdf.PdfDocument#addBackgroundPdf(PdfDocument) }</p>
 * <p>See : {@link com.ironsoftware.ironpdf.PdfDocument#addForegroundPdf(PdfDocument)}  }</p>
 */
public class BackgroundForegroundOptions {

    /**
     * Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     */
    private int backgroundForegroundPdfPageIndex = 0;
    /**
     * PageSelection to which the background/foreground will be added. Default is
     * PageSelection.AllPages().
     */
    private PageSelection pageSelection = PageSelection.allPages();

    /**
     * Instantiates a new Background foreground options.
     */
    public BackgroundForegroundOptions() {
    }

    /**
     * Gets background foreground pdf page index. Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     *
     * @return the background foreground pdf page index.
     */
    public int getBackgroundForegroundPdfPageIndex() {
        return backgroundForegroundPdfPageIndex;
    }

    /**
     * Sets background foreground pdf page index. Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     *
     * @param value the value.
     */
    public void setBackgroundForegroundPdfPageIndex(int value) {
        this.backgroundForegroundPdfPageIndex = value;
    }

    /**
     * Gets page selection. PageSelection to which the background/foreground will be added. Default is
     * PageSelection.AllPages().
     *
     * @return the page selection
     */
    public PageSelection getPageSelection() {
        return pageSelection;
    }

    /**
     * Sets page selection. PageSelection to which the background/foreground will be added. Default is
     * PageSelection.AllPages().
     *
     * @param pageIndexes the page indexes
     */
    public void setPageSelection(PageSelection pageIndexes) {
        pageSelection = pageIndexes;
    }

}
