package com.ironsoftware.ironpdf.signature;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Signature_Api;

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

    public List<VerifiedSignature> getVerifiedSignature(){
        return Signature_Api.getVerifiedSignatures(internalPdfDocument);
    }

    public void SignPdfWithSignature(Signature signature){
        Signature_Api.signPdfWithSignatureFile(internalPdfDocument,signature);
    }

    public boolean VerifyPdfSignatures(){
        return Signature_Api.verifyPdfSignatures(internalPdfDocument);
    }
}
