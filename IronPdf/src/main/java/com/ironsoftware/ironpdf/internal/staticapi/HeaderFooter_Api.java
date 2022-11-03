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

public final class HeaderFooter_Api {

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) {
        addTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) {
        RpcClient client = Access.ensureConnection();

        AddTextHeaderFooterRequest req = setAddTextHeaderFooterRequest(pdfDocument, header,
                firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, pdfTitle, true);

        EmptyResult emptyResult = client.blockingStub.pdfDocumentHeaderFooterAddTextHeaderFooter(
                req);
        Utils_Util.handleEmptyResult(emptyResult);
    }

    private static AddTextHeaderFooterRequest setAddTextHeaderFooterRequest(InternalPdfDocument pdfDocument, TextHeaderFooter textHeaderFooter, int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle, boolean isHeader) {
        AddTextHeaderFooterRequest.Builder req = AddTextHeaderFooterRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setFirstPageNumber(firstPageNumber);
        req.setMarginLeftMm(marginLeftMm);
        req.setMarginRightMm(marginRightMm);
        req.setMarginTopMm(marginTopMm);
        req.setMarginBottomMm(marginBottomMm);
        req.setPdfTitle(pdfTitle);

        req.setLeftText(
                textHeaderFooter.getLeftText() == null ? null : Utils_Util.nullGuard(textHeaderFooter.getLeftText()));
        req.setCenterText(
                textHeaderFooter.getCenterText() == null ? null : Utils_Util.nullGuard(textHeaderFooter.getCenterText()));
        req.setRightText(
                textHeaderFooter.getRightText() == null ? null : Utils_Util.nullGuard(textHeaderFooter.getRightText()));
        req.setDrawDividerLine(textHeaderFooter.isDrawDividerLine());
        req.setFont(FontTypes_Converter.toProto(textHeaderFooter.getFont()));
        req.setFontSize(textHeaderFooter.getFontSize());
        req.setSpacing(textHeaderFooter.getSpacing());

        req.setIsHeader(isHeader);
        if (pageIndexesToAddFootersTo != null) {
            req.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }
        return req.build();
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) {
        addTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) {
        addTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, 0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) {
        addTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header          A new instance of {@link TextHeaderFooter} that defines the header
     *                        content and layout.
     * @param firstPageNumber Optional. The number of first page.
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber) {
        addTextHeader(pdfDocument, header, firstPageNumber, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header A new instance of {@link TextHeaderFooter} that defines the header content and
     *               layout.
     */
    public static void addTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header) {
        addTextHeader(pdfDocument, header, 1, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) {
        addTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) {
        RpcClient client = Access.ensureConnection();

        AddTextHeaderFooterRequest req = setAddTextHeaderFooterRequest(pdfDocument, footer,
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
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) {
        addTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) {
        addTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer                    A new instance of {@link TextHeaderFooter} that defines the
     *                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) {
        addTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer          A new instance of {@link TextHeaderFooter} that defines the header
     *                        content and layout.
     * @param firstPageNumber Optional. The number of first page.
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber) {
        addTextFooter(pdfDocument, footer, firstPageNumber, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer A new instance of {@link TextHeaderFooter} that defines the header content and
     *               layout.
     */
    public static void addTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer) {
        addTextFooter(pdfDocument, footer, 1, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the
     *                                  *                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                marginTopMm, marginBottomMm, renderPdfCssMediaType, pdfTitle, "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the
     *                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     * @param htmlTitle                 Optional htmlTitle
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) {

        AddHtmlHeaderFooterRequestStream.Info.Builder info = AddHtmlHeaderFooterRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);
        info.setIsHeader(true);
        info.setBaseUrl(header.getBaseUrl() != null ? header.getBaseUrl() : "");
        info.setFirstPageNumber(firstPageNumber);
        info.setMarginLeftMm(marginLeftMm);
        info.setMarginRightMm(marginRightMm);
        info.setMarginTopMm(marginTopMm);
        info.setMarginBottomMm(marginBottomMm);
        info.setCssMediaType(Render_Converter.toProto(renderPdfCssMediaType));
        info.setPdfTitle(pdfTitle);
        info.setHtmlTitle(htmlTitle);
        info.setDrawDividerLine(header.isDrawDividerLine());

        if (pageIndexesToAddFootersTo != null) {
            info.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }

        if (header.getMaxHeight() != null) {
            info.setMaxHeight(header.getMaxHeight());
        }

        addHtmlHeaderFooter_Internal(info.build(), header.getHtmlFragment());
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
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the
     *                                  CSS3 Media Queries standard.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm,
                                     CssMediaType renderPdfCssMediaType) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, renderPdfCssMediaType, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param marginBottomMm            The bottom margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                marginTopMm, marginBottomMm, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param marginTopMm               The top margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                marginTopMm, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param marginRightMm             The right margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param marginLeftMm              The left margin of the header on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header                    A new instance of IronPdf.HtmlHeaderFooter that defines the
     *                                  header content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the header will be added.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header          A new instance of IronPdf.HtmlHeaderFooter that defines the header
     *                        content and layout.
     * @param firstPageNumber Optional. The number of first page.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber) {
        addHtmlHeader(pdfDocument, header, firstPageNumber, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header A new instance of IronPdf.HtmlHeaderFooter that defines the header content and
     *               layout.
     */
    public static void addHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header) {
        addHtmlHeader(pdfDocument, header, 1, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the
     *                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, renderPdfCssMediaType, pdfTitle, "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the
     *                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     * @param htmlTitle                 Optional htmlTitle
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) {
        RpcClient client = Access.ensureConnection();

        AddHtmlHeaderFooterRequestStream.Info.Builder info = AddHtmlHeaderFooterRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);
        info.setIsHeader(false);
        info.setBaseUrl(footer.getBaseUrl() != null ? footer.getBaseUrl() : "");
        info.setFirstPageNumber(firstPageNumber);
        info.setMarginLeftMm(marginLeftMm);
        info.setMarginRightMm(marginRightMm);
        info.setMarginTopMm(marginTopMm);
        info.setMarginBottomMm(marginBottomMm);
        info.setCssMediaType(Render_Converter.toProto(renderPdfCssMediaType));
        info.setPdfTitle(pdfTitle);
        info.setHtmlTitle(htmlTitle);
        info.setDrawDividerLine(footer.isDrawDividerLine());

        if (pageIndexesToAddFootersTo != null) {
            info.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }

        if (footer.getMaxHeight() != null) {
            info.setMaxHeight(footer.getMaxHeight());
        }

        addHtmlHeaderFooter_Internal(info.build(), footer.getHtmlFragment());
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     * @param renderPdfCssMediaType     The style-sheet. 'Print' or 'Screen'. This matches the
     *                                  CSS3 Media Queries standard.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, renderPdfCssMediaType, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param marginBottomMm            The bottom margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, marginBottomMm, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param marginTopMm               The top margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, marginTopMm, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param marginRightMm             The right margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
                marginRightMm, 0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param marginLeftMm              The left margin of the footer on the page in mm.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer                    A new instance of IronPdf.HtmlFooterFooter that defines the
     *                                  footer content and layout.
     * @param firstPageNumber           Optional. The number of first page.
     * @param pageIndexesToAddFootersTo Optional# The PageIndexes (zero-based page numbers) to which
     *                                  the footer will be added.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0,
                CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer          A new instance of IronPdf.HtmlFooterFooter that defines the footer
     *                        content and layout.
     * @param firstPageNumber Optional. The number of first page.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber) {
        addHtmlFooter(pdfDocument, footer, firstPageNumber, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer A new instance of IronPdf.HtmlFooterFooter that defines the footer content and
     *               layout.
     */
    public static void addHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer) {
        addHtmlFooter(pdfDocument, footer, 1, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }
}
