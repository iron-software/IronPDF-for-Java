package com.ironsoftware.ironpdf.internal.staticapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Tests for the engine download checks: the SHA-256 of the zip and the zip-slip guard.
 * These tests do not download or start IronPdfEngine.
 */
public class EngineDownloadIntegrityTests {

    @TempDir
    Path tmp;

    private static Map<String, Map<String, String>> tableFor(String version, String key, String sha256) {
        Map<String, String> perPlatform = new HashMap<>();
        perPlatform.put(key, sha256);
        Map<String, Map<String, String>> table = new HashMap<>();
        table.put(version, perPlatform);
        return table;
    }

    private static String sha256(byte[] data) throws Exception {
        StringBuilder hex = new StringBuilder();
        for (byte b : MessageDigest.getInstance("SHA-256").digest(data)) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    private Path zipWith(String fileName, String... entryNamesAndContents) throws IOException {
        Path zip = tmp.resolve(fileName);
        try (OutputStream out = Files.newOutputStream(zip); ZipOutputStream zos = new ZipOutputStream(out)) {
            for (int i = 0; i < entryNamesAndContents.length; i += 2) {
                zos.putNextEntry(new ZipEntry(entryNamesAndContents[i]));
                zos.write(entryNamesAndContents[i + 1].getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
        }
        return zip;
    }

    @Test
    public void TableHasAHashForEveryPlatformOfThisEngineVersionTest() {
        for (String key : new String[]{"Windows.x64", "Windows.x86", "Linux.x64", "MacOS.arm64", "MacOS.x64"}) {
            String[] parts = key.split("\\.");
            String hash = EngineDownloadIntegrity.expectedSha256(Setting_Api.IRON_PDF_ENGINE_VERSION, parts[0], parts[1]);
            Assertions.assertNotNull(hash, key);
            Assertions.assertTrue(hash.matches("[0-9a-f]{64}"), key + " " + hash);
        }
    }

    @Test
    public void VerifyAcceptsTheExpectedZipAndRefusesAnotherTest() throws Exception {
        Path zip = tmp.resolve("engine.zip");
        Files.write(zip, "the engine".getBytes(StandardCharsets.UTF_8));

        EngineDownloadIntegrity.verify(tableFor("9.9.9", "Linux.x64", sha256("the engine".getBytes(StandardCharsets.UTF_8))),
                zip, "9.9.9", "Linux", "x64", false);

        IOException e = Assertions.assertThrows(IOException.class, () -> EngineDownloadIntegrity.verify(
                tableFor("9.9.9", "Linux.x64", sha256("another engine".getBytes(StandardCharsets.UTF_8))),
                zip, "9.9.9", "Linux", "x64", false));
        Assertions.assertTrue(e.getMessage().contains("expects"), e.getMessage());
    }

    @Test
    public void VerifyRefusesAnUnknownVersionOrPlatformTest() throws Exception {
        Path zip = tmp.resolve("engine.zip");
        Files.write(zip, "x".getBytes(StandardCharsets.UTF_8));
        Map<String, Map<String, String>> table = tableFor("9.9.9", "Linux.x64", "00");

        IOException version = Assertions.assertThrows(IOException.class,
                () -> EngineDownloadIntegrity.verify(table, zip, "1.0.0", "Linux", "x64", false));
        Assertions.assertTrue(version.getMessage().contains("no SHA-256"), version.getMessage());
        IOException platform = Assertions.assertThrows(IOException.class,
                () -> EngineDownloadIntegrity.verify(table, zip, "9.9.9", "MacOS", "arm64", false));
        Assertions.assertTrue(platform.getMessage().contains("no SHA-256"), platform.getMessage());
    }

    @Test
    public void VerifyContinuesWhenTheUserAllowsAnUnverifiedDownloadTest() throws Exception {
        Path zip = tmp.resolve("engine.zip");
        Files.write(zip, "x".getBytes(StandardCharsets.UTF_8));
        Map<String, Map<String, String>> table = tableFor("9.9.9", "Linux.x64", "00");

        EngineDownloadIntegrity.verify(table, zip, "1.0.0", "Linux", "x64", true);
    }

    @Test
    public void VerifyRefusesAKnownHashMismatchEvenWhenUnverifiedIsAllowedTest() throws Exception {
        Path zip = tmp.resolve("engine.zip");
        Files.write(zip, "x".getBytes(StandardCharsets.UTF_8));
        Map<String, Map<String, String>> table = tableFor("9.9.9", "Linux.x64", "00");

        Assertions.assertThrows(IOException.class,
                () -> EngineDownloadIntegrity.verify(table, zip, "9.9.9", "Linux", "x64", true));
    }

    @Test
    public void AllowUnverifiedFollowsTheSettingThenThePropertyTest() {
        Boolean saved = Setting_Api.allowUnverifiedEngineDownload;
        try {
            Setting_Api.allowUnverifiedEngineDownload = null;
            System.clearProperty(EngineDownloadIntegrity.ALLOW_UNVERIFIED_PROPERTY);
            System.setProperty(EngineDownloadIntegrity.ALLOW_UNVERIFIED_PROPERTY, "true");
            Assertions.assertTrue(EngineDownloadIntegrity.allowUnverified());

            Setting_Api.allowUnverifiedEngineDownload = false;
            Assertions.assertFalse(EngineDownloadIntegrity.allowUnverified());
            Setting_Api.allowUnverifiedEngineDownload = true;
            System.clearProperty(EngineDownloadIntegrity.ALLOW_UNVERIFIED_PROPERTY);
            Assertions.assertTrue(EngineDownloadIntegrity.allowUnverified());
        } finally {
            Setting_Api.allowUnverifiedEngineDownload = saved;
            System.clearProperty(EngineDownloadIntegrity.ALLOW_UNVERIFIED_PROPERTY);
        }
    }

    @Test
    public void ResolveSafeEntryBlocksPathsOutsideTheFolderTest() throws Exception {
        Path dest = tmp.resolve("engine");
        Assertions.assertEquals(dest.resolve("bin").resolve("IronPdfEngineConsole").toAbsolutePath().normalize(),
                EngineDownloadIntegrity.resolveSafeEntry(dest, "bin/IronPdfEngineConsole"));
        for (String bad : new String[]{"../evil.txt", "a/../../evil.txt", "../engine-evil/x"}) {
            IOException e = Assertions.assertThrows(IOException.class,
                    () -> EngineDownloadIntegrity.resolveSafeEntry(dest, bad), bad);
            Assertions.assertTrue(e.getMessage().contains("Blocked zip entry"), e.getMessage());
        }
    }

    @Test
    public void UnzipExtractsANormalZipTest() throws Exception {
        Path zip = zipWith("ok.zip", "dir/a.txt", "hello", "b.txt", "world");
        Path dest = tmp.resolve("out");

        Access.unzip(zip.toString(), dest.toString());

        Assertions.assertEquals("hello", new String(Files.readAllBytes(dest.resolve("dir").resolve("a.txt")), StandardCharsets.UTF_8));
        Assertions.assertEquals("world", new String(Files.readAllBytes(dest.resolve("b.txt")), StandardCharsets.UTF_8));
    }

    @Test
    public void UnzipWritesNothingWhenOneEntryLeavesTheFolderTest() throws Exception {
        Path zip = zipWith("evil.zip", "good.txt", "good", "../escaped.txt", "bad");
        Path dest = tmp.resolve("out");

        IOException e = Assertions.assertThrows(IOException.class, () -> Access.unzip(zip.toString(), dest.toString()));

        Assertions.assertTrue(e.getMessage().contains("Blocked zip entry"), e.getMessage());
        Assertions.assertFalse(Files.exists(tmp.resolve("escaped.txt")), "the entry escaped the folder");
        Assertions.assertFalse(Files.exists(dest.resolve("good.txt")), "a file was written before the check");
    }
}
