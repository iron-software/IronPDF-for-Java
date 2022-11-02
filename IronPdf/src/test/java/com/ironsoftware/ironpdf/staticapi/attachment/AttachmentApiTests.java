package com.ironsoftware.ironpdf.staticapi.attachment;

import com.google.common.collect.Iterators;
import com.ironsoftware.ironpdf.staticapi.Attachment_Api;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class AttachmentApiTests extends TestBase {

    @Test
    public final void GetPdfAttachmentCollectionTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        AssertNullOrEmpty(Attachment_Api.GetPdfAttachmentCollection(doc));
    }

    @Test
    public final void GetPdfAttachmentDataTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        AssertNullOrEmpty(Attachment_Api.GetPdfAttachmentCollection(doc));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        Attachment_Api.AddPdfAttachment(doc, "googleAttachment", bytes);
        byte[] result = Attachment_Api.GetPdfAttachmentData(doc, "googleAttachment");
        Assertions.assertArrayEquals(bytes, result);
    }

    @Test
    public final void AddPdfAttachmentTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        Attachment_Api.AddPdfAttachment(doc, "googleAttachment", bytes);
        Assertions.assertEquals(1, Iterators.size(Attachment_Api.GetPdfAttachmentCollection(doc)));
    }

    @Test
    public final void RemovePdfAttachmentTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        Attachment_Api.AddPdfAttachment(doc, "googleAttachment", bytes);
        Assertions.assertEquals(1, Iterators.size(Attachment_Api.GetPdfAttachmentCollection(doc)));
        Attachment_Api.RemovePdfAttachment(doc, "googleAttachment");
        Assertions.assertEquals(0, Iterators.size(Attachment_Api.GetPdfAttachmentCollection(doc)));
    }
}
