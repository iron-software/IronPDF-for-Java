package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.form.*;
import com.ironsoftware.ironpdf.internal.proto.PdfiumFormFieldP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumPdfFormP;

import java.util.List;
import java.util.stream.Collectors;

final class Form_Converter {

    static List<FormField> fromProto(PdfiumPdfFormP proto) {
        return proto.getFormFieldsList().stream().map(x ->
        {
            switch (x.getSubTypeCase()) {
                case COMBO_BOX_FIELD:
                    return fromProtoToComboBoxField(x);
                case CHECK_BOX_FIELD:
                    return fromProtoToCheckBoxField(x);
                case TEXT_FIELD:
                    return fromProtoToTextField(x);
                case SUBTYPE_NOT_SET:
                case UNKNOWN_FIELD:
                    return fromProtoToFormField(x);
                default:
                    throw new IndexOutOfBoundsException();
            }
        }).collect(Collectors.toList());
    }

    static ComboBoxField fromProtoToComboBoxField(
            com.ironsoftware.ironpdf.internal.proto.PdfiumFormFieldP proto) {
        return new ComboBoxField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                proto.getX(), proto.getY(), proto.getWidth(), proto.getHeight(),
                proto.getValue(), proto.getComboBoxField().getOptionsList(),
                proto.getComboBoxField().getSelectedIndex(), proto.getReadOnly());
    }

    static CheckBoxField fromProtoToCheckBoxField(
            com.ironsoftware.ironpdf.internal.proto.PdfiumFormFieldP proto) {
        return new CheckBoxField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                proto.getX(), proto.getY(), proto.getWidth(), proto.getHeight(),
                proto.getValue(), proto.getCheckBoxField().getBooleanValue(), proto.getReadOnly());
    }

    static TextField fromProtoToTextField(com.ironsoftware.ironpdf.internal.proto.PdfiumFormFieldP proto) {
        return new TextField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                proto.getX(), proto.getY(),
                proto.getWidth(), proto.getHeight(), proto.getValue(), proto.getReadOnly());
    }

    static FormField fromProtoToFormField(com.ironsoftware.ironpdf.internal.proto.PdfiumFormFieldP proto) {
        return new FormField(proto.getAnnotationIndex(), proto.getName(), proto.getPageIndex(),
                fromProto(proto.getType()), proto.getX(), proto.getY(), proto.getWidth(), proto.getHeight(),
                proto.getValue(), proto.getReadOnly());
    }

    static FormFieldTypes fromProto(com.ironsoftware.ironpdf.internal.proto.PdfiumFormFieldTypesP input) {
        return FormFieldTypes.values()[input.getEnumValue()];
    }
}
