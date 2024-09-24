package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.annotation.AnnotationManager;
import com.ironsoftware.ironpdf.annotation.TextAnnotation;
import com.ironsoftware.ironpdf.internal.proto.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Annotation api.
 */
public final class Annotation_Api {

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param internalPdfDocument the internal pdf document
     * @param textAnnotation      The annotation as a {@link TextAnnotation} object.
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
        textAnnotation.setPageIndex(pageIndex);
        textAnnotation.setX(x);
        textAnnotation.setY(y);
        textAnnotation.setWidth(width);
        textAnnotation.setHeight(height);
        addTextAnnotation(internalPdfDocument, textAnnotation);
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
}
