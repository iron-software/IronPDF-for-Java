package com.ironsoftware.ironpdf.signature;

/**
 * Hashing algorithms available for the PKCS#7/CMS signature digest (the "Hash Algorithm" Adobe
 * Reader shows under Signature Details). SHA-256 is the modern default; SHA-1 remains selectable
 * only for compatibility with legacy validators and is cryptographically weak.
 */
public enum SignatureHashAlgorithms {
    /**
     * SHA-256 algorithm (default, recommended).
     */
    SHA256(0),
    /**
     * SHA-384 algorithm.
     */
    SHA384(1),
    /**
     * SHA-512 algorithm.
     */
    SHA512(2),
    /**
     * SHA-1 algorithm (legacy, cryptographically weak. Avoid for new signatures).
     */
    SHA1(3);

    private final int value;

    SignatureHashAlgorithms(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
