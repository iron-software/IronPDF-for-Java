package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.edit.Length;
import com.ironsoftware.ironpdf.edit.MeasurementUnit;
import com.ironsoftware.ironpdf.stamp.BarcodeEncoding;
import com.ironsoftware.ironpdf.stamp.HorizontalAlignment;
import com.ironsoftware.ironpdf.stamp.VerticalAlignment;

public final class Stamp_Converter {

    public static com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding enumToProto(
            BarcodeEncoding input) {
        com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding toProto(
            BarcodeEncoding input) {
        com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.VerticalAlignment toProto(
            VerticalAlignment input) {
        com.ironsoftware.ironpdf.internal.proto.VerticalAlignment.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.VerticalAlignment.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.HorizontalAlignment toProto(
            HorizontalAlignment input) {
        com.ironsoftware.ironpdf.internal.proto.HorizontalAlignment.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.HorizontalAlignment.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.Length toProto(Length input) {
        com.ironsoftware.ironpdf.internal.proto.Length.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.Length.newBuilder();
        tempVar.setUnit(Stamp_Converter.toProto(input.getUnit()));
        tempVar.setValue(input.getValue());
        return tempVar.build();
    }

    public static com.ironsoftware.ironpdf.internal.proto.MeasurementUnit toProto(
            MeasurementUnit input) {
        com.ironsoftware.ironpdf.internal.proto.MeasurementUnit.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.MeasurementUnit.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
