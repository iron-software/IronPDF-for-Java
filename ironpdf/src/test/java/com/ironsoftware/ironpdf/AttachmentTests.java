package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.attachment.AttachmentManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AttachmentTests extends TestBase {

    @Test
    public final void GetPdfAttachmentCollectionTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        AssertNullOrEmpty(doc.getAttachment().getAttachments());
    }

    @Test
    public final void GetPdfAttachmentDataTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        AttachmentManager attachmentManager = doc.getAttachment();
        AssertNullOrEmpty(attachmentManager.getAttachments());
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        attachmentManager.addAttachment("googleAttachment", bytes);
//        TODO Wait for deployment to GetPdfAttachmentData
//        byte[] result = attachmentManager.getAttachmentData("googleAttachment");
//        Assertions.assertArrayEquals(bytes, result);
    }

    @Test
    public final void AddPdfAttachmentTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        AttachmentManager attachmentManager = doc.getAttachment();
        attachmentManager.addAttachment("googleAttachment", bytes);
        Assertions.assertEquals(1, attachmentManager.getAttachments().size());
        Assertions.assertEquals("googleAttachment", attachmentManager.getAttachments().get(0));
    }

    @Test
    public final void RemovePdfAttachmentTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        byte[] bytes = Files.readAllBytes(Paths.get(getTestFile("/Data/empty.pdf")));
        AttachmentManager attachmentManager = doc.getAttachment();
        attachmentManager.addAttachment("googleAttachment", bytes);
        Assertions.assertEquals(1, attachmentManager.getAttachments().size());
        attachmentManager.removeAttachment("googleAttachment");
        Assertions.assertEquals(0, attachmentManager.getAttachments().size());
    }

}
