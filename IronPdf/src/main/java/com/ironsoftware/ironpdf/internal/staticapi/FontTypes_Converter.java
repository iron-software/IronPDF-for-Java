package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.internal.proto.FontType;

final class FontTypes_Converter {

    static FontType toProto(FontTypes Iron) {
        FontType.Builder proto = FontType.newBuilder();
        proto.setId(Iron.getId());
        proto.setName(Utils_Util.nullGuard(Iron.getName()));

        if (!Utils_StringHelper.isNullOrWhiteSpace(Iron.getCustomFontFilePath())) {
            proto.setFontFilePath(Iron.getCustomFontFilePath());
        }

        return proto.build();
    }
}
