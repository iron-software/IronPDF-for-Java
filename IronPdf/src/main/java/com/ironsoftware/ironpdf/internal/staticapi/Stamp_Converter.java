package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.edit.Length;
import com.ironsoftware.ironpdf.edit.MeasurementUnit;
import com.ironsoftware.ironpdf.stamp.BarcodeEncoding;
import com.ironsoftware.ironpdf.stamp.HorizontalAlignment;
import com.ironsoftware.ironpdf.stamp.VerticalAlignment;

public final class Stamp_Converter {

    public static com.ironsoftware.ironpdf.internal.proto.ChromeBarcodeEncodingP toProto(
            BarcodeEncoding input) {
        com.ironsoftware.ironpdf.internal.proto.ChromeBarcodeEncodingP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.ChromeBarcodeEncodingP.newBuilder();
        tempVar.setEnumValue(input.getValue());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.VerticalAlignmentP toProto(
            VerticalAlignment input) {
        com.ironsoftware.ironpdf.internal.proto.VerticalAlignmentP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.VerticalAlignmentP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.HorizontalAlignmentP toProto(
            HorizontalAlignment input) {
        com.ironsoftware.ironpdf.internal.proto.HorizontalAlignmentP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.HorizontalAlignmentP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.LengthP toProto(Length input) {
        com.ironsoftware.ironpdf.internal.proto.LengthP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.LengthP.newBuilder();
        tempVar.setUnit(Stamp_Converter.toProto(input.getUnit()));
        tempVar.setValue(input.getValue());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.MeasurementUnitP toProto(
            MeasurementUnit input) {
        com.ironsoftware.ironpdf.internal.proto.MeasurementUnitP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.MeasurementUnitP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
