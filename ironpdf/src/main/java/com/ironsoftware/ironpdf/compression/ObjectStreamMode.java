package com.ironsoftware.ironpdf.compression;

/**
 * Object stream mode used when writing the compressed PDF.
 */
public enum ObjectStreamMode {
    /**
     * Pack indirect objects into object streams. Smallest output for PDF 1.5+ (default).
     */
    GENERATE("generate"),

    /**
     * Keep the input's object stream layout unchanged.
     */
    PRESERVE("preserve"),

    /**
     * Write every object as a top-level indirect object (no object streams).
     * Use when consumer tools cannot read object streams.
     */
    DISABLE("disable");

    private final String wire;

    ObjectStreamMode(String wire) {
        this.wire = wire;
    }

    /**
     * The wire-format string the engine expects on
     * {@code QPdfCompressionFlagsP.object_streams}.
     */
    public String toWire() {
        return wire;
    }
}
