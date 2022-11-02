package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.PdfSecuritySettings;

import java.io.IOException;

import static com.ironsoftware.ironpdf.staticapi.Security_Converter.ToProto;
import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;

public final class Security_Api {

    /**
     * Removes all user and owner password security for a PDF document.  Also disables content
     * encryption.
     * <p>Content is encrypted at 128 bit. Copy and paste of content is disallowed. Annotations and
     * form
     * editing are disabled.</p>
     */
    public static void RemovePasswordsAndEncryption(InternalPdfDocument pdfDocument)
            throws IOException {
        RpcClient client = Access.EnsureConnection();
        RemovePasswordsAndEncryptionRequest.Builder req = RemovePasswordsAndEncryptionRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        EmptyResult res = client.BlockingStub.pdfDocumentSecurityRemovePasswordsAndEncryption(
                req.buildPartial());
        HandleEmptyResult(res);
    }

    /**
     * Get PDF security settings
     *
     * @param pdfDocument PDF document object.
     * @return PdfSecuritySettings
     */
    public static PdfSecuritySettings GetPdfSecuritySettings(InternalPdfDocument pdfDocument)
            throws IOException {
        RpcClient client = Access.EnsureConnection();
        GetPdfSecuritySettingsRequest.Builder req = GetPdfSecuritySettingsRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);

        GetPdfSecuritySettingsResult res = client.BlockingStub.pdfDocumentSecurityGetPdfSecuritySettings(
                req.build());
        if (res.getResultOrExceptionCase()
                == GetPdfSecuritySettingsResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(res.getException());
        }

        return Security_Converter.FromProto(res.getSecuritySettings());
    }

    /**
     * Makes this PDF document read only such that: <p>Content is encrypted at 128 bit. Copy and paste
     * of content is disallowed. Annotations and form editing are disabled.</p>
     *
     * @param pdfDocument PDF document object.
     * @param ownerPassword The owner password for the PDF.  A string for owner password is required
     *                      to enable PDF encryption and all document security options.
     */
    public static void MakePdfDocumentReadOnly(InternalPdfDocument pdfDocument, String ownerPassword)
            throws IOException {
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
        iron.setAllowUserEdits(ToProto(PdfEditSecurity.NO_EDIT));
        iron.setAllowUserFormData(false);
        iron.setAllowUserPrinting(ToProto(PdfPrintSecurity.FULL_PRINT_RIGHTS));
        SetPdfSecuritySettings(pdfDocument, Security_Converter.FromProto(iron.build()));
    }

    /**
     * Set PDF security settings
     *
     * @param pdfDocument PDF document object.
     * @param pdfSecuritySettings A {@link com.ironsoftware.ironpdf.internal.proto.PdfSecuritySettings} object.
     */
    public static void SetPdfSecuritySettings(InternalPdfDocument pdfDocument,
                                              PdfSecuritySettings pdfSecuritySettings) throws IOException {
        RpcClient client = Access.EnsureConnection();
        SetPdfSecuritySettingsRequest.Builder req = SetPdfSecuritySettingsRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setSecuritySettings(ToProto(pdfSecuritySettings));

        EmptyResult res = client.BlockingStub.pdfDocumentSecuritySetPdfSecuritySettings(
                req.buildPartial());
        HandleEmptyResult(res);
    }
}
