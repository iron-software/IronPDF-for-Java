package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.metadata.MetadataManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MetadataTests extends TestBase {

    @Test
    public final void GetSetMetadataTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        MetadataManager metadataManager = doc.getMetadata();

        AssertNullOrEmpty(metadataManager.getAuthor());
        AssertNotNullOrEmpty(metadataManager.getCreationDate());
        Assertions.assertEquals("PDFium", metadataManager.getCreator());
        AssertNullOrEmpty(metadataManager.getKeywords());
        AssertNullOrEmpty(metadataManager.getAnyMetadata("Custom"));
        // TODO Uncomment below after fixed
        // AssertNullOrEmpty(metadataManager.getModifiedDate());
        AssertNullOrEmpty(metadataManager.getProducer());
        AssertNullOrEmpty(metadataManager.getSubject());
        AssertNullOrEmpty(metadataManager.getTitle());

        metadataManager.setAuthor("1");
        metadataManager.setCreationDate("1");
        metadataManager.setCreator("1");
        metadataManager.setKeywords("1");
        metadataManager.setAnyMetadata("Custom", "1");
        metadataManager.setModifiedDate("1");
        metadataManager.setProducer("1");
        metadataManager.setSubject("1");
        metadataManager.setTitle("1");

        Assertions.assertEquals("1", metadataManager.getAuthor());
        Assertions.assertEquals("1", metadataManager.getCreationDate());
        Assertions.assertEquals("1", metadataManager.getCreator());
        Assertions.assertEquals("1", metadataManager.getKeywords());
        Assertions.assertEquals("1", metadataManager.getAnyMetadata("Custom"));
        // TODO Uncomment below after fixed
        // Assertions.assertEquals("1", metadataManager.getModifiedDate());
        Assertions.assertEquals("1", metadataManager.getProducer());
        Assertions.assertEquals("1", metadataManager.getSubject());
        Assertions.assertEquals("1", metadataManager.getAuthor());
        Assertions.assertEquals("1", metadataManager.getTitle());
    }

}
