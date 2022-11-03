package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;

import static com.ironsoftware.ironpdf.internal.staticapi.Exception_Converter.fromProto;

public final class Metadata_Api {

    /**
     * Gets the Author of the document.
     */
    public static String getAuthor(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "Author");
    }

    /**
     * Gets any metadata value of the document form given key.
     */
    public static String getMetadata(InternalPdfDocument pdfDocument, String key) {
        RpcClient client = Access.ensureConnection();

        GetMetadataRequest.Builder req = GetMetadataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setKey(key);
        MetadataFieldResult res = client.blockingStub.pdfDocumentMetadataGetMetadata(
                req.build());

        if (res.getResultOrExceptionCase() == MetadataFieldResult.ResultOrExceptionCase.EXCEPTION) {
            throw fromProto(res.getException());
        }

        return res.getResult().getValue();
    }

    /**
     * Sets the Author of the document.
     */
    public static void setAuthor(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "Author", value);
    }

    /**
     * Sets any metadata value of the document form given key.
     */
    public static void setMetadata(InternalPdfDocument pdfDocument, String key, String value) {
        RpcClient client = Access.ensureConnection();

        SetMetadataRequest.Builder req = SetMetadataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setKey(key);
        req.setValue(value);
        EmptyResult res = client.blockingStub.pdfDocumentMetadataSetMetadata(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Gets the PDF file creation DateTime.
     */
    public static String getCreationDate(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "CreationDate");
    }

    /**
     * Sets the PDF file creation DateTime.
     */
    public static void setCreationDate(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "CreationDate", value);
    }

    /**
     * Gets the PDF file last-modified DateTime.
     */
    public static String getModifiedDate(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "ModDate");
    }

    /**
     * Gets the PDF file last-modified DateTime.
     */
    public static void setModifiedDate(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "ModDate", value);
    }

    /**
     * Gets the Creator of the document.
     */
    public static String getCreator(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "Creator");
    }

    /**
     * Sets the Creator of the document.
     */
    public static void setCreator(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "Creator", value);
    }

    /**
     * Gets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     */
    public static String getKeywords(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "Keywords");
    }

    /**
     * Sets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     */
    public static void setKeywords(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "Keywords", value);
    }

    /**
     * Gets the Producer of the document.
     */
    public static String getProducer(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "Producer");
    }

    /**
     * Sets the Producer of the document.
     */
    public static void setProducer(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "Producer", value);
    }

    /**
     * Gets Subject of the document.  This helps search indexes and operating systems correctly index
     * the PDF, and may appear in PDF viewer software.
     */
    public static String getSubject(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "Subject");
    }

    /**
     * Sets the Subject of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public static void setSubject(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "Subject", value);
    }

    /**
     * Gets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public static String getTitle(InternalPdfDocument pdfDocument) {
        return getMetadata(pdfDocument, "Title");
    }

    /**
     * Sets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public static void setTitle(InternalPdfDocument pdfDocument, String value) {
        setMetadata(pdfDocument, "Title", value);
    }

    /**
     * Method for removing Metadata property by its name.
     *
     * @param key The name of the property.
     */
    public static void removeMetadata(InternalPdfDocument pdfDocument, String key) {
        RpcClient client = Access.ensureConnection();

        RemoveMetadataRequest.Builder req = RemoveMetadataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setKey(key);
        EmptyResult res = client.blockingStub.pdfDocumentMetadataRemoveMetadata(req.build());
        Utils_Util.handleEmptyResult(res);
    }
}
