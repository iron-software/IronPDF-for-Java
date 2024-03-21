package com.ironsoftware.ironpdf.render;

/**
 * Table of contents layout type
 */
public enum TableOfContentsTypes {
    /**
     * Do not create a table of contents
     */
    None(0),
    /**
     * Create a table of contents without page numbers
     * More performance, but does not include page numbers
     */
    Basic(1),
    /**
     * Create a table of contents with page numbers
     */
    WithPageNumbers(2);

    private final int value;

    private TableOfContentsTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

