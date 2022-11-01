package com.ironsoftware.ironpdf.staticapi;


import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.PdfSecuritySettings;

final class Security_Converter {

    static com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings ToProto(
            PdfSecuritySettings iron) {
        com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings.Builder proto = com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings.newBuilder();
        if (iron.getAllowUserAnnotations() != null) {
            proto.setAllowUserAnnotations(iron.getAllowUserAnnotations());
        }

        if (iron.getAllowUserCopyPasteContent() != null) {
            proto.setAllowUserCopyPasteContent(iron.getAllowUserCopyPasteContent());
        }

        if (iron.getAllowUserCopyPasteContentForAccessibility() != null) {
            proto.setAllowUserCopyPasteContentForAccessibility(
                    iron.getAllowUserCopyPasteContentForAccessibility());
        }

        if (iron.getAllowUserEdits() != null) {
            proto.setAllowUserEdits(ToProto(iron.getAllowUserEdits()));
        }

        if (iron.getAllowUserFormData() != null) {
            proto.setAllowUserFormData(iron.getAllowUserFormData());
        }

        if (iron.getAllowUserPrinting() != null) {
            proto.setAllowUserPrinting(ToProto(iron.getAllowUserPrinting()));
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(iron.getOwnerPassword())) {
            proto.setOwnerPassword(iron.getOwnerPassword() != null ? iron.getOwnerPassword() : "");
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(iron.getUserPassword())) {
            proto.setUserPassword(iron.getUserPassword() != null ? iron.getUserPassword() : "");
        }

        return proto.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity ToProto(PdfEditSecurity input) {
        com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity ToProto(PdfPrintSecurity input) {
        com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static PdfSecuritySettings FromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings proto) {
        PdfSecuritySettings iron = new PdfSecuritySettings();
        if (proto.hasAllowUserAnnotations()) {
            iron.setAllowUserAnnotations(proto.getAllowUserAnnotations());
        }

        if (proto.hasAllowUserCopyPasteContent()) {
            iron.setAllowUserCopyPasteContent(proto.getAllowUserCopyPasteContent());
        }

        if (proto.hasAllowUserCopyPasteContentForAccessibility()) {
            iron.setAllowUserCopyPasteContentForAccessibility(
                    proto.getAllowUserCopyPasteContentForAccessibility());
        }

        if (proto.hasAllowUserFormData()) {
            iron.setAllowUserFormData(proto.getAllowUserFormData());
        }

        if (proto.hasAllowUserEdits()) {
            iron.setAllowUserEdits(Security_Converter.FromProto(proto.getAllowUserEdits()));
        }

        if (proto.hasAllowUserPrinting()) {
            iron.setAllowUserPrinting(Security_Converter.FromProto(proto.getAllowUserPrinting()));
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(proto.getUserPassword())) {
            iron.setUserPassword(proto.getUserPassword());
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(proto.getOwnerPassword())) {
            iron.setOwnerPassword(proto.getOwnerPassword());
        }

        return iron;
    }

    static PdfEditSecurity FromProto(com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity input) {
        return PdfEditSecurity.values()[input.getEnumValue()];
    }

    static PdfPrintSecurity FromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity input) {
        return PdfPrintSecurity.values()[input.getEnumValue()];
    }
}
