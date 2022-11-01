package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import com.ironsoftware.ironpdf.internal.proto.AddHtmlHeaderFooterRequestStream;
import com.ironsoftware.ironpdf.internal.proto.AddTextHeaderFooterRequest;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.render.CssMediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.*;

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
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) throws IOException {
        AddTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) throws IOException {
        RpcClient client = Access.EnsureConnection();

        AddTextHeaderFooterRequest.Builder req = AddTextHeaderFooterRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setFirstPageNumber(firstPageNumber);
        req.setMarginLeftMm(marginLeftMm);
        req.setMarginRightMm(marginRightMm);
        req.setMarginTopMm(marginTopMm);
        req.setMarginBottomMm(marginBottomMm);
        req.setPdfTitle(pdfTitle);

        req.setLeftText(
                header.getLeftText() == null ? null : Utils_Util.NullGuard(header.getLeftText()));
        req.setCenterText(
                header.getCenterText() == null ? null : Utils_Util.NullGuard(header.getCenterText()));
        req.setRightText(
                header.getRightText() == null ? null : Utils_Util.NullGuard(header.getRightText()));
        req.setDrawDividerLine(header.getDrawDividerLine());
        req.setFont(FontTypes_Converter.ToProto(header.getFont()));
        req.setFontSize(header.getFontSize());
        req.setSpacing(header.getSpacing());
        req.setIsHeader(true);

        if (pageIndexesToAddFootersTo != null) {
            req.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }

        EmptyResult emptyResult = client.BlockingStub.pdfDocumentHeaderFooterAddTextHeaderFooter(
                req.build());
        HandleEmptyResult(emptyResult);
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
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) throws IOException {
        AddTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) throws IOException {
        AddTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm)
            throws IOException {
        AddTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
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
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) throws IOException {
        AddTextHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0, "");
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
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header,
                                     int firstPageNumber) throws IOException {
        AddTextHeader(pdfDocument, header, firstPageNumber, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page headers to an existing PDF File <p>Margin spacing on the PDF page for the header
     * are set to default values of 25mm. An overload method allow header margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param header A new instance of {@link TextHeaderFooter} that defines the header content and
     *               layout.
     */
    public static void AddTextHeader(InternalPdfDocument pdfDocument, TextHeaderFooter header)
            throws IOException {
        AddTextHeader(pdfDocument, header, 1, null, 0, 0, 0, 0, "");
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
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) throws IOException {
        AddTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, String pdfTitle) throws IOException {
        RpcClient client = Access.EnsureConnection();

        AddTextHeaderFooterRequest.Builder req = AddTextHeaderFooterRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setFirstPageNumber(firstPageNumber);
        req.setMarginLeftMm(marginLeftMm);
        req.setMarginRightMm(marginRightMm);
        req.setMarginTopMm(marginTopMm);
        req.setMarginBottomMm(marginBottomMm);
        req.setPdfTitle(pdfTitle);

        req.setLeftText(
                footer.getLeftText() == null ? null : Utils_Util.NullGuard(footer.getLeftText()));
        req.setCenterText(
                footer.getCenterText() == null ? null : Utils_Util.NullGuard(footer.getCenterText()));
        req.setRightText(
                footer.getRightText() == null ? null : Utils_Util.NullGuard(footer.getRightText()));
        req.setDrawDividerLine(footer.getDrawDividerLine());
        req.setFont(FontTypes_Converter.ToProto(footer.getFont()));
        req.setFontSize(footer.getFontSize());
        req.setSpacing(footer.getSpacing());
        req.setIsHeader(false);

        if (pageIndexesToAddFootersTo != null) {
            req.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }

        EmptyResult emptyResult = client.BlockingStub.pdfDocumentHeaderFooterAddTextHeaderFooter(
                req.build());

        HandleEmptyResult(emptyResult);
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
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) throws IOException {
        AddTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) throws IOException {
        AddTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm)
            throws IOException {
        AddTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
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
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) throws IOException {
        AddTextFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0, "");
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
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer,
                                     int firstPageNumber) throws IOException {
        AddTextFooter(pdfDocument, footer, firstPageNumber, null, 0, 0, 0, 0, "");
    }

    /**
     * Renders page footers to an existing PDF File <p>Margin spacing on the PDF page for the footer
     * are set to default values of 25mm. An overload method allow footer margins to be chosen
     * specifically or set to zero.</p>
     *
     * @param footer A new instance of {@link TextHeaderFooter} that defines the header content and
     *               layout.
     */
    public static void AddTextFooter(InternalPdfDocument pdfDocument, TextHeaderFooter footer)
            throws IOException {
        AddTextFooter(pdfDocument, footer, 1, null, 0, 0, 0, 0, "");
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
     *      *                                  CSS3 Media Queries standard.
     * @param pdfTitle                  Optional pdfTitle
     */
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle) throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) throws IOException {

        AddHtmlHeaderFooterRequestStream.Info.Builder info = AddHtmlHeaderFooterRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);
        info.setIsHeader(true);
        info.setBaseUrl(header.getBaseUrl() != null ? header.getBaseUrl() : "");
        info.setFirstPageNumber(firstPageNumber);
        info.setMarginLeftMm(marginLeftMm);
        info.setMarginRightMm(marginRightMm);
        info.setMarginTopMm(marginTopMm);
        info.setMarginBottomMm(marginBottomMm);
        info.setCssMediaType(Render_Converter.ToProto(renderPdfCssMediaType));
        info.setPdfTitle(pdfTitle);
        info.setHtmlTitle(htmlTitle);
        info.setDrawDividerLine(header.getDrawDividerLine());

        if (pageIndexesToAddFootersTo != null) {
            info.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }

        if (header.getMaxHeight() != null) {
            info.setMaxHeight(header.getMaxHeight());
        }

        AddHtmlHeaderFooter_Internal(info.build(), header.getHtmlFragment());
    }

    private static void AddHtmlHeaderFooter_Internal(AddHtmlHeaderFooterRequestStream.Info info,
                                                     String html) throws IOException {

        RpcClient client = Access.EnsureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<EmptyResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<AddHtmlHeaderFooterRequestStream> requestStream =
                client.Stub.pdfDocumentHeaderFooterAddHtmlHeaderFooter(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        AddHtmlHeaderFooterRequestStream.Builder infoMsg =
                AddHtmlHeaderFooterRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<char[]> it = Chunk(html.toCharArray()); it.hasNext(); ) {
            char[] chars = it.next();
            AddHtmlHeaderFooterRequestStream.Builder msg = AddHtmlHeaderFooterRequestStream.newBuilder();
            msg.setHtmlChunk(String.valueOf(chars));
            requestStream.onNext(msg.build());
        }
        requestStream.onCompleted();

        WaitAndCheck(finishLatch, resultChunks);

        HandleEmptyResultChunks(resultChunks);
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
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm,
                                     CssMediaType renderPdfCssMediaType) throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm) throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm) throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm)
            throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm) throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
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
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo) throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, pageIndexesToAddFootersTo, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header          A new instance of IronPdf.HtmlHeaderFooter that defines the header
     *                        content and layout.
     * @param firstPageNumber Optional. The number of first page.
     */
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header,
                                     int firstPageNumber) throws IOException {
        AddHtmlHeader(pdfDocument, header, firstPageNumber, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header A new instance of IronPdf.HtmlHeaderFooter that defines the header content and
     *               layout.
     */
    public static void AddHtmlHeader(InternalPdfDocument pdfDocument, HtmlHeaderFooter header)
            throws IOException {
        AddHtmlHeader(pdfDocument, header, 1, null, 0, 0,
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle) throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber,
                                     Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm, int marginRightMm,
                                     int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType,
                                     String pdfTitle, String htmlTitle) throws IOException {
        RpcClient client = Access.EnsureConnection();

        AddHtmlHeaderFooterRequestStream.Info.Builder info = AddHtmlHeaderFooterRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);
        info.setIsHeader(false);
        info.setBaseUrl(footer.getBaseUrl() != null ? footer.getBaseUrl() : "");
        info.setFirstPageNumber(firstPageNumber);
        info.setMarginLeftMm(marginLeftMm);
        info.setMarginRightMm(marginRightMm);
        info.setMarginTopMm(marginTopMm);
        info.setMarginBottomMm(marginBottomMm);
        info.setCssMediaType(Render_Converter.ToProto(renderPdfCssMediaType));
        info.setPdfTitle(pdfTitle);
        info.setHtmlTitle(htmlTitle);
        info.setDrawDividerLine(footer.getDrawDividerLine());

        if (pageIndexesToAddFootersTo != null) {
            info.addAllTargetPageIndexes(pageIndexesToAddFootersTo);
        }

        if (footer.getMaxHeight() != null) {
            info.setMaxHeight(footer.getMaxHeight());
        }

        AddHtmlHeaderFooter_Internal(info.build(), footer.getHtmlFragment());
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm, CssMediaType renderPdfCssMediaType)
            throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm, int marginBottomMm) throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm, int marginTopMm) throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm,
                                     int marginRightMm) throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm,
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo, int marginLeftMm)
            throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, marginLeftMm, 0,
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
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber, Iterable<Integer> pageIndexesToAddFootersTo) throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, pageIndexesToAddFootersTo, 0, 0, 0, 0,
                CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer          A new instance of IronPdf.HtmlFooterFooter that defines the footer
     *                        content and layout.
     * @param firstPageNumber Optional. The number of first page.
     */
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer,
                                     int firstPageNumber) throws IOException {
        AddHtmlFooter(pdfDocument, footer, firstPageNumber, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer A new instance of IronPdf.HtmlFooterFooter that defines the footer content and
     *               layout.
     */
    public static void AddHtmlFooter(InternalPdfDocument pdfDocument, HtmlHeaderFooter footer)
            throws IOException {
        AddHtmlFooter(pdfDocument, footer, 1, null, 0, 0,
                0, 0, CssMediaType.PRINT, "", "");
    }
}
