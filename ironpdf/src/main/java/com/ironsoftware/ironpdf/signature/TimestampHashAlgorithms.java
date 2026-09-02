package com.ironsoftware.ironpdf.signature;

/**
 * Algorithm requested from the RFC 3161 time-stamping server for the message imprint. Defaults to
 * SHA-256.
 */
public enum TimestampHashAlgorithms {
    /**
     * SHA-1 algorithm.
     */
    SHA1(0),
    /**
     * SHA-256 algorithm.
     */
    SHA256(1),
    /**
     * SHA-512 algorithm.
     */
    SHA512(2);

    private final int value;

    TimestampHashAlgorithms(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
