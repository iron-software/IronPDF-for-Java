package com.ironsoftware.ironpdf.internal.staticapi.security;


import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.SecurityOptions;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.internal.staticapi.Security_Api;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SecurityApiTests extends TestBase {

    @Test
    public final void RemovePasswordsAndEncryptionTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/google_encrypted.pdf"),
                "123123");

        SecurityOptions secInfoBefore = Security_Api.getPdfSecurityOptions(doc);
        AssertNullOrEmpty(secInfoBefore.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoBefore.getUserPassword());
        Assertions.assertTrue(secInfoBefore.isAllowUserAnnotations());
        Assertions.assertTrue(secInfoBefore.isAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoBefore.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoBefore.getAllowUserEdits());
        Assertions.assertTrue(secInfoBefore.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoBefore.getAllowUserPrinting());

        Security_Api.removePasswordsAndEncryption(doc);

        SecurityOptions secInfoAfter = Security_Api.getPdfSecurityOptions(doc);
        AssertNullOrEmpty(secInfoAfter.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoAfter.getUserPassword());
        Assertions.assertTrue(secInfoAfter.isAllowUserAnnotations());
        Assertions.assertTrue(secInfoAfter.isAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoAfter.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoAfter.getAllowUserEdits());
        Assertions.assertTrue(secInfoAfter.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoAfter.getAllowUserPrinting());
    }

    @Test
    public final void SetPdfSecuritySettingsTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        SecurityOptions tempVar = new SecurityOptions();
        tempVar.setAllowUserAnnotations(false);
        tempVar.setAllowUserCopyPasteContent(false);
        tempVar.setAllowUserCopyPasteContentForAccessibility(false);
        tempVar.setAllowUserEdits(PdfEditSecurity.EDIT_PAGES);
        tempVar.setAllowUserFormData(false);
        tempVar.setAllowUserPrinting(PdfPrintSecurity.PRINT_LOW_QUALITY);
        tempVar.setUserPassword("123123");
        tempVar.setOwnerPassword("123123");

        Security_Api.setPdfSecuritySettings(doc, tempVar);

        SecurityOptions secInfoAfter = Security_Api.getPdfSecurityOptions(doc);
        Assertions.assertEquals("123123", secInfoAfter.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoAfter.getUserPassword());
        Assertions.assertFalse(secInfoAfter.isAllowUserAnnotations());
        Assertions.assertFalse(secInfoAfter.isAllowUserCopyPasteContent());
        Assertions.assertFalse(secInfoAfter.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_PAGES, secInfoAfter.getAllowUserEdits());
        Assertions.assertFalse(secInfoAfter.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.PRINT_LOW_QUALITY, secInfoAfter.getAllowUserPrinting());
    }

    @Test
    public final void GetPdfSecurityTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/google_encrypted.pdf"),
                "123123");

        SecurityOptions secInfoBefore = Security_Api.getPdfSecurityOptions(doc);

        AssertNullOrEmpty(secInfoBefore.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoBefore.getUserPassword());
        Assertions.assertTrue(secInfoBefore.isAllowUserAnnotations());
        Assertions.assertTrue(secInfoBefore.isAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoBefore.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoBefore.getAllowUserEdits());
        Assertions.assertTrue(secInfoBefore.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoBefore.getAllowUserPrinting());
    }

    @Test
    public final void MakePdfDocumentReadOnlyTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        Security_Api.makePdfDocumentReadOnly(doc, "123123");

        SecurityOptions secInfoAfter = Security_Api.getPdfSecurityOptions(doc);

        Assertions.assertEquals("123123", secInfoAfter.getOwnerPassword());
        AssertNullOrEmpty(secInfoAfter.getUserPassword());
        Assertions.assertFalse(secInfoAfter.isAllowUserAnnotations());
        Assertions.assertFalse(secInfoAfter.isAllowUserCopyPasteContent());
        Assertions.assertFalse(secInfoAfter.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.NO_EDIT, secInfoAfter.getAllowUserEdits());
        Assertions.assertFalse(secInfoAfter.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoAfter.getAllowUserPrinting());
    }
}
