package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.Setting_Api;

import java.nio.file.Path;

/**
 * Important global settings for configuration of IronPDF for Java
 */
public final class Settings {

    /**
     * Gets iron pdf engine version.
     *
     * @return The current IronPdfEngine version
     */
    @SuppressWarnings("SameReturnValue")
    public static String getIronPdfEngineVersion() {
        return Setting_Api.IRON_PDF_ENGINE_VERSION;
    }


    /**
     * Is debug boolean.
     *
     * @return true if debug mode is enabled.
     */
    public static boolean isDebug() {
        return Setting_Api.enableDebug;
    }

    /**
     * Enable or disable debug mode.
     * <p>***Please set before calling any IronPdf function.***
     *
     * @param isDebug the is debug enabled
     */
    public static void setDebug(boolean isDebug) {
        Setting_Api.enableDebug = isDebug;
    }

    /**
     * We will look for IronPdfEngine (binaries) in this folder first.
     * If binaries are not present or are of an older version, the software will automatically try to download all the latest required binaries into this folder.
     *
     * @return custom IronPdfEngine folder.
     */
    public static Path getIronPdfEngineFolder() {
        return Setting_Api.ironPdfEngineFolder;
    }

    /**
     * Sets a custom IronPdfEngine folder path. We will look for IronPdfEngine (binaries) in this folder first.
     * <p>***Please set before calling any IronPdf function.***
     * If binaries are not present or are of an older version, the software will automatically try to download all the latest required binaries into this folder.
     *
     * @param path the path
     */
    public static void setIronPdfEngineFolder(Path path) {
        Setting_Api.ironPdfEngineFolder = path;
    }

    /**
     * Gets IronPdfEngine log file path.
     *
     * @return the log file path
     */
    public static Path getLogPath() {
        return Setting_Api.logPath;
    }

    /**
     * Sets IronPdfEngine log file path.
     * <p>***Please set before calling any IronPdf function.***
     *
     * @param path the log file path
     */
    public static void setLogPath(Path path) {
        Setting_Api.logPath = path;
    }

    /**
     * Set iron pdf engine host.
     *
     * @param host the host
     */
    public static void setIronPdfEngineHost(String host){
        Setting_Api.subProcessHost = host;
    }

    /**
     * Get IronPdfEngine host string.
     *
     * @return the host string
     */
    public static String getIronPdfEngineHost(){
        return Setting_Api.subProcessHost;
    }

    /**
     * Set IronPdfEngine port. Default port will pick automatically.
     *
     * @param port the port
     */
    public static void setIronPdfEnginePort(int port){
        Setting_Api.subProcessPort = port;
    }

    /**
     * Get IronPdfEngine port int. Default port will pick automatically.
     *
     * @return the port int
     */
    public static int getIronPdfEnginePort(){
        return Setting_Api.subProcessPort;
    }
}
