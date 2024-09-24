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
}