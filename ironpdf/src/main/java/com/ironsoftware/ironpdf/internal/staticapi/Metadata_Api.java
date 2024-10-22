package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;

import static com.ironsoftware.ironpdf.internal.staticapi.Exception_Converter.fromProto;

/**
 * The type Metadata api.
 */
public final class Metadata_Api {

    /**
     * Gets the Author of the document.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the author
     */
    public static String getAuthor(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "Author");
    }

    /**
     * Gets any metadata value of the document form given key.
     *
     * @param internalPdfDocument the internal pdf document
     * @param key                 the key
     * @return the metadata
     */
    public static String getMetadata(InternalPdfDocument internalPdfDocument, String key) {
        RpcClient client = Access.ensureConnection();

        PdfiumGetMetadataRequestP.Builder req = PdfiumGetMetadataRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setKey(key);
        PdfiumMetadataFieldResultP res = client.GetBlockingStub("getMetadata").pdfiumMetadataGetMetadata(
                req.build());

        if (res.getResultOrExceptionCase() == PdfiumMetadataFieldResultP.ResultOrExceptionCase.EXCEPTION) {
            throw fromProto(res.getException());
        }

        return res.getResult().getValue();
    }

    /**
     * Sets the Author of the document.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setAuthor(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "Author", value);
    }

    /**
     * Sets any metadata value of the document form given key.
     *
     * @param internalPdfDocument the internal pdf document
     * @param key                 the key
     * @param value               the value
     */
    public static void setMetadata(InternalPdfDocument internalPdfDocument, String key, String value) {
        RpcClient client = Access.ensureConnection();

        PdfiumSetMetadataRequestP.Builder req = PdfiumSetMetadataRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setKey(key);
        req.setValue(value);
        EmptyResultP res = client.GetBlockingStub("setMetadata").pdfiumMetadataSetMetadata(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Gets the PDF file creation DateTime.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the creation date
     */
    public static String getCreationDate(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "CreationDate");
    }

    /**
     * Sets the PDF file creation DateTime.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setCreationDate(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "CreationDate", value);
    }

    /**
     * Gets the PDF file last-modified DateTime.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the modified date
     */
    public static String getModifiedDate(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "ModDate");
    }

    /**
     * Gets the PDF file last-modified DateTime.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setModifiedDate(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "ModDate", value);
    }

    /**
     * Gets the Creator of the document.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the creator
     */
    public static String getCreator(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "Creator");
    }

    /**
     * Sets the Creator of the document.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setCreator(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "Creator", value);
    }

    /**
     * Gets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the keywords
     */
    public static String getKeywords(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "Keywords");
    }

    /**
     * Sets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setKeywords(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "Keywords", value);
    }

    /**
     * Gets the Producer of the document.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the producer
     */
    public static String getProducer(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "Producer");
    }

    /**
     * Sets the Producer of the document.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setProducer(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "Producer", value);
    }

    /**
     * Gets Subject of the document.  This helps search indexes and operating systems correctly index
     * the PDF, and may appear in PDF viewer software.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the subject
     */
    public static String getSubject(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "Subject");
    }

    /**
     * Sets the Subject of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setSubject(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "Subject", value);
    }

    /**
     * Gets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the title
     */
    public static String getTitle(InternalPdfDocument internalPdfDocument) {
        return getMetadata(internalPdfDocument, "Title");
    }

    /**
     * Sets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     *
     * @param internalPdfDocument the internal pdf document
     * @param value               the value
     */
    public static void setTitle(InternalPdfDocument internalPdfDocument, String value) {
        setMetadata(internalPdfDocument, "Title", value);
    }

    /**
     * Method for removing Metadata property by its name.
     *
     * @param internalPdfDocument the internal pdf document
     * @param key                 The name of the property.
     */
    public static void removeMetadata(InternalPdfDocument internalPdfDocument, String key) {
        RpcClient client = Access.ensureConnection();

        PdfiumRemoveMetadataRequestP.Builder req = PdfiumRemoveMetadataRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setKey(key);
        EmptyResultP res = client.GetBlockingStub("removeMetadata").pdfiumMetadataRemoveMetadata(req.build());
        Utils_Util.handleEmptyResult(res);
    }
}
