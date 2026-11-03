package com.ironsoftware.ironpdf;


import io.grpc.ManagedChannel;

import java.nio.file.Files;
import java.nio.file.Path;

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
    private String authToken;
    private boolean tls;
    private boolean insecureTls;
    private Path trustedCertificate;
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
        this.port = port;
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
     * <P>Optional. Sets the authentication token for IronPdfEngine 2026.11 or later.</p>
     * <P>When a token is set, IronPDF sends the gRPC metadata
     * {@code authorization: Bearer <token>} with every call to IronPdfEngine.</p>
     * <P>Use it with an engine that was started with the environment variable
     * {@code IRONPDF_ENGINE_AUTH_TOKEN} (for example the IronPdfEngine Docker image).</p>
     * <P>In SUBPROCESS mode, IronPDF also gives the token to the IronPdfEngine subprocess
     * through the {@code IRONPDF_ENGINE_AUTH_TOKEN} environment variable of that subprocess.
     * IronPDF does not log the token.</p>
     * <P>If you do not call this method, IronPDF does not send the header and does not set the variable.
     * IronPdfEngine versions before 2026.11 ignore the header and the variable.</p>
     * <P>In CUSTOM mode, you can also add the header to your own channel with a {@code ClientInterceptor}.
     * Do not do both, because the engine then receives two {@code authorization} values.</p>
     *
     * @param authToken The token. Use {@code null} to remove a token that you set before.
     * @return IronPdfEngineConnection The same instance, for a fluent configuration.
     * @throws IllegalArgumentException if the token is empty, or if it contains a character that is not
     *                                  a visible ASCII character (for example a space).
     */
    public IronPdfEngineConnection withAuthToken(String authToken) {
        if (authToken == null) {
            this.authToken = null;
            return this;
        }
        if (authToken.isEmpty()) {
            throw new IllegalArgumentException("Auth token cannot be empty. Use null to remove the token.");
        }
        for (int i = 0; i < authToken.length(); i++) {
            char c = authToken.charAt(i);
            if (c < 0x21 || c > 0x7E) {
                throw new IllegalArgumentException(
                        "Auth token can contain only visible ASCII characters (no spaces or control characters).");
            }
        }
        this.authToken = authToken;
        return this;
    }

    /**
     * <P>Optional. Encrypts the connection to a remote IronPdfEngine 2026.11 or later with TLS.</p>
     * <P>IronPDF checks the engine certificate against the default trust store of the JVM.
     * Use this method when a public CA or a CA in the JVM trust store signed the engine certificate.
     * For a self-signed certificate or a private CA, use {@link #withTrustedCertificate(Path)}.</p>
     * <P>The engine must serve TLS. The IronPdfEngine operator turns on TLS with the environment variable
     * {@code IRONPDF_ENGINE_TLS_CERT_PATH}. The certificate must contain the host name or the IP address
     * that the client uses (subject alternative name).</p>
     * <P>Applies to the HOST_PORT and TARGET modes.
     * In SUBPROCESS mode, IronPDF starts the engine on 127.0.0.1 without a certificate: IronPDF logs a warning
     * and uses plaintext. OFFICIAL_CLOUD always uses TLS, and this method does not change it.
     * In CUSTOM mode, the security settings of your own channel apply.</p>
     * <P>If you do not call this method, HOST_PORT uses plaintext (h2c) as before. TARGET and OFFICIAL_CLOUD
     * always use TLS and always check the certificate against the JVM trust store.</p>
     *
     * <pre>
     * {@code
     * IronPdfEngineConnection connection = IronPdfEngineConnection.configure()
     *      .withHostPort("pdf-engine.example.com", 33350)
     *      .withTls()
     *      .withAuthToken(System.getenv("IRONPDF_ENGINE_AUTH_TOKEN"));
     * }
     * </pre>
     *
     * @return IronPdfEngineConnection The same instance, for a fluent configuration.
     */
    public IronPdfEngineConnection withTls() {
        this.tls = true;
        this.insecureTls = false;
        this.trustedCertificate = null;
        return this;
    }

    /**
     * <P>Optional. Encrypts the connection to a remote IronPdfEngine 2026.11 or later with TLS, and trusts only
     * the certificates in the given PEM file.</p>
     * <P>Use this method for an engine with a self-signed certificate (give the engine certificate) or with a
     * certificate from a private CA (give the CA certificate). IronPDF does not use the JVM trust store.
     * IronPDF still checks that the certificate contains the host name or the IP address that the client uses.</p>
     * <P>This method turns on TLS. See {@link #withTls()} for the modes to which TLS applies.</p>
     *
     * <pre>
     * {@code
     * IronPdfEngineConnection connection = IronPdfEngineConnection.configure()
     *      .withHostPort("10.0.0.5", 33350)
     *      .withTrustedCertificate(Paths.get("/etc/ironpdf/engine.crt"))
     *      .withAuthToken(System.getenv("IRONPDF_ENGINE_AUTH_TOKEN"));
     * }
     * </pre>
     *
     * @param pemFile A PEM file with one or more X.509 certificates. Do not give the private key.
     * @return IronPdfEngineConnection The same instance, for a fluent configuration.
     * @throws IllegalArgumentException if the path is null, or if the file does not exist.
     */
    public IronPdfEngineConnection withTrustedCertificate(Path pemFile) {
        if (pemFile == null) {
            throw new IllegalArgumentException("Trusted certificate file cannot be null. Use withTls() for the JVM trust store.");
        }
        if (!Files.isRegularFile(pemFile)) {
            throw new IllegalArgumentException("Trusted certificate file does not exist: " + pemFile);
        }
        this.tls = true;
        this.insecureTls = false;
        this.trustedCertificate = pemFile;
        return this;
    }

    /**
     * <P><b>Not recommended.</b> For the TARGET and OFFICIAL_CLOUD modes, uses TLS but does not check the
     * certificate of the server. This is how IronPDF Java before 2026.11.1 connected.</p>
     * <P>Anybody on the network path (for example a proxy, a router or a fake DNS answer) can then read
     * the HTML, the PDF data and the license key. Use it only as a short-term workaround, and prefer
     * {@link #withTrustedCertificate(Path)} or the JVM trust store.</p>
     * <P>IronPDF logs a warning each time it connects with this setting.
     * The system property {@code ironpdf.tls.insecure=true} or the environment variable
     * {@code IRONPDF_TLS_INSECURE=true} has the same effect without a code change.</p>
     * <P>It has no effect in the SUBPROCESS, HOST_PORT and CUSTOM modes.</p>
     *
     * @return IronPdfEngineConnection The same instance, for a fluent configuration.
     */
    public IronPdfEngineConnection withInsecureTls() {
        this.insecureTls = true;
        this.tls = false;
        this.trustedCertificate = null;
        return this;
    }

    /**
     * Tells if {@link #withInsecureTls()} turned off the certificate check for TARGET and OFFICIAL_CLOUD.
     *
     * @return boolean {@code true} when IronPDF does not check the certificate of the server.
     */
    public boolean isInsecureTls() {
        return insecureTls;
    }

    /**
     * Tells if {@link #withTls()} or {@link #withTrustedCertificate(Path)} turned on TLS.
     *
     * @return boolean {@code true} when TLS is turned on for a remote engine.
     */
    public boolean isTls() {
        return tls;
    }

    /**
     * Retrieves the PEM file that {@link #withTrustedCertificate(Path)} set.
     *
     * @return Path The PEM file, or {@code null} when IronPDF uses the JVM trust store.
     */
    public Path getTrustedCertificate() {
        return trustedCertificate;
    }

    /**
     * Retrieves the authentication token that {@link #withAuthToken(String)} set.
     *
     * @return String The token, or {@code null} when no token is set.
     */
    public String getAuthToken() {
        return authToken;
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
        return describeMode() + (tls ? ", tls=true" : "") + (insecureTls ? ", insecureTls=true" : "");
    }

    private String describeMode() {
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
