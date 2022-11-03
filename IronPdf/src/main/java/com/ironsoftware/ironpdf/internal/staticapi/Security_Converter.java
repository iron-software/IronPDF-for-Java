package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.SecurityOptions;

final class Security_Converter {

    static com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings toProto(
            SecurityOptions iron) {
        com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings.Builder proto = com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings.newBuilder();
        if (iron.isAllowUserAnnotations() != null) {
            proto.setAllowUserAnnotations(iron.isAllowUserAnnotations());
        }

        if (iron.isAllowUserCopyPasteContent() != null) {
            proto.setAllowUserCopyPasteContent(iron.isAllowUserCopyPasteContent());
        }

        if (iron.isAllowUserCopyPasteContentForAccessibility() != null) {
            proto.setAllowUserCopyPasteContentForAccessibility(
                    iron.isAllowUserCopyPasteContentForAccessibility());
        }

        if (iron.getAllowUserEdits() != null) {
            proto.setAllowUserEdits(toProto(iron.getAllowUserEdits()));
        }

        if (iron.isAllowUserFormData() != null) {
            proto.setAllowUserFormData(iron.isAllowUserFormData());
        }

        if (iron.getAllowUserPrinting() != null) {
            proto.setAllowUserPrinting(toProto(iron.getAllowUserPrinting()));
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(iron.getOwnerPassword())) {
            proto.setOwnerPassword(iron.getOwnerPassword() != null ? iron.getOwnerPassword() : "");
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(iron.getUserPassword())) {
            proto.setUserPassword(iron.getUserPassword() != null ? iron.getUserPassword() : "");
        }

        return proto.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity toProto(PdfEditSecurity input) {
        com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity toProto(PdfPrintSecurity input) {
        com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static SecurityOptions fromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings proto) {
        SecurityOptions iron = new SecurityOptions();
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
            iron.setAllowUserEdits(Security_Converter.fromProto(proto.getAllowUserEdits()));
        }

        if (proto.hasAllowUserPrinting()) {
            iron.setAllowUserPrinting(Security_Converter.fromProto(proto.getAllowUserPrinting()));
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(proto.getUserPassword())) {
            iron.setUserPassword(proto.getUserPassword());
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(proto.getOwnerPassword())) {
            iron.setOwnerPassword(proto.getOwnerPassword());
        }

        return iron;
    }

    static PdfEditSecurity fromProto(com.ironsoftware.ironpdf.internal.proto.PdfEditSecurity input) {
        return PdfEditSecurity.values()[input.getEnumValue()];
    }

    static PdfPrintSecurity fromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfPrintSecurity input) {
        return PdfPrintSecurity.values()[input.getEnumValue()];
    }
}
