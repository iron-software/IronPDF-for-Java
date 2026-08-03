package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.security.PdfEncryptionType;
import com.ironsoftware.ironpdf.security.SecurityOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * End-to-end tests for the AES encryption support added via
 * {@link SecurityOptions#setEncryptionType(PdfEncryptionType)} and
 * {@link SecurityOptions#setEncryptMetadata(Boolean)}. Mirrors the C# IronPdf SecurityTests.
 */
public class AesEncryptionTests extends TestBase {

    private static final String HTML = "<html><body><h1>AES Encryption Test</h1></body></html>";

    @Test
    public final void SecurityOptions_Defaults_Preserve_Backward_Compatibility() {
        SecurityOptions securityOptions = new SecurityOptions();
        Assertions.assertEquals(PdfEncryptionType.RC4_128, securityOptions.getEncryptionType());
        Assertions.assertTrue(securityOptions.isEncryptMetadata());
    }

    @ParameterizedTest
    @EnumSource(value = PdfEncryptionType.class, names = {"AES_128", "AES_256"})
    public final void Pdf_Document_Should_Be_Encrypted_With_Aes(PdfEncryptionType encryptionType)
            throws IOException {
        Path outputPath = Paths.get("TestOutput", "output_aes_" + encryptionType + ".pdf");
        Files.createDirectories(outputPath.getParent());
        try {
            PdfDocument pdf = PdfDocument.renderHtmlAsPdf(HTML);
            SecurityOptions options = new SecurityOptions();
            options.setEncryptionType(encryptionType);
            options.setOwnerPassword("password_owner");
            options.setUserPassword("password_user");
            pdf.getSecurity().setSecurityOptions(options);
            pdf.saveAs(outputPath);

            // The /Encrypt dictionary is unencrypted by spec, so the cipher is verifiable on disk.
            // This is what distinguishes real AES from the RC4-128 default: the reopen assertions
            // below pass either way, but AESV2/AESV3 is only present for V>=4.
            String expectedCf = encryptionType == PdfEncryptionType.AES_256 ? "AESV3" : "AESV2";
            int expectedV = encryptionType == PdfEncryptionType.AES_256 ? 5 : 4;
            String raw = new String(Files.readAllBytes(outputPath), StandardCharsets.ISO_8859_1);
            Assertions.assertTrue(raw.contains(expectedCf),
                    "Expected " + expectedCf + " in /Encrypt (document written as RC4 instead of "
                            + encryptionType + "?)");
            Assertions.assertTrue(Pattern.compile("/V\\s+" + expectedV + "\\b").matcher(raw).find(),
                    "Expected /V " + expectedV + " in /Encrypt for " + encryptionType);

            // Opens with the correct user password
            PdfDocument reopened = PdfDocument.fromFile(outputPath, "password_user");
            AssertNotNullOrEmpty(reopened.getBinaryData());

            // Cannot be opened without a password (and specifically for a password reason).
            assertThrowsPasswordError(() -> PdfDocument.fromFile(outputPath));
        } finally {
            Files.deleteIfExists(outputPath);
        }
    }

    @Test
    public final void Pdf_Document_Aes256_Should_Open_With_NonAscii_Password() throws IOException {
        // AES-256 encodes passwords as UTF-8 (ISO 32000-2). A non-ASCII password within the
        // Latin-1 range must still round-trip.
        final String nonAsciiPassword = "pÄsswörd";
        Path outputPath = Paths.get("TestOutput", "output_aes256_nonascii.pdf");
        Files.createDirectories(outputPath.getParent());
        try {
            PdfDocument pdf = PdfDocument.renderHtmlAsPdf(HTML);
            SecurityOptions options = new SecurityOptions();
            options.setEncryptionType(PdfEncryptionType.AES_256);
            options.setUserPassword(nonAsciiPassword);
            pdf.getSecurity().setSecurityOptions(options);
            pdf.saveAs(outputPath);

            PdfDocument reopened = PdfDocument.fromFile(outputPath, nonAsciiPassword);
            AssertNotNullOrEmpty(reopened.getBinaryData());
        } finally {
            Files.deleteIfExists(outputPath);
        }
    }

    @Test
    public final void Pdf_Document_Aes256_Should_Open_With_Unicode_Password() throws IOException {
        // AES-256 passwords are UTF-8 (ISO 32000-2), so a password with code points above
        // U+00FF (which Latin-1 cannot represent) must round-trip end to end.
        final String unicodePassword = "机密文件";
        Path outputPath = Paths.get("TestOutput", "output_aes256_unicode.pdf");
        Files.createDirectories(outputPath.getParent());
        try {
            PdfDocument pdf = PdfDocument.renderHtmlAsPdf(HTML);
            SecurityOptions options = new SecurityOptions();
            options.setEncryptionType(PdfEncryptionType.AES_256);
            options.setUserPassword(unicodePassword);
            pdf.getSecurity().setSecurityOptions(options);
            pdf.saveAs(outputPath);

            PdfDocument reopened = PdfDocument.fromFile(outputPath, unicodePassword);
            AssertNotNullOrEmpty(reopened.getBinaryData());

            // The Unicode password is required to open the document
            assertThrowsPasswordError(() -> PdfDocument.fromFile(outputPath));
        } finally {
            Files.deleteIfExists(outputPath);
        }
    }

    @Test
    public final void Pdf_Document_Aes_Should_Respect_EncryptMetadata_Disabled() throws IOException {
        Path outputPath = Paths.get("TestOutput", "output_aes_metadata.pdf");
        Files.createDirectories(outputPath.getParent());
        try {
            PdfDocument pdf = PdfDocument.renderHtmlAsPdf(HTML);
            SecurityOptions options = new SecurityOptions();
            options.setEncryptionType(PdfEncryptionType.AES_256);
            options.setEncryptMetadata(false);
            options.setOwnerPassword("password_owner");
            options.setUserPassword("password_user");
            pdf.getSecurity().setSecurityOptions(options);
            pdf.saveAs(outputPath);

            // The /Encrypt dictionary is not itself encrypted, so the flag is verifiable on disk.
            // Assert EncryptMetadata=false was actually written, otherwise the setting would be
            // silently ignored and this test would pass on reopen alone.
            String encryptDict = new String(Files.readAllBytes(outputPath), StandardCharsets.ISO_8859_1);
            Assertions.assertTrue(Pattern.compile("/EncryptMetadata\\s+false").matcher(encryptDict).find(),
                    "Expected /EncryptMetadata false to be written to the PDF");

            PdfDocument reopened = PdfDocument.fromFile(outputPath, "password_user");
            AssertNotNullOrEmpty(reopened.getBinaryData());
        } finally {
            Files.deleteIfExists(outputPath);
        }
    }

    @Test
    public final void Pdf_Document_Aes256_Should_Open_With_Owner_Password_Only() throws IOException {
        // The owner password differs from the user password, so opening with it must succeed.
        final String ownerPassword = "owner-only-secret";
        final String userPassword = "different-user-secret";
        Path outputPath = Paths.get("TestOutput", "output_aes256_owner_only.pdf");
        Files.createDirectories(outputPath.getParent());
        try {
            PdfDocument pdf = PdfDocument.renderHtmlAsPdf(HTML);
            SecurityOptions options = new SecurityOptions();
            options.setEncryptionType(PdfEncryptionType.AES_256);
            options.setOwnerPassword(ownerPassword);
            options.setUserPassword(userPassword);
            pdf.getSecurity().setSecurityOptions(options);
            pdf.saveAs(outputPath);

            // Opens with the owner password only, which is not the user password.
            PdfDocument reopened = PdfDocument.fromFile(outputPath, ownerPassword);
            AssertNotNullOrEmpty(reopened.getBinaryData());

            // A wrong password cannot open the document.
            assertThrowsPasswordError(() -> PdfDocument.fromFile(outputPath, "not-the-password"));
        } finally {
            Files.deleteIfExists(outputPath);
        }
    }

    /**
     * Asserts that opening the document throws specifically because of a bad/missing password, rather
     * than accepting any Exception (which a gRPC failure, missing engine or file-lock would also satisfy).
     */
    private static void assertThrowsPasswordError(org.junit.jupiter.api.function.Executable action) {
        Exception ex = Assertions.assertThrows(Exception.class, action);
        String msg = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
        Assertions.assertTrue(msg.contains("password"),
                "Expected a password error, but got: " + ex.getMessage());
    }
}
