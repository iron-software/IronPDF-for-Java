package com.ironsoftware.ironpdf.signature;

/**
 Permission options for a PDF document which is digitally signed
 */
public enum SignaturePermissions
{
    /**
     No changes are allowed
     */
    NoChangesAllowed(1) ,
    /**
     Changing form field values is allowed
     */
    FormFillingAllowed(2),
    /**
     Changing form field values and modifying annotations are allowed
     */
    FormFillingAndAnnotationsAllowed(3);

    private final int value;

    private SignaturePermissions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
