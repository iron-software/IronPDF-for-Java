package com.ironsoftware.ironpdf.signature;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Signature_Api;

import java.nio.file.Path;
import java.util.List;


/**
 * Class used to add , edit and remove bookmarks from a {@link PdfDocument} outline.
 * <p>Bookmarks are arranged and navigated in a parent/child node hierarchy, similar to an HTML DOM.</p>
 * <p> See: {@link PdfDocument#getBookmark()} </p>
 * <p> See: {@link Bookmark} </p>
 */
public class SignatureManager {

    private final InternalPdfDocument internalPdfDocument;

    /**
     * Please get BookmarkManager by {@link PdfDocument#getBookmark()} instead.
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
        Signature_Api.signPdfWithSignatureFile(internalPdfDocument,signature, permissions);
    }

    /**
     * Verifies all the PDF signatures for this PDF document and returns <c>true</c> if there are no invalid
     * signatures.
     * @return true if all digital signatures for this PDF document are currently valid. Editing a PDF document in
     * any way will invalidate signatures.
     */
    public boolean VerifyPdfSignatures(){
        return Signature_Api.verifyPdfSignatures(internalPdfDocument);
    }

    /**
     *
     */
    public void RemoveSignature(){
        Signature_Api.removeSignature(internalPdfDocument);
    }
}
