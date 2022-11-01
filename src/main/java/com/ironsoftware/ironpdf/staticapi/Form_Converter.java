package com.ironsoftware.ironpdf.staticapi;


import com.ironsoftware.ironpdf.form.*;
import com.ironsoftware.ironpdf.internal.proto.PdfForm;

import java.util.List;
import java.util.stream.Collectors;

final class Form_Converter {

    static List<FormField> FromProto(PdfForm proto) {
        return proto.getFormFieldsList().stream().map(x ->
        {
            switch (x.getSubTypeCase()) {
                case COMBO_BOX_FIELD:
                    return FromProtoToComboBoxField(x);
                case CHECK_BOX_FIELD:
                    return FromProtoToCheckBoxField(x);
                case TEXT_FIELD:
                    return FromProtoToTextField(x);
                case SUBTYPE_NOT_SET:
                case UNKNOWN_FIELD:
                    return FromProtoToFormField(x);
                default:
                    throw new IndexOutOfBoundsException();
            }
        }).collect(Collectors.toList());
    }

    static ComboBoxField FromProtoToComboBoxField(
            com.ironsoftware.ironpdf.internal.proto.FormField proto) {
        return new ComboBoxField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                proto.getX(), proto.getY(), proto.getWidth(), proto.getHeight(),
                proto.getValue(), proto.getComboBoxField().getOptionsList().iterator(),
                proto.getComboBoxField().getSelectedIndex(), proto.getReadOnly());
    }

    static CheckBoxField FromProtoToCheckBoxField(
            com.ironsoftware.ironpdf.internal.proto.FormField proto) {
        return new CheckBoxField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                proto.getX(), proto.getY(), proto.getWidth(), proto.getHeight(),
                proto.getValue(), proto.getCheckBoxField().getBooleanValue(), proto.getReadOnly());
    }

    static TextField FromProtoToTextField(com.ironsoftware.ironpdf.internal.proto.FormField proto) {
        return new TextField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                proto.getX(), proto.getY(),
                proto.getWidth(), proto.getHeight(), proto.getValue(), proto.getReadOnly());
    }

    static FormField FromProtoToFormField(com.ironsoftware.ironpdf.internal.proto.FormField proto) {
        return new FormField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                FromProto(proto.getType()), proto.getX(), proto.getY(), proto.getWidth(), proto.getHeight(),
                proto.getValue(), proto.getReadOnly());
    }


    static FormFieldTypes FromProto(com.ironsoftware.ironpdf.internal.proto.FormFieldTypes input) {
        return FormFieldTypes.values()[input.getEnumValue()];
    }


    static com.ironsoftware.ironpdf.internal.proto.FormFieldTypes ToProto(FormFieldTypes input) {
        com.ironsoftware.ironpdf.internal.proto.FormFieldTypes.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.FormFieldTypes.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
