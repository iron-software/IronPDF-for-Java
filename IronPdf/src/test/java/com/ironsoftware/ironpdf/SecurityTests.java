package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.security.PdfEditSecurity;
import com.ironsoftware.ironpdf.security.PdfPrintSecurity;
import com.ironsoftware.ironpdf.security.SecurityOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class SecurityTests extends TestBase {

    @Test
    public final void RemovePasswordsAndEncryptionTest() throws IOException {

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google_encrypted.pdf"),
                "123123");

        com.ironsoftware.ironpdf.security.SecurityManager securityManager = doc.getSecurity();

        SecurityOptions secInfoBefore = securityManager.getCurrentSecurityOptions();
        AssertNullOrEmpty(secInfoBefore.getOwnerPassword());
        Assertions.assertEquals("123123", secInfoBefore.getUserPassword());
        Assertions.assertTrue(secInfoBefore.isAllowUserAnnotations());
        Assertions.assertTrue(secInfoBefore.isAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoBefore.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoBefore.getAllowUserEdits());
        Assertions.assertTrue(secInfoBefore.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoBefore.getAllowUserPrinting());

        securityManager.removePasswordsAndEncryption();

        SecurityOptions secInfoAfter = securityManager.getCurrentSecurityOptions();
        AssertNullOrEmpty(secInfoAfter.getOwnerPassword());
        Assertions.assertEquals("", secInfoAfter.getUserPassword());
        Assertions.assertTrue(secInfoAfter.isAllowUserAnnotations());
        Assertions.assertTrue(secInfoAfter.isAllowUserCopyPasteContent());
        Assertions.assertTrue(secInfoAfter.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.EDIT_ALL, secInfoAfter.getAllowUserEdits());
        Assertions.assertTrue(secInfoAfter.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoAfter.getAllowUserPrinting());
    }

    @Test
    public final void SetPdfSecuritySettingsTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        SecurityOptions tempVar = new SecurityOptions();
        tempVar.setAllowUserAnnotations(false);
        tempVar.setAllowUserCopyPasteContent(false);
        tempVar.setAllowUserCopyPasteContentForAccessibility(false);
        tempVar.setAllowUserEdits(PdfEditSecurity.EDIT_PAGES);
        tempVar.setAllowUserFormData(false);
        tempVar.setAllowUserPrinting(PdfPrintSecurity.PRINT_LOW_QUALITY);
        tempVar.setUserPassword("123123");
        tempVar.setOwnerPassword("123123");

        com.ironsoftware.ironpdf.security.SecurityManager securityManager = doc.getSecurity();
        securityManager.setSecurityOptions(tempVar);

        SecurityOptions secInfoAfter = securityManager.getCurrentSecurityOptions();
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

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google_encrypted.pdf"),
                "123123");

        com.ironsoftware.ironpdf.security.SecurityManager securityManager = doc.getSecurity();
        SecurityOptions secInfoBefore = securityManager.getCurrentSecurityOptions();

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
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        com.ironsoftware.ironpdf.security.SecurityManager securityManager = doc.getSecurity();
        securityManager.makePdfDocumentReadOnly("123123");

        SecurityOptions secInfoAfter = securityManager.getCurrentSecurityOptions();

        Assertions.assertEquals("123123", secInfoAfter.getOwnerPassword());
        AssertNullOrEmpty(secInfoAfter.getUserPassword());
        Assertions.assertFalse(secInfoAfter.isAllowUserAnnotations());
        Assertions.assertFalse(secInfoAfter.isAllowUserCopyPasteContent());
        Assertions.assertFalse(secInfoAfter.isAllowUserCopyPasteContentForAccessibility());
        Assertions.assertEquals(PdfEditSecurity.NO_EDIT, secInfoAfter.getAllowUserEdits());
        Assertions.assertFalse(secInfoAfter.isAllowUserFormData());
        Assertions.assertEquals(PdfPrintSecurity.FULL_PRINT_RIGHTS, secInfoAfter.getAllowUserPrinting());
    }



    @Test
    public final void SetIncompleteSecurityOptions() throws IOException {
        PdfDocument pdf = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        SecurityOptions securityOptions = new SecurityOptions();
        securityOptions.setAllowUserCopyPasteContent(false);
        securityOptions.setAllowUserAnnotations(false);
        securityOptions.setAllowUserPrinting(PdfPrintSecurity.FULL_PRINT_RIGHTS);
        securityOptions.setAllowUserFormData(false);
        securityOptions.setOwnerPassword("top-secret");
        securityOptions.setUserPassword("sharable");

        com.ironsoftware.ironpdf.security.SecurityManager securityManager = pdf.getSecurity();
        securityManager.removePasswordsAndEncryption();
        securityManager.makePdfDocumentReadOnly("secret-key");

        securityManager.setSecurityOptions(securityOptions); // should not throw error
    }

}
