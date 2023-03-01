package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.signature.SignaturePermissions;
import com.ironsoftware.ironpdf.signature.VerifiedSignature;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

final class Signature_Converter {
    static List<VerifiedSignature> fromProto(
            com.ironsoftware.ironpdf.internal.proto.GetVerifySignatureResult input) {
        return input.getVerifiedSignatures().getVerifiedSignaturesList().stream().map(vs ->
                new VerifiedSignature(vs.getSignatureName(), vs.getSigningContact(),
                        vs.getSigningReason(), vs.getSigningLocation(), Instant.ofEpochSecond(vs.getSigningDate().getSeconds(),
                        vs.getSigningDate().getNanos()), vs.getIsValid(), vs.getFilter())).collect(Collectors.toList());
    }

    static com.ironsoftware.ironpdf.internal.proto.SignaturePermissions toProto(SignaturePermissions input) {
        com.ironsoftware.ironpdf.internal.proto.SignaturePermissions.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.SignaturePermissions.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
