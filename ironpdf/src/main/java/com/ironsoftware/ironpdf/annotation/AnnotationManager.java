package com.ironsoftware.ironpdf.annotation;

import com.ironsoftware.ironpdf.internal.staticapi.Annotation_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;

/**
 * Class used to add annotations to a {@link com.ironsoftware.ironpdf.PdfDocument}.
 * <p> See: {@link com.ironsoftware.ironpdf.PdfDocument#getAnnotation()} </p>
 */
public class AnnotationManager {

    private final InternalPdfDocument internalPdfDocument;

    /**
     * Please get AnnotationManager by {@link com.ironsoftware.ironpdf.PdfDocument#getAnnotation()} instead.
     *
     * @param internalPdfDocument the internal pdf document
     */
    public AnnotationManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Adds an annotation to a page of this {@link InternalPdfDocument}
     *
     * @param options The annotation options as a {@link AnnotationOptions} object.
     * @param pageIndex Index of the page to add the annotation. The first page has a PageIndex of 0
     */
    public void addTextAnnotation(AnnotationOptions options, int pageIndex) {

        TextAnnotation textAnnotation1 = new TextAnnotation();
        textAnnotation1.setColorCode(options.getRgbColor());
        textAnnotation1.setContents(options.getContents());
        textAnnotation1.setHidden(options.isHidden());
        textAnnotation1.setOpacity(options.getOpacity());
        textAnnotation1.setOpenByDefault(options.isOpen());
        textAnnotation1.setPrintable(options.isPrintable());
        textAnnotation1.setReadOnly(options.isReadonly());
        textAnnotation1.setRotatable(options.isRotateable());
        textAnnotation1.setSubject(options.getSubject());
        textAnnotation1.setTitle(options.getTitle());
        textAnnotation1.setIcon(options.getIcon());

        Annotation_Api.addTextAnnotation(internalPdfDocument,
                textAnnotation1, pageIndex, options.getX(), options.getY(), options.getWidth(),
                options.getHeight());
    }

    /**
     * Retrieves the number of annotations contained on the specified page
     *
     * @param pageIndex Index of the page to get the annotation. The first page has a PageIndex of 0
     * @return Number of annotations contained on the page
     */
    public int getAnnotationCount(int pageIndex) {
        return Annotation_Api.getAnnotationCount(internalPdfDocument,
                pageIndex);
    }


}
