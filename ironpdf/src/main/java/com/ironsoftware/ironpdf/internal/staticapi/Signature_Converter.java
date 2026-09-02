package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSignatureP;
import com.ironsoftware.ironpdf.signature.Signature;
import com.ironsoftware.ironpdf.signature.SignaturePermissions;
import com.ironsoftware.ironpdf.signature.VerifiedSignature;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

final class Signature_Converter {
    static List<VerifiedSignature> fromProto(
            com.ironsoftware.ironpdf.internal.proto.PdfiumGetVerifySignatureResultP input) {
        return input.getVerifiedSignatures().getVerifiedSignaturesList().stream().map(vs ->
                new VerifiedSignature(vs.getSignatureName(), vs.getSigningContact(),
                        vs.getSigningReason(), vs.getSigningLocation(), Instant.ofEpochSecond(vs.getSigningDate().getSeconds(),
                        vs.getSigningDate().getNanos()), vs.getIsValid(), vs.getFilter())).collect(Collectors.toList());
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfiumSignaturePermissionsP toProto(SignaturePermissions input) {
        com.ironsoftware.ironpdf.internal.proto.PdfiumSignaturePermissionsP.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.PdfiumSignaturePermissionsP.newBuilder();
        tempVar.setEnumValue(input.getValue());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSignatureP toProto(Signature input, Instant signingInstant) {
        PdfiumPdfSignatureP.Builder proto = PdfiumPdfSignatureP.newBuilder();
        proto.setIndex(input.getInternalIndex());
        if(input.getPassword() != null){
            proto.setPassword(input.getPassword());
        }
        if(input.getTimeStampUrl() != null){
            proto.setTimestampUrl(input.getTimeStampUrl());
        }
        // Carry the digest choices and signing instant so the engine's save-path rebuild honours
        // them, since getBytes reconstructs the signature server-side from this descriptor.
        proto.setSignatureHashAlgorithm(input.getSignatureHashAlgorithm().getValue());
        proto.setTimestampHashAlgorithm(input.getTimestampHashAlgorithm().getValue());
        // The instant was resolved and recorded on the document when the placeholder was reserved,
        // so the save request names the same moment the sign request wrote into /M.
        if (signingInstant != null) {
            proto.setSignatureDate(com.google.protobuf.Timestamp.newBuilder()
                    .setSeconds(signingInstant.getEpochSecond())
                    .setNanos(signingInstant.getNano()));
        }
        return proto.build();
    }
}
