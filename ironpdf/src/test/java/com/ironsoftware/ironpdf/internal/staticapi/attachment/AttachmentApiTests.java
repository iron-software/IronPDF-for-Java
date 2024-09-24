package com.ironsoftware.ironpdf.internal.staticapi.attachment;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.internal.staticapi.Attachment_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class AttachmentApiTests extends TestBase {

    @Test
    public final void GetPdfAttachmentCollectionTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        AssertNullOrEmpty(Attachment_Api.getPdfAttachmentCollection(doc));
    }

    @Test
    public final void GetPdfAttachmentDataTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        AssertNullOrEmpty(Attachment_Api.getPdfAttachmentCollection(doc));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        Attachment_Api.addPdfAttachment(doc, "googleAttachment", bytes);
        byte[] result = Attachment_Api.getPdfAttachmentData(doc, "googleAttachment");
        Assertions.assertArrayEquals(bytes, result);
    }

    @Test
    public final void AddPdfAttachmentTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        Attachment_Api.addPdfAttachment(doc, "googleAttachment", bytes);
        Assertions.assertEquals(1, Attachment_Api.getPdfAttachmentCollection(doc).size());
    }

    @Test
    public final void RemovePdfAttachmentTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        Attachment_Api.addPdfAttachment(doc, "googleAttachment", bytes);
        Assertions.assertEquals(1, Attachment_Api.getPdfAttachmentCollection(doc).size());
        Attachment_Api.removePdfAttachment(doc, "googleAttachment");
        Assertions.assertEquals(0, Attachment_Api.getPdfAttachmentCollection(doc).size());
    }
}
