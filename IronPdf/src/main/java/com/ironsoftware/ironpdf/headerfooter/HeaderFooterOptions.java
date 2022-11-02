package com.ironsoftware.ironpdf.headerfooter;

import com.ironsoftware.ironpdf.render.CssMediaType;

public class HeaderFooterOptions {

    /**
     * The left margin of the header on the page in mm. Default is 0mm.
     */
    private int marginLeftMm = 0;
    /**
     * The right margin of the header on the page in mm. Default is 0mm.
     */
    private int marginRightMm = 0;
    /**
     * The top margin of the header on the page in mm. Default is 0mm.
     */
    private int marginTopMm = 0;
    /**
     * The bottom margin of the header on the page in mm. Default is 0mm.
     */
    private int marginBottomMm = 0;
    /**
     * The number of first page. Default is 0.
     */
    private int firstPageNumber = 0;
    /**
     * The PageIndexes (zero-based page numbers) to which the header/footer will be added. Default is
     * apply to all pages (null).
     */
    private Iterable<java.lang.Integer> pageIndexesToAddHeaderFootersTo = null;
    /**
     * RenderPdfCssMediaType, Default is PRINT. This will be ignored if the header/footer is
     * TextHeaderFooter.
     */
    private CssMediaType renderPdfCssMediaType = CssMediaType.PRINT;
    /**
     * PdfTitle, Default is null.
     */
    private String pdfTitle = null;
    /**
     * HtmlTitle, Default is null. This will be ignored if the header/footer is TextHeaderFooter.
     */
    private String htmlTitle = null;

    public HeaderFooterOptions() {
    }

    public int getMarginLeftMm() {
        return marginLeftMm;
    }

    public void setMarginLeftMm(int marginLeftMm) {
        this.marginLeftMm = marginLeftMm;
    }

    public int getMarginRightMm() {
        return marginRightMm;
    }

    public void setMarginRightMm(int marginRightMm) {
        this.marginRightMm = marginRightMm;
    }

    public int getMarginTopMm() {
        return marginTopMm;
    }

    public void setMarginTopMm(int marginTopMm) {
        this.marginTopMm = marginTopMm;
    }

    public int getMarginBottomMm() {
        return marginBottomMm;
    }

    public void setMarginBottomMm(int marginBottomMm) {
        this.marginBottomMm = marginBottomMm;
    }

    public int getFirstPageNumber() {
        return firstPageNumber;
    }

    public void setFirstPageNumber(int firstPageNumber) {
        this.firstPageNumber = firstPageNumber;
    }

    public Iterable<java.lang.Integer> getPageIndexesToAddHeaderFootersTo() {
        return pageIndexesToAddHeaderFootersTo;
    }

    public void setPageIndexesToAddHeaderFootersTo(
            Iterable<java.lang.Integer> pageIndexesToAddHeaderFootersTo) {
        this.pageIndexesToAddHeaderFootersTo = pageIndexesToAddHeaderFootersTo;
    }

    public CssMediaType getRenderPdfCssMediaType() {
        return renderPdfCssMediaType;
    }

    public void setRenderPdfCssMediaType(CssMediaType renderPdfCssMediaType) {
        this.renderPdfCssMediaType = renderPdfCssMediaType;
    }

    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }
}
