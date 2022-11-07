package com.ironsoftware.ironpdf.security;

import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Security_Api;

/**
 * A class defining user security settings for a PDF document. <p>Allows the developer to control
 * user access passwords, encryption, and also who may edit, print and copy content from the PDF
 * document</p>
 * <p>See: {@link SecurityManager#setSecurityOptions(SecurityOptions)}.</p>
 */
public class SecurityOptions {

    /**
     * Gets or sets the permissions for users to annotate the PDF document with comments. <p>If
     * AllowUserAnnotations is set false>, the {@link SecurityOptions#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p>
     */
    private Boolean allowUserAnnotations = null;
    /**
     * Gets or sets the permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document. <p>If AllowUserCopyPasteContent is set false,  the
     * {@link SecurityOptions#setOwnerPassword(String)} must also be set for the security measure
     * to take effect.</p>
     */
    private Boolean allowUserCopyPasteContent = null;
    /**
     * Gets or sets the permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document for accessibility.
     */
    private Boolean allowUserCopyPasteContentForAccessibility = null;
    /**
     * Gets or sets the permissions for users edit the PDF document.  The features to edit the
     * document depends entirely on the PDF client software used by the end user. <p>If editing rights
     * are restricted, then the {@link SecurityOptions#setOwnerPassword(String)} must be set for
     * the security measure to take effect.</p>
     */
    private PdfEditSecurity allowUserEdits = PdfEditSecurity.values()[0];
    /**
     * Gets or sets the permissions for users to fill-in (enter data into) forms in the PDF document.
     * <p>If AllowUserFormData is set false, the {@link SecurityOptions#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p> <p>Note. If you want to make the form
     * readonly in Adobe Acrobat Reader please call
     * {@link Security_Api#makePdfDocumentReadOnly(InternalPdfDocument, String)} method or set
     * {@link SecurityOptions#setAllowUserEdits(PdfEditSecurity)} to
     * {@link PdfEditSecurity#NO_EDIT} and set
     * {@link SecurityOptions#setOwnerPassword(String)}.</p>
     */
    private Boolean allowUserFormData = null;
    /**
     * Gets or sets the permissions for users to print the PDF document. <p>If print rights are
     * restricted, then the {@link SecurityOptions#setOwnerPassword(String)} must be set for the
     * security measure to take effect.</p>
     */
    private PdfPrintSecurity allowUserPrinting = PdfPrintSecurity.values()[0];
    /**
     * Sets the owner password and enables 128Bit encryption of PDF content. An owner password is one
     * used to enable and disable all other security settings. <p>OwnerPassword must be set to a non
     * empty string value for {@link SecurityOptions#setAllowUserCopyPasteContent(Boolean)},
     * {@link SecurityOptions#setAllowUserAnnotations(Boolean)},
     * {@link SecurityOptions#setAllowUserFormData(Boolean)} and
     * {@link SecurityOptions#setAllowUserEdits(PdfEditSecurity)} to be restricted.</p>
     */
    private String ownerPassword;
    /**
     * Sets the user password and enables 128Bit encryption of PDF content. <p>A user password if a
     * password that each user must enter to open or print the PDF document.</p>
     */
    private String userPassword;

    /**
     * Instantiates a new Security options. A class defining user security settings for a PDF document. <p>Allows the developer to control
     * user access passwords, encryption, and also who may edit, print and copy content from the PDF
     * document</p>
     * <p>See: {@link SecurityManager#setSecurityOptions(SecurityOptions)}.</p>
     */
    public SecurityOptions() {
    }

    /**
     * Is allow user annotations boolean. The permissions for users to annotate the PDF document with comments. <p>If
     * AllowUserAnnotations is set false, the {@link SecurityOptions#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p>
     *
     * @return the boolean
     */
    public final Boolean isAllowUserAnnotations() {
        return allowUserAnnotations;
    }

    /**
     * Sets allow user annotations. The permissions for users to annotate the PDF document with comments. <p>If
     * AllowUserAnnotations is set false, the {@link SecurityOptions#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p>
     *
     * @param value the value
     */
    public final void setAllowUserAnnotations(Boolean value) {
        allowUserAnnotations = value;
    }

    /**
     * Is allow user copy and paste content boolean. The permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document. <p>If AllowUserCopyPasteContent is set false,  the
     * {@link SecurityOptions#setOwnerPassword(String)} must also be set for the security measure
     * to take effect.</p>
     *
     * @return the boolean
     */
    public final Boolean isAllowUserCopyPasteContent() {
        return allowUserCopyPasteContent;
    }

    /**
     * Sets allow user copy and paste content. The permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document. <p>If AllowUserCopyPasteContent is set false,  the
     * {@link SecurityOptions#setOwnerPassword(String)} must also be set for the security measure
     * to take effect.</p>
     *
     * @param value the value
     */
    public final void setAllowUserCopyPasteContent(Boolean value) {
        allowUserCopyPasteContent = value;
    }

    /**
     * Is allow user copy and paste content for accessibility boolean. The permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document for accessibility.
     *
     * @return the boolean
     */
    public final Boolean isAllowUserCopyPasteContentForAccessibility() {
        return allowUserCopyPasteContentForAccessibility;
    }

