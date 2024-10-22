package com.ironsoftware.ironpdf.attachment;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Attachment_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;

import java.util.List;

/**
 * Class used to edit attachment to a {@link com.ironsoftware.ironpdf.PdfDocument}.
 * <p> See: {@link com.ironsoftware.ironpdf.PdfDocument#getAttachment()} </p>
 */
public class AttachmentManager {

    private final InternalPdfDocument internalPdfDocument;

    /**
     * Please get AttachmentManager by {@link PdfDocument#getAttachment()} instead.
     *
     * @param internalPdfDocument the internal pdf document
     */
    public AttachmentManager(InternalPdfDocument internalPdfDocument) {
        this.internalPdfDocument = internalPdfDocument;
    }

    /**
     * A Collection of file attachment names (keys) contained within this PdfDocument.
     * @return A list of attachments names
     */
    public final List<String> getAttachments() {
        return Attachment_Api.getPdfAttachmentCollection(
                this.internalPdfDocument);
    }

    /**
     * Add a new attachment.
     *
     * @param name           attachment name
     * @param attachmentData attachment data
     */
    public final void addAttachment(String name, byte[] attachmentData) {
        Attachment_Api.addPdfAttachment(this.internalPdfDocument, name, attachmentData);
    }

    /**
     * Remove an attachment by attachment name
     *
     * @param name attachment name
     */
    public final void removeAttachment(String name) {
        Attachment_Api.removePdfAttachment(this.internalPdfDocument, name);
    }

    /**
     * Remove an attachment by attachment index
     *
     * @param index attachment index
     */
    public final void removeAttachment(int index) {
        Attachment_Api.removePdfAttachment(this.internalPdfDocument, index);
    }

    /**
     * Gets attachment data from attachment name
     *
     * @param name Attachment name
     * @return a attachment data
     */
    public final byte[] getAttachmentData(String name) {
        return Attachment_Api.getPdfAttachmentData(this.internalPdfDocument, name);
    }

    /**
     * Gets attachment data from attachment index
     *
     * @param index Attachment index
     * @return a attachment data
     */
    public final byte[] getAttachmentData(int index) {
        return Attachment_Api.getPdfAttachmentData(this.internalPdfDocument, index);
    }

}
