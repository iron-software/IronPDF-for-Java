package com.ironsoftware.ironpdf.staticapi.security;


import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.PdfSecuritySettings;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.Security_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SecurityApiTests extends TestBase {

    @Test
    public final void RemovePasswordsAndEncryptionTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/google_encrypted.pdf"),
                "123123");

        PdfSecuritySettings secInfoBefore = Security_Api.GetPdfSecuritySettings(doc);
        AssertNullOrEmpty(secInfoBefore.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoBefore.getUserPassword());
        Assertions.assertTrue(secInfoBefore.getAllowUserAnnotations());
        Assertions.assertTrue(secInfoBefore.getAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoBefore.getAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoBefore.getAllowUserEdits());
        Assertions.assertTrue(secInfoBefore.getAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoBefore.getAllowUserPrinting());

        Security_Api.RemovePasswordsAndEncryption(doc);

        PdfSecuritySettings secInfoAfter = Security_Api.GetPdfSecuritySettings(doc);
        AssertNullOrEmpty(secInfoAfter.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoAfter.getUserPassword());
        Assertions.assertTrue(secInfoAfter.getAllowUserAnnotations());
        Assertions.assertTrue(secInfoAfter.getAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoAfter.getAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoAfter.getAllowUserEdits());
        Assertions.assertTrue(secInfoAfter.getAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoAfter.getAllowUserPrinting());
    }

    @Test
    public final void SetPdfSecuritySettingsTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        PdfSecuritySettings tempVar = new PdfSecuritySettings();
        tempVar.setAllowUserAnnotations(false);
        tempVar.setAllowUserCopyPasteContent(false);
        tempVar.setAllowUserCopyPasteContentForAccessibility(false);
        tempVar.setAllowUserEdits(PdfEditSecurity.EDIT_PAGES);
        tempVar.setAllowUserFormData(false);
        tempVar.setAllowUserPrinting(PdfPrintSecurity.PRINT_LOW_QUALITY);
        tempVar.setUserPassword("123123");
        tempVar.setOwnerPassword("123123");

        Security_Api.SetPdfSecuritySettings(doc, tempVar);

        PdfSecuritySettings secInfoAfter = Security_Api.GetPdfSecuritySettings(doc);
        Assertions.assertEquals("123123", secInfoAfter.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoAfter.getUserPassword());
        Assertions.assertFalse(secInfoAfter.getAllowUserAnnotations());
        Assertions.assertFalse(secInfoAfter.getAllowUserCopyPasteContent());
        Assertions.assertFalse(secInfoAfter.getAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_PAGES, secInfoAfter.getAllowUserEdits());
        Assertions.assertFalse(secInfoAfter.getAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.PRINT_LOW_QUALITY, secInfoAfter.getAllowUserPrinting());
    }

    @Test
    public final void GetPdfSecurityTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/google_encrypted.pdf"),
                "123123");

        PdfSecuritySettings secInfoBefore = Security_Api.GetPdfSecuritySettings(doc);

        AssertNullOrEmpty(secInfoBefore.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoBefore.getUserPassword());
        Assertions.assertTrue(secInfoBefore.getAllowUserAnnotations());
        Assertions.assertTrue(secInfoBefore.getAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoBefore.getAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoBefore.getAllowUserEdits());
        Assertions.assertTrue(secInfoBefore.getAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoBefore.getAllowUserPrinting());
    }

    @Test
    public final void MakePdfDocumentReadOnlyTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        Security_Api.MakePdfDocumentReadOnly(doc, "123123");

        PdfSecuritySettings secInfoAfter = Security_Api.GetPdfSecuritySettings(doc);

        Assertions.assertEquals("123123", secInfoAfter.getOwnerPassword());
        AssertNullOrEmpty(secInfoAfter.getUserPassword());
        Assertions.assertFalse(secInfoAfter.getAllowUserAnnotations());
        Assertions.assertFalse(secInfoAfter.getAllowUserCopyPasteContent());
        Assertions.assertFalse(secInfoAfter.getAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.NO_EDIT, secInfoAfter.getAllowUserEdits());
        Assertions.assertFalse(secInfoAfter.getAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoAfter.getAllowUserPrinting());
    }
}
