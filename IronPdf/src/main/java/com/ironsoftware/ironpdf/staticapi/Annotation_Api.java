package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.annotation.TextAnnotation;
import com.ironsoftware.ironpdf.internal.proto.AddTextAnnotationRequest;
import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.internal.proto.GetAnnotationCountRequest;
import com.ironsoftware.ironpdf.internal.proto.IntResult;

import java.io.IOException;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;

public final class Annotation_Api {

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param textAnnotation The annotation as a {@link TextAnnotation} object.
     * @param pageIndex      Index of the page to add the annotation. The first page has a PageIndex
     *                       of 0
     * @param x              The horizontal X position of the annotation on your page in pixels
     * @param y              The vertical Y position of the annotation on your page in pixels.
     *                       Measured from bottom upwards.
     * @param width          The width of your annotation's icon and interactive area in pixels
     */
    public static void AddTextAnnotation(InternalPdfDocument pdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y, int width) throws IOException {
        AddTextAnnotation(pdfDocument, textAnnotation, pageIndex, x, y, width, 30);
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param textAnnotation The annotation as a {@link TextAnnotation} object.
     * @param pageIndex      Index of the page to add the annotation. The first page has a PageIndex
     *                       of 0
     * @param x              The horizontal X position of the annotation on your page in pixels
     * @param y              The vertical Y position of the annotation on your page in pixels.
     *                       Measured from bottom upwards.
     * @param width          The width of your annotation's icon and interactive area in pixels
     * @param height         The height of your annotation's icon and interactive area in pixels
     */
    public static void AddTextAnnotation(InternalPdfDocument pdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y, int width, int height) throws IOException {
        RpcClient client = Access.EnsureConnection();

        AddTextAnnotationRequest.Builder req = AddTextAnnotationRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setHeight(height);
        req.setPageIndex(pageIndex);
        req.setTextAnnotation(Annotation_Converter.ToProto(textAnnotation));
        req.setWidth(width);
        req.setX(x);
        req.setY(y);

        EmptyResult res = client.BlockingStub.pdfDocumentAnnotationAddTextAnnotation(
                req.build());

        HandleEmptyResult(res);
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param textAnnotation The annotation as a {@link TextAnnotation} object.
     * @param pageIndex      Index of the page to add the annotation. The first page has a PageIndex
     *                       of 0
     * @param x              The horizontal X position of the annotation on your page in pixels
     * @param y              The vertical Y position of the annotation on your page in pixels.
     *                       Measured from bottom upwards.
     */
    public static void AddTextAnnotation(InternalPdfDocument pdfDocument,
                                         TextAnnotation textAnnotation,
                                         int pageIndex, int x, int y) throws IOException {
        AddTextAnnotation(pdfDocument, textAnnotation, pageIndex, x, y, 30, 30);
    }

    /**
     * Retrieve the number of annotations contained on the specified page
     *
     * @param pageIndex Page index
     * @return Number of annotations contained on the page
     */
    public static int GetAnnotationCount(InternalPdfDocument pdfDocument, int pageIndex)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        GetAnnotationCountRequest.Builder req = GetAnnotationCountRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setPageIndex(pageIndex);

        IntResult res = client.BlockingStub.pdfDocumentAnnotationGetAnnotationCountRequest(
                req.build());

        if (res.getResultOrExceptionCase() == IntResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(res.getException());
        }

        return res.getResult();
    }
}
