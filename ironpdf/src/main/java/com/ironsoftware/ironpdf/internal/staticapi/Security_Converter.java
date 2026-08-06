package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.internal.proto.PdfiumPdfDocumentPermissionsP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSecuritySettingsP;
import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfEncryptionType;
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

        if(securityOptions.isAllowUserCopyPasteContent() != null) {
            if (securityOptions.isAllowUserCopyPasteContent()) {
                enumObject |= PdfDocumentPermissionsEnum.AllowExtractContent.getValue();
            } else {
                enumObject &= ~PdfDocumentPermissionsEnum.AllowExtractContent.getValue();
            }
        }

        if(securityOptions.isAllowUserCopyPasteContentForAccessibility() != null) {
            if (securityOptions.isAllowUserCopyPasteContentForAccessibility()) {
                enumObject |= PdfDocumentPermissionsEnum.AllowAccessibilityExtractContent.getValue();
            } else {
                enumObject &= ~PdfDocumentPermissionsEnum.AllowAccessibilityExtractContent.getValue();
            }
        }

        if(securityOptions.isAllowUserAnnotations() != null) {
            if (securityOptions.isAllowUserAnnotations()) {
                enumObject |= PdfDocumentPermissionsEnum.AllowAnnotations.getValue();
            } else {
                enumObject &= ~PdfDocumentPermissionsEnum.AllowAnnotations.getValue();
            }
        }

        if(securityOptions.isAllowUserFormData() != null) {
            if (securityOptions.isAllowUserFormData()) {
                enumObject |= PdfDocumentPermissionsEnum.AllowFillForms.getValue();
            } else {
                enumObject &= ~PdfDocumentPermissionsEnum.AllowFillForms.getValue();
            }
        }

        if(securityOptions.getAllowUserEdits() != null) {
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
        }

        if(securityOptions.getAllowUserPrinting() != null) {
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

        // Only carry the encryption settings on the wire when a password is actually present.
        // Encryption without a password would write an /Encrypt dictionary keyed on an empty
        // password (a no-op protection and a format change), so we defer it until a password is set,
        // preserving the pre-AES behaviour and the order-independence contract (setEncryptionType
        // before the password must not encrypt).
        boolean hasPassword = !Utils_StringHelper.isNullOrWhiteSpace(iron.getOwnerPassword())
                || !Utils_StringHelper.isNullOrWhiteSpace(iron.getUserPassword());
        if (hasPassword) {
            // Map explicitly rather than casting the ordinal into the proto enum, so a future
            // reordering of either enum cannot silently select the wrong cipher.
            PdfiumPdfSecuritySettingsP.EncryptionAlgorithm algorithm;
            switch (iron.getEncryptionType() != null ? iron.getEncryptionType() : PdfEncryptionType.RC4_128) {
                case AES_128:
                    algorithm = PdfiumPdfSecuritySettingsP.EncryptionAlgorithm.AES_128;
                    break;
                case AES_256:
                    algorithm = PdfiumPdfSecuritySettingsP.EncryptionAlgorithm.AES_256;
                    break;
                default:
                    algorithm = PdfiumPdfSecuritySettingsP.EncryptionAlgorithm.RC4_128;
                    break;
            }
            proto.setEncryptionAlgorithm(algorithm);
            // Only write the optional field when the caller has an explicit value. Leaving it unset
            // lets the engine apply the PDF default (metadata encrypted), matching presence semantics.
            if (iron.isEncryptMetadata() != null) {
                proto.setEncryptMetadata(iron.isEncryptMetadata());
            }
        }

        return proto.build();
    }

    private static PdfEncryptionType fromProto(PdfiumPdfSecuritySettingsP.EncryptionAlgorithm input) {
        // Map explicitly (not by ordinal) so a future reordering of either enum can't mis-map the cipher.
        switch (input) {
            case AES_128:
                return PdfEncryptionType.AES_128;
            case AES_256:
                return PdfEncryptionType.AES_256;
            default:
                return PdfEncryptionType.RC4_128;
        }
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

        // Round-trip the encryption settings so a read-modify-write (e.g. getSecurityOptions() ->
        // setSecurityOptions()) does not silently downgrade an AES document to the RC4-128 default.
        // Safe even against an engine that doesn't populate these yet: proto3 default 0 is RC4_128
        // and unset encrypt_metadata falls back to the PDF default (true), which is the prior behavior.
        iron.setEncryptionType(fromProto(proto.getEncryptionAlgorithm()));
        iron.setEncryptMetadata(proto.hasEncryptMetadata() ? proto.getEncryptMetadata() : true);

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
