package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.Page;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;

final class Page_Converter {

    static com.ironsoftware.ironpdf.internal.proto.PageRotation toProto(PageRotation input) {
        com.ironsoftware.ironpdf.internal.proto.PageRotation.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PageRotation.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static PageInfo fromProto(Page input) {
        return new PageInfo(input.getWidth(), input.getHeight(), input.getPrintWidth(),
                input.getPrintHeight(),
                fromProto(input.getPageRotation()));
    }

    static PageRotation fromProto(com.ironsoftware.ironpdf.internal.proto.PageRotation input) {
        return PageRotation.values()[input.getEnumValue()];
    }
}
