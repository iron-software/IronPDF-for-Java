package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.internal.proto.PdfiumPdfDocumentPermissionsP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSecuritySettingsP;
import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.SecurityOptions;

import java.lang.reflect.Field;

final class Security_Converter {
    public enum PdfDocumentPermissionsEnum {
        None(-3904),
        AllowAccessibilityExtractContent(0b1000000000),
        AllowAnnotations(0b100000),
        AllowAssembleDocument(0b10000000000),
        AllowExtractContent(0b10000),
        AllowFillForms(0b100000000),
        AllowPrintFullQuality(0b100000000000),
        AllowModify(0b1000),
        AllowPrint(0b100),
        AllowAll(-4);

        private final int value;

        PdfDocumentPermissionsEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static int convertPermissionsInterfaceToEnum(SecurityOptions securityOptions) {
        int enumObject = PdfDocumentPermissionsEnum.AllowAll.getValue();

        if (securityOptions.isAllowUserCopyPasteContent()) {
            enumObject |= PdfDocumentPermissionsEnum.AllowExtractContent.getValue();
        } else {
            enumObject &= ~PdfDocumentPermissionsEnum.AllowExtractContent.getValue();
        }

        if (securityOptions.isAllowUserCopyPasteContentForAccessibility()) {
            enumObject |= PdfDocumentPermissionsEnum.AllowAccessibilityExtractContent.getValue();
        } else {
            enumObject &= ~PdfDocumentPermissionsEnum.AllowAccessibilityExtractContent.getValue();
        }

        if (securityOptions.isAllowUserAnnotations()) {
            enumObject |= PdfDocumentPermissionsEnum.AllowAnnotations.getValue();
        } else {
            enumObject &= ~PdfDocumentPermissionsEnum.AllowAnnotations.getValue();
        }

        if (securityOptions.isAllowUserFormData()) {
            enumObject |= PdfDocumentPermissionsEnum.AllowFillForms.getValue();
        } else {
            enumObject &= ~PdfDocumentPermissionsEnum.AllowFillForms.getValue();
        }

        switch (securityOptions.getAllowUserEdits()) {
            case NO_EDIT:
                enumObject &= ~PdfDocumentPermissionsEnum.AllowModify.getValue();
                enumObject &= ~PdfDocumentPermissionsEnum.AllowAssembleDocument.getValue();
                break;
            case EDIT_PAGES:
                enumObject &= ~PdfDocumentPermissionsEnum.AllowModify.getValue();
                enumObject |= PdfDocumentPermissionsEnum.AllowAssembleDocument.getValue();
                break;
            case EDIT_ALL:
                enumObject |= PdfDocumentPermissionsEnum.AllowModify.getValue();
                enumObject |= PdfDocumentPermissionsEnum.AllowAssembleDocument.getValue();
                break;
        }

        switch (securityOptions.getAllowUserPrinting()) {
            case NO_PRINT:
                enumObject &= ~PdfDocumentPermissionsEnum.AllowPrint.getValue();
                enumObject &= ~PdfDocumentPermissionsEnum.AllowPrintFullQuality.getValue();
                break;
            case PRINT_LOW_QUALITY:
                enumObject |= PdfDocumentPermissionsEnum.AllowPrint.getValue();
                enumObject &= ~PdfDocumentPermissionsEnum.AllowPrintFullQuality.getValue();
                break;
            case FULL_PRINT_RIGHTS:
                enumObject |= PdfDocumentPermissionsEnum.AllowPrint.getValue();
                enumObject |= PdfDocumentPermissionsEnum.AllowPrintFullQuality.getValue();
                break;
        }

        return enumObject;
    }

    public static SecurityOptions convertPdfiumPdfDocumentPermissionsPToInterface(PdfiumPdfDocumentPermissionsP inputEnum) {
        SecurityOptions securityOptions = new SecurityOptions();

        // Manual mapping for each field
        securityOptions.setAllowUserAnnotations((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowAnnotations.getValue()) != 0);
        securityOptions.setAllowUserCopyPasteContent((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowExtractContent.getValue()) != 0);

        if ((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowModify.getValue()) != 0) {
            securityOptions.setAllowUserEdits(PdfEditSecurity.EDIT_ALL);
        } else if ((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowAssembleDocument.getValue()) != 0) {
            securityOptions.setAllowUserEdits(PdfEditSecurity.EDIT_PAGES);
        } else {
            securityOptions.setAllowUserEdits(PdfEditSecurity.NO_EDIT);
        }

        securityOptions.setAllowUserCopyPasteContentForAccessibility((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowAccessibilityExtractContent.getValue()) != 0);

        if ((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowPrint.getValue()) != 0) {
            if (((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowPrintFullQuality.getValue()) != 0)) {
                securityOptions.setAllowUserPrinting(PdfPrintSecurity.FULL_PRINT_RIGHTS);
            } else {
                securityOptions.setAllowUserPrinting(PdfPrintSecurity.PRINT_LOW_QUALITY);
            }
        } else {
            securityOptions.setAllowUserPrinting(PdfPrintSecurity.NO_PRINT);
        }

        securityOptions.setAllowUserFormData((inputEnum.getEnumValue() & PdfDocumentPermissionsEnum.AllowFillForms.getValue()) != 0);

        return securityOptions;
    }


    static com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSecuritySettingsP toProto(
            SecurityOptions iron) {
        PdfiumPdfDocumentPermissionsP.Builder permission = PdfiumPdfDocumentPermissionsP.newBuilder();
        permission.setEnumValue(convertPermissionsInterfaceToEnum(iron));

        PdfiumPdfSecuritySettingsP.Builder proto = PdfiumPdfSecuritySettingsP.newBuilder();
        proto.setOwnerPassword(iron.getOwnerPassword());
        proto.setUserPassword(iron.getUserPassword());
        proto.setPermissions(permission);

        return proto.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfiumPdfEditSecurityP toProto(PdfEditSecurity input) {
        com.ironsoftware.ironpdf.internal.proto.PdfiumPdfEditSecurityP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfiumPdfEditSecurityP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfiumPdfPrintSecurityP toProto(PdfPrintSecurity input) {
        com.ironsoftware.ironpdf.internal.proto.PdfiumPdfPrintSecurityP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfiumPdfPrintSecurityP.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static SecurityOptions fromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSecuritySettingsP proto) {
        SecurityOptions iron = convertPdfiumPdfDocumentPermissionsPToInterface(proto.getPermissions());

        if (!Utils_StringHelper.isNullOrWhiteSpace(proto.getUserPassword())) {
            iron.setUserPassword(proto.getUserPassword());
        }

        if (!Utils_StringHelper.isNullOrWhiteSpace(proto.getOwnerPassword())) {
            iron.setOwnerPassword(proto.getOwnerPassword());
        }

        return iron;
    }

    static PdfEditSecurity fromProto(com.ironsoftware.ironpdf.internal.proto.PdfiumPdfEditSecurityP input) {
        return PdfEditSecurity.values()[input.getEnumValue()];
    }

    static PdfPrintSecurity fromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfiumPdfPrintSecurityP input) {
        return PdfPrintSecurity.values()[input.getEnumValue()];
    }
}
