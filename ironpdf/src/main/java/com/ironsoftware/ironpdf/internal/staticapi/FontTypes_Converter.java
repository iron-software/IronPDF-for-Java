package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.internal.proto.PdfiumFontInfoP;

final class FontTypes_Converter {

    static PdfiumFontInfoP toProto(FontTypes Iron) {
        PdfiumFontInfoP.Builder proto = PdfiumFontInfoP.newBuilder();
        proto.setObjNum(Iron.getId());
        proto.setName(Utils_Util.nullGuard(Iron.getName()));

        return proto.build();
    }
}
