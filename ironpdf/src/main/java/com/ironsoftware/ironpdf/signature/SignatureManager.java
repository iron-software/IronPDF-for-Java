package com.ironsoftware.ironpdf.signature;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Signature_Api;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;


/**
 * Class used to sign , get and signature from a {@link PdfDocument}.
 * <p> See: {@link PdfDocument#getSignature()} </p>
 * <p> See: {@link Signature} </p>
 */
public class SignatureManager {

    private final InternalPdfDocument internalPdfDocument;

    /**
     * Please get SignatureManager by {@link PdfDocument#getSignature()} instead.
     *
     * @param internalPdfDocument the internal pdf document
     */
    public SignatureManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * Returns a list of {@link VerifiedSignature}.
     * @return list of {@link VerifiedSignature}
     */
    public List<VerifiedSignature> getVerifiedSignature(){
        return Signature_Api.getVerifiedSignatures(internalPdfDocument);
    }

    /**
     * Signs the PDF with digital signature with advanced options.
     * Note that the PDF will not be fully signed until Saved using {@link PdfDocument#saveAs(Path)}"
     * or {@link PdfDocument#getBinaryData()}
     * @param signature the PdfSignature
     * @throws UnsupportedOperationException if this document already carries a signature applied in this
     *         session (sign, save, re-open the saved PDF, then sign again), or if a signature field name is
     *         combined with a signature image
     */
    public void SignPdfWithSignature(Signature signature){
        SignPdfWithSignature(signature,SignaturePermissions.NoChangesAllowed);
    }

    /**
     * Signs the PDF with digital signature with advanced options.
     * Note that the PDF will not be fully signed until Saved using {@link PdfDocument#saveAs(Path)}"
     * or {@link PdfDocument#getBinaryData()}
     * @param signature the PdfSignature
     * @param permissions Permissions regarding modifications to the document after the digital signature is applied
     * @throws UnsupportedOperationException if this document already carries a signature applied in this
     *         session (sign, save, re-open the saved PDF, then sign again), or if a signature field name is
     *         combined with a signature image
     */
    public void SignPdfWithSignature(Signature signature, SignaturePermissions permissions){

        // Signing into a named field keeps the pre-placed block; an image would rewrite its rectangle
        // and page. Reject the combination before anything is sent (mirrors .NET and the engine).
        String fieldName = signature.getSignatureFieldName();
        if (fieldName != null && !fieldName.trim().isEmpty() && signature.getSignatureImage() != null) {
            throw new UnsupportedOperationException(
                    "A signature image cannot be combined with signatureFieldName. Applying a signature image rewrites the field's rectangle and page, which would move the pre-placed signature block. Sign the named field without an image, or leave signatureFieldName unset to append a new field.");
        }

        // One new signature per document (mirrors .NET and the engine): a second signature on the same
        // document, whether or not it was saved in between, would leave only the last one verifying,
        // because each covers the other's contents. Fail here, before the certificate is streamed,
        // rather than surfacing the engine's remote exception. The supported flow is sign, save,
        // re-open the saved PDF, then sign again.
        if (internalPdfDocument.hasAppliedSignature()) {
            throw new UnsupportedOperationException(
                    "Applying more than one digital signature to a single document is not supported: only the last signature would verify, because each signature covers the other signatures' contents. Sign, save, re-open the saved PDF, then sign the next.");
        }

        // Resolve the signing instant now (defaulting to the current time when the caller left the
        // signature date null) so the /M entry written by the sign request and the CMS signingTime
        // written later at save time denote the same moment. It is recorded on the document, not on
        // the caller-owned Signature, so reusing one Signature across documents is safe.
        Instant signingInstant = signature.getSignatureDate() != null
                ? signature.getSignatureDate() : Instant.now();

        int index = Signature_Api.signPdfWithSignatureFile(internalPdfDocument, signature, permissions, signingInstant);
        signature.internalIndex = index;
        internalPdfDocument.addAppliedSignature(signature, signingInstant);
    }

    /**
     * Verifies all the PDF signatures for this PDF document and returns true if there are no invalid
     * signatures.
     * @return true if no digital signatures or all digital signatures are currently valid. Editing a PDF document in
     * any way will invalidate signatures.
     */
    public boolean VerifyPdfSignatures(){
        return Signature_Api.verifyPdfSignatures(internalPdfDocument);
    }

    /**
     * Removes all signature from the PDF document.
     */
    public void RemoveSignature(){
        internalPdfDocument.clearAppliedSignatures();
        Signature_Api.removeSignature(internalPdfDocument);
    }
}
