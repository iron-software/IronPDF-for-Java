package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.Setting_Api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Important global settings for configuration of IronPDF for Java
 */
public final class Settings {

    /**
     * Gets IronPDF engine version.
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
     * <p>***Recommended to set before calling any IronPdf function.***
     *
     * @param isDebug the is debug enabled
     */
    public static void setDebug(boolean isDebug) {
        Setting_Api.enableDebug = isDebug;
    }

    /**
     * Gets path to IronPDF engine working directory. default is current directory.
     * If IronPdfEngine binary does not exist, We will download automatically to this folder.
     * @return custom IronPdfEngine folder.
     */
    public static Path getIronPdfEngineWorkingDirectory() {
        return Setting_Api.ironPdfEngineWorkingDirectory;
    }

    /**
     * Sets path to IronPDF engine working directory. default is current directory.
     * If IronPdfEngine binary does not exist, We will download automatically to this folder.
     * <p>***Recommended to set before calling any IronPdf function.***
     *
     * @param path the path
     * @throws IOException the io exception
     */
    public static void setIronPdfEngineWorkingDirectory(Path path) throws IOException {
        Files.createDirectories(path);
        Setting_Api.ironPdfEngineWorkingDirectory = path;
    }

    /**
     * Gets IronPDF engine log file path.
     *
     * @return the log file path
     */
    public static Path getLogPath() {
        return Setting_Api.logPath;
    }

    /**
     * Sets IronPDF engine log file path.
     * <p>***Recommended to set before calling any IronPdf function.***
     *
     * @param path the log file path
     */
    public static void setLogPath(Path path) {
        Setting_Api.logPath = path;
    }

    /**
     * Set IronPDF engine host.
     *
     * @param host the host
     */
    public static void setIronPdfEngineHost(String host) {
        Setting_Api.subProcessHost = host;
    }

    /**
     * Get IronPDF engine host string.
     *
     * @return the host string
     */
    public static String getIronPdfEngineHost() {
        return Setting_Api.subProcessHost;
    }

    /**
     * Set IronPdf engine port. Default port will pick automatically.
     *
     * @param port the port
     */
    public static void setIronPdfEnginePort(int port) {
        Setting_Api.subProcessPort = port;
    }

    /**
     * Get IronPdf engine port int. Default port will pick automatically.
     *
     * @return the port int
     */
    public static int getIronPdfEnginePort() {
        return Setting_Api.subProcessPort;
    }


    /**
     * Set temp folder path.
     *
     * @param tempFolderPath the temp folder path
     */
    public static void setTempFolderPath(Path tempFolderPath) {
        Setting_Api.tempFolderPath = tempFolderPath;
    }


    /**
     * Get temp folder path.
     *
     * @return the path
     */
    public static Path getTempFolderPath() {
        return Setting_Api.tempFolderPath;
    }

    /**
     * Use IronPDF engine docker. All PDF operation will happen on the connected IronPDF engine docker.
     * This setting required IronPdfEngine is already up and running in docker.  (with default port)
     * License key need to set as a Docker environment.
     * <p> Get IronPDF engine docker @see <a href="https://hub.docker.com/r/ironsoftwareofficial/ironpdfengine">official-ironpdfengine-docker</a>}
     */
    public static void useIronPdfEngineDocker(){
        Setting_Api.useIronPdfEngineDocker(33350);

}

    /**
     * Use IronPDF engine docker. All PDF operation will happen on the connected IronPDF engine docker.
     * This setting required IronPdfEngine is already up and running in docker. (on the given port)
     * License key need to set as a Docker environment.
     * <p> Get IronPDF engine docker @see <a href="https://hub.docker.com/r/ironsoftwareofficial/ironpdfengine">official-ironpdfengine-docker</a>}
     * @param port Set the port for IronPdfEngineDocker to listen on
     */
    public static void useIronPdfEngineDocker(int port){
        Setting_Api.useIronPdfEngineDocker(port);
    }
}
