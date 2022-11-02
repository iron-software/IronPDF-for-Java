package com.ironsoftware.ironpdf.staticapi.metadata;


import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.Metadata_Api;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MetadataApiTests extends TestBase {

    @Test
    public final void GetSetMetadataTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        AssertNullOrEmpty(Metadata_Api.GetAuthor(doc));
        AssertNotNullOrEmpty(Metadata_Api.GetCreationDate(doc));
        Assertions.assertEquals("PDFium", Metadata_Api.GetCreator(doc));
        AssertNullOrEmpty(Metadata_Api.GetKeywords(doc));
        AssertNullOrEmpty(Metadata_Api.GetMetadata(doc, "Custom"));
        AssertNullOrEmpty(Metadata_Api.GetModifiedDate(doc));
        AssertNullOrEmpty(Metadata_Api.GetProducer(doc));
        AssertNullOrEmpty(Metadata_Api.GetSubject(doc));
        AssertNullOrEmpty(Metadata_Api.GetTitle(doc));

        Metadata_Api.SetAuthor(doc, "1");
        Metadata_Api.SetCreationDate(doc, "1");
        Metadata_Api.SetCreator(doc, "1");
        Metadata_Api.SetKeywords(doc, "1");
        Metadata_Api.SetMetadata(doc, "Custom", "1");
        Metadata_Api.SetModifiedDate(doc, "1");
        Metadata_Api.SetProducer(doc, "1");
        Metadata_Api.SetSubject(doc, "1");
        Metadata_Api.SetTitle(doc, "1");

        Assertions.assertEquals("1", Metadata_Api.GetAuthor(doc));
        Assertions.assertEquals("1", Metadata_Api.GetCreationDate(doc));
        Assertions.assertEquals("1", Metadata_Api.GetCreator(doc));
        Assertions.assertEquals("1", Metadata_Api.GetKeywords(doc));
        Assertions.assertEquals("1", Metadata_Api.GetMetadata(doc, "Custom"));
        Assertions.assertEquals("1", Metadata_Api.GetModifiedDate(doc));
        Assertions.assertEquals("1", Metadata_Api.GetProducer(doc));
        Assertions.assertEquals("1", Metadata_Api.GetSubject(doc));
        Assertions.assertEquals("1", Metadata_Api.GetAuthor(doc));
        Assertions.assertEquals("1", Metadata_Api.GetTitle(doc));
    }

    @Test
    public final void RemoveMetadataTest() {
        //todo: Does not implement in main IronPdf yet.
    }
}
