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
        PdfiumRemovePasswordsAndEncryptionRequestP.Builder req = PdfiumRemovePasswordsAndEncryptionRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        EmptyResultP res = client.blockingStub.pdfiumSecurityRemovePasswordsAndEncryption(
                req.buildPartial());
        Utils_Util.handleEmptyResult(res);
        internalPdfDocument.userPassword = "";
        internalPdfDocument.ownerPassword = "";
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
        PdfiumGetPdfSecuritySettingsRequestP.Builder req = PdfiumGetPdfSecuritySettingsRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        PdfiumGetPdfSecuritySettingsResultP res = client.blockingStub.pdfiumSecurityGetPdfSecuritySettings(
                req.build());
        if (res.getResultOrExceptionCase()
                == PdfiumGetPdfSecuritySettingsResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        SecurityOptions opt = Security_Converter.fromProto(res.getSecuritySettings());
        opt.setOwnerPassword(internalPdfDocument.ownerPassword);
        opt.setUserPassword(internalPdfDocument.userPassword);
        return opt;
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

        com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSecuritySettingsP.Builder iron = com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSecuritySettingsP.newBuilder();

        SecurityOptions securityOptions = new SecurityOptions();
        securityOptions.setAllowUserCopyPasteContent(false);
        securityOptions.setAllowUserCopyPasteContentForAccessibility(false);
        securityOptions.setAllowUserAnnotations(false);
        securityOptions.setAllowUserEdits(PdfEditSecurity.NO_EDIT);
        securityOptions.setAllowUserFormData(false);
        securityOptions.setAllowUserPrinting(PdfPrintSecurity.FULL_PRINT_RIGHTS);
        securityOptions.setOwnerPassword(ownerPassword);



//        PdfiumPdfSecuritySettingsP proto = Security_Converter.toProto(securityOptions);
//        iron.setAllowUserCopyPasteContent(false);
//        iron.setAllowUserCopyPasteContentForAccessibility(false);
//        iron.setAllowUserAnnotations(false);
//        iron.setAllowUserEdits(Security_Converter.toProto(PdfEditSecurity.NO_EDIT));
//        iron.setAllowUserFormData(false);
//        iron.setAllowUserPrinting(toProto(PdfPrintSecurity.FULL_PRINT_RIGHTS));
        setPdfSecuritySettings(internalPdfDocument, securityOptions);

        internalPdfDocument.userPassword = securityOptions.getUserPassword();
        internalPdfDocument.ownerPassword = securityOptions.getOwnerPassword();
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
        PdfiumSetPdfSecuritySettingsRequestP.Builder req = PdfiumSetPdfSecuritySettingsRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setSettings(Security_Converter.toProto(securityOptions));

        PdfDocumentResultP res = client.blockingStub.pdfiumSecuritySetPdfSecuritySettings(
                req.buildPartial());

        if (res.getResultOrExceptionCase() == PdfDocumentResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        internalPdfDocument.userPassword = securityOptions.getUserPassword();
        internalPdfDocument.ownerPassword = securityOptions.getOwnerPassword();
    }
}
