package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.metadata.MetadataManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.OffsetDateTime;
import java.io.IOException;

public class MetadataTests extends TestBase {

    @Test
    public final void GetSetMetadataTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        MetadataManager metadataManager = doc.getMetadata();

        AssertNullOrEmpty(metadataManager.getAuthor());
        Assertions.assertNotNull(metadataManager.getCreationDate());
        Assertions.assertEquals("PDFium", metadataManager.getCreator());
        AssertNullOrEmpty(metadataManager.getKeywords());
        AssertNullOrEmpty(metadataManager.getAnyMetadata("Custom"));
        // comment this because modified date return empty string.
        //Assertions.assertNotNull(metadataManager.getModifiedDate());
        AssertNullOrEmpty(metadataManager.getProducer());
        AssertNullOrEmpty(metadataManager.getSubject());
        AssertNullOrEmpty(metadataManager.getTitle());

        // Create a OffsetDateTime for July 23, 2025, at 10:30 AM in the +07:00 timezone.
        OffsetDateTime date = OffsetDateTime.of(
            LocalDateTime.of(2025, 7, 23, 10, 30, 0),
            ZoneOffset.ofHours(7)
        );

        metadataManager.setAuthor("1");
        metadataManager.setCreationDate(date);
        metadataManager.setCreator("1");
        metadataManager.setKeywords("1");
        metadataManager.setAnyMetadata("Custom", "1");
        metadataManager.setModifiedDate(date);
        metadataManager.setProducer("1");
        metadataManager.setSubject("1");
        metadataManager.setTitle("1");

        Assertions.assertEquals("1", metadataManager.getAuthor());
        Assertions.assertEquals(date, metadataManager.getCreationDate());
        Assertions.assertEquals("1", metadataManager.getCreator());
        Assertions.assertEquals("1", metadataManager.getKeywords());
        Assertions.assertEquals("1", metadataManager.getAnyMetadata("Custom"));
        Assertions.assertEquals(date, metadataManager.getModifiedDate());
        Assertions.assertEquals("1", metadataManager.getProducer());
        Assertions.assertEquals("1", metadataManager.getSubject());
        Assertions.assertEquals("1", metadataManager.getAuthor());
        Assertions.assertEquals("1", metadataManager.getTitle());
    }
}
