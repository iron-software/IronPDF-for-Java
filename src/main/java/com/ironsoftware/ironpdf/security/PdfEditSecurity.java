package com.ironsoftware.ironpdf.security;

/**
 * Enumeration defining levels of PDF user access rights to edit a PDF.  Edit rights may also be
 * limited by the User's PDF document client software. <p>See
 * {@link PdfSecuritySettings#setAllowUserEdits(PdfEditSecurity)}}</p>
 */
public enum PdfEditSecurity {
    /**
     * The user may not edit the PDF unless they have the Owner password.
     */
    NO_EDIT,

    /**
     * The user may re-arrange pages, rotate pages and manage PDF thumbnails, but may not otherwise
     * edit the PDF unless they have the Owner password.
     */
    EDIT_PAGES,

    /**
     * The user may edit the PDF as allowed by their PDF client software.
     */
    EDIT_ALL
}
