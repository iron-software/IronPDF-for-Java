package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.annotation.AnnotationIcon;
import com.ironsoftware.ironpdf.annotation.TextAnnotation;

final class Annotation_Converter {

    static com.ironsoftware.ironpdf.internal.proto.TextAnnotation toProto(
            TextAnnotation iron) {
        com.ironsoftware.ironpdf.internal.proto.TextAnnotation.Builder proto =
                com.ironsoftware.ironpdf.internal.proto.TextAnnotation.newBuilder();
        proto.setContents(iron.getContents());
        proto.setHidden(iron.isHidden());
        proto.setOpacity(iron.getOpacity());
        proto.setOpenByDefault(iron.isOpenByDefault());
        proto.setPrintable(iron.isPrintable());
        proto.setReadOnly(iron.isReadOnly());
        proto.setRotateable(iron.isRotateable());
        proto.setSubject(iron.getSubject());
        proto.setTitle(iron.getTitle());
        proto.setIcon(toProto(iron.getIcon()));

        if (!Utils_StringHelper.isNullOrWhiteSpace(iron.getColorCode())) {
            proto.setColorCode(iron.getColorCode());
        }

        return proto.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.AnnotationIcon toProto(AnnotationIcon input) {
        com.ironsoftware.ironpdf.internal.proto.AnnotationIcon.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.AnnotationIcon.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
