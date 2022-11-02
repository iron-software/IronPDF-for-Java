package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;

import java.io.IOException;

import static com.ironsoftware.ironpdf.staticapi.Exception_Converter.FromProto;
import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;

public final class Metadata_Api {

    /**
     * Gets the Author of the document.
     */
    public static String GetAuthor(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "Author");
    }

    /**
     * Gets any metadata value of the document form given key.
     */
    public static String GetMetadata(InternalPdfDocument pdfDocument, String key) throws IOException {
        RpcClient client = Access.EnsureConnection();

        GetMetadataRequest.Builder req = GetMetadataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setKey(key);
        MetadataFieldResult res = client.BlockingStub.pdfDocumentMetadataGetMetadata(
                req.build());

        if (res.getResultOrExceptionCase() == MetadataFieldResult.ResultOrExceptionCase.EXCEPTION) {
            throw FromProto(res.getException());
        }

        return res.getResult().getValue();
    }

    /**
     * Sets the Author of the document.
     */
    public static void SetAuthor(InternalPdfDocument pdfDocument, String value) throws IOException {
        SetMetadata(pdfDocument, "Author", value);
    }

    /**
     * Sets any metadata value of the document form given key.
     */
    public static void SetMetadata(InternalPdfDocument pdfDocument, String key, String value)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        SetMetadataRequest.Builder req = SetMetadataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setKey(key);
        req.setValue(value);
        EmptyResult res = client.BlockingStub.pdfDocumentMetadataSetMetadata(req.build());
        HandleEmptyResult(res);
    }

    /**
     * Gets the PDF file creation DateTime.
     */
    public static String GetCreationDate(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "CreationDate");
    }

    /**
     * Sets the PDF file creation DateTime.
     */
    public static void SetCreationDate(InternalPdfDocument pdfDocument, String value)
            throws IOException {
        SetMetadata(pdfDocument, "CreationDate", value);
    }

    /**
     * Gets the PDF file last-modified DateTime.
     */
    public static String GetModifiedDate(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "ModDate");
    }

    /**
     * Gets the PDF file last-modified DateTime.
     */
    public static void SetModifiedDate(InternalPdfDocument pdfDocument, String value)
            throws IOException {
        SetMetadata(pdfDocument, "ModDate", value);
    }

    /**
     * Gets the Creator of the document.
     */
    public static String GetCreator(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "Creator");
    }

    /**
     * Sets the Creator of the document.
     */
    public static void SetCreator(InternalPdfDocument pdfDocument, String value) throws IOException {
        SetMetadata(pdfDocument, "Creator", value);
    }

    /**
     * Gets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     */
    public static String GetKeywords(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "Keywords");
    }

    /**
     * Sets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     */
    public static void SetKeywords(InternalPdfDocument pdfDocument, String value) throws IOException {
        SetMetadata(pdfDocument, "Keywords", value);
    }

    /**
     * Gets the Producer of the document.
     */
    public static String GetProducer(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "Producer");
    }

    /**
     * Sets the Producer of the document.
     */
    public static void SetProducer(InternalPdfDocument pdfDocument, String value) throws IOException {
        SetMetadata(pdfDocument, "Producer", value);
    }

    /**
     * Gets Subject of the document.  This helps search indexes and operating systems correctly index
     * the PDF, and may appear in PDF viewer software.
     */
    public static String GetSubject(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "Subject");
    }

    /**
     * Sets the Subject of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public static void SetSubject(InternalPdfDocument pdfDocument, String value) throws IOException {
        SetMetadata(pdfDocument, "Subject", value);
    }

    /**
     * Gets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public static String GetTitle(InternalPdfDocument pdfDocument) throws IOException {
        return GetMetadata(pdfDocument, "Title");
    }

    /**
     * Sets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public static void SetTitle(InternalPdfDocument pdfDocument, String value) throws IOException {
        SetMetadata(pdfDocument, "Title", value);
    }

    /**
     * Method for removing Metadata property by its name.
     *
     * @param key The name of the property.
     */
    public static void RemoveMetadata(InternalPdfDocument pdfDocument, String key)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        RemoveMetadataRequest.Builder req = RemoveMetadataRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        req.setKey(key);
        EmptyResult res = client.BlockingStub.pdfDocumentMetadataRemoveMetadata(req.build());
        HandleEmptyResult(res);
    }
}
