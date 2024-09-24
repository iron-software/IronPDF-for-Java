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
     * Get IronPdfEngineConnection to set up various connection modes for IronPdfEngine.
     *
     * <p>The supported connection modes are:
     * <p>- SUBPROCESS: (Default) Launch a local IronPdfEngine as a subprocess.
     * <p>- HOST_PORT: Connect to IronPdfEngine using a host and port.
     * <p>- TARGET: Connect to IronPdfEngine using a target string (e.g., DNS-based resolution).
     * <p>- OFFICIAL_CLOUD: Connect to an official cloud-based IronPdfEngine. (require subscription License)
     * <p>- CUSTOM: Connect to IronPdfEngine using a custom gRPC connection.
     * <p> Example usage:
     * <pre>
     * {@code IronPdfEngineConnection settings = IronPdfEngineConnection.configure()
     *      .withHostPort("localhost", 33350); }
     * </pre>
     * <P>Get IronPDF engine docker @see <a href="https://hub.docker.com/r/ironsoftwareofficial/ironpdfengine">official-ironpdfengine-docker</a></p>
     */
    public static IronPdfEngineConnection getConnectionMode(){ return Setting_Api.connectionMode; }

    /**
     * Set IronPdfEngineConnection to set up various connection modes for IronPdfEngine.
     *
     * <p>The supported connection modes are:
     * <p>- SUBPROCESS: Launch a local IronPdfEngine as a subprocess.
     * <p>- HOST_PORT: Connect to IronPdfEngine using a host and port.
     * <p>- TARGET: Connect to IronPdfEngine using a target string (e.g., DNS-based resolution).
     * <p>- OFFICIAL_CLOUD: (Default for IronPDF for Java Cloud) Connect to an official cloud-based IronPdfEngine. (require subscription License)
     * <p>- CUSTOM: Connect to IronPdfEngine using a custom gRPC connection.
     * <p> Example usage:
     * <pre>
     * {@code IronPdfEngineConnection settings = IronPdfEngineConnection.configure()
     *      .withHostPort("localhost", 33350); }
     * </pre>
     * <P>Get IronPDF engine docker @see <a href="https://hub.docker.com/r/ironsoftwareofficial/ironpdfengine">official-ironpdfengine-docker</a></p>
     */
    public static void setConnectionMode(IronPdfEngineConnection ironPdfEngineConnection) {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = ironPdfEngineConnection; }

    /**
     * A Timeout (in seconds) to wait for any response from IronPdfEngine
     * default 120 seconds
     * @return IronPdfEngineTimeout (seconds)
     */
    public static int getIronPdfEngineTimeout() {
        return Setting_Api.ironPdfEngineTimeout;
    }

    /**
     * A Timeout (in seconds) to wait for any response from IronPdfEngine
     * default 120 seconds
     * @param ironPdfEngineTimeout (seconds)
     */
    public static void setIronPdfEngineTimeout(int ironPdfEngineTimeout) {
        Setting_Api.ironPdfEngineTimeout = ironPdfEngineTimeout;
    }

    //region ---------------------SubProcess options---------------------

    /**
     * Is debug boolean.
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return true if debug mode is enabled.
     */
    public static boolean isDebug() {
        return Setting_Api.enableDebug;
    }

    /**
     * Enable or disable debug mode.
     * <p>***Needed to set before calling any IronPdf function.***</p>
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     *
     * @param isDebug is debug enabled
     */
    public static void setDebug(boolean isDebug) {
        Setting_Api.enableDebug = isDebug;
    }

    /**
     * Gets path to IronPDF engine working directory. default is current directory.
     * If IronPdfEngine binary does not exist, We will download automatically to this folder.
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return custom IronPdfEngine folder.
     */
    public static Path getIronPdfEngineWorkingDirectory() {
        return Setting_Api.ironPdfEngineWorkingDirectory;
    }

    /**
     * Sets path to IronPDF engine working directory. default is current directory.
     * If IronPdfEngine binary does not exist, We will download automatically to this folder.
     * <p>***Needed to set before calling any IronPdf function.***
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param path the path
     * @throws IOException the io exception
     */
    public static void setIronPdfEngineWorkingDirectory(Path path) throws IOException {
        Files.createDirectories(path);
        Setting_Api.ironPdfEngineWorkingDirectory = path;
    }

    /**
     * Gets IronPDF engine log file path.
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return the log file path
     */
    public static Path getLogPath() {
        return Setting_Api.logPath;
    }

    /**
     * Sets IronPDF engine log file path.
     * <p>***Recommended to set before calling any IronPdf function.***
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param path the log file path
     */
    public static void setLogPath(Path path) {
        Setting_Api.logPath = path;
    }

    /**
     * Set temp folder path.
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param tempFolderPath the temp folder path
     */
    public static void setTempFolderPath(Path tempFolderPath) {
        Setting_Api.tempFolderPath = tempFolderPath;
    }

    /**
     * Get temp folder path.
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return the path
     */
    public static Path getTempFolderPath() {
        return Setting_Api.tempFolderPath;
    }

    /**
     * SingleProcess mode Forces Chrome renderer to perform everything in the current process, rather than using subprocesses
     * default is false
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return true if SingleProcess mode is enabled.
     */
    public static boolean isSingleProcess() {
        return Setting_Api.singleProcess;
    }

    /**
     * SingleProcess mode Forces Chrome renderer to perform everything in the current process, rather than using subprocesses
     * default is false
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param isSingleProcess is SingleProcess enabled
     */
    public static void setSingleProcess(boolean isSingleProcess) {
        Setting_Api.singleProcess = isSingleProcess;
    }

    /**
     * Set Maximum number of concurrent browsers when using the Chrome renderer
     * default is 30
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param chromeBrowserLimit
     */
    public static void setChromeBrowserLimit(int chromeBrowserLimit) { Setting_Api.chromeBrowserLimit = chromeBrowserLimit; }

    /**
     * Get Maximum number of concurrent browsers when using the Chrome renderer
     * default is 30
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return chromeBrowserLimit
     */
    public static int getChromeBrowserLimit() {
        return Setting_Api.chromeBrowserLimit;
    }

    /**
     * Disk cache path for Chrome browser instances
     * default null
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return chromeBrowserCachePath.
     */
    public static Path getChromeBrowserCachePath() {
        return Setting_Api.chromeBrowserCachePath;
    }

    /**
     * Disk cache path for Chrome browser instances
     * default null
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param chromeBrowserCachePath
     * @throws IOException the io exception
     */
    public static void setChromeBrowserCachePath(Path chromeBrowserCachePath) throws IOException {
        Files.createDirectories(chromeBrowserCachePath);
        Setting_Api.chromeBrowserCachePath = chromeBrowserCachePath;
    }

    /**
     * Chrome renderer GPU compatibility mode. In special environment like Docker or Cloud Service please use ChromeGpuModes.Disabled
     * default null
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return chromeBrowserCachePath.
     */
    public static ChromeGpuModes getChromeGpuMode() {
        return ChromeGpuModes.values()[Setting_Api.chromeGpuMode];
    }

    /**
     * Chrome renderer GPU compatibility mode. In special environment like Docker or Cloud Service please use ChromeGpuModes.Disabled
     * default Disabled
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param chromeGpuMode
     * @throws IOException the io exception
     */
    public static void setChromeGpuMode(ChromeGpuModes chromeGpuMode) throws IOException {
        Setting_Api.chromeGpuMode = chromeGpuMode.ordinal();
    }

    /**
     * If true The necessary package dependencies for IronPDF rendering will we automatically installed to
     * Docker and Debian / Ubuntu Linux deployments.
     * This will take a few minutes the next time you run IronPDF.
     * Set this to false if manual Docker / Linux setup is more convenient:
     * <a href="https://ironpdf.com/docs/questions/docker-linux/">more info</a>
     *
     * default is true
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @return true if AutoInstallDependency mode is enabled.
     */
    public static boolean isAutoInstallDependency() {
        return Setting_Api.linuxAndDockerAutoConfig;
    }

    /**
     * If true The necessary package dependencies for IronPDF rendering will we automatically installed to
     * Docker and Debian / Ubuntu Linux deployments.
     * This will take a few minutes the next time you run IronPDF.
     * Set this to false if manual Docker / Linux setup is more convenient:
     * <a href="https://ironpdf.com/docs/questions/docker-linux/">more info</a>
     *
     * default is true
     * <p>***Only works with {@link IronPdfEngineConnection#withSubprocess()}</p>
     * @param isAutoInstallDependency is SingleProcess enabled
     */
    public static void setAutoInstallDependency(boolean isAutoInstallDependency) {
        Setting_Api.linuxAndDockerAutoConfig = isAutoInstallDependency;
    }

    /**
     * Chrome GPU hardware utilization when rendering HTML to PDF
     */
    public enum ChromeGpuModes {

        /**
         * Disable GPU hardware utilization
         */
        Disabled,
        /**
         * Enable software acceleration
         */
        Software,
        /**
         * Enable hardware acceleration
         */
        Hardware,
        /**
         * Enable hardware acceleration with Vulkan features
         */
        HardwareFull
    }
    //endregion

    //region ---------------------Deprecated---------------------
    /**
     * @deprecated This method is obsolete. Use {@link #setConnectionMode(IronPdfEngineConnection)} instead.
     */
    @Deprecated
    public static void setIronPdfEngineHost(String host) {
        Setting_Api.useDeprecatedConnectionSettings = true;
        Setting_Api.subProcessHost = host;
    }

    /**
     * @deprecated This method is obsolete. Use {@link #getConnectionMode()} instead.
     */
    @Deprecated
    public static String getIronPdfEngineHost() {
        return Setting_Api.subProcessHost;
    }

    /**
     * @deprecated This method is obsolete. Use {@link #setConnectionMode(IronPdfEngineConnection)} instead.
     */
    @Deprecated
    public static void setIronPdfEnginePort(int port) {
        Setting_Api.useDeprecatedConnectionSettings = true;
        Setting_Api.subProcessPort = port;
    }

    /**
     * @deprecated This method is obsolete. Use {@link #getConnectionMode()} instead.
     */
    @Deprecated
    public static int getIronPdfEnginePort() { return Setting_Api.subProcessPort; }

    /**
     * @deprecated This method is obsolete. Use {@link #setConnectionMode(IronPdfEngineConnection)} instead.
     */
    @Deprecated
    public static void useIronPdfEngineDocker() {
        Setting_Api.useDeprecatedConnectionSettings = true;
        Setting_Api.useIronPdfEngineDocker(33350);
    }

    @Deprecated
    /**
     * @deprecated This method is obsolete. Use {@link #setConnectionMode(IronPdfEngineConnection)} instead.
     */
    public static void useIronPdfEngineDocker(int port) {
        Setting_Api.useDeprecatedConnectionSettings = true;
        Setting_Api.useIronPdfEngineDocker(port);
    }
    //endregion
}