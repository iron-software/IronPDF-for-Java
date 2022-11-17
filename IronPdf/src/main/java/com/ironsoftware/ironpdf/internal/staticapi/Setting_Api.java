package com.ironsoftware.ironpdf.internal.staticapi;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.ironsoftware.ironpdf.internal.staticapi.Utils_Util.logInfoOrSystemOut;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

/**
 * The type Setting api.
 */
public final class Setting_Api {

    /**
     * The Logger.
     */
    static final Logger logger = LoggerFactory.getLogger(Setting_Api.class);

    /**
     * The constant subProcessPort.
     */
//will use unique port numbers to avoid conflicts with other instances of IronPdf
    public static int subProcessPort = findFreePort();
    /**
     * The constant licenseKey.
     */
    public static String licenseKey = "";
    /**
     * The constant enableDebug.
     */
    public static boolean enableDebug = false;

    /**
     * Path to IronPdfEngine log.
     */
    public static Path logPath = Paths.get("ironpdfengine.log");
    /**
     * Path to IronPdfEngine folder, default is current
     * {currentDirectory}/IronPdfEngine.{revision}.{osName}.{osArch}/ . If IronPdfEngine binary does
     * not exist, it will download automatically to this folder.
     */
    public static Path ironPdfEngineFolder = Paths.get(System.getProperty("user.dir"),
            getIronPdfEngineFolderName());
    /**
     * The constant defaultPathToIronPdfEngineFolder.
     */
    public final static Path ironPdfEngineDefaultFolder = Paths.get(getIronPdfEngineFolderName());
    /**
     * The constant subProcessHost.
     */
    public static String subProcessHost = "127.0.0.1";

    /**
     * Find free port int.
     *
     * @return the int
     */
    public static int findFreePort() {
        //InetAddress must be "127.0.0.1" to prevent firewall popups
        try (ServerSocket socket = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            socket.close();
            return port;
        } catch (IOException ignored) {
        }
        throw new IllegalStateException("Could not find a free port to start IronPdfEngine");
    }

    /**
     * Gets available iron pdf engine file.
     *
     * @return the available iron pdf engine file
     */
    public static Optional<File> getAvailableIronPdfEngineFile() {

        try {
            if (Files.exists(getIronPdfEngineExecutablePath())) {
                logger.info("IronPdfEngine found at: " + getIronPdfEngineExecutablePath());
                return Optional.of(getIronPdfEngineExecutablePath().toFile());
            }
        } catch (Exception ignored) {
        }
        logger.info("IronPdfEngine not found at: " + getIronPdfEngineExecutablePath());

        try {
            try (InputStream ad = Class.forName(
                    "com.ironsoftware.ironpdf.internal.EngineResource" + currentOsFullName()
                            + currentOsArch()).getResourceAsStream("/" + getIronPdfEngineFolderName() + ".zip")) {

                Path zipFilePath = Paths.get(Setting_Api.ironPdfEngineDefaultFolder + ".zip");
                copyInputStreamToFile(ad, zipFilePath.toFile());

                logInfoOrSystemOut(logger, "Unzipping IronPdfEngine (from dependency) to dir: " + Setting_Api.ironPdfEngineFolder.toAbsolutePath());

                Access.unzip(zipFilePath.toAbsolutePath().toString(),
                        Setting_Api.ironPdfEngineFolder.toAbsolutePath().toString());
                Files.deleteIfExists(zipFilePath.toAbsolutePath());
            }

            Path fp = getIronPdfEngineExecutablePath();
            if (Files.exists(fp)) {
                logger.info(
                        "IronPdfEngine found (from ironpdf-engine package) (EngineResource) at: " + fp);
                return Optional.of(fp.toFile());
            }
        } catch (ClassNotFoundException ignored) {

        } catch (Exception e) {
            logger.debug("IronPdfEngine from ironpdf-engine package not found", e);
        }

        try {
            if (Files.exists(getIronPdfEngineExecutableDefaultPath())) {
                logger.info("IronPdfEngine found at: " + getIronPdfEngineExecutableDefaultPath());
                return Optional.of(getIronPdfEngineExecutableDefaultPath().toFile());
            }
        } catch (Exception ignored) {
        }
        logger.info("IronPdfEngine not found at Default: " + getIronPdfEngineExecutableDefaultPath());

        try {
            Path dir = Paths.get(System.getProperty("user.dir"), getIronPdfEngineFolderName());
            if (Files.exists(dir)) {
                logger.info("IronPdfEngine found at: " + dir);
                return Optional.of(dir.toFile());
            }
        } catch (Exception ignored) {
        }
        logger.info("IronPdfEngine not found at: " + "System.getProperty(\"user.dir\")/"
                + getIronPdfEngineFolderName());
        return Optional.empty();
    }

    /**
     * Current os full name string.
     *
     * @return the string
     */
    static String currentOsFullName() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "Windows";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "Linux";
        } else if (SystemUtils.IS_OS_MAC) {
            //todo also check for M1
            return "MacOS";
        }
        throw new RuntimeException("unknown OS:" + SystemUtils.OS_NAME);
    }

    /**
     * Current os arch string.
     *
     * @return the string
     */
    static String currentOsArch() {
        if (SystemUtils.IS_OS_WINDOWS) {
            if (System.getProperty("os.arch").equalsIgnoreCase("x86")) {
                return "x86";
            } else {
                return "x64";
            }
        } else if (SystemUtils.IS_OS_LINUX) {
            return "x64";
        } else if (SystemUtils.IS_OS_MAC) {
            //todo also check for M1
            return "x64";
        }
        throw new RuntimeException("unknown OS:" + SystemUtils.OS_NAME);
    }

    private static String getIronPdfEngineFolderName() {
        return "IronPdfEngine." +
                IRON_PDF_ENGINE_VERSION +
                "." +
                currentOsFullName() +
                "." +
                currentOsArch();
    }

    private static String getIronPdfEngineExecutableFileName() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "IronPdfEngine.exe";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "IronPdfEngine";
        } else if (SystemUtils.IS_OS_MAC) {
            //todo also check for M1
            return "IronPdfEngine";
        } else {
            //default, should not reach
            return "IronPdfEngine";
        }
    }

    /**
     * Gets custom iron pdf engine path.
     *
     * @return the custom iron pdf engine path
     */
    public static Path getIronPdfEngineExecutablePath() {
        return Paths.get(ironPdfEngineFolder.toAbsolutePath().toString(), getIronPdfEngineExecutableFileName());
    }

    /**
     * Gets default iron pdf engine path.
     *
     * @return the default iron pdf engine path
     */
    public static Path getIronPdfEngineExecutableDefaultPath() {
        return Paths.get(getIronPdfEngineFolderName(), getIronPdfEngineExecutableFileName());
    }

    /**
     * The constant IRON_PDF_ENGINE_VERSION.
     */
    public static final String IRON_PDF_ENGINE_VERSION = "2022.11.10577";

}
