package com.ironsoftware.ironpdf.internal.staticapi;

import java.awt.Rectangle;

import com.ironsoftware.ironpdf.annotation.AnnotationIcon;
import com.ironsoftware.ironpdf.annotation.TextAnnotation;
import com.ironsoftware.ironpdf.internal.proto.PdfiumTextAnnotationP;

final class Annotation_Converter {

    static com.ironsoftware.ironpdf.internal.proto.PdfiumTextAnnotationP toProto(
            TextAnnotation iron) {
        PdfiumTextAnnotationP.Builder proto = PdfiumTextAnnotationP.newBuilder();
        proto.setContents(iron.getContents());
        proto.setHidden(iron.isHidden());
        proto.setOpacity(iron.getOpacity());
        proto.setOpenByDefault(iron.isOpenByDefault());
        proto.setPrintable(iron.isPrintable());
        proto.setReadOnly(iron.isReadOnly());
        proto.setRotatable(iron.isRotatable());
        proto.setSubject(iron.getSubject());
        proto.setTitle(iron.getTitle());
        proto.setIcon(toProto(iron.getIcon()));
        com.ironsoftware.ironpdf.internal.proto.Rectangle.Builder rectangleProto = com.ironsoftware.ironpdf.internal.proto.Rectangle.newBuilder();
        rectangleProto.setX(iron.getRectangle().getX());
        rectangleProto.setY(iron.getRectangle().getY());
        rectangleProto.setHeight(iron.getRectangle().getWidth());
        rectangleProto.setWidth(iron.getRectangle().getHeight());
        proto.setRectangle(rectangleProto);
        proto.setPageIndex(iron.getPageIndex());
        proto.setAnnotIndex(iron.getAnnotationIndex());

        if (!Utils_StringHelper.isNullOrWhiteSpace(iron.getColorCode())) {
            proto.setColorCode(iron.getColorCode());
        }

        return proto.build();
    }

    static TextAnnotation fromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfiumTextAnnotationP proto) {
        TextAnnotation textAnnotation = new TextAnnotation();
        textAnnotation.setContents(proto.getContents());
        textAnnotation.setHidden(proto.getHidden());
        textAnnotation.setOpacity(proto.getOpacity());
        textAnnotation.setOpenByDefault(proto.getOpenByDefault());
        textAnnotation.setPrintable(proto.getPrintable());
        textAnnotation.setReadOnly(proto.getReadOnly());
        textAnnotation.setRotatable(proto.getRotatable());
        textAnnotation.setSubject(proto.getSubject());
        textAnnotation.setTitle(proto.getTitle());
        textAnnotation.setIcon(Annotation_Converter.fromProto(proto.getIcon()));
        Rectangle rectangle = new Rectangle(
            (int)proto.getRectangle().getX(),
            (int)proto.getRectangle().getY(),
            (int)proto.getRectangle().getWidth(),
            (int)proto.getRectangle().getHeight());
        textAnnotation.setRectangle(rectangle);
        textAnnotation.setPageIndex(proto.getPageIndex());
        textAnnotation.setAnnotationIndex(proto.getAnnotIndex());
        textAnnotation.setColorCode(proto.getColorCode());

        return textAnnotation;
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfiumAnnotationIconP toProto(AnnotationIcon input) {
        com.ironsoftware.ironpdf.internal.proto.PdfiumAnnotationIconP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfiumAnnotationIconP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static AnnotationIcon fromProto(com.ironsoftware.ironpdf.internal.proto.PdfiumAnnotationIconP input) {
        return AnnotationIcon.values()[input.getEnumValue()];
    }
}
