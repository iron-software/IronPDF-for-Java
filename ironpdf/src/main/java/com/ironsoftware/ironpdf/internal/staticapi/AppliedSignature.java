package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.signature.Signature;

import java.time.Instant;

/**
 * A signature applied to a document paired with the signing instant resolved when its placeholder was
 * reserved. Pairing them in one object keeps the signature and its instant structurally aligned, so
 * the save path cannot pick up a neighbouring signature's instant.
 */
final class AppliedSignature {
    final Signature signature;
    final Instant signingInstant;

    AppliedSignature(Signature signature, Instant signingInstant) {
        this.signature = signature;
        this.signingInstant = signingInstant;
    }
}
