package com.ironsoftware.ironpdf.internal.staticapi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks a downloaded IronPdfEngine zip against the SHA-256 that this release contains, before the client
 * unzips and runs it. The engine zip comes over HTTPS only, so a certificate that the JVM wrongly trusts
 * (see the eMudhra CA incident) could otherwise swap the engine.
 */
final class EngineDownloadIntegrity {
    private static final Logger logger = LoggerFactory.getLogger(EngineDownloadIntegrity.class);

    static final String ALLOW_UNVERIFIED_PROPERTY = "ironpdf.engine.allowUnverifiedDownload";
    static final String ALLOW_UNVERIFIED_ENV = "IRONPDF_ENGINE_ALLOW_UNVERIFIED_DOWNLOAD";

    /** SHA-256 of each engine zip, by engine version, then by "Platform.arch". */
    private static final Map<String, Map<String, String>> ENGINE_ZIP_SHA256 = new HashMap<>();

    static {
        Map<String, String> v20261101 = new HashMap<>();
        v20261101.put("Windows.x64",
                "8221d6226c60c63dd69a751ac5e52896370041b18b485751409bc50656ca8d94");
        v20261101.put("Windows.x86",
                "87e08137d7e156907fc5304ef57c306cc47d66db098f0c2171d8d56635199006");
        v20261101.put("Linux.x64",
                "03a28d9e4dfc2eb99cc28ffeaef08266f7d79eb9fa4f12676f3f9910a9123856");
        v20261101.put("MacOS.arm64",
                "ae0044727d7974744cf8c1b27502272ee305981d7c06945b44d89faa1fc05008");
        v20261101.put("MacOS.x64",
                "26c5ca6ab679fe27818f2ee307a5210e27cfe00c117b2ce4a87e0dec824ead3e");
        ENGINE_ZIP_SHA256.put("2026.11.1", Collections.unmodifiableMap(v20261101));
    }

    private EngineDownloadIntegrity() {
    }

    static String expectedSha256(String version, String platform, String arch) {
        return expectedSha256(ENGINE_ZIP_SHA256, version, platform, arch);
    }

    static String expectedSha256(Map<String, Map<String, String>> table, String version, String platform, String arch) {
        Map<String, String> perPlatform = table.get(version);
        return perPlatform == null ? null : perPlatform.get(platform + "." + arch);
    }

    /**
     * Tells if the user allows an engine download that the client cannot verify.
     * The setting in {@link com.ironsoftware.ironpdf.Settings} has priority over the system property and the
     * environment variable.
     */
    static boolean allowUnverified() {
        if (Setting_Api.allowUnverifiedEngineDownload != null) {
            return Setting_Api.allowUnverifiedEngineDownload;
        }
        String value = System.getProperty(ALLOW_UNVERIFIED_PROPERTY);
        if (value == null) {
            value = System.getenv(ALLOW_UNVERIFIED_ENV);
        }
        return value != null && value.trim().equalsIgnoreCase("true");
    }

    static String sha256Of(Path file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream in = Files.newInputStream(file)) {
                byte[] buffer = new byte[1 << 16];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }
            StringBuilder hex = new StringBuilder();
            for (byte b : digest.digest()) {
                hex.append(String.format(Locale.ROOT, "%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available in this JVM", e);
        }
    }

    static void verify(Path zip, String version, String platform, String arch) throws IOException {
        verify(ENGINE_ZIP_SHA256, zip, version, platform, arch, allowUnverified());
    }

    /**
     * Throws when the zip differs from the expected SHA-256, or when the table has no SHA-256 for the engine
     * version and platform, unless {@code allowUnverified} is true. Then it only logs a warning. A zip that differs
     * from a known SHA-256 is refused in every case.
     *
     * @throws IOException with a clear message when the zip must not be used
     */
    static void verify(Map<String, Map<String, String>> table, Path zip, String version, String platform, String arch,
                       boolean allowUnverified) throws IOException {
        String what = "IronPdfEngine " + version + " " + platform + " " + arch;
        String expected = expectedSha256(table, version, platform, arch);
        if (expected == null) {
            if (allowUnverified) {
                logger.warn("No SHA-256 is known for " + what + ". The download is not verified because "
                        + "Settings.setAllowUnverifiedEngineDownload, -D" + ALLOW_UNVERIFIED_PROPERTY + " or "
                        + ALLOW_UNVERIFIED_ENV + " allows it.");
                return;
            }
            throw new IOException("Cannot verify the " + what + " download: this IronPDF release has no SHA-256 for it. "
                    + "Add the ironpdf-engine-<platform> Maven dependency, use the engine version this release was built for, "
                    + "or allow an unverified engine with Settings.setAllowUnverifiedEngineDownload(true) "
                    + "(or " + ALLOW_UNVERIFIED_ENV + "=true).");
        }
        String actual = sha256Of(zip);
        if (!actual.equalsIgnoreCase(expected)) {
            // allowUnverified covers only a version without a known SHA-256. A known SHA-256 that does not match is
            // always refused, so a process-wide setting cannot let a changed download run.
            throw new IOException("The " + what + " download has SHA-256 " + actual + ", but this IronPDF release expects "
                    + expected + ". IronPDF did not unzip or run it. The download may be corrupt or changed on the network.");
        }
    }

    /**
     * Returns the path of a zip entry inside {@code destDir}, or throws when the entry would leave it
     * (for example {@code ../x} or an absolute path).
     */
    static Path resolveSafeEntry(Path destDir, String entryName) throws IOException {
        Path root = destDir.toAbsolutePath().normalize();
        Path target = root.resolve(entryName).normalize();
        if (!target.startsWith(root)) {
            throw new IOException("Blocked zip entry outside the engine folder: " + entryName);
        }
        return target;
    }
}
