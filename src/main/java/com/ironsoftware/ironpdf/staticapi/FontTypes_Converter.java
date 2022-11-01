package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.internal.proto.FontType;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.NullGuard;

final class FontTypes_Converter {

    static FontType ToProto(FontTypes Iron) {
        FontType.Builder proto = FontType.newBuilder();
        proto.setId(Iron.getId());
        proto.setName(NullGuard(Iron.getName()));

        if (!Utils_StringHelper.isNullOrWhiteSpace(Iron.getCustomFontFilePath())) {
            proto.setFontFilePath(Iron.getCustomFontFilePath());
        }

        return proto.build();
    }
}
