package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.annotation.AnnotationIcon;
import com.ironsoftware.ironpdf.annotation.TextAnnotation;

final class Annotation_Converter {

    static com.ironsoftware.ironpdf.internal.proto.TextAnnotation ToProto(
            TextAnnotation iron) {
        com.ironsoftware.ironpdf.internal.proto.TextAnnotation.Builder proto =
                com.ironsoftware.ironpdf.internal.proto.TextAnnotation.newBuilder();
        proto.setContents(iron.getContents());
        proto.setHidden(iron.getHidden());
        proto.setOpacity(iron.getOpacity());
        proto.setOpenByDefault(iron.getOpenByDefault());
        proto.setPrintable(iron.getPrintable());
        proto.setReadOnly(iron.getReadOnly());
        proto.setRotateable(iron.getRotateable());
        proto.setSubject(iron.getSubject());
        proto.setTitle(iron.getTitle());
        proto.setIcon(ToProto(iron.getIcon()));

        if (!Utils_StringHelper.isNullOrWhiteSpace(iron.getColorCode())) {
            proto.setColorCode(iron.getColorCode());
        }

        return proto.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.AnnotationIcon ToProto(AnnotationIcon input) {
        com.ironsoftware.ironpdf.internal.proto.AnnotationIcon.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.AnnotationIcon.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static AnnotationIcon FromProto(com.ironsoftware.ironpdf.internal.proto.AnnotationIcon input) {
        return AnnotationIcon.values()[input.getEnumValue()];
    }
}
