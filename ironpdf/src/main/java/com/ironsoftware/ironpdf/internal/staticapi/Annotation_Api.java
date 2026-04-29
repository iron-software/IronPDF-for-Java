package com.ironsoftware.ironpdf.internal.staticapi;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
 
import com.ironsoftware.ironpdf.annotation.LinkAnnotation;
import com.ironsoftware.ironpdf.annotation.TextAnnotation;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.IntResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumAddLinkAnnotationRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumAddTextAnnotationRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetAnnotationCountRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetAnnotationRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetAnnotationsRequestP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetAnnotationsResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumGetTextAnnotationResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumPdfAnnotationCollectionP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumTextAnnotationP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumWrappedPdfAnnotationP;

/**
 * The type Annotation api.
 */
public final class Annotation_Api {

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     * @param pageIndex           Index of the page to add the annotation. The first page has a PageIndex of 0
     * @param x                   The horizontal X position of the annotation on your page in pixels
     * @param y                   The vertical Y position of the annotation on your page in pixels. Measured from bottom upwards.
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y) {
        addTextAnnotation(internalPdfDocument, textAnnotation, pageIndex, new java.awt.Rectangle(x, y, 30, 30));
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y, int width) {
        addTextAnnotation(internalPdfDocument, textAnnotation, pageIndex, new java.awt.Rectangle(x, y, width, 30));
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     * @param pageIndex           Index of the page to add the annotation. The first page has a PageIndex of 0
     * @param x                   The horizontal X position of the annotation on your page in pixels
     * @param y                   The vertical Y position of the annotation on your page in pixels. Measured from bottom upwards.
     * @param width               The width of your annotation's icon and interactive area in pixels
     * @param height              The height of your annotation's icon and interactive area in pixels
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y, int width, int height) {
        addTextAnnotation(internalPdfDocument, textAnnotation, pageIndex, new java.awt.Rectangle(x, y, width, height));
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     * @param pageIndex           Index of the page to add the annotation. The first page has a PageIndex of 0
     * @param rectangle           The rectangle defines the X and Y coordinates and the dimensions of the annotation on your page in pixels.
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, java.awt.Rectangle rectangle) {
        textAnnotation.setPageIndex(pageIndex);
        textAnnotation.setRectangle(rectangle);
        addTextAnnotation(internalPdfDocument, textAnnotation);
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation) {
        RpcClient client = Access.ensureConnection();

        PdfiumTextAnnotationP annotation = Annotation_Converter.toProto(textAnnotation);

        PdfiumAddTextAnnotationRequestP.Builder req = PdfiumAddTextAnnotationRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setAnnotation(annotation);
        IntResultP res = client.GetBlockingStub("addTextAnnotation").pdfiumAnnotationAddTextAnnotation(
                req.build());

        if (res.getResultOrExceptionCase() == IntResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }
    }

    /**
     * Retrieve the number of annotations contained on the specified page
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndex           Page index
     * @return Number of annotations contained on the page
     */
    public static int getAnnotationCount(InternalPdfDocument internalPdfDocument, int pageIndex) {
        RpcClient client = Access.ensureConnection();

        PdfiumGetAnnotationCountRequestP.Builder req = PdfiumGetAnnotationCountRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPageIndex(pageIndex);

        IntResultP res = client.GetBlockingStub("getAnnotationCount").pdfiumAnnotationGetAnnotationCountRequestP(
                req.build());

        if (res.getResultOrExceptionCase() == IntResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        return res.getResult();
    }

    public static List<TextAnnotation> getAnnotaionList(InternalPdfDocument internalPdfDocument) {

        RpcClient client = Access.ensureConnection();

        List<TextAnnotation> annotationList = new ArrayList<>();

        for (int pageIndex = 0; pageIndex < Page_Api.getPagesInfo(internalPdfDocument).size(); pageIndex++) {
            int annotationCount = getAnnotationCount(internalPdfDocument, pageIndex);

            for (int annotationIndex = 0; annotationIndex < annotationCount; annotationIndex++) {
                PdfiumGetAnnotationRequestP.Builder req = PdfiumGetAnnotationRequestP.newBuilder();
                req.setDocument(internalPdfDocument.remoteDocument);
                req.setPageIndex(pageIndex);
                req.setAnnotIndex(annotationIndex);

                PdfiumGetTextAnnotationResultP res = client.GetBlockingStub("getAnnotaionList").pdfiumAnnotationGetTextAnnotationRequestP(req.build());

                if (res.getResultOrExceptionCase() == PdfiumGetTextAnnotationResultP.ResultOrExceptionCase.EXCEPTION) {
                    throw Exception_Converter.fromProto(res.getException());
                }

                annotationList.add(Annotation_Converter.fromProto(res.getResult()));
            }
        }
        return annotationList;
    }

    /**
     * Retrieve all heterogeneous annotations (text, free-text, link, ...) on a single page
     * via the {@code Pdfium_Annotation_GetAnnotations} RPC.
     *
     * <p>Returned wrapped annotations may carry any of the proto sub-types
     * ({@code text}, {@code freetext}, {@code link}). Callers can inspect the oneof
     * via {@link PdfiumWrappedPdfAnnotationP#getAnnotationsCase()} to dispatch.</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndex           zero-based page index
     * @return all annotations on the given page (never null; may be empty)
     */
    public static List<PdfiumWrappedPdfAnnotationP> getAnnotations(InternalPdfDocument internalPdfDocument, int pageIndex) {
        RpcClient client = Access.ensureConnection();
 
        PdfiumGetAnnotationsRequestP.Builder req = PdfiumGetAnnotationsRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPageIndex(pageIndex);
 
        PdfiumGetAnnotationsResultP res = client.GetBlockingStub("getAnnotations")
                .pdfiumAnnotationGetAnnotationsRequestP(req.build());
 
        if (res.getResultOrExceptionCase() == PdfiumGetAnnotationsResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }
 
        PdfiumPdfAnnotationCollectionP collection = res.getResult();
        return new ArrayList<>(collection.getAnnotationsList());
    }
 
    /**
     * URL prefix used to encode an internal hyperlink (goto-page) target inside the
     * existing {@code Pdfium_Annotation_AddLinkAnnotation} RPC. Must match the
     * {@code InternalLinkPrefix} constant in the C# {@code IronPdfServiceHandler}.
     */
    private static final String INTERNAL_LINK_PREFIX = "x-ironpdf-goto-page:";
 
    /**
     * Adds an internal hyperlink annotation to the document. The link navigates to a
     * destination page within the same PDF when clicked.
     *
     * <p>Reuses the existing {@code Pdfium_Annotation_AddLinkAnnotation} RPC by encoding
     * the destination parameters into a special URL format:
     * {@code ironpdf-goto-page:{destPage},{destType},{left},{right},{top},{bottom},{zoom}}.
     * The engine detects this prefix and routes the request to its internal link handler.</p>
     *
     * <p>Mirrors {@code IronPdf.Engines.Pdfium.GrpcPdfClient.AddInternalLinkAnnotation}
     * on the C# side.</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param link                the link annotation to add
     */
    public static void addLinkAnnotation(InternalPdfDocument internalPdfDocument, LinkAnnotation link) {
        if (link == null) {
            throw new IllegalArgumentException("link annotation cannot be null");
        }
        if (link.getPageIndex() < 0) {
            throw new IllegalArgumentException("Invalid page index when adding link annotation");
        }
        if (link.getDestinationPageIndex() < 0) {
            throw new IllegalArgumentException("Invalid destination page index when adding link annotation");
        }
 
        RpcClient client = Access.ensureConnection();
 
        int destTypeOrdinal = link.getDestinationType() == null
                ? 0
                : link.getDestinationType().ordinal();
        String encodedUrl = INTERNAL_LINK_PREFIX
                + link.getDestinationPageIndex() + ","
                + destTypeOrdinal + ","
                + link.getDestinationLeft() + ","
                + link.getDestinationRight() + ","
                + link.getDestinationTop() + ","
                + link.getDestinationBottom() + ","
                + link.getDestinationZoom() + ","
                + (link.isShowBorder() ? 1 : 0);
 
        Rectangle rect = link.getRectangle();
        if (rect == null) {
            // Match C# default: (0, 0, 100, 20).
            rect = new Rectangle(0, 0, 100, 20);
        }
 
        PdfiumAddLinkAnnotationRequestP.Builder req = PdfiumAddLinkAnnotationRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setName(link.getTitle() == null ? "" : link.getTitle());
        req.setUrl(encodedUrl);
        req.setPageIndex(link.getPageIndex());
        req.setRectangle(com.ironsoftware.ironpdf.internal.proto.Rectangle.newBuilder()
                .setX(rect.x)
                .setY(rect.y)
                .setWidth(rect.width)
                .setHeight(rect.height)
                .build());
        req.setColorCode(link.getColorCode() != null && !link.getColorCode().isEmpty()
                ? link.getColorCode()
                : "#000000");
        req.setHidden(link.isHidden());
 
        EmptyResultP res = client.GetBlockingStub("addLinkAnnotation")
                .pdfiumAnnotationAddLinkAnnotation(req.build());
 
        Utils_Util.handleEmptyResult(res);
 
        // After a successful add, the new annotation index is the old count - 1.
        int newCount = getAnnotationCount(internalPdfDocument, link.getPageIndex());
        link.setAnnotationIndex(newCount - 1);
    }
}
