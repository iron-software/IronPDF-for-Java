package com.ironsoftware.ironpdf.internal.staticapi;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public final class Setting_Api {

    static final Logger logger = LoggerFactory.getLogger(Setting_Api.class);

    //will use unique port numbers to avoid conflicts with other instances of IronPdf
    public static int subProcessPort = findFreePort();
    public static String licenseKey = "";
    public static boolean enableDebug = false;
    /**
     * Path to IronPdfEngine folder, default is current
     * {currentDirectory}/IronPdfEngine.{revision}.{osName}.{osArch}/ . If IronPdfEngine binary does
     * not exist, it will download automatically to this folder.
     */
    public static Path ironPdfEngineFolder = Paths.get(System.getProperty("user.dir"),
            getIronPdfEngineFolderName());
    public static Path defaultPathToIronPdfEngineFolder = Paths.get(getIronPdfEngineFolderName());
    public static String subProcessIp = "127.0.0.1";

    private static int findFreePort() {
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

    public static Optional<File> getAvailableIronPdfEngineFile() {
        try {
            //find IronEngine from ironpdf-engine-os-arch maven package
            URL resourceUrl = Class.forName(
                    "com.ironsoftware.ironpdf.internal.EngineResource" + currentOsFullName()
                            + currentOsArch()).getResource("/" + getIronPdfEngineFolderName());
            Path fp = Paths.get(Paths.get(Objects.requireNonNull(resourceUrl).toURI()).toString(),
                    getIronPdfEngineFileName());
            if (Files.exists(fp)) {
                logger.info(
                        "IronPdfEngine found (from ironpdf-engine package) (EngineResource) at: " + fp);
                return Optional.of(fp.toFile());
            }
        } catch (Exception ignored) {
        }

        try {
            if (Files.exists(getCustomIronPdfEnginePath())) {
                logger.info("IronPdfEngine found at: " + getCustomIronPdfEnginePath());
                return Optional.of(getCustomIronPdfEnginePath().toFile());
            }
        } catch (Exception ignored) {
        }
        logger.info("IronPdfEngine not found at: " + getCustomIronPdfEnginePath());

        try {
            if (Files.exists(getDefaultIronPdfEnginePath())) {
                logger.info("IronPdfEngine found at: " + getDefaultIronPdfEnginePath());
                return Optional.of(getDefaultIronPdfEnginePath().toFile());
            }
        } catch (Exception ignored) {
        }
        logger.info("IronPdfEngine not found at: " + getDefaultIronPdfEnginePath());

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

    private static String getIronPdfEngineFileName() {
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

    public static Path getCustomIronPdfEnginePath() {
        return Paths.get(ironPdfEngineFolder.toAbsolutePath().toString(), getIronPdfEngineFileName());
    }

    public static Path getDefaultIronPdfEnginePath() {
        return Paths.get(getIronPdfEngineFolderName(), getIronPdfEngineFileName());
    }

    public static final String IRON_PDF_ENGINE_VERSION = "2022.11.10253";

}