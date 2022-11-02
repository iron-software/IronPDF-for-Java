package com.ironsoftware.ironpdf.metadata;

import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.Metadata_Api;

import java.io.IOException;


public class MetadataManager {

    private final InternalPdfDocument static_pdfDocument;


    public MetadataManager(InternalPdfDocument staticPdfDocument) {
        this.static_pdfDocument = staticPdfDocument;
    }

    /**
     * Gets the Author of the document.
     */
    public String GetAuthor() throws IOException {
        return GetAnyMetadata("Author");
    }

    /**
     * Gets any metadata value of the document form given key.
     */
    public String GetAnyMetadata(String key) throws IOException {
        return Metadata_Api.GetMetadata(static_pdfDocument, key);
    }

    /**
     * Sets the Author of the document.
     */
    public void SetAuthor(String value) throws IOException {
        SetAnyMetadata("Author", value);
    }

    /**
     * Sets any metadata value of the document form given key.
     */
    public void SetAnyMetadata(String key, String value) throws IOException {
        Metadata_Api.SetMetadata(static_pdfDocument, key, value);
    }

    /**
     * Gets the PDF file creation DateTime.
     */
    public String GetCreationDate() throws IOException {
        return GetAnyMetadata("CreationDate");
    }

    /**
     * Sets the PDF file creation DateTime.
     */
    public void SetCreationDate(String value) throws IOException {
        SetAnyMetadata("CreationDate", value);
    }

    /**
     * Gets the PDF file last-modified DateTime.
     */
    public String GetModifiedDate() throws IOException {
        return GetAnyMetadata("ModDate");
    }

    /**
     * Sets the PDF file last-modified DateTime.
     */
    public void SetModifiedDate(String value) throws IOException {
        SetAnyMetadata("ModDate", value);
    }

    /**
     * Gets the Creator of the document.
     */
    public String GetCreator() throws IOException {
        return GetAnyMetadata("Creator");
    }

    /**
     * Sets the Creator of the document.
     */
    public void SetCreator(String value) throws IOException {
        SetAnyMetadata("Creator", value);
    }

    /**
     * Gets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     */
    public String GetKeywords() throws IOException {
        return GetAnyMetadata("Keywords");
    }

    /**
     * Sets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     */
    public void SetKeywords(String value) throws IOException {
        SetAnyMetadata("Keywords", value);
    }

    /**
     * Gets the Producer of the document.
     */
    public String GetProducer() throws IOException {
        return GetAnyMetadata("Producer");
    }

    /**
     * Sets the Producer of the document.
     */
    public void SetProducer(String value) throws IOException {
        SetAnyMetadata("Producer", value);
    }

    /**
     * Gets Subject of the document.  This helps search indexes and operating systems correctly index
     * the PDF, and may appear in PDF viewer software.
     */
    public String GetSubject() throws IOException {
        return GetAnyMetadata("Subject");
    }

    /**
     * Sets the Subject of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public void SetSubject(String value) throws IOException {
        SetAnyMetadata("Subject", value);
    }

    /**
     * Gets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public String GetTitle() throws IOException {
        return GetAnyMetadata("Title");
    }

    /**
     * Sets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     */
    public void SetTitle(String value) throws IOException {
        SetAnyMetadata("Title", value);
    }

    /**
     * Method for removing Metadata property by its name.
     *
     * @param key The name of the property.
     */
    public void RemoveMetadata(String key) throws IOException {
        Metadata_Api.RemoveMetadata(static_pdfDocument, key);
    }

}
