package com.ironsoftware.ironpdf.security;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Security_Api;

/**
 * Allows the developer to control: user access passwords, encryption, and also who may edit, print and copy content from the PDF document.
 * <p> See: {@link com.ironsoftware.ironpdf.PdfDocument#getAnnotation()} </p>
 */
public class SecurityManager {

    private final InternalPdfDocument internalPdfDocument;

    /**
     * Please get SecurityManager by {@link PdfDocument#getSecurity()} instead.
     *
     * @param internalPdfDocument the internal pdf document
     */
    public SecurityManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Gets the owner password and enables 128-bit encryption of PDF content.
     * <p>An owner password enables other security settings. <br>OwnerPassword must be set to a non-empty
     * string value for AllowUserCopyPasteContent, AllowUserAnnotation, AllowUserFormData,
     * AllowUserPrinting, AllowUserEdits to be restricted.</p>
     */
    public final String getOwnerPassword() {
        return Security_Api.getPdfSecurityOptions(internalPdfDocument).getOwnerPassword();
    }

    /**
     * Sets the owner password and enables 128-bit encryption of PDF content.
     * <p>An owner password enables other security settings. <br>OwnerPassword must be set to a non-empty
     * string value for AllowUserCopyPasteContent, AllowUserAnnotation, AllowUserFormData,
     * AllowUserPrinting, AllowUserEdits to be restricted.</p>
     */
    public final void setOwnerPassword(String value) {
        Security_Api.getPdfSecurityOptions(internalPdfDocument).setOwnerPassword(value);
    }

    /**
     * Gets a Password used to protect and encrypt the PDF File.
     * <p>Setting a password will cause IronPDF to automatically protect the PDF file content using strong 128-bit encryption. </p>
     */
    public final String getPassword() {
        return Security_Api.getPdfSecurityOptions(internalPdfDocument).getOwnerPassword();
    }

    /**
     * Sets a Password used to protect and encrypt the PDF File.
     * <p>Setting a password will cause IronPDF to automatically protect the PDF file content using strong 128-bit encryption. </p>
     * <p>Setting the password to null will remove any existing password.</p>
     */
    public final void setPassword(String value) {
        Security_Api.getPdfSecurityOptions(internalPdfDocument).setOwnerPassword(value);
    }

    /**
     * Removes all user and owner password security for a PDF document.  Also disables content
     * encryption.
     * <p>If content is encrypted at 128 bit, copy and paste of content, annotations and form editing may be disabled.</p>
     */
    public final void removePasswordsAndEncryption() {
        Security_Api.removePasswordsAndEncryption(internalPdfDocument);
    }

    /**
     * Makes this PDF document read only such that:
     * <p> - Content is encrypted at 128 bit.<br>
     * - Copy and paste of content is disallowed. <br>
     * - Annotations and form editing are disabled.</p>
     *
     * @param ownerPassword The owner password for the PDF.  A string for owner password is required
     *                      to enable PDF encryption and all document security options.
     */
    public void makePdfDocumentReadOnly(String ownerPassword) {
        Security_Api.makePdfDocumentReadOnly(internalPdfDocument, ownerPassword);
    }

    /**
     * Sets advanced security settings for the PDF.
     * <p>Allows the developer to control: user access passwords, encryption, and also who may edit, print and copy content from the PDF document</p>
     *
     * @param securityOptions Advanced security settings for this PDF as an instance of {@link SecurityOptions}
     */
    public void setSecurityOptions(SecurityOptions securityOptions) {
        Security_Api.setPdfSecuritySettings(internalPdfDocument, securityOptions);
    }

    /**
     * Gets the advanced security settings for this PDF document.
     * <p>Allows the developer to control: user access passwords, encryption, and also who may edit, print and copy content from the PDF document</p>
     */
    public SecurityOptions getCurrentSecurityOptions() {
        return Security_Api.getPdfSecurityOptions(internalPdfDocument);
    }
}
