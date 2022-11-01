package com.ironsoftware.ironpdf.security;

import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.Security_Api;

/**
 * A class defining user security settings for a PDF document. <p>Allows the developer to control
 * user access passwords, encryption, and also who may edit, print and copy content from the PDF
 * document</p>
 * <p>Implemented in {@link PdfSecuritySettings}.</p>
 */
public class PdfSecuritySettings {

    /**
     * Gets or sets the permissions for users to annotate the PDF document with comments. <p>If
     * AllowUserAnnotations is set false>, the {@link PdfSecuritySettings#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p>
     */
    private Boolean AllowUserAnnotations = null;
    /**
     * Gets or sets the permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document. <p>If AllowUserCopyPasteContent is set false,  the
     * {@link PdfSecuritySettings#setOwnerPassword(String)} must also be set for the security measure
     * to take effect.</p>
     */
    private Boolean AllowUserCopyPasteContent = null;
    /**
     * Gets or sets the permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document for accessibility.
     */
    private Boolean AllowUserCopyPasteContentForAccessibility = null;
    /**
     * Gets or sets the permissions for users edit the PDF document.  The features to edit the
     * document depends entirely on the PDF client software used by the end user. <p>If editing rights
     * are restricted, then the {@link PdfSecuritySettings#setOwnerPassword(String)} must be set for
     * the security measure to take effect.</p>
     */
    private PdfEditSecurity AllowUserEdits = PdfEditSecurity.values()[0];
    /**
     * Gets or sets the permissions for users to fill-in (enter data into) forms in the PDF document.
     * <p>If AllowUserFormData is set false, the {@link PdfSecuritySettings#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p> <p>Note. If you want to make the form
     * readonly in Adobe Acrobat Reader please call
     * {@link Security_Api#MakePdfDocumentReadOnly(InternalPdfDocument, String)} method or set
     * {@link PdfSecuritySettings#setAllowUserEdits(PdfEditSecurity)} to
     * {@link PdfEditSecurity#NO_EDIT} and set
     * {@link PdfSecuritySettings#setOwnerPassword(String)}.</p>
     */
    private Boolean AllowUserFormData = null;
    /**
     * Gets or sets the permissions for users to print the PDF document. <p>If print rights are
     * restricted, then the {@link PdfSecuritySettings#setOwnerPassword(String)} must be set for the
     * security measure to take effect.</p>
     */
    private PdfPrintSecurity AllowUserPrinting = PdfPrintSecurity.values()[0];
    /**
     * Sets the owner password and enables 128Bit encryption of PDF content. An owner password is one
     * used to enable and disable all other security settings. <p>OwnerPassword must be set to a non
     * empty string value for {@link PdfSecuritySettings#setAllowUserCopyPasteContent(Boolean)},
     * {@link PdfSecuritySettings#setAllowUserAnnotations(Boolean)},
     * {@link PdfSecuritySettings#setAllowUserFormData(Boolean)} and
     * {@link PdfSecuritySettings#setAllowUserEdits(PdfEditSecurity)} to be restricted.</p>
     */
    private String OwnerPassword;
    /**
     * Sets the user password and enables 128Bit encryption of PDF content . <p>A user password if a
     * password that each user must enter to open or print the PDF document.</p>
     */
    private String UserPassword;

    public PdfSecuritySettings() {
    }

    public final Boolean getAllowUserAnnotations() {
        return AllowUserAnnotations;
    }

    public final void setAllowUserAnnotations(Boolean value) {
        AllowUserAnnotations = value;
    }

    public final Boolean getAllowUserCopyPasteContent() {
        return AllowUserCopyPasteContent;
    }

    public final void setAllowUserCopyPasteContent(Boolean value) {
        AllowUserCopyPasteContent = value;
    }

    public final Boolean getAllowUserCopyPasteContentForAccessibility() {
        return AllowUserCopyPasteContentForAccessibility;
    }

    public final void setAllowUserCopyPasteContentForAccessibility(Boolean value) {
        AllowUserCopyPasteContentForAccessibility = value;
    }

    public final PdfEditSecurity getAllowUserEdits() {
        return AllowUserEdits;
    }

    public final void setAllowUserEdits(PdfEditSecurity value) {
        AllowUserEdits = value;
    }

    public final Boolean getAllowUserFormData() {
        return AllowUserFormData;
    }

    public final void setAllowUserFormData(Boolean value) {
        AllowUserFormData = value;
    }

    public final PdfPrintSecurity getAllowUserPrinting() {
        return AllowUserPrinting;
    }

    public final void setAllowUserPrinting(PdfPrintSecurity value) {
        AllowUserPrinting = value;
    }

    public final String getOwnerPassword() {
        return OwnerPassword;
    }

    public final void setOwnerPassword(String value) {
        OwnerPassword = value;
    }

    public final String getUserPassword() {
        return UserPassword;
    }

    public final void setUserPassword(String value) {
        UserPassword = value;
    }
}
