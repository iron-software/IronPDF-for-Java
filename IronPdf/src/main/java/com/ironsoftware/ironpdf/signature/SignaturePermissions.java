package com.ironsoftware.ironpdf.signature;

/**
 Permission options for a PDF document which is digitally signed
 */
public enum SignaturePermissions
{
    /**
     No changes are allowed
     */
    NoChangesAllowed,
    /**
     Changing form field values is allowed
     */
    FormFillingAllowed,
    /**
     Changing form field values and modifying annotations are allowed
     */
    FormFillingAndAnnotationsAllowed;
}
