package com.ironsoftware.ironpdf.internal.staticapi;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    static boolean isIronPdfEngineDocker = false;

    /**
     * Path to IronPdfEngine log.
     */
    public static Path logPath = Paths.get("ironpdfengine.log");
    /**
     * Path to IronPdfEngine working directory. default is current directory.
     * If IronPdfEngine binary does not exist, we will download automatically to this folder.
     */
    public static Path ironPdfEngineWorkingDirectory = Paths.get(System.getProperty("user.dir"));
    /**
     * The constant subProcessHost.
     */
    public static String subProcessHost = "127.0.0.1";

    public static Path tempFolderPath = null;

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

    public static String getIronPdfEngineFolderName() {
        return "IronPdfEngine." +
                IRON_PDF_ENGINE_VERSION +
                "." +
                currentOsFullName() +
                "." +
                currentOsArch();
    }

    public static String getIronPdfEngineZipName() {
        return getIronPdfEngineFolderName() + ".zip";
    }

    public static String getIronPdfEngineExecutableFileName() {
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
    public static Path getIronPdfEngineExecutablePath(Path workingDir) {
        return Paths.get(workingDir.toAbsolutePath().toString()
                , getIronPdfEngineFolderName()
                , getIronPdfEngineExecutableFileName());
    }


    public static void useIronPdfEngineDocker(int port){
        logger.info("Using IronPdfEngine Docker port:" +port);
        subProcessPort = port;
        isIronPdfEngineDocker = true;
    }

    /**
     * The constant IRON_PDF_ENGINE_VERSION.
     */
    public static final String IRON_PDF_ENGINE_VERSION = "2023.1.11674";

}
