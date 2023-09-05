package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.PdfiumPageP;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;

final class Page_Converter {

    static com.ironsoftware.ironpdf.internal.proto.PdfiumPageRotationP toProto(PageRotation input) {
        com.ironsoftware.ironpdf.internal.proto.PdfiumPageRotationP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfiumPageRotationP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static PageInfo fromProto(PdfiumPageP input) {
        return new PageInfo(input.getPageIndex(), input.getWidth(), input.getHeight(), input.getPrintWidth(),
                input.getPrintHeight(),
                fromProto(input.getPageRotation()));
    }

    static PdfiumPageP toProto(PageInfo input){
        return PdfiumPageP.newBuilder()
                .setHeight(input.getHeight())
                .setWidth(input.getWidth())
                .setPageIndex(input.getPageIndex())
                .setPageRotation(toProto(input.getPageRotation()))
                .setPrintWidth(input.getPrintWidth())
                .setPrintHeight(input.getPrintHeight())
                .build();
    }

    static PageRotation fromProto(com.ironsoftware.ironpdf.internal.proto.PdfiumPageRotationP input) {
        return PageRotation.values()[input.getEnumValue()];
    }
}
