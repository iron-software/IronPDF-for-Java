package com.ironsoftware.ironpdf.internal.staticapi.metadata;


import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Metadata_Api;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Metadata_Api.setAuthor(doc, "1");
        Metadata_Api.setCreationDate(doc, "1");
        Metadata_Api.setCreator(doc, "1");
        Metadata_Api.setKeywords(doc, "1");
        Metadata_Api.setMetadata(doc, "Custom", "1");
        Metadata_Api.setModifiedDate(doc, "1");
        Metadata_Api.setProducer(doc, "1");
        Metadata_Api.setSubject(doc, "1");
        Metadata_Api.setTitle(doc, "1");

        Assertions.assertEquals("1", Metadata_Api.getAuthor(doc));
        Assertions.assertEquals("1", Metadata_Api.getCreationDate(doc));
        Assertions.assertEquals("1", Metadata_Api.getCreator(doc));
        Assertions.assertEquals("1", Metadata_Api.getKeywords(doc));
        Assertions.assertEquals("1", Metadata_Api.getMetadata(doc, "Custom"));
        Assertions.assertEquals("1", Metadata_Api.getModifiedDate(doc));
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
