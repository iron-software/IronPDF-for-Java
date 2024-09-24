package com.ironsoftware.ironpdf;


import io.grpc.ManagedChannel;

/**
 * IronPdfEngineConnection is a configuration class used to set up various connection
 * modes for IronPdfEngine. It provides a fluent API to configure different connection
 * methods such as Host/Port, Target, Subprocess, or a predefined Cloud-based service.
 *
 * <p>The supported connection modes are:
 * <p>- SUBPROCESS: (Default) Launch a local IronPdfEngine as a subprocess.
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
public class IronPdfEngineConnection {
    public enum ConnectionMode {
        SUBPROCESS, HOST_PORT, TARGET, OFFICIAL_CLOUD, CUSTOM
    }
    private String host;
    private int port;
    private String target;
    private ConnectionMode mode; // Use enum to define connection mode
    private ManagedChannel channel;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String DEFAULT_OFFICIAL_CLOUD_TARGET = "grpc.ironpdfservice.com:443";
    private static final int DEFAULT_PORT = 33350;  // Default port for subprocess if not dynamically set

    /**
     * Static factory method to initiate the configuration of IronPdfEngineConnection.
     * This method serves as the entry point for setting up the connection configuration.
     *
     * Example:
     * <pre>
     * {@code
     * IronPdfEngineConnection settings = IronPdfEngineConnection.configure()
     *      .withHostPort("localhost", 8080);
     * }
     * </pre>
     *
     * @return IronPdfEngineConnection The instance of the settings to be configured.
     */
    public static IronPdfEngineConnection configure() {
        return new IronPdfEngineConnection();
    }

    /**
     * <P>Recommended for hosting IronPdfEngine Docker in local network.</p>
     * <P>Set IronPdfEngine connection mode using host and port.</p>
     * <P>This mode allows connecting to an already running IronPdfEngine.</p>
     * <P></p>
     * <P>Use IronPDF engine docker. All PDF operation will happen on the connected IronPDF engine docker.</p>
     * <P>This setting required IronPdfEngine is already up and running in docker. (on the given port)</p>
     * <P>Get IronPDF engine docker @see <a href="https://hub.docker.com/r/ironsoftwareofficial/ironpdfengine">official-ironpdfengine-docker</a>}</p>
     * <P></p>
     * @param host The hostname or IP address of the IronPdfEngine.
     * @param port The port number where the IronPdfEngine is running.
     * @throws IllegalArgumentException if the host is null or empty, or if the port is out of range.
     */
    public IronPdfEngineConnection withHostPort(String host, int port) {
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Host cannot be null or empty.");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535.");
        }
        this.host = host;
        this.port = port;
        this.target = null;
        this.mode = ConnectionMode.HOST_PORT;
        this.channel = null;
        return this;
    }

    /**
     * <P>Recommended for hosting IronPdfEngine Docker in Cloud or remote server.</p>
     * <P>Set IronPdfEngine connection mode using a target string.</p>
     * <P>This mode is typically used for service discovery mechanisms (e.g., DNS, load balancing).</p>
     *
     * @param target The target string (e.g., "dns:///my-service:8080").
     */
    public IronPdfEngineConnection withTarget(String target) {
        this.target = target;
        this.host = null;
        this.port = -1;
        this.mode = ConnectionMode.TARGET;
        return this;
    }

    /**
     * <P>Default use-case. IronPDF Java will spawn IronPdfEngine as a local subprocess.</p>
     * <P>Launch a IronPdfEngine as a subprocess.</p>
     * <P>The subprocess will be launched with a predefined host (localhost) and port (33350).</p>
     *
     * <P>To use another port {@link #withSubprocess(int)}</p>
     */
    public IronPdfEngineConnection withSubprocess() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
        this.target = null;
        this.mode = ConnectionMode.SUBPROCESS;
        this.channel = null;
        return this;
    }

    /**
     * <P>Recommended if port 33350 is not available.</p>
     * <P>Launch a IronPdfEngine as a subprocess with a custom port.</p>
     * <P>The subprocess will be launched with a predefined host (localhost) and custom port.</p>
     *
     * @param port The port number where the IronPdfEngine subprocess will be used.
     */
    public IronPdfEngineConnection withSubprocess(int port) {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
        this.target = null;
        this.mode = ConnectionMode.SUBPROCESS;
        this.channel = null;
        return this;
    }

    /**
     * <P>Configures IronPDF Java to use an official cloud-based IronPdfEngine.</p>
     * <P>Only works for Subscription license. For more information please contact sales@ironsoftware.com</p>
     */
    public IronPdfEngineConnection withOfficialCloud() {
        this.target = DEFAULT_OFFICIAL_CLOUD_TARGET;
        this.host = null;
        this.port = -1;
        this.mode = ConnectionMode.OFFICIAL_CLOUD;
        this.channel = null;
        return this;
    }

    public IronPdfEngineConnection withCustomGrpcConnection(ManagedChannel customChannel) {
        this.channel = customChannel;
        this.target = null;
        this.host = null;
        this.port = -1;
        this.mode = ConnectionMode.CUSTOM;
        return this;
    }

    /**
     * Retrieves the host name of the gRPC server.
     *
     * @return String The hostname or IP address of the IronPdfEngine.
     */
    public String getHost() {
        return host;
    }

    /**
     * Retrieves the port number of the IronPdfEngine.
     *
     * @return int The port number where the IronPdfEngine server is running.
     */
    public int getPort() {
        return port;
    }

    /**
     * Retrieves the target string used for service discovery (if applicable).
     *
     * @return String The target string (e.g., "dns:///my-service:8080").
     */
    public String getTarget() {
        return target;
    }

    /**
     * Retrieves the current connection mode (HOST_PORT, TARGET, SUBPROCESS, or CUSTOM).
     *
     * @return ConnectionMode The connection mode being used by the settings.
     */
    public ConnectionMode getMode() {
        return mode;
    }

    /**
     * Retrieves custom channel.
     *
     * @return ManagedChannel custom channel.
     */
    public ManagedChannel getCustomChannel() {
        return channel;
    }

    /**
     * Provides a string representation of the current IronPdfEngine settings.
     * Displays either the host/port, target, or subprocess details based on the mode.
     *
     * @return String A description of the current IronPdfEngine connection settings.
     */
    @Override
    public String toString() {
        switch (mode) {
            case HOST_PORT:
                return "IronPdfEngine Host: " + host + ", Port: " + port;
            case TARGET:
                return "IronPdfEngine Target: " + target;
            case SUBPROCESS:
                return "IronPdfEngine launched as subprocess on port: " + port;
            case OFFICIAL_CLOUD:
                return "IronPdfEngine official cloud Target: " + target;
            case CUSTOM:
                return "Custom connection";
            default:
                return "Unknown IronPdfEngine configuration. Host: " + host + ", Port: " + port + ", Target: " + target;
        }
    }
}
