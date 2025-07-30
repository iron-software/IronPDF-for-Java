package com.ironsoftware.ironpdf.internal.staticapi.metadata;


import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Metadata_Api;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public class MetadataApiTests extends TestBase {

    @Test
    public final void GetSetMetadataTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        AssertNullOrEmpty(Metadata_Api.getAuthor(doc));
        AssertNotNullOrEmpty(Metadata_Api.getCreationDate(doc));
        Assertions.assertEquals("PDFium", Metadata_Api.getCreator(doc));
        AssertNullOrEmpty(Metadata_Api.getKeywords(doc));
        AssertNullOrEmpty(Metadata_Api.getMetadata(doc, "Custom"));
        AssertNullOrEmpty(Metadata_Api.getModifiedDate(doc));
        AssertNullOrEmpty(Metadata_Api.getProducer(doc));
        AssertNullOrEmpty(Metadata_Api.getSubject(doc));
        AssertNullOrEmpty(Metadata_Api.getTitle(doc));

        // 1. Create a OffsetDateTime for July 23, 2025, at 10:30 AM in the +07:00 timezone.
        OffsetDateTime dateTime = OffsetDateTime.of(
                LocalDateTime.of(2025, 7, 23, 10, 30, 0),
                ZoneOffset.ofHours(7)
        );

        // 2. Use a formatter that produces a standard offset format like +07:00.
        // The 'xxx' pattern provides the offset as +HH:mm.
        DateTimeFormatter baseFormatter = DateTimeFormatter.ofPattern("'D:'yyyyMMddHHmmssxxx");
        String almostCorrectFormat = dateTime.format(baseFormatter);
        // This produces: "D:20250723103000+07:00"

        // 3. Replace the last colon (:) with an apostrophe (') and append another apostrophe.
        int lastColonIndex = almostCorrectFormat.lastIndexOf(':');
        String finalPdfDateFormat = new StringBuilder(almostCorrectFormat)
                .replace(lastColonIndex, lastColonIndex + 1, "'")
                .append("'")
                .toString();

        Metadata_Api.setAuthor(doc, "1");
        Metadata_Api.setCreationDate(doc, finalPdfDateFormat);
        Metadata_Api.setCreator(doc, "1");
        Metadata_Api.setKeywords(doc, "1");
        Metadata_Api.setMetadata(doc, "Custom", "1");
        Metadata_Api.setModifiedDate(doc, finalPdfDateFormat);
        Metadata_Api.setProducer(doc, "1");
        Metadata_Api.setSubject(doc, "1");
        Metadata_Api.setTitle(doc, "1");

        Assertions.assertEquals("1", Metadata_Api.getAuthor(doc));
        Assertions.assertEquals(finalPdfDateFormat, Metadata_Api.getCreationDate(doc));
        Assertions.assertEquals("1", Metadata_Api.getCreator(doc));
        Assertions.assertEquals("1", Metadata_Api.getKeywords(doc));
        Assertions.assertEquals("1", Metadata_Api.getMetadata(doc, "Custom"));
        Assertions.assertEquals(finalPdfDateFormat, Metadata_Api.getModifiedDate(doc));
        Assertions.assertEquals("1", Metadata_Api.getProducer(doc));
        Assertions.assertEquals("1", Metadata_Api.getSubject(doc));
        Assertions.assertEquals("1", Metadata_Api.getAuthor(doc));
        Assertions.assertEquals("1", Metadata_Api.getTitle(doc));
    }

    @Test
    public final void RemoveMetadataTest() {
        //todo: Does not implement in main IronPdf yet.
    }
}
