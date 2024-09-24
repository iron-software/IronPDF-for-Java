package com.ironsoftware.ironpdf.signature;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Signature_Api;

import java.nio.file.Path;
import java.util.ArrayList;
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
     */
    public void SignPdfWithSignature(Signature signature, SignaturePermissions permissions){

        int index = Signature_Api.signPdfWithSignatureFile(internalPdfDocument, signature, permissions);
        signature.internalIndex = index;
        internalPdfDocument.signatures.add(signature);
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
        internalPdfDocument.signatures = new ArrayList<>();
        Signature_Api.removeSignature(internalPdfDocument);
    }
}