    /**
     * Sets allow user copy and paste content for accessibility. The permissions for users to extract or 'copy &amp; paste' content (text and
     * images) from the PDF document for accessibility.
     *
     * @param value the value
     */
    public final void setAllowUserCopyPasteContentForAccessibility(Boolean value) {
        allowUserCopyPasteContentForAccessibility = value;
    }

    /**
     * Gets allow user edits. The permissions for users edit the PDF document.  The features to edit the
     * document depends entirely on the PDF client software used by the end user. <p>If editing rights
     * are restricted, then the {@link SecurityOptions#setOwnerPassword(String)} must be set for
     * the security measure to take effect.</p>
     *
     * @return allow user edits value
     */
    public final PdfEditSecurity getAllowUserEdits() {
        return allowUserEdits;
    }

    /**
     * Sets allow user edits. The permissions for users edit the PDF document.  The features to edit the
     * document depends entirely on the PDF client software used by the end user. <p>If editing rights
     * are restricted, then the {@link SecurityOptions#setOwnerPassword(String)} must be set for
     * the security measure to take effect.</p>
     *
     * @param value the value
     */
    public final void setAllowUserEdits(PdfEditSecurity value) {
        allowUserEdits = value;
    }

    /**
     * Is allow user form data boolean. The permissions for users to fill-in (enter data into) forms in the PDF document.
     * <p>If AllowUserFormData is set false, the {@link SecurityOptions#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p> <p>Note. If you want to make the form
     * readonly in Adobe Acrobat Reader please call
     * {@link Security_Api#makePdfDocumentReadOnly(InternalPdfDocument, String)} method or set
     * {@link SecurityOptions#setAllowUserEdits(PdfEditSecurity)} to
     * {@link PdfEditSecurity#NO_EDIT} and set
     * {@link SecurityOptions#setOwnerPassword(String)}.</p>
     *
     * @return allow user form data value
     */
    public final Boolean isAllowUserFormData() {
        return allowUserFormData;
    }

    /**
     * Sets allow user form data. The permissions for users to fill-in (enter data into) forms in the PDF document.
     * <p>If AllowUserFormData is set false, the {@link SecurityOptions#setOwnerPassword(String)}
     * must be set for the security measure to take effect.</p> <p>Note. If you want to make the form
     * readonly in Adobe Acrobat Reader please call
     * {@link Security_Api#makePdfDocumentReadOnly(InternalPdfDocument, String)} method or set
     * {@link SecurityOptions#setAllowUserEdits(PdfEditSecurity)} to
     * {@link PdfEditSecurity#NO_EDIT} and set
     * {@link SecurityOptions#setOwnerPassword(String)}.</p>
     *
     * @param value the value
     */
    public final void setAllowUserFormData(Boolean value) {
        allowUserFormData = value;
    }

    /**
     * Gets allow user printing. The permissions for users to print the PDF document. <p>If print rights are
     * restricted, then the {@link SecurityOptions#setOwnerPassword(String)} must be set for the
     * security measure to take effect.</p>
     *
     * @return allow user printing value
     */
    public final PdfPrintSecurity getAllowUserPrinting() {
        return allowUserPrinting;
    }

    /**
     * Sets allow user printing. The permissions for users to print the PDF document. <p>If print rights are
     * restricted, then the {@link SecurityOptions#setOwnerPassword(String)} must be set for the
     * security measure to take effect.</p>
     *
     * @param value the value
     */
    public final void setAllowUserPrinting(PdfPrintSecurity value) {
        allowUserPrinting = value;
    }

    /**
     * Gets owner password. The owner password and enables 128Bit encryption of PDF content. An owner password is one
     * used to enable and disable all other security settings. <p>OwnerPassword must be set to a non
     * empty string value for {@link SecurityOptions#setAllowUserCopyPasteContent(Boolean)},
     * {@link SecurityOptions#setAllowUserAnnotations(Boolean)},
     * {@link SecurityOptions#setAllowUserFormData(Boolean)} and
     * {@link SecurityOptions#setAllowUserEdits(PdfEditSecurity)} to be restricted.</p>
     *
     * @return the owner password
     */
    public final String getOwnerPassword() {
        return ownerPassword;
    }

    /**
     * Sets owner password. The owner password and enables 128Bit encryption of PDF content. An owner password is one
     * used to enable and disable all other security settings. <p>OwnerPassword must be set to a non
     * empty string value for {@link SecurityOptions#setAllowUserCopyPasteContent(Boolean)},
     * {@link SecurityOptions#setAllowUserAnnotations(Boolean)},
     * {@link SecurityOptions#setAllowUserFormData(Boolean)} and
     * {@link SecurityOptions#setAllowUserEdits(PdfEditSecurity)} to be restricted.</p>
     *
     * @param value the value
     */
    public final void setOwnerPassword(String value) {
        ownerPassword = value;
    }

    /**
     * Gets user password. The user password and enables 128Bit encryption of PDF content. <p>A user password if a
     * password that each user must enter to open or print the PDF document.</p>
     *
     * @return the user password
     */
    public final String getUserPassword() {
        return userPassword;
    }

    /**
     * Sets user password. The user password and enables 128Bit encryption of PDF content. <p>A user password if a
     * password that each user must enter to open or print the PDF document.</p>
     *
     * @param value the value
     */
    public final void setUserPassword(String value) {
        userPassword = value;
    }
}
