package com.ironsoftware.ironpdf.security;

/**
 * Enumeration defining levels of PDF user access rights to print a PDF. <p>See
 * {@link SecurityOptions#setAllowUserPrinting(PdfPrintSecurity)} </p>
 */
public enum PdfPrintSecurity {
    /**
     * The user may not print the PDF unless they have the Owner password.
     */
    NO_PRINT,

    /**
     * The user may only print the PDF at low resolution unless they have the Owner password.
     */
    PRINT_LOW_QUALITY,

    /**
     * Users may print the PDF without restriction.
     */
    FULL_PRINT_RIGHTS
}
