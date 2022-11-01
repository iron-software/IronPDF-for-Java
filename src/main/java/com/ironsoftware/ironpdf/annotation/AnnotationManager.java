package com.ironsoftware.ironpdf.annotation;

import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;

import java.io.IOException;

public class AnnotationManager {

    private final InternalPdfDocument internalPdfDocument;

    public AnnotationManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param options The annotation options as a {@link AnnotationOptions} object.
     */
    public void addTextAnnotation(AnnotationOptions options) throws IOException {

        TextAnnotation textAnnotation1 = new TextAnnotation();
        textAnnotation1.setColorCode(options.getRgbColor());
        textAnnotation1.setContents(options.getContents());
        textAnnotation1.setHidden(options.getHidden());
        textAnnotation1.setOpacity(options.getOpacity());
        textAnnotation1.setOpenByDefault(options.getOpen());
        textAnnotation1.setPrintable(options.getPrintable());
        textAnnotation1.setReadOnly(options.getReadonly());
        textAnnotation1.setRotateable(options.getRotateable());
        textAnnotation1.setSubject(options.getSubject());
        textAnnotation1.setTitle(options.getTitle());
        textAnnotation1.setIcon(options.getIcon());

        com.ironsoftware.ironpdf.staticapi.Annotation_Api.AddTextAnnotation(internalPdfDocument,
                textAnnotation1, options.getPageIndex(), options.getX(), options.getY(), options.getWidth(),
                options.getHeight());
    }


    /**
     * Retrieve the number of annotations contained on the specified page
     *
     * @param pageIndex Page index
     * @return Number of annotations contained on the page
     */
    public int getAnnotationCount(int pageIndex) throws IOException {
        return com.ironsoftware.ironpdf.staticapi.Annotation_Api.GetAnnotationCount(internalPdfDocument,
                pageIndex);
    }
}
