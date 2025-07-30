package com.ironsoftware.ironpdf.metadata;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Metadata_Api;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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
     * @return the creation date as OffsetDateTime object.
     */
    public OffsetDateTime getCreationDate() {
        return ConvertPdfDateFormatStringToOffsetDateTime(getAnyMetadata("CreationDate"));
    }

    /**
     * @deprecated As of version 2025.7.17, replaced by {@link #setCreationDate()}.
     * This method will be removed in a future release. Please use the new
     * method to provide an OffsetDateTime object instead of a String.
     */
    @Deprecated
    public void setCreationDate(String value) {
        setAnyMetadata("CreationDate", value);
    }

    /**
     * Sets the PDF file creation DateTime.
     *
     * @param value the DateTime value.
     */
    public void setCreationDate(OffsetDateTime value) {
        setAnyMetadata("CreationDate", ConvertOffsetDateTimeToPdfDateFormat(value, "yyyyMMddHHmmss"));
    }

    /**
     * Gets the PDF file last-modified DateTime.
     *
     * @return the modified date as OffsetDateTime object.
     */
    public OffsetDateTime getModifiedDate() {
        return ConvertPdfDateFormatStringToOffsetDateTime(getAnyMetadata("ModDate"));
    }

    /**
     * @deprecated As of version 2025.7.17, replaced by {@link #setModifiedDate()}.
     * This method will be removed in a future release. Please use the new
     * method to provide an OffsetDateTime object instead of a String.
     */
    @Deprecated
    public void setModifiedDate(String value) {
        setAnyMetadata("ModDate", value);
    }

    /**
     * Sets the PDF file last-modified DateTime.
     *
     * @param value the DateTime value.
     */
    public void setModifiedDate(OffsetDateTime value) {
        setAnyMetadata("ModDate", ConvertOffsetDateTimeToPdfDateFormat(value, "yyyyMMddHHmmss"));
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
     * Convert a OffsetDateTime object to date format defined in the PDF specification (ISO 32000).
     * Returns date and time formatted according to the PDF specification as String:
     * "D:YYYYMMDDHHMMSS+TZ", where the timezone offset is expressed as "+HH'mm'".
     *
     * Example output: "D:20250627162947+06'00'"
     * 
     * @param dateTime the date value as OffsetDateTime object.
     * @param pattern the pattern value of DateTimeFormatter as String.
     */
    private String ConvertOffsetDateTimeToPdfDateFormat(OffsetDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }

        // 1. Format the date and time part without the offset.
        DateTimeFormatter baseFormatter = DateTimeFormatter.ofPattern(pattern);
        String dateTimePart = dateTime.format(baseFormatter);

        // 2. Get the offset from the dateTime object.
        ZoneOffset offset = dateTime.getOffset();
        String offsetPart;

        // 3. Manually format the offset. The PDF specification requires 'Z' for UTC.
        if (offset.getTotalSeconds() == 0) {
            offsetPart = "Z";
        } else {
            // For all other offsets, build the +HH'mm' string.
            int totalSeconds = offset.getTotalSeconds();
            // Calculate hours and minutes from the total seconds.
            long hours = totalSeconds / 3600;
            long minutes = (Math.abs(totalSeconds) / 60) % 60;
            // Format to the required pattern, e.g., +06'00' or -05'00'.
            offsetPart = String.format("%+03d'%02d'", hours, minutes);
        }

        // 4. Combine the parts with the 'D:' prefix.
        return "D:" + dateTimePart + offsetPart;
    }

    /**
     * Convert a date format String defined in the PDF specification (ISO 32000)
     * to a OffsetDateTime object. The method can parse formats like
     * "D:YYYYMMDDHHMMSS+TZ", where the timezone offset is expressed as "+HH'mm'".
     * 
     * @param dateTime The date string from the PDF, e.g., "D:20250627162947+06'00'".
     */
    private OffsetDateTime ConvertPdfDateFormatStringToOffsetDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return null;
        }

        String parsableDateTime = dateTime;
        if (parsableDateTime.startsWith("D:")) {
            parsableDateTime = parsableDateTime.substring(2);
        }

        // Simply remove apostrophes to convert non-standard +07'00' into standard +0700
        parsableDateTime = parsableDateTime.replace("'", "");

        // Build a single, flexible formatter that can handle inputs with or without an offset
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyyMMddHHmmss")
                // Make the offset section optional
                .optionalStart()
                // The 'X' pattern handles offsets like 'Z' or '+0700'
                .appendPattern("X")
                .optionalEnd()
                // If the optional offset was not found, default to UTC
                .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
                .toFormatter();

        // Parse the cleaned string directly to an OffsetDateTime
        return OffsetDateTime.parse(parsableDateTime, formatter);
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