package com.ironsoftware.ironpdf.standard;

/**
 * Versions of PDF/UA compliant (Universal Accessibility)
 */
public enum PdfUAVersions {
    /**
     * A PDF compliant with the ISO 14289-1 (PDF/UA-1)
     */
    PdfUA1(1),
    /**
     * A PDF compliant with the ISO 14289-2 (PDF/UA-2)
     */
    PdfUA2(2);

    private final int value;

    private PdfUAVersions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
