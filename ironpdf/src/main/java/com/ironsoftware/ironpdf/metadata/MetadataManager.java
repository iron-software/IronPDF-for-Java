package com.ironsoftware.ironpdf.metadata;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Metadata_Api;

/**
 * Class used to read and edit MetaData in a {@link com.ironsoftware.ironpdf.PdfDocument}.
 * <p> See: {@link com.ironsoftware.ironpdf.PdfDocument#getMetadata()} </p>
 */
public class MetadataManager {

    private final InternalPdfDocument static_pdfDocument;


    /**
     * Please get MetadataManager by {@link PdfDocument#getMetadata()} instead.
     *
     * @param internalPdfDocument the internal pdf document
     */
    public MetadataManager(InternalPdfDocument internalPdfDocument) {
        this.static_pdfDocument = internalPdfDocument;
    }

    /**
     * Gets the Author of the document.
     *
     * @return the author
     */
    public String getAuthor() {
        return getAnyMetadata("Author");
    }

    /**
     * Gets any metadata value of the document form given key.
     *
     * @param key the key
     * @return the metadata value
     */
    public String getAnyMetadata(String key) {
        return Metadata_Api.getMetadata(static_pdfDocument, key);
    }

    /**
     * Sets the Author of the document.
     *
     * @param value the value
     */
    public void setAuthor(String value) {
        setAnyMetadata("Author", value);
    }

    /**
     * Sets any metadata value of the document form given key.
     *
     * @param key   the key
     * @param value the value
     */
    public void setAnyMetadata(String key, String value) {
        Metadata_Api.setMetadata(static_pdfDocument, key, value);
    }

    /**
     * Gets the PDF file creation DateTime.
     *
     * @return the creation date
     */
    public String getCreationDate() {
        return getAnyMetadata("CreationDate");
    }

    /**
     * Sets the PDF file creation DateTime.
     *
     * @param value the value
     */
    public void setCreationDate(String value) {
        setAnyMetadata("CreationDate", value);
    }

    /**
     * Gets the PDF file last-modified DateTime.
     *
     * @return the modified date
     */
    public String setModifiedDate() {
        return getAnyMetadata("ModDate");
    }

    /**
     * Sets the PDF file last-modified DateTime.
     *
     * @param value the value
     */
    public void setModifiedDate(String value) {
        setAnyMetadata("ModDate", value);
    }

    /**
     * Gets the Creator of the document.
     *
     * @return the creator
     */
    public String getCreator() {
        return getAnyMetadata("Creator");
    }

    /**
     * Sets the Creator of the document.
     *
     * @param value the value
     */
    public void setCreator(String value) {
        setAnyMetadata("Creator", value);
    }

    /**
     * Gets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     *
     * @return the keywords
     */
    public String getKeywords() {
        return getAnyMetadata("Keywords");
    }

    /**
     * Sets Keywords of the document.  This helps search indexes and operating systems correctly index
     * the PDF.
     *
     * @param value the value
     */
    public void setKeywords(String value) {
        setAnyMetadata("Keywords", value);
    }

    /**
     * Gets the Producer of the document.
     *
     * @return the producer
     */
    public String getProducer() {
        return getAnyMetadata("Producer");
    }

    /**
     * Sets the Producer of the document.
     *
     * @param value the value
     */
    public void setProducer(String value) {
        setAnyMetadata("Producer", value);
    }

    /**
     * Gets Subject of the document.  This helps search indexes and operating systems correctly index
     * the PDF, and may appear in PDF viewer software.
     *
     * @return the subject
     */
    public String getSubject() {
        return getAnyMetadata("Subject");
    }

    /**
     * Sets the Subject of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     *
     * @param value the value
     */
    public void setSubject(String value) {
        setAnyMetadata("Subject", value);
    }

    /**
     * Gets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     *
     * @return the title
     */
    public String getTitle() {
        return getAnyMetadata("Title");
    }

    /**
     * Sets the Title of the document.  This helps search indexes and operating systems correctly
     * index the PDF, and may appear in PDF viewer software.
     *
     * @param value the value
     */
    public void setTitle(String value) {
        setAnyMetadata("Title", value);
    }

    /**
     * Method for removing Metadata property by its name.
     *
     * @param key The name of the property.
     */
    public void removeMetadata(String key) {
        Metadata_Api.removeMetadata(static_pdfDocument, key);
    }

}
