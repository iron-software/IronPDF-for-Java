package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.image.ImageBehavior;

final class Image_Converter {

    static com.ironsoftware.ironpdf.internal.proto.ChromeImageBehaviorP toProto(ImageBehavior input) {
        com.ironsoftware.ironpdf.internal.proto.ChromeImageBehaviorP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.ChromeImageBehaviorP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
