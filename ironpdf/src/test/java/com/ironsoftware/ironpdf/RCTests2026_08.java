package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.security.PdfEncryptionType;
import com.ironsoftware.ironpdf.security.SecurityOptions;
import com.ironsoftware.ironpdf.signature.Signature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * RC smoke tests for IronPdf Java 2026.8. Exhaustive AES coverage (AES-128/256, non-ASCII/Unicode
 * passwords, EncryptMetadata, RC4 default) lives in {@link AesEncryptionTests}, which runs on all
 * platforms; this class only carries release-wiring checks that don't duplicate it.
 */
public class RCTests2026_08 extends TestBase {
    // ==================== PDF-592: AES encryption ====================

    /**
     * Test02: PDF-592 flagship smoke — an AES-256 document is actually written with the AES-256 cipher
     * (verified in the unencrypted /Encrypt dictionary, so it can't pass as the RC4-128 default) and
     * round-trips through a password-protected reopen.
     */
    @Test
    public final void Test02_PDF592_Aes256_Smoke() throws IOException {
        Path outputPath = Paths.get("TestOutput", "RC0208_aes256.pdf");
        Files.createDirectories(outputPath.getParent());
        try {
            PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>RC 2026.8 AES-256</h1>");
            SecurityOptions options = new SecurityOptions();
            options.setEncryptionType(PdfEncryptionType.AES_256);
            options.setOwnerPassword("password_owner");
            options.setUserPassword("password_user");
            pdf.getSecurity().setSecurityOptions(options);
            pdf.saveAs(outputPath);

            String raw = new String(Files.readAllBytes(outputPath), StandardCharsets.ISO_8859_1);
            Assertions.assertTrue(raw.contains("AESV3"),
                    "Expected AESV3 in /Encrypt (document written as RC4 instead of AES-256?)");
            Assertions.assertTrue(Pattern.compile("/V\\s+5\\b").matcher(raw).find(),
                    "Expected /V 5 in /Encrypt for AES-256");

            PdfDocument reopened = PdfDocument.fromFile(outputPath, "password_user");
            AssertNotNullOrEmpty(reopened.getBinaryData());

            Exception ex = Assertions.assertThrows(Exception.class, () -> PdfDocument.fromFile(outputPath));
            String msg = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
            Assertions.assertTrue(msg.contains("password"), "Expected a password error, but got: " + ex.getMessage());
        } finally {
            Files.deleteIfExists(outputPath);
        }
    }

    // ==================== PDF-2241: signature trusted timestamp ====================

    /**
     * Test05: the RFC-3161 timestamp URL is wired onto {@link Signature} and round-trips. A live
     * TSA is required to embed an actual timestamp token, so only the client-side property is
     * asserted here (a typo / accidental removal of the getter/setter would fail this).
     */
    @Test
    public final void Test05_PDF2241_Signature_TimeStampUrl_RoundTrips() throws IOException {
        Signature signature = new Signature(getTestFile("/Data/IronSoftware.pfx"), "123456");

        Assertions.assertNull(signature.getTimeStampUrl(), "TimeStampUrl should default to null (no timestamp)");

        final String tsa = "https://freetsa.org/tsr";
        signature.setTimeStampUrl(tsa);
        Assertions.assertEquals(tsa, signature.getTimeStampUrl());
    }
}
