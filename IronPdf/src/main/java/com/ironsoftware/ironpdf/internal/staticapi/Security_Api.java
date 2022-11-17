package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.SecurityOptions;

import static com.ironsoftware.ironpdf.internal.staticapi.Security_Converter.toProto;

/**
 * The type Security api.
 */
public final class Security_Api {

    /**
     * Removes all user and owner password security for a PDF document.  Also disables content
     * encryption.
     * <p>Content is encrypted at 128 bit. Copy and paste of content is disallowed. Annotations and
     * form
     * editing are disabled.</p>
     *
     * @param internalPdfDocument the internal pdf document
     */
    public static void removePasswordsAndEncryption(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();
        RemovePasswordsAndEncryptionRequest.Builder req = RemovePasswordsAndEncryptionRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        EmptyResult res = client.blockingStub.pdfDocumentSecurityRemovePasswordsAndEncryption(
                req.buildPartial());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Get PDF security settings
     * <p>
     * .
     *
     * @param internalPdfDocument the internal pdf document
     * @return PdfSecuritySettings pdf security options
     */
    public static SecurityOptions getPdfSecurityOptions(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();
        GetPdfSecuritySettingsRequest.Builder req = GetPdfSecuritySettingsRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        GetPdfSecuritySettingsResult res = client.blockingStub.pdfDocumentSecurityGetPdfSecuritySettings(
                req.build());
        if (res.getResultOrExceptionCase()
                == GetPdfSecuritySettingsResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        return Security_Converter.fromProto(res.getSecuritySettings());
    }

    /**
     * Makes this PDF document read only such that: <p>Content is encrypted at 128 bit. Copy and paste
     * of content is disallowed. Annotations and form editing are disabled.</p>
     * <p>
     * .
     *
     * @param internalPdfDocument the internal pdf document
     * @param ownerPassword       The owner password for the PDF.  A string for owner password is required                      to enable PDF encryption and all document security options.
     */
    public static void makePdfDocumentReadOnly(InternalPdfDocument internalPdfDocument, String ownerPassword) {
        if (Utils_StringHelper.isNullOrWhiteSpace(ownerPassword)) {
            throw new RuntimeException(
                    "MakePdfDocumentReadOnly :: A string for owner password is required to enable PDF encryption and all document security options.");
        }

        com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings.Builder iron = com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings.newBuilder();

        if (!Utils_StringHelper.isNullOrWhiteSpace(ownerPassword)) {
            iron.setOwnerPassword(ownerPassword);
        }

        iron.setAllowUserCopyPasteContent(false);
        iron.setAllowUserCopyPasteContentForAccessibility(false);
        iron.setAllowUserAnnotations(false);
        iron.setAllowUserEdits(Security_Converter.toProto(PdfEditSecurity.NO_EDIT));
        iron.setAllowUserFormData(false);
        iron.setAllowUserPrinting(toProto(PdfPrintSecurity.FULL_PRINT_RIGHTS));
        setPdfSecuritySettings(internalPdfDocument, Security_Converter.fromProto(iron.build()));
    }

    /**
     * Set PDF security settings
     * <p>
     * .
     *
     * @param internalPdfDocument the internal pdf document
     * @param securityOptions     A {@link com.ironsoftware.ironpdf.security.SecurityOptions} object.
     */
    public static void setPdfSecuritySettings(InternalPdfDocument internalPdfDocument,
                                              SecurityOptions securityOptions) {
        RpcClient client = Access.ensureConnection();
        SetPdfSecuritySettingsRequest.Builder req = SetPdfSecuritySettingsRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setSecuritySettings(Security_Converter.toProto(securityOptions));

        EmptyResult res = client.blockingStub.pdfDocumentSecuritySetPdfSecuritySettings(
                req.buildPartial());
        Utils_Util.handleEmptyResult(res);
    }
}
