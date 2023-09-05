package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * The type Header footer api.
 */
public final class HeaderFooter_Api {

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) {
        addTextHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) {
        RpcClient client = Access.ensureConnection();

        PdfiumAddTextHeaderFooterRequestP req = setAddTextHeaderFooterRequest(internalPdfDocument, header,
                firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, pdfTitle, true);

        EmptyResultP emptyResult = client.blockingStub.pdfiumHeaderFooterAddTextHeaderFooter(
                req);
        Utils_Util.handleEmptyResult(emptyResult);
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) {
        addTextHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) {
        addTextHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, 0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addTextHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo) {
        addTextHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param header              A new instance of {@link TextHeaderFooter} that defines the header                        content and layout.
     * @param firstPageNumber     Optional. The number of first page.
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header,
                                     int firstPageNumber) {
        addTextHeader(internalPdfDocument, header, firstPageNumber, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param header              A new instance of {@link TextHeaderFooter} that defines the header content and                    layout.
     */
    public static void addTextHeader(InternalPdfDocument internalPdfDocument, TextHeaderFooter header) {
        addTextHeader(internalPdfDocument, header, 1, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) {
        addTextFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) {
        RpcClient client = Access.ensureConnection();

        PdfiumAddTextHeaderFooterRequestP req = setAddTextHeaderFooterRequest(internalPdfDocument, footer,
                firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, pdfTitle, false);

        EmptyResultP emptyResult = client.blockingStub.pdfiumHeaderFooterAddTextHeaderFooter(
                req);

        Utils_Util.handleEmptyResult(emptyResult);
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) {
        addTextFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) {
        addTextFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addTextFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo) {
        addTextFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param footer              A new instance of {@link TextHeaderFooter} that defines the header                        content and layout.
     * @param firstPageNumber     Optional. The number of first page.
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber) {
        addTextFooter(internalPdfDocument, footer, firstPageNumber, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param footer              A new instance of {@link TextHeaderFooter} that defines the header content and                    layout.
     */
    public static void addTextFooter(InternalPdfDocument internalPdfDocument, TextHeaderFooter footer) {
        addTextFooter(internalPdfDocument, footer, 1, null, 0, 0, 0, 0, "");
    }

    private static PdfiumAddTextHeaderFooterRequestP setAddTextHeaderFooterRequest(InternalPdfDocument internalPdfDocument, TextHeaderFooter textHeaderFooter, int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                                                                   int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle, boolean isHeader) {
        PdfiumAddTextHeaderFooterRequestP.Builder req = PdfiumAddTextHeaderFooterRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        ChromePdfRenderOptionsP.Builder optionBuilder = ChromePdfRenderOptionsP.newBuilder();

        optionBuilder.setFirstPageNumber(firstPageNumber);
        optionBuilder.setMarginLeft(marginLeftMm);
        optionBuilder.setMarginRight(marginRightMm);
        optionBuilder.setMarginTop(marginTopMm);
        optionBuilder.setMarginBottom(marginBottomMm);

        ChromeUseMarginsP chromeUseMarginsP = ChromeUseMarginsP.newBuilder().setEnumValue(15).build(); //ALL
        optionBuilder.setUseMarginsOnHeaderFooter(chromeUseMarginsP);

        ChromeTextHeaderFooterP.Builder headerFooterBuilder = ChromeTextHeaderFooterP.newBuilder();

        if (pdfTitle != null)
            req.setPdfTitle(pdfTitle);

        if (textHeaderFooter.getLeftText() != null)
            headerFooterBuilder.setLeftText(textHeaderFooter.getLeftText());

        if (textHeaderFooter.getCenterText() != null)
            headerFooterBuilder.setCenterText(textHeaderFooter.getCenterText());

        if (textHeaderFooter.getRightText() != null)
            headerFooterBuilder.setRightText(textHeaderFooter.getRightText());

        if (textHeaderFooter.getDividerLineColor() != null)
            headerFooterBuilder.setDividerLineColor(textHeaderFooter.getDividerLineColor());

        headerFooterBuilder.setDrawDividerLine(textHeaderFooter.isDrawDividerLine());
        headerFooterBuilder.setFont(FontTypes_Converter.toProto(textHeaderFooter.getFont()));
        headerFooterBuilder.setFontSize(textHeaderFooter.getFontSize());

        if (isHeader) {
            optionBuilder.setTextHeader(headerFooterBuilder.build());
        } else {
            optionBuilder.setTextFooter(headerFooterBuilder.build());
        }

        req.setOptions(optionBuilder.build());


        if(pageIndexesToAddFootersTo == null || pageIndexesToAddFootersTo.isEmpty()){
            pageIndexesToAddFootersTo = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        req.addAllPageIndexes(pageIndexesToAddFootersTo);

        return req.build();
    }


    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the                                  *                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                marginTopMm, marginBottomMm, renderPdfCssMediaType, pdfTitle, "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     * @param htmlTitle                 Optional htmlTitle
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) {

        ChromeAddHtmlHeaderFooterRequestP req = setAddHtmlHeaderFooterRequest(internalPdfDocument, header, firstPageNumber,
                pageIndexesToAddFootersTo, marginLeftMm, marginRightMm, marginTopMm, marginBottomMm,
                renderPdfCssMediaType, pdfTitle, htmlTitle, true);

        addHtmlHeaderFooter_Internal(req);
    }

    private static void addHtmlHeaderFooter_Internal(ChromeAddHtmlHeaderFooterRequestP req) {

        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<EmptyResultP> resultChunks = new ArrayList<>();


        client.stub.chromeHeaderFooterAddHtmlHeaderFooter(req,
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        Utils_Util.handleEmptyResultChunks(resultChunks);
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the                                  CSS3 Media Queries standard.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm,
                                     CssMediaType renderPdfCssMediaType) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, renderPdfCssMediaType, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                marginTopMm, marginBottomMm, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                marginTopMm, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the header will be added.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument the internal pdf document
     * @param header              A new instance of IronPdf.HtmlHeaderFooter that defines the header                        content and layout.
     * @param firstPageNumber     Optional. The number of first page.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber) {
        addHtmlHeader(internalPdfDocument, header, firstPageNumber, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param internalPdfDocument the internal pdf document
     * @param header              A new instance of IronPdf.HtmlHeaderFooter that defines the header content and                    layout.
     */
    public static void addHtmlHeader(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter header) {
        addHtmlHeader(internalPdfDocument, header, 1, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, renderPdfCssMediaType, pdfTitle, "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     * @param htmlTitle                 Optional htmlTitle
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber,
                                     List<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) {
        ChromeAddHtmlHeaderFooterRequestP req = setAddHtmlHeaderFooterRequest(internalPdfDocument, footer, firstPageNumber,
                pageIndexesToAddFootersTo, marginLeftMm, marginRightMm, marginTopMm, marginBottomMm,
                renderPdfCssMediaType, pdfTitle, htmlTitle, false);
        addHtmlHeaderFooter_Internal(req);
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the                                  CSS3 Media Queries standard.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, renderPdfCssMediaType, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, 0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument       the internal pdf document
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which                                  the footer will be added.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, List<Integer> pageIndexesToAddFootersTo) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0,
                CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument the internal pdf document
     * @param footer              A new instance of IronPdf.HtmlFooterFooter that defines the footer                        content and layout.
     * @param firstPageNumber     Optional. The number of first page.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber) {
        addHtmlFooter(internalPdfDocument, footer, firstPageNumber, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param internalPdfDocument the internal pdf document
     * @param footer              A new instance of IronPdf.HtmlFooterFooter that defines the footer content and                    layout.
     */
    public static void addHtmlFooter(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter footer) {
        addHtmlFooter(internalPdfDocument, footer, 1, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    private static ChromeAddHtmlHeaderFooterRequestP setAddHtmlHeaderFooterRequest(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter htmlHeaderFooter, int firstPageNumber, List<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                                                                   int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType, String pdfTitle, String htmlTitle, boolean isHeader) {
        ChromeAddHtmlHeaderFooterRequestP.Builder hf = ChromeAddHtmlHeaderFooterRequestP.newBuilder();
        hf.setDocument(internalPdfDocument.remoteDocument);

        ChromePdfRenderOptionsP.Builder optionBuilder = ChromePdfRenderOptionsP.newBuilder();

        optionBuilder.setCustomPaperHeight(297);
        optionBuilder.setCustomPaperWidth(210);
        optionBuilder.setWaitFor(Render_Converter.toProto(new WaitFor()));
        optionBuilder.setViewPortHeight(1280);
        optionBuilder.setViewPortWidth(1024);
        optionBuilder.setPaperSize(Render_Converter.toProto(PaperSize.A4));
        optionBuilder.setFitToPaperMode(Render_Converter.toProto(FitToPaperModes.Zoom));

        optionBuilder.setFirstPageNumber(firstPageNumber);
        optionBuilder.setMarginLeft(marginLeftMm);
        optionBuilder.setMarginRight(marginRightMm);
        optionBuilder.setMarginTop(marginTopMm);
        optionBuilder.setMarginBottom(marginBottomMm);
        optionBuilder.setCssMediaType(Render_Converter.toProto(renderPdfCssMediaType));
        optionBuilder.setUseMarginsOnHeaderFooter(ChromeUseMarginsP.newBuilder().setEnumValue(15)); //ALL
        ChromeHtmlHeaderFooterP.Builder chromeHtmlHeaderFooterPBuilder = ChromeHtmlHeaderFooterP.newBuilder();
        chromeHtmlHeaderFooterPBuilder.setBaseUrl(htmlHeaderFooter.getBaseUrl() != null ? htmlHeaderFooter.getBaseUrl() : "");
        chromeHtmlHeaderFooterPBuilder.setHtmlFragment(htmlHeaderFooter.getHtmlFragment());
        if (htmlHeaderFooter.getDividerLineColor() != null)
            chromeHtmlHeaderFooterPBuilder.setDividerLineColor(htmlHeaderFooter.getDividerLineColor());
        chromeHtmlHeaderFooterPBuilder.setDrawDividerLine(htmlHeaderFooter.isDrawDividerLine());
        if (htmlHeaderFooter.getMaxHeight() != null)
            chromeHtmlHeaderFooterPBuilder.setMaxHeight(htmlHeaderFooter.getMaxHeight());
        chromeHtmlHeaderFooterPBuilder.setLoadStylesAndCSSFromMainHtmlDocument(htmlHeaderFooter.isLoadStylesAndCSSFromMainHtmlDocument());

        if (isHeader) {
            optionBuilder.setHtmlHeader(chromeHtmlHeaderFooterPBuilder);
        } else {
            optionBuilder.setHtmlHeader(chromeHtmlHeaderFooterPBuilder);
            optionBuilder.setHtmlFooter(chromeHtmlHeaderFooterPBuilder);
        }

        if (pdfTitle != null)
            hf.setPdfTitle(pdfTitle);
        if (htmlTitle != null)
            hf.setHtmlTitle(htmlTitle);

        if(pageIndexesToAddFootersTo == null || pageIndexesToAddFootersTo.isEmpty()){
            pageIndexesToAddFootersTo = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        hf.addAllPageIndexes(pageIndexesToAddFootersTo);

        hf.setOptions(optionBuilder);

        return hf.build();
    }

}
