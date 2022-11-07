package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.annotation.TextAnnotation;
import com.ironsoftware.ironpdf.internal.proto.AddTextAnnotationRequest;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.internal.proto.GetAnnotationCountRequest;
import com.ironsoftware.ironpdf.internal.proto.IntResult;

/**
 * The type Annotation api.
 */
public final class Annotation_Api {

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     * @param pageIndex           Index of the page to add the annotation. The first page has a PageIndex                       of 0
     * @param x                   The horizontal X position of the annotation on your page in pixels
     * @param y                   The vertical Y position of the annotation on your page in pixels.                       Measured from bottom upwards.
     * @param width               The width of your annotation's icon and interactive area in pixels
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y, int width) {
        addTextAnnotation(internalPdfDocument, textAnnotation, pageIndex, x, y, width, 30);
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     * @param pageIndex           Index of the page to add the annotation. The first page has a PageIndex                       of 0
     * @param x                   The horizontal X position of the annotation on your page in pixels
     * @param y                   The vertical Y position of the annotation on your page in pixels.                       Measured from bottom upwards.
     * @param width               The width of your annotation's icon and interactive area in pixels
     * @param height              The height of your annotation's icon and interactive area in pixels
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y, int width, int height) {
        RpcClient client = Access.ensureConnection();

        AddTextAnnotationRequest.Builder req = AddTextAnnotationRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setHeight(height);
        req.setPageIndex(pageIndex);
        req.setTextAnnotation(Annotation_Converter.toProto(textAnnotation));
        req.setWidth(width);
        req.setX(x);
        req.setY(y);

        EmptyResult res = client.blockingStub.pdfDocumentAnnotationAddTextAnnotation(
                req.build());

        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
     * @param pageIndex           Index of the page to add the annotation. The first page has a PageIndex                       of 0
     * @param x                   The horizontal X position of the annotation on your page in pixels
     * @param y                   The vertical Y position of the annotation on your page in pixels.                       Measured from bottom upwards.
     */
    public static void addTextAnnotation(InternalPdfDocument internalPdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y) {
        addTextAnnotation(internalPdfDocument, textAnnotation, pageIndex, x, y, 30, 30);
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

        GetAnnotationCountRequest.Builder req = GetAnnotationCountRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPageIndex(pageIndex);

        IntResult res = client.blockingStub.pdfDocumentAnnotationGetAnnotationCountRequest(
                req.build());

        if (res.getResultOrExceptionCase() == IntResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        return res.getResult();
    }
}
