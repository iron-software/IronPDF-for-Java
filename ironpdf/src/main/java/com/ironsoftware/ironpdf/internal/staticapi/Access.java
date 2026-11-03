package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.IronPdfEngineConnection;
import com.ironsoftware.ironpdf.internal.proto.HandshakeRequestP;
import com.ironsoftware.ironpdf.internal.proto.HandshakeResponseP;
import io.grpc.ChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.TlsChannelCredentials;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.okhttp.OkHttpChannelBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.ironsoftware.ironpdf.internal.staticapi.Setting_Api.*;
import static com.ironsoftware.ironpdf.internal.staticapi.Utils_Util.logInfoOrSystemOut;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

final class Access {
    static final Logger logger = LoggerFactory.getLogger(Access.class);
    static final Logger engineLogger = LoggerFactory.getLogger("com.ironsoftware.ironpdf.IronPdfEngine");
    static RpcClient client = null;
    static boolean isTryDownloaded = false;
    private static ManagedChannel channel = null;
    private static Process ironPdfProcess = null;
    private static boolean tryAgain = true;
    private static BufferedReader stdInput;
    private static BufferedReader stdError;
    private static CountDownLatch serverReady;
    private static final int MAX_RETRY_ATTEMPTS = 20;

    private static ManagedChannel createChannel(){
        int attempts = 0;
        Exception lastException = new Exception("unknown reason");
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                if (useDeprecatedConnectionSettings) {
                    //remove this in the future
                    return ManagedChannelBuilder.forAddress(Setting_Api.subProcessHost,
                            Setting_Api.subProcessPort).usePlaintext().build();
                } else {
                    logger.info("Connecting to " + Setting_Api.connectionMode.toString());
                    return buildChannel(Setting_Api.connectionMode);
                }
            } catch (IllegalArgumentException e) {
                // A bad TLS setting (for example a PEM file that IronPDF cannot read) does not go away with a retry.
                throw e;
            } catch (Exception e) {
                lastException = e;
                attempts++;
                logger.info("Failed to connect IronPdfEngine. (Retry "+attempts+"/"+MAX_RETRY_ATTEMPTS+")");
                try {
                    TimeUnit.SECONDS.sleep(2); // Wait for 2 seconds before retrying
                } catch (InterruptedException ignored) {
                }
            }
        }
        if(useDeprecatedConnectionSettings){
            throw new RuntimeException(String.format("Cannot connected to IronPdfEngine: %s:%d", subProcessHost, subProcessPort), lastException);
        }else {
            throw new RuntimeException(String.format("Cannot connected to %s", Setting_Api.connectionMode.toString()), lastException);
        }
    }

    /**
     * Builds the gRPC channel for a connection setting.
     *
     * <p>Without {@link IronPdfEngineConnection#withTls()} or
     * {@link IronPdfEngineConnection#withTrustedCertificate(Path)}, SUBPROCESS and HOST_PORT use plaintext, as before.
     * TARGET and OFFICIAL_CLOUD always use TLS with a certificate check against the JVM trust store (or the PEM file
     * of {@code withTrustedCertificate} for TARGET). Only {@link IronPdfEngineConnection#withInsecureTls()}, the system
     * property {@code ironpdf.tls.insecure} or the environment variable {@code IRONPDF_TLS_INSECURE} turn the check off.</p>
     */
    static ManagedChannel buildChannel(IronPdfEngineConnection connection) throws Exception {
        String warning = tlsWarning(connection);
        if (warning != null) {
            logger.warn(warning);
        }
        boolean tls = tlsApplies(connection);
        switch (connection.getMode()) {
            case SUBPROCESS:
            case HOST_PORT:
                if (tls) {
                    ChannelCredentials credentials = tlsCredentials(connection);
                    if (isAndroid()) {
                        return OkHttpChannelBuilder.forAddress(connection.getHost(), connection.getPort(), credentials)
                                .build();
                    }
                    return NettyChannelBuilder.forAddress(connection.getHost(), connection.getPort(), credentials)
                            .build();
                }
                if(isAndroid()){
                    return OkHttpChannelBuilder.forAddress(connection.getHost(), connection.getPort())
                            .usePlaintext().build();
                } else {
                    return ManagedChannelBuilder.forAddress(connection.getHost(), connection.getPort())
                            .usePlaintext().build();
                }

            case TARGET:
            case OFFICIAL_CLOUD:
                if (!insecureTlsRequested(connection)) {
                    // The certificate and the host name are checked. A PEM file of the user applies to TARGET only.
                    ChannelCredentials credentials = tls ? tlsCredentials(connection) : TlsChannelCredentials.create();
                    if (isAndroid()) {
                        return OkHttpChannelBuilder.forTarget(connection.getTarget(), credentials).build();
                    }
                    return NettyChannelBuilder.forTarget(connection.getTarget(), credentials).build();
                }
                logger.warn("IronPDF does not check the TLS certificate of " + connection.getTarget() + " "
                        + "(withInsecureTls, ironpdf.tls.insecure or IRONPDF_TLS_INSECURE). Anybody on the network path can read "
                        + "the HTML, the PDF data and the license key. Use withTrustedCertificate or the JVM trust store instead.");
                if(isAndroid()){
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, InsecureTrustManagerFactory.INSTANCE.getTrustManagers(), null);
                    return OkHttpChannelBuilder.forTarget(connection.getTarget())
                            .sslSocketFactory(sslContext.getSocketFactory())  // Set the SSL socket factory
                            .build();
                }else{
                    return NettyChannelBuilder.forTarget(connection.getTarget())
                            .negotiationType(NegotiationType.TLS)
                            .useTransportSecurity()
                            .sslContext(GrpcSslContexts.forClient()
                                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build())
                            .build();
                }
            case CUSTOM:
                return connection.getCustomChannel();
            default:
                throw new IllegalStateException("Unknown IronPdfEngine connection mode: " + connection.getMode());
        }
    }

    /**
     * Tells if the certificate check of TARGET and OFFICIAL_CLOUD is turned off on purpose.
     *
     * <p>The system property and the environment variable apply to the whole process, so they do not override a TLS
     * setting that the code made for this connection ({@code withTls} or {@code withTrustedCertificate}).</p>
     */
    static boolean insecureTlsRequested(IronPdfEngineConnection connection) {
        if (connection != null && connection.isInsecureTls()) {
            return true;
        }
        if (tlsApplies(connection)) {
            return false;
        }
        String value = System.getProperty("ironpdf.tls.insecure");
        if (value == null) {
            value = System.getenv("IRONPDF_TLS_INSECURE");
        }
        return value != null && value.trim().equalsIgnoreCase("true");
    }

    /**
     * Tells if the client uses the TLS settings of the user for this connection.
     *
     * <p>The TLS settings apply only to a remote engine that the user names: HOST_PORT and TARGET.
     * In SUBPROCESS mode, the client starts IronPdfEngine on 127.0.0.1 without a certificate.
     * OFFICIAL_CLOUD keeps its own TLS setting. In CUSTOM mode, the channel of the user applies.</p>
     */
    static boolean tlsApplies(IronPdfEngineConnection connection) {
        if (connection == null || !connection.isTls()) {
            return false;
        }
        return connection.getMode() == IronPdfEngineConnection.ConnectionMode.HOST_PORT
                || connection.getMode() == IronPdfEngineConnection.ConnectionMode.TARGET;
    }

    /**
     * Gets the warning for TLS settings that do not apply to the connection mode.
     *
     * @return the warning text, or {@code null} when there is nothing to tell.
     */
    static String tlsWarning(IronPdfEngineConnection connection) {
        if (connection == null || !connection.isTls()) {
            return null;
        }
        switch (connection.getMode()) {
            case SUBPROCESS:
                return "IronPdfEngineConnection.withTls/withTrustedCertificate applies only to a remote IronPdfEngine. "
                        + "In SUBPROCESS mode, IronPDF starts IronPdfEngine on 127.0.0.1 without a certificate "
                        + "and uses plaintext.";
            case OFFICIAL_CLOUD:
                return "IronPdfEngineConnection.withTls/withTrustedCertificate does not change the OFFICIAL_CLOUD "
                        + "connection. The official cloud connection always uses TLS.";
            case CUSTOM:
                return "IronPdfEngineConnection.withTls/withTrustedCertificate does not apply to a custom gRPC channel. "
                        + "Set TLS on your own channel.";
            default:
                return null;
        }
    }

    private static ChannelCredentials tlsCredentials(IronPdfEngineConnection connection) {
        Path pemFile = connection.getTrustedCertificate();
        if (pemFile == null) {
            // The JVM default trust store.
            return TlsChannelCredentials.create();
        }
        try {
            return TlsChannelCredentials.newBuilder().trustManager(pemFile.toFile()).build();
        } catch (IOException | RuntimeException e) {
            throw new IllegalArgumentException("Cannot read the trusted certificate file " + pemFile
                    + ". Give a PEM file with one or more X.509 certificates.", e);
        }
    }

    private static HandshakeResponseP handshakeWithRetry(RpcClient newClient){
        HandshakeRequestP.Builder handshakeRequest = HandshakeRequestP.newBuilder();
        handshakeRequest.setExpectedVersion(Setting_Api.IRON_PDF_ENGINE_VERSION);
        handshakeRequest.setProgLang("java");

        int attempts = 0;
        Exception lastException = new Exception("unknown reason");
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try{
                return newClient.GetBlockingStub("handshake").handshake(
                        handshakeRequest.build());
            }catch (Exception exception){
                lastException=exception;
                attempts++;
                logger.info("Waiting for IronPdfEngine is ready. (Retry "+attempts+"/"+MAX_RETRY_ATTEMPTS+")");
                try {
                    TimeUnit.SECONDS.sleep(4); // Wait for 4 seconds before retrying
                } catch (InterruptedException ignored1) {
                }
            }
        }
        throw new RuntimeException(String.format("Cannot handshake with IronPdfEngine: %s:%d due to:", subProcessHost, subProcessPort), lastException);
    }

    static synchronized RpcClient ensureConnection() {
        if (client != null) {
            return client;
        }

        if (useDeprecatedConnectionSettings) {
            if (!isIronPdfEngineDocker) {
                startServer();
            }
        } else if (Setting_Api.connectionMode.getMode() == IronPdfEngineConnection.ConnectionMode.SUBPROCESS) {
            startServer();
        }

        if (channel == null) {
            channel = createChannel();
        }

        RpcClient newClient = new RpcClient(channel);

        logger.debug("Handshaking, Expected IronPdfEngine Version : " + Setting_Api.IRON_PDF_ENGINE_VERSION);

        HandshakeResponseP handshakeResult = handshakeWithRetry(newClient);

        logger.debug("Handshake result:" + handshakeResult);

        switch (handshakeResult.getResultOrExceptionCase()) {
            case SUCCESS:
                client = newClient;
                setLicenseKey();
                return client;
            case REQUIREDVERSION:
                logger.warn(String.format("Mismatch IronPdfEngine version expected: %s but found: %s", Setting_Api.IRON_PDF_ENGINE_VERSION, handshakeResult.getRequiredVersion()));
//                //todo download new Binary
                if (tryAgain) {
                    //try download if it is a subprocess mode
                    if (useDeprecatedConnectionSettings) {
                        if (!isIronPdfEngineDocker) {
                            tryAgain = false;
                            stopIronPdfEngine();
                            downloadIronPdfEngine();
                            return ensureConnection();
                        }
                    } else if (Setting_Api.connectionMode.getMode() == IronPdfEngineConnection.ConnectionMode.SUBPROCESS) {
                        tryAgain = false;
                        stopIronPdfEngine();
                        downloadIronPdfEngine();
                        return ensureConnection();
                    }
                }
                client = newClient;
                setLicenseKey();
                return client;

            case EXCEPTION:
                throw Exception_Converter.fromProto(handshakeResult.getException());
            default:
                throw new RuntimeException("Unexpected result from handshake");
        }
    }

    static final String ENGINE_AUTH_TOKEN_ENV = "IRONPDF_ENGINE_AUTH_TOKEN";

    /**
     * Gets the IronPdfEngine authentication token that the user set with
     * {@link IronPdfEngineConnection#withAuthToken(String)}.
     *
     * <p>The deprecated host and port settings do not use {@link Setting_Api#connectionMode},
     * so they never have a token.</p>
     *
     * @return the token, or {@code null} when the user did not set one.
     */
    static String engineAuthToken() {
        if (useDeprecatedConnectionSettings || connectionMode == null) {
            return null;
        }
        return connectionMode.getAuthToken();
    }

    /**
     * Puts the opt-in authentication token into the environment of an IronPdfEngine subprocess.
     *
     * <p>The token goes into the environment and not into the command line, because the
     * command line is visible in the process list and in the IronPdfEngine start-up log.
     * IronPdfEngine 2026.11 refuses {@code auth_token=} on the command line.</p>
     *
     * <p>With no token, this method removes an {@code IRONPDF_ENGINE_AUTH_TOKEN} value that the subprocess
     * inherits from the JVM environment. Without a token, the client sends no {@code authorization} header.
     * IronPdfEngine 2026.11 would then refuse every call from the client that started it.
     * Engines before 2026.11 ignore the variable, so the removal does not change their behaviour.</p>
     *
     * @param environment the environment of the subprocess (for example {@link ProcessBuilder#environment()}).
     * @param authToken   the token, or {@code null}.
     */
    static void applyEngineAuthTokenToEnvironment(Map<String, String> environment, String authToken) {
        if (authToken != null) {
            environment.put(ENGINE_AUTH_TOKEN_ENV, authToken);
        } else if (environment.remove(ENGINE_AUTH_TOKEN_ENV) != null) {
            // Never log the value.
            logger.warn("The JVM environment has " + ENGINE_AUTH_TOKEN_ENV + ", but no token is set with "
                    + "IronPdfEngineConnection.withAuthToken. IronPDF does not give the variable to the "
                    + "IronPdfEngine subprocess, because the client would not send the token.");
        }
    }

    static final String ENGINE_LICENSE_KEY_ENV = "IRONPDF_ENGINE_LICENSE_KEY";

    /**
     * Gets the license key of the client.
     *
     * <p>The key comes from {@link Setting_Api#licenseKey}. When that value is empty, the key comes from
     * {@code IRONPDF_LICENSE_KEY} in {@code config.properties}.</p>
     *
     * @return the key, or {@code null} when no key is set.
     */
    static String licenseKey() {
        String lk = Utils_StringHelper.isNullOrWhiteSpace(Setting_Api.licenseKey) ?
                new ConfigLoader().getProperty("IRONPDF_LICENSE_KEY") : Setting_Api.licenseKey;
        return Utils_StringHelper.isNullOrWhiteSpace(lk) ? null : lk;
    }

    /**
     * Puts the license key into the environment of an IronPdfEngine subprocess.
     *
     * <p>The key goes into the environment and not into the command line, because the
     * command line is visible in the process list and in the IronPdfEngine start-up log.
     * IronPdfEngine 2026.11 and later read {@code IRONPDF_ENGINE_LICENSE_KEY} at start-up.
     * Earlier engines ignore the variable. For all engines, {@link #setLicenseKey()} also sends
     * the key over gRPC after each successful handshake.</p>
     *
     * <p>With a key, this method replaces a value that the subprocess inherits from the JVM environment.
     * With no key, this method does not change the environment.</p>
     *
     * @param environment the environment of the subprocess (for example {@link ProcessBuilder#environment()}).
     * @param licenseKey  the key, or {@code null}.
     */
    static void applyLicenseKeyToEnvironment(Map<String, String> environment, String licenseKey) {
        if (!Utils_StringHelper.isNullOrWhiteSpace(licenseKey)) {
            environment.put(ENGINE_LICENSE_KEY_ENV, licenseKey);
        }
    }

    static void setLicenseKey() {
        //Set License
        String lk = licenseKey();
        if (lk != null)
            License_Api.SetLicensed(lk);
    }

    static synchronized void downloadIronPdfEngine() {
        try {
            if (isTryDownloaded) {
                return;
            }
            Path zipFilePath = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(),
                    Setting_Api.getIronPdfEngineZipName());

            logInfoOrSystemOut(logger, "Download IronPdfEngine to working dir: " + zipFilePath.toAbsolutePath());
            logInfoOrSystemOut(logger, "You can skip this downloading step by adding IronPdfEngine as a Maven dependency. see: https://github.com/iron-software/IronPDF-for-Java#install-ironpdf-engine-as-a-maven-dependency");
            isTryDownloaded = true;

            URL downloadUrl = new URL("https://ironpdfengine.azurewebsites.net/api/IronPdfEngineDownload?version="
                    + Setting_Api.IRON_PDF_ENGINE_VERSION +
                    "&platform=" + Setting_Api.currentOsFullName() +
                    "&architect=" + Setting_Api.currentOsArch());
            try (InputStream stream = downloadUrl.openStream()) {

                long downloadSize = stream.available();
                try (DownloadInputStream pis = new DownloadInputStream(stream, downloadSize, logger)) {
                    copyInputStreamToFile(pis, zipFilePath.toFile());
                }
            }

            // The client runs this engine, so it must be the one this release knows.
            try {
                EngineDownloadIntegrity.verify(zipFilePath, Setting_Api.IRON_PDF_ENGINE_VERSION,
                        Setting_Api.currentOsFullName(), Setting_Api.currentOsArch());
            } catch (IOException e) {
                Files.deleteIfExists(zipFilePath);
                throw e;
            }

            Path unzippedFolder = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(), Setting_Api.getIronPdfEngineFolderName());
            logInfoOrSystemOut(logger, "Unzipping IronPdfEngine to dir: " + unzippedFolder);
            unzip(zipFilePath.toAbsolutePath().toString(),
                    unzippedFolder.toAbsolutePath().toString());
            logger.info("Delete zip file: " + zipFilePath.toAbsolutePath());
            Files.deleteIfExists(zipFilePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to download IronPdfEngine binary", e);
            throw new RuntimeException("Failed to download IronPdfEngine binary", e);
        }
    }

    static void unzip(String zipFilePath, String destDir) throws IOException {
        try (java.util.zip.ZipFile zipFile = new ZipFile(zipFilePath)) {
            Path destRoot = Paths.get(destDir);
            // Check every entry before the first file is written, so a bad zip leaves nothing behind.
            for (Enumeration<? extends ZipEntry> check = zipFile.entries(); check.hasMoreElements(); ) {
                EngineDownloadIntegrity.resolveSafeEntry(destRoot, check.nextElement().getName());
            }
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = EngineDownloadIntegrity.resolveSafeEntry(destRoot, entry.getName()).toFile();
                if (entry.isDirectory()) {
                    Files.createDirectories(entryDestination.toPath());
                    setPermission(entryDestination);
                } else {
                    File parentDir = entryDestination.getParentFile();
                    Files.createDirectories(parentDir.toPath());
                    setPermission(parentDir);
                    try (InputStream in = zipFile.getInputStream(entry);
                         OutputStream out = Files.newOutputStream(entryDestination.toPath())) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        }
    }

    /**
     * Builds the command line that starts an IronPdfEngine subprocess.
     *
     * <p>{@code serverHost} is the address that the client connects to (127.0.0.1 by default).
     * When it is {@code localhost} or an IP address, IronPdfEngine 2026.11 and later listen only
     * on that address. Thus other machines cannot connect to a subprocess on 127.0.0.1.
     * Earlier IronPdfEngine versions ignore the argument and listen on all interfaces.</p>
     *
     * <p>Do not put secrets on this command line. The license key and the authentication token
     * go into the environment, see {@link #applyLicenseKeyToEnvironment(Map, String)} and
     * {@link #applyEngineAuthTokenToEnvironment(Map, String)}.</p>
     *
     * @param executablePath the absolute path of the IronPdfEngine executable.
     * @param serverHost     the address that the subprocess listens on and that the client connects to.
     * @param serverPort     the port that the subprocess listens on.
     * @return the command and its arguments.
     */
    static List<String> buildEngineCommand(String executablePath, String serverHost, int serverPort) {
        List<String> cmdList = new ArrayList<>();

        cmdList.add(executablePath);
        cmdList.add("host=" + serverHost);
        cmdList.add("port=" + serverPort);
        cmdList.add("enable_debug=" + Setting_Api.enableDebug);
        cmdList.add("log_path=" + Setting_Api.logPath);
        cmdList.add("programming_language=" + "java");
        cmdList.add("single_process=" + (Setting_Api.singleProcess || currentOsFullName().equalsIgnoreCase("MacOS")));
        cmdList.add("docker_build=false");
        cmdList.add("linux_and_docker_auto_config=" + Setting_Api.linuxAndDockerAutoConfig);
        cmdList.add("skip_initialization=false");
        cmdList.add("chrome_browser_limit=" + Setting_Api.chromeBrowserLimit);
        if (chromeBrowserCachePath != null)
            cmdList.add("chrome_cache_path=" + Setting_Api.chromeBrowserCachePath.toAbsolutePath());
        cmdList.add("chrome_gpu_mode=" + Setting_Api.chromeGpuMode);

        if (Setting_Api.jobQueueWatchdogTimeoutSeconds > 0) {
            cmdList.add("job_queue_watchdog_timeout_seconds=" + Setting_Api.jobQueueWatchdogTimeoutSeconds);
        }

        if (Setting_Api.tempFolderPath != null)
            cmdList.add("temp_folder_path=" + Setting_Api.tempFolderPath.toAbsolutePath());

        return cmdList;
    }

    static synchronized void startServer() {
        try {

            String serverHost = useDeprecatedConnectionSettings ? Setting_Api.subProcessHost : connectionMode.getHost();
            int serverPort = useDeprecatedConnectionSettings ? Setting_Api.subProcessPort : connectionMode.getPort();

            Optional<File> selectedFile = getAvailableIronPdfEngineFile();
            if (selectedFile.isPresent()) {
                logger.info("Using IronPdfEngine from: " + selectedFile.get().getAbsolutePath());

                try {
//                    logger.info("Try Setting IronPdfEngine permission...");
                    try {
                        File parentDir = selectedFile.get().getParentFile();
                        Files.createDirectories(parentDir.toPath());
                        setPermission(parentDir);
                    } catch (Exception e) {
                        logger.warn("Cannot set IronPdfEngine parent folder permission ", e);
                    }

                    Arrays.stream(Objects.requireNonNull(selectedFile.get().getParentFile().listFiles()))
                            .forEach(file -> {
                                try {
                                    setPermission(file);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (Exception e) {
                    logger.warn("Cannot set IronPdfEngine files permission ", e);
                }

                List<String> cmdList = buildEngineCommand(
                        selectedFile.get().toPath().toAbsolutePath().toString(), serverHost, serverPort);
                serverReady = new CountDownLatch(1);
                ProcessBuilder pb = new ProcessBuilder(cmdList);
                // Opt-in only: no token is set unless the user called IronPdfEngineConnection.withAuthToken.
                // Without a token, this call also removes an inherited IRONPDF_ENGINE_AUTH_TOKEN.
                applyEngineAuthTokenToEnvironment(pb.environment(), engineAuthToken());
                // The license key goes into the environment, never into the command line.
                applyLicenseKeyToEnvironment(pb.environment(), licenseKey());

                logger.info("Start IronPdfEngine");
                if (enableDebug) {
                    logger.debug("options: " + cmdList);
                }

                Process proc = pb.start();
                catchServerMessage(proc);

                //This addShutdownHook will run when ...
                // * When all of JVM threads have completed execution
                // * System.exit()
                // * CTRL-C
                // * System level shutdown or User Log-Off
                //BUT IT WILL ***NOT*** RUN WHEN
                // * VM crashes
                // * OS kill command
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    shutdownChanel();
                    logger.info("Shutdown IronPdfEngine process");
                    proc.destroy();
                }));

                //wait for IronPdfEngine finish initialize
                //not throw timeout exception because we will try to connect IronPdfEngine on first method called.
                boolean ignored = serverReady.await(60, TimeUnit.SECONDS);

                ironPdfProcess = proc;

            } else {
                logger.debug(
                        "Cannot find IronPdfEngine from ironPdf working dir:" + Setting_Api.ironPdfEngineWorkingDirectory
                                .toAbsolutePath());
                if (tryAgain) {
                    downloadIronPdfEngine();
                    logger.info("Try to start IronPdfEngine again");
                    tryAgain = false;
                    startServer();
                } else {
                    throw new RuntimeException(String.format("Cannot locate IronPdfEngine. at %1$s",
                            Setting_Api.getIronPdfEngineExecutablePath(ironPdfEngineWorkingDirectory)
                                    .toAbsolutePath())
                            + " An alternative approach is to install one of ironpdf-engine packages https://search.maven.org/search?q=ironpdf%20engine, more information: https://github.com/iron-software/IronPDF-for-Java#install-ironpdf-engine-as-a-maven-dependency");
                }
            }
        } catch (Exception e) {
            logger.error("Cannot start IronPdfEngine (working dir: " + ironPdfEngineWorkingDirectory.toAbsolutePath() + ")", e);
            e.printStackTrace();
            throw new RuntimeException("Cannot start IronPdfEngine due to " + e.getMessage(), e);
        }
    }

    private static void catchServerMessage(Process proc) {

        stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        Thread threadInput = new Thread(() -> {
            engineLogger.debug("listening IronPdfEngine");
            // Read the output from the command
            stdInput.lines().forEach(line -> {
                if (line.trim().equalsIgnoreCase("IronPdfEngine is up")) {
                    serverReady.countDown();
                }
                if (Setting_Api.enableDebug)
                    engineLogger.info("[IronPdfEngine]" + line);
            });

        });
        //Note: Whenever the last non-daemon thread terminates, all the daemon threads will be terminated automatically.
        threadInput.setDaemon(true);
        threadInput.start();

        stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        Thread threadError = new Thread(() -> {
            // Read the error from the command
            stdError.lines().forEach(line -> {
               //do nothing
            });
        });

        //Note: Whenever the last non-daemon thread terminates, all the daemon threads will be terminated automatically.
        threadError.setDaemon(true);
        threadError.start();
    }

    private static void shutdownChanel() {
        if (channel != null && !channel.isShutdown() && !channel.isTerminated()) {
            channel.shutdown();
            channel = null;
        }
    }

    /**
     * Stop IronPdfEngine process.
     *
     * <p>{@code synchronized} on the same {@code Access} monitor as
     * {@link #ensureConnection()} so a watchdog thread stopping the engine
     * cannot interleave with a worker thread mid-connect and leave a live
     * client over a torn-down channel. The JVM shutdown hook does not call this
     * method (it invokes {@link #shutdownChanel()}/{@code proc.destroy()}
     * directly), so this introduces no shutdown deadlock.</p>
     */
    public static synchronized void stopIronPdfEngine() {
        shutdownChanel();

        if (ironPdfProcess != null) {
            ironPdfProcess.destroy();
        }
        ironPdfProcess = null;

        client = null;
    }

    /**
     * Checks whether IronPdfEngine is currently reachable and responsive.
     *
     * <p>Unlike {@link #ensureConnection()} this never starts a subprocess and
     * never blocks on the long retry loops. It performs a single, short-deadline
     * handshake against the existing connection. This makes it safe to poll as a
     * lightweight health check.</p>
     *
     * <p>Liveness is decided by the handshake, not by the launched process
     * handle: depending on the IronPdfEngine version the locally launched
     * process can be a short-lived launcher that exits while the engine keeps
     * serving, so {@code ironPdfProcess.isAlive()} is not a reliable signal.</p>
     *
     * @return {@code true} if a connection exists and the engine answers a
     * handshake within the deadline; {@code false} otherwise (never connected,
     * channel closed, or engine unresponsive).
     */
    static synchronized boolean isIronPdfEngineActive() {
        // Never connected yet -> not active. Do not auto-connect here.
        if (client == null) {
            return false;
        }

        if (channel == null || channel.isShutdown() || channel.isTerminated()) {
            return false;
        }

        try {
            HandshakeRequestP.Builder handshakeRequest = HandshakeRequestP.newBuilder();
            handshakeRequest.setExpectedVersion(Setting_Api.IRON_PDF_ENGINE_VERSION);
            handshakeRequest.setProgLang("java");

            client.GetBlockingStub("handshake")
                    .withDeadlineAfter(5, TimeUnit.SECONDS)
                    .handshake(handshakeRequest.build());
            return true;
        } catch (Exception e) {
            logger.debug("IronPdfEngine active check failed", e);
            return false;
        }
    }

    /**
     * Stops IronPdfEngine (if running) and establishes a fresh connection.
     *
     * <p>This is the recovery path for an engine that has been interrupted by an
     * external event (host restart, OS {@code kill}, VM crash). After such an
     * event the cached client is stale and {@link #ensureConnection()} alone
     * would keep returning the dead client, so the state is fully reset first.</p>
     *
     * <ul>
     *   <li>In {@code SUBPROCESS} mode a new IronPdfEngine subprocess is launched.</li>
     *   <li>In remote modes ({@code HOST_PORT}, {@code TARGET}, {@code OFFICIAL_CLOUD})
     *       the channel is rebuilt and a new handshake is performed against the
     *       (re)started server.</li>
     * </ul>
     *
     * <p>{@code CUSTOM} mode is not supported here: the gRPC channel is owned by
     * the caller and cannot be rebuilt once shut down. Callers are guarded against
     * this in {@link Engine_Api#restartEngine()}.</p>
     */
    static synchronized void restartIronPdfEngine() {
        logInfoOrSystemOut(logger, "Restarting IronPdfEngine");
        stopIronPdfEngine();
        ensureConnection();
    }

    private static void setPermission(File file) {
        try {
            boolean setExecutablePermissionResult = file.setExecutable(true, false);
            boolean setWritablePermissionResult = file.setWritable(true, false);
            boolean setReadablePermissionResult = file.setReadable(true, false);
            logger.debug(file.getAbsolutePath() + " permission status:" +
                    System.lineSeparator() +
                    " Executable:" + setExecutablePermissionResult +
                    System.lineSeparator() +
                    " Writable:" + setWritablePermissionResult +
                    System.lineSeparator() +
                    " Readable:" + setReadablePermissionResult
            );
        } catch (Exception exception) {
            logger.warn("Set permission failed : " + file.getAbsolutePath(), exception);
        }
    }

    /**
     * Gets available iron pdf engine file.
     *
     * @return the available iron pdf engine file
     */
    private static Optional<File> getAvailableIronPdfEngineFile() {

        Path binFromWorkingDir = getIronPdfEngineExecutablePath(ironPdfEngineWorkingDirectory);
        try {
            if (Files.exists(binFromWorkingDir)) {
                logger.info("IronPdfEngine found at: " + binFromWorkingDir);
                return Optional.of(binFromWorkingDir.toFile());
            }
        } catch (Exception ignored) {
        }
        logger.debug("IronPdfEngine not found at IronPdfEngine working directory: " + binFromWorkingDir.toAbsolutePath());

        //check for ironpdf-engine package
        try {
            String engineResourceClassName = "com.ironsoftware.ironpdf.internal.EngineResource" + currentOsFullName() + currentOsArch();
            try (InputStream inputStream = Class.forName(engineResourceClassName)
                    .getResourceAsStream("/" + getIronPdfEngineFolderName() + ".zip")) {

                Path zipFilePath = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(), Setting_Api.getIronPdfEngineFolderName() + ".zip");
                copyInputStreamToFile(inputStream, zipFilePath.toFile());

                Path unzipTargetPath = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(), Setting_Api.getIronPdfEngineFolderName() + "/");
                logInfoOrSystemOut(logger, "Unzipping IronPdfEngine (from dependency) to dir: " + unzipTargetPath);

                Access.unzip(zipFilePath.toAbsolutePath().toString(),
                        unzipTargetPath.toAbsolutePath().toString());
                Files.deleteIfExists(zipFilePath.toAbsolutePath());
            }

            if (Files.exists(binFromWorkingDir)) {
                logger.info(
                        "IronPdfEngine found (extracted from ironpdf-engine package) (EngineResource) at: " + binFromWorkingDir);
                return Optional.of(binFromWorkingDir.toFile());
            }
        } catch (ClassNotFoundException ignored) {
            //it is ok if engineResourceClassName not exists (e.g. ironpdf-engine-linux-x64 package)
            logger.debug("Cannot detect IronPdfEngine from ironpdf-engine package, skipped");
        } catch (Exception e) {
            logger.debug("IronPdfEngine from ironpdf-engine package not found", e);
        }

        //backup by look at System.getProperty("user.dir")
        Path binFromUserDir = getIronPdfEngineExecutablePath(Paths.get(System.getProperty("user.dir")));
        try {
            if (Files.exists(binFromUserDir)) {
                logger.info("IronPdfEngine found at: " + binFromUserDir);
                return Optional.of(binFromUserDir.toFile());
            }
        } catch (Exception ignored) {
        }
        logger.debug("IronPdfEngine not found at: " + "(System.getProperty(\"user.dir\")): " + binFromUserDir.toAbsolutePath());

        //backup by look at current dir
        Path binFromCurrentDir = getIronPdfEngineExecutablePath(Paths.get("."));
        try {
            if (Files.exists(binFromCurrentDir)) {
                logger.info("IronPdfEngine found at: " + binFromCurrentDir);
                return Optional.of(binFromCurrentDir.toFile());
            }
        } catch (Exception ignored) {
        }
        logger.debug("IronPdfEngine not found at current dir: " + binFromCurrentDir.toAbsolutePath());


        return Optional.empty();
    }

    public static boolean isAndroid() {
        try {
            // Try to load an Android-specific class
            Class.forName("android.os.Build");
            return true;  // It's Android if this class is found
        } catch (ClassNotFoundException e) {
            return false; // Not Android
        }
    }
}
