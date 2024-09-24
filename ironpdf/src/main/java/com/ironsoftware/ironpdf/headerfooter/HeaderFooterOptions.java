package com.ironsoftware.ironpdf.headerfooter;

import com.ironsoftware.ironpdf.render.CssMediaType;


/**
 * Defines options for Headers and Footers applied to a {@link com.ironsoftware.ironpdf.PdfDocument} during rendering or at any other time.
 * <p> See: {@link HtmlHeaderFooter} </p>
 * <p> See: {@link TextHeaderFooter} </p>
 * <p> See: {@link com.ironsoftware.ironpdf.render.ChromePdfRenderOptions} </p>
 */
public class HeaderFooterOptions {

    /**
     * The left margin of the header on the page; in millimeters. Default is 0mm.
     */
    private int marginLeftMm = 0;
    /**
     * The right margin of the header on the page; in millimeters. Default is 0mm.
     */
    private int marginRightMm = 0;
    /**
     * The top margin of the header on the page; in millimeters. Default is 0mm.
     */
    private int marginTopMm = 0;
    /**
     * The bottom margin of the header on the page; in millimeters. Default is 0mm.
     */
    private int marginBottomMm = 0;
    /**
     * First number used in {page} merge fields on the first page of the PDF.  Subsequent page numbers will increment from this number. Default is 0.
     */
    private int firstPageNumber = 1;
    /**
     * RenderPdfCssMediaType. Default is PRINT. This will be ignored if the header/footer is
     * TextHeaderFooter.
     */
    private CssMediaType renderPdfCssMediaType = CssMediaType.PRINT;
    /**
     * PdfTitle. Default is null.
     */
    private String pdfTitle = null;
    /**
     * HtmlTitle. Default is null. This will be ignored if the header/footer is TextHeaderFooter.
     */
    private String htmlTitle = null;

    /**
     * Instantiates a new Header footer options.
     */
    public HeaderFooterOptions() {
    }

    /**
     * Gets margin left mm. The left margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @return the margin left mm
     */
    public int getMarginLeftMm() {
        return marginLeftMm;
    }

    /**
     * Sets margin left mm. The left margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @param marginLeftMm the margin left mm
     */
    public void setMarginLeftMm(int marginLeftMm) {
        this.marginLeftMm = marginLeftMm;
    }

    /**
     * Gets margin right mm. The top margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @return the margin right mm
     */
    public int getMarginRightMm() {
        return marginRightMm;
    }

    /**
     * Sets margin right mm. The top margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @param marginRightMm the margin right mm
     */
    public void setMarginRightMm(int marginRightMm) {
        this.marginRightMm = marginRightMm;
    }

    /**
     * Gets margin top mm. The top margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @return the margin top mm
     */
    public int getMarginTopMm() {
        return marginTopMm;
    }

    /**
     * Sets margin top mm. The top margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @param marginTopMm the margin top mm
     */
    public void setMarginTopMm(int marginTopMm) {
        this.marginTopMm = marginTopMm;
    }

    /**
     * Gets margin bottom mm. The bottom margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @return the margin bottom mm
     */
    public int getMarginBottomMm() {
        return marginBottomMm;
    }

    /**
     * Sets margin bottom mm. The bottom margin of the header on the page; in millimeters. Default is 0mm.
     *
     * @param marginBottomMm the margin bottom mm
     */
    public void setMarginBottomMm(int marginBottomMm) {
        this.marginBottomMm = marginBottomMm;
    }

    /**
     * Gets first page number.First number used in {page} merge fields on the first page of the PDF.  Subsequent page numbers will increment from this number. Default is 0.
     *
     * @return the first page number
     */
    public int getFirstPageNumber() {
        return firstPageNumber;
    }

    /**
     * Sets first page number. First number used in {page} merge fields on the first page of the PDF.  Subsequent page numbers will increment from this number. Default is 0.
     *
     * @param firstPageNumber the first page number
     */
    public void setFirstPageNumber(int firstPageNumber) {
        this.firstPageNumber = firstPageNumber;
    }

    /**
     * Gets render pdf css media type. RenderPdfCssMediaType, Default is PRINT. This will be ignored if the header/footer is
     * TextHeaderFooter.
     *
     * @return the render pdf css media type
     */
    public CssMediaType getRenderPdfCssMediaType() {
        return renderPdfCssMediaType;
    }

    /**
     * Sets render pdf css media type. RenderPdfCssMediaType, Default is PRINT. This will be ignored if the header/footer is
     * TextHeaderFooter.
     *
     * @param renderPdfCssMediaType the render pdf css media type
     */
    public void setRenderPdfCssMediaType(CssMediaType renderPdfCssMediaType) {
        this.renderPdfCssMediaType = renderPdfCssMediaType;
    }

    /**
     * Gets pdf title. Default is null.
     *
     * @return the pdf title
     */
    public String getPdfTitle() {
        return pdfTitle;
    }

    /**
     * Sets pdf title. Default is null.
     *
     * @param pdfTitle the pdf title
     */
    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    /**
     * Gets html title. Default is null. This will be ignored if the header/footer is TextHeaderFooter.
     *
     * @return the html title
     */
    public String getHtmlTitle() {
        return htmlTitle;
    }

    /**
     * Sets html title. Default is null. This will be ignored if the header/footer is TextHeaderFooter.
     *
     * @param htmlTitle the html title
     */
    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }
}


