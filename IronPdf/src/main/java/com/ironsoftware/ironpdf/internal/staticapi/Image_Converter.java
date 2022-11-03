package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.image.ImageBehavior;

final class Image_Converter {

    static com.ironsoftware.ironpdf.internal.proto.ImageBehavior toProto(ImageBehavior input) {
        com.ironsoftware.ironpdf.internal.proto.ImageBehavior.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.ImageBehavior.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
