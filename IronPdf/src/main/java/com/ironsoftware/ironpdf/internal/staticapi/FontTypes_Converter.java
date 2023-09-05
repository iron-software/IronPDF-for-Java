package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.internal.proto.FontTypeP;

final class FontTypes_Converter {

    static FontTypeP toProto(FontTypes Iron) {
        FontTypeP.Builder proto = FontTypeP.newBuilder();
        proto.setId(Iron.getId());
        proto.setName(Utils_Util.nullGuard(Iron.getName()));

        if (!Utils_StringHelper.isNullOrWhiteSpace(Iron.getCustomFontFilePath())) {
            proto.setFontFilePath(Iron.getCustomFontFilePath());
        }

        return proto.build();
    }
}
