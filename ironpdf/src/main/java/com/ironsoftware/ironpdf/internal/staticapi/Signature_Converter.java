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
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }

    static com.ironsoftware.ironpdf.internal.proto.PdfiumPdfSignatureP toProto(Signature input) {
        PdfiumPdfSignatureP.Builder proto = PdfiumPdfSignatureP.newBuilder();
        proto.setIndex(input.getInternalIndex());
        if(input.getPassword() != null){
            proto.setPassword(input.getPassword());
        }
        if(input.getTimeStampUrl() != null){
            proto.setTimestampUrl(input.getTimeStampUrl());
        }
        return proto.build();
    }
}
