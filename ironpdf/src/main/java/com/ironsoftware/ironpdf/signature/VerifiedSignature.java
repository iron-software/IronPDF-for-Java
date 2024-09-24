package com.ironsoftware.ironpdf.signature;

import java.time.Instant;

/**
 *  A class that represents a verified digital signature for a PDF document.
 */
public class VerifiedSignature {
    private String signatureName;

    private String signingContact ;

    private String signingReason ;

    private String signingLocation ;

    private Instant signingDate;

    private boolean isValid;

    private String filter;

    public VerifiedSignature(String signatureName, String signingContact, String signingReason, String signingLocation, Instant signingDate, boolean isValid, String filter) {
        this.signatureName = signatureName;
        this.signingContact = signingContact;
        this.signingReason = signingReason;
        this.signingLocation = signingLocation;
        this.signingDate = signingDate;
        this.isValid = isValid;
        this.filter = filter;
    }



    /**
     * Gets the field name of the digital signature.
     *
     * @return the signature name
     */
    public String getSignatureName() {
        return signatureName;
    }

    /**
     * Gets the contact person or email address for signing related inquiries (optional).
     *
     * @return the contact
     */
    public String getSigningContact() {
        return signingContact;
    }

    /**
     * Gets reason the PDF was signed (optional).
     *
     * @return the signing reason
     */
    public String getSigningReason() {
        return signingReason;
    }

    /**
     * Gets physical location the PDF was signed (optional).
     *
     * @return the signing location
     */
    public String getSigningLocation() {
        return signingLocation;
    }

    /**
     * Gets date and time of the digital signature.
     *
     * @return the signing date
     */
    public Instant getSigningDate() {
        return signingDate;
    }

    /**
     * If the signature is valid,
     *
     * @return the boolean
     */
    public boolean isValid() {
        return isValid;
    }


    /**
     * Signature filter
     *
     * @return Signature filter
     */
    public String getFilter() {
        return filter;
    }


}
