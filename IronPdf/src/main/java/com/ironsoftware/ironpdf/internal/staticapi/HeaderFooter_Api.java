package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import com.ironsoftware.ironpdf.internal.proto.AddHtmlHeaderFooterRequestStream;
import com.ironsoftware.ironpdf.internal.proto.AddTextHeaderFooterRequest;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.render.CssMediaType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) {
        RpcClient client = Access.ensureConnection();

        AddTextHeaderFooterRequest req = setAddTextHeaderFooterRequest(internalPdfDocument, header,
                firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, pdfTitle, true);

        EmptyResult emptyResult = client.blockingStub.pdfDocumentHeaderFooterAddTextHeaderFooter(
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) {
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) {
        RpcClient client = Access.ensureConnection();

        AddTextHeaderFooterRequest req = setAddTextHeaderFooterRequest(internalPdfDocument, footer,
                firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, pdfTitle, false);

        EmptyResult emptyResult = client.blockingStub.pdfDocumentHeaderFooterAddTextHeaderFooter(
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) {
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

    private static AddTextHeaderFooterRequest setAddTextHeaderFooterRequest(InternalPdfDocument internalPdfDocument, TextHeaderFooter textHeaderFooter, int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                                                            int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle, boolean isHeader) {
        AddTextHeaderFooterRequest.Builder req = AddTextHeaderFooterRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setFirstPageNumber(firstPageNumber);
        req.setMarginLeftMm(marginLeftMm);
        req.setMarginRightMm(marginRightMm);
        req.setMarginTopMm(marginTopMm);
        req.setMarginBottomMm(marginBottomMm);
        if(pdfTitle != null)
            req.setPdfTitle(pdfTitle);

        if(textHeaderFooter.getLeftText() != null)
            req.setLeftText(textHeaderFooter.getLeftText());

        if(textHeaderFooter.getCenterText() != null)
            req.setCenterText(textHeaderFooter.getCenterText());

        if(textHeaderFooter.getRightText() != null)
            req.setRightText(textHeaderFooter.getRightText());

        if(textHeaderFooter.getDividerLineColor() != null)
            req.setDividerLineColor(textHeaderFooter.getDividerLineColor());

        req.setDrawDividerLine(textHeaderFooter.isDrawDividerLine());
        req.setFont(FontTypes_Converter.toProto(textHeaderFooter.getFont()));
        req.setFontSize(textHeaderFooter.getFontSize());

        req.setIsHeader(isHeader);
        if (pageIndexesToAddFootersTo != null) {
            req.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }
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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) {

        AddHtmlHeaderFooterRequestStream.Info info = setAddHtmlHeaderFooterRequest(internalPdfDocument, header, firstPageNumber,
                pageIndexesToAddFootersTo, marginLeftMm, marginRightMm, marginTopMm, marginBottomMm,
                renderPdfCssMediaType, pdfTitle, htmlTitle, true);

        addHtmlHeaderFooter_Internal(info, header.getHtmlFragment());
    }

    private static void addHtmlHeaderFooter_Internal(AddHtmlHeaderFooterRequestStream.Info info,
                                                     String html) {

        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<EmptyResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<AddHtmlHeaderFooterRequestStream> requestStream =
                client.stub.pdfDocumentHeaderFooterAddHtmlHeaderFooter(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        AddHtmlHeaderFooterRequestStream.Builder infoMsg =
                AddHtmlHeaderFooterRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<char[]> it = Utils_Util.chunk(html.toCharArray()); it.hasNext(); ) {
            char[] chars = it.next();
            AddHtmlHeaderFooterRequestStream.Builder msg = AddHtmlHeaderFooterRequestStream.newBuilder();
            msg.setHtmlChunk(String.valueOf(chars));
            requestStream.onNext(msg.build());
        }
        requestStream.onCompleted();

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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm) {
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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
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
                                     Iterable<Integer> pageIndexesToAddFootersTo) {
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) {
        RpcClient client = Access.ensureConnection();

        AddHtmlHeaderFooterRequestStream.Info info = setAddHtmlHeaderFooterRequest(internalPdfDocument, footer, firstPageNumber,
                pageIndexesToAddFootersTo, marginLeftMm, marginRightMm, marginTopMm, marginBottomMm,
                renderPdfCssMediaType, pdfTitle, htmlTitle, false);
        addHtmlHeaderFooter_Internal(info, footer.getHtmlFragment());
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
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
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) {
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

    private static AddHtmlHeaderFooterRequestStream.Info setAddHtmlHeaderFooterRequest(InternalPdfDocument internalPdfDocument, HtmlHeaderFooter htmlHeaderFooter, int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                                                                       int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType, String pdfTitle, String htmlTitle, boolean isHeader) {
        AddHtmlHeaderFooterRequestStream.Info.Builder info = AddHtmlHeaderFooterRequestStream.Info.newBuilder();
        info.setDocument(internalPdfDocument.remoteDocument);
        info.setIsHeader(false);
        info.setBaseUrl(htmlHeaderFooter.getBaseUrl() != null ? htmlHeaderFooter.getBaseUrl() : "");
        info.setFirstPageNumber(firstPageNumber);
        info.setMarginLeftMm(marginLeftMm);
        info.setMarginRightMm(marginRightMm);
        info.setMarginTopMm(marginTopMm);
        info.setMarginBottomMm(marginBottomMm);
        info.setCssMediaType(Render_Converter.toProto(renderPdfCssMediaType));
        if (pdfTitle != null)
            info.setPdfTitle(pdfTitle);
        if (htmlTitle != null)
            info.setHtmlTitle(htmlTitle);

        if(htmlHeaderFooter.getDividerLineColor() != null)
            info.setDividerLineColor(htmlHeaderFooter.getDividerLineColor());

        info.setDrawDividerLine(htmlHeaderFooter.isDrawDividerLine());

        if (pageIndexesToAddFootersTo != null) {
            info.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }

        if (htmlHeaderFooter.getMaxHeight() != null) {
            info.setMaxHeight(htmlHeaderFooter.getMaxHeight());
        }
        info.setIsHeader(isHeader);
        if (pageIndexesToAddFootersTo != null) {
            info.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }
        return info.build();
    }

}
