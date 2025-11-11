package com.ironsoftware.ironpdf.standard;

/**
 Versions of PDF/A compliant
 */
public enum PdfAVersions
{
    /**
     A PDF compliant with the ISO 19005-1 (PDF/A-1B)
     */
    PdfA1b(1),
    /**
     A PDF compliant with the ISO 19005-2 (PDF/A-2B)
     */
    PdfA2b(2),
    /**
     A PDF compliant with the ISO 19005-3 (PDF/A-3B)
     */
    PdfA3b(3),
    /**
     A PDF compliant with the ISO 19005-1 (PDF/A-1A)
     */
    PdfA1a(5),
    /**
     A PDF compliant with the ISO 19005-2 (PDF/A-2A)
     */
    PdfA2a(6),
    /**
     A PDF compliant with the ISO 19005-3 (PDF/A-3A)
     */
    PdfA3a(7),
    /**
     A PDF compliant with ISO 19005-4 (PDF/A-4)
     */
    PdfA4(8),
    /**
     A PDF compliant with ISO 19005-4 (PDF/A-4E — engineering documents)
     */
    PdfA4e(9),
    /**
     A PDF compliant with ISO 19005-4 (PDF/A-4F — embedded files allowed)
     */
    PdfA4f(10);

    private final int value;

    private PdfAVersions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}