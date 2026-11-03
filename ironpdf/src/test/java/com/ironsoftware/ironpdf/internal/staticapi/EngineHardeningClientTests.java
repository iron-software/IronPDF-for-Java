package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.IronPdfEngineConnection;
import com.ironsoftware.ironpdf.internal.proto.ChromeRenderPdfDocumentFromHtmlFileRequestStreamP;
import com.ironsoftware.ironpdf.internal.proto.ChromeRenderPdfDocumentFromHtmlRequestStreamP;
import com.ironsoftware.ironpdf.internal.proto.EmptyP;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.IronPdfServiceGrpc;
import com.ironsoftware.ironpdf.internal.proto.PdfDocumentP;
import com.ironsoftware.ironpdf.internal.proto.PdfDocumentResultP;
import com.ironsoftware.ironpdf.internal.proto.RemoteExceptionP;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.TlsServerCredentials;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Client-side tests for the IronPdfEngine hardening settings (iron-software/IronPdf#1118).
 * These tests do not start IronPdfEngine. A fake engine on 127.0.0.1 takes the place of IronPdfEngine.
 */
public class EngineHardeningClientTests {

    private static final String REFUSAL =
            "Rendering an HTML file by path is disabled on this IronPdfEngine (allow_local_file_access=false). "
                    + "Send the HTML content instead, or use RenderZipFileAsPdf.";

    private IronPdfEngineConnection savedConnection;
    private boolean savedDeprecated;
    private boolean savedDocker;
    private RpcClient savedClient;
    private String savedLicenseKey;
    private Server server;
    private ManagedChannel channel;
    private FakeEngine engine;

    @BeforeEach
    public void saveState() {
        savedConnection = Setting_Api.connectionMode;
        savedDeprecated = Setting_Api.useDeprecatedConnectionSettings;
        savedDocker = Setting_Api.isIronPdfEngineDocker;
        savedClient = Access.client;
        savedLicenseKey = Setting_Api.licenseKey;
    }

    @AfterEach
    public void restoreState() throws InterruptedException {
        Access.client = savedClient;
        Setting_Api.licenseKey = savedLicenseKey;
        Setting_Api.connectionMode = savedConnection;
        Setting_Api.useDeprecatedConnectionSettings = savedDeprecated;
        Setting_Api.isIronPdfEngineDocker = savedDocker;
        if (channel != null) {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
        if (server != null) {
            server.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // ---------- A: loopback binding of the subprocess ----------

    @Test
    public void SubprocessDefaultsToLoopbackHostTest() {
        IronPdfEngineConnection subprocess = IronPdfEngineConnection.configure().withSubprocess();
        Assertions.assertEquals("127.0.0.1", subprocess.getHost());
        Assertions.assertEquals("127.0.0.1", IronPdfEngineConnection.configure().withSubprocess(40000).getHost());
    }

    @Test
    public void EngineCommandBindsTheHostThatTheClientConnectsToTest() {
        List<String> cmd = Access.buildEngineCommand("IronPdfEngineConsole", "127.0.0.1", 33350);
        Assertions.assertEquals("IronPdfEngineConsole", cmd.get(0));
        Assertions.assertTrue(cmd.contains("host=127.0.0.1"), cmd.toString());
        Assertions.assertTrue(cmd.contains("port=33350"), cmd.toString());
        Assertions.assertEquals(1, cmd.stream().filter(a -> a.startsWith("host=")).count());
    }

    // ---------- B: opt-in authentication token ----------

    @Test
    public void EngineCommandNeverContainsAuthTokenTest() {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess().withAuthToken("secret-token");
        List<String> cmd = Access.buildEngineCommand("IronPdfEngineConsole", "127.0.0.1", 33350);
        Assertions.assertTrue(cmd.stream().noneMatch(a -> a.startsWith("auth_token=")), cmd.toString());
        Assertions.assertTrue(cmd.stream().noneMatch(a -> a.contains("secret-token")), cmd.toString());
    }

    @Test
    public void NoAuthTokenByDefaultTest() {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess();
        Assertions.assertNull(Access.engineAuthToken());

        Map<String, String> env = new HashMap<>();
        Access.applyEngineAuthTokenToEnvironment(env, Access.engineAuthToken());
        Assertions.assertFalse(env.containsKey(Access.ENGINE_AUTH_TOKEN_ENV));
    }

    @Test
    public void AuthTokenGoesToSubprocessEnvironmentTest() {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess().withAuthToken("abc123");
        Map<String, String> env = new HashMap<>();
        Access.applyEngineAuthTokenToEnvironment(env, Access.engineAuthToken());
        Assertions.assertEquals("abc123", env.get("IRONPDF_ENGINE_AUTH_TOKEN"));
    }

    @Test
    public void InheritedAuthTokenIsRemovedWithoutClientTokenTest() {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess();
        Map<String, String> env = new HashMap<>();
        env.put(Access.ENGINE_AUTH_TOKEN_ENV, "inherited-from-jvm");
        env.put("PATH", "unchanged");
        Access.applyEngineAuthTokenToEnvironment(env, Access.engineAuthToken());
        Assertions.assertFalse(env.containsKey(Access.ENGINE_AUTH_TOKEN_ENV));
        Assertions.assertEquals("unchanged", env.get("PATH"));
    }

    @Test
    public void ClientTokenReplacesInheritedAuthTokenTest() {
        Map<String, String> env = new HashMap<>();
        env.put(Access.ENGINE_AUTH_TOKEN_ENV, "inherited-from-jvm");
        Access.applyEngineAuthTokenToEnvironment(env, "client-token");
        Assertions.assertEquals("client-token", env.get(Access.ENGINE_AUTH_TOKEN_ENV));
    }

    @Test
    public void DeprecatedSettingsHaveNoAuthTokenTest() {
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess().withAuthToken("abc123");
        Setting_Api.useDeprecatedConnectionSettings = true;
        Assertions.assertNull(Access.engineAuthToken());
    }

    @Test
    public void AuthTokenValidationTest() {
        IronPdfEngineConnection c = IronPdfEngineConnection.configure().withHostPort("localhost", 33350);
        Assertions.assertThrows(IllegalArgumentException.class, () -> c.withAuthToken(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> c.withAuthToken("has space"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> c.withAuthToken("line\nbreak"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> c.withAuthToken("caf\u00e9"));
        Assertions.assertEquals("a-B_9.~+/=", c.withAuthToken("a-B_9.~+/=").getAuthToken());
        Assertions.assertNull(c.withAuthToken(null).getAuthToken());
        Assertions.assertFalse(c.withAuthToken("secret-token").toString().contains("secret-token"));
    }

    @Test
    public void NoAuthorizationHeaderWithoutTokenTest() throws Exception {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withHostPort("127.0.0.1", 33350);
        startFakeEngine(false);

        // Close the document here. Else its finalizer can start a real IronPdfEngine during a later test.
        renderThroughFakeEngine(true).close();

        Assertions.assertFalse(engine.authorizationHeaders.isEmpty(), "the fake engine saw no call");
        for (String header : engine.authorizationHeaders) {
            Assertions.assertNull(header);
        }
    }

    @Test
    public void AuthorizationHeaderWithTokenTest() throws Exception {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withHostPort("127.0.0.1", 33350)
                .withAuthToken("abc123");
        startFakeEngine(false);

        renderThroughFakeEngine(true).close();

        Assertions.assertFalse(engine.authorizationHeaders.isEmpty(), "the fake engine saw no call");
        for (String header : engine.authorizationHeaders) {
            Assertions.assertEquals("Bearer abc123", header);
        }
    }

    // ---------- C: HTML file fallback ----------

    @Test
    public void RefusedPathFallsBackToHtmlContentTest() throws Exception {
        startFakeEngine(true);

        InternalPdfDocument doc = renderThroughFakeEngine(true);

        Assertions.assertEquals(1, engine.pathCalls.size());
        Assertions.assertEquals("C:/data/report.html", engine.pathCalls.get(0));
        Assertions.assertEquals(1, engine.htmlCalls.size());
        Assertions.assertEquals("<h1>Hello</h1>", engine.htmlCalls.get(0));
        Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId());
        doc.close();
    }

    @Test
    public void AcceptedPathDoesNotSendHtmlContentTest() throws Exception {
        startFakeEngine(false);

        InternalPdfDocument doc = renderThroughFakeEngine(true);

        Assertions.assertEquals(1, engine.pathCalls.size());
        Assertions.assertTrue(engine.htmlCalls.isEmpty());
        Assertions.assertEquals("from-path", doc.remoteDocument.getDocumentId());
        doc.close();
    }

    @Test
    public void NoLocalEngineProcessSendsHtmlContentTest() throws Exception {
        startFakeEngine(false);

        InternalPdfDocument doc = renderThroughFakeEngine(false);

        Assertions.assertTrue(engine.pathCalls.isEmpty());
        Assertions.assertEquals(1, engine.htmlCalls.size());
        Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId());
        doc.close();
    }

    // ---------- D: remote IronPdfEngine gets the HTML content, never the path ----------

    @Test
    public void SubprocessModeIsClientStartedSubprocessTest() {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess();
        Assertions.assertTrue(Render_Api.isClientStartedSubprocess());
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess(40000);
        Assertions.assertTrue(Render_Api.isClientStartedSubprocess());

        Setting_Api.useDeprecatedConnectionSettings = true;
        Setting_Api.isIronPdfEngineDocker = false;
        Assertions.assertTrue(Render_Api.isClientStartedSubprocess());
    }

    @Test
    public void RemoteModesAreNotClientStartedSubprocessTest() throws Exception {
        startFakeEngine(false);
        Setting_Api.useDeprecatedConnectionSettings = false;
        for (IronPdfEngineConnection remote : remoteConnections()) {
            Setting_Api.connectionMode = remote;
            Assertions.assertFalse(Render_Api.isClientStartedSubprocess(), remote.getMode().toString());
        }

        Setting_Api.useDeprecatedConnectionSettings = true;
        Setting_Api.isIronPdfEngineDocker = true;
        Assertions.assertFalse(Render_Api.isClientStartedSubprocess(), "useIronPdfEngineDocker");
    }

    @Test
    public void LoopbackHostPortCountsAsThisMachineTest() {
        Setting_Api.useDeprecatedConnectionSettings = false;
        for (String host : new String[]{"127.0.0.1", "localhost", "::1"}) {
            Setting_Api.connectionMode = IronPdfEngineConnection.configure().withHostPort(host, 33350);
            Assertions.assertTrue(Render_Api.isLoopbackHostPort(), host);
        }
        for (String host : new String[]{"10.0.0.5", "engine.example.com"}) {
            Setting_Api.connectionMode = IronPdfEngineConnection.configure().withHostPort(host, 33350);
            Assertions.assertFalse(Render_Api.isLoopbackHostPort(), host);
        }
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withTarget("127.0.0.1:443");
        Assertions.assertFalse(Render_Api.isLoopbackHostPort(), "TARGET");
        Assertions.assertFalse(Render_Api.sendsPath(true), "TARGET");
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withHostPort("127.0.0.1", 33350);
        Assertions.assertTrue(Render_Api.sendsPath(true), "loopback HOST_PORT, engine process running");
        Assertions.assertFalse(Render_Api.sendsPath(false), "loopback HOST_PORT, no engine process");
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withHostPort("10.0.0.5", 33350);
        Assertions.assertFalse(Render_Api.sendsPath(true), "remote HOST_PORT");
        Setting_Api.connectionMode = IronPdfEngineConnection.configure().withSubprocess();
        Assertions.assertFalse(Render_Api.isLoopbackHostPort(), "SUBPROCESS");
    }

    @Test
    public void RemoteEngineReceivesHtmlContentAndNoPathTest() throws Exception {
        Path htmlFile = Files.createTempFile("ironpdf-remote-", ".html");
        try {
            Files.write(htmlFile, "<h1>Remote \u00e9</h1>".getBytes(StandardCharsets.UTF_8));
            startFakeEngine(false);
            Setting_Api.useDeprecatedConnectionSettings = false;
            for (IronPdfEngineConnection remote : remoteConnections()) {
                Setting_Api.connectionMode = remote;
                InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(htmlFile.toString());
                Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId(), remote.getMode().toString());
                doc.close();
            }
            Assertions.assertTrue(engine.pathCalls.isEmpty(), engine.pathCalls.toString());
            Assertions.assertEquals(3, engine.htmlCalls.size());
            for (String html : engine.htmlCalls) {
                Assertions.assertEquals("<h1>Remote \u00e9</h1>", html);
            }
        } finally {
            Files.deleteIfExists(htmlFile);
        }
    }

    @Test
    public void DockerEngineReceivesHtmlContentAndNoPathTest() throws Exception {
        Path htmlFile = Files.createTempFile("ironpdf-docker-", ".html");
        try {
            Files.write(htmlFile, "<h1>Docker</h1>".getBytes(StandardCharsets.UTF_8));
            startFakeEngine(false);
            // Settings.useIronPdfEngineDocker(port) sets these two values.
            Setting_Api.useDeprecatedConnectionSettings = true;
            Setting_Api.isIronPdfEngineDocker = true;

            InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(htmlFile.toString());

            Assertions.assertTrue(engine.pathCalls.isEmpty(), engine.pathCalls.toString());
            Assertions.assertEquals(1, engine.htmlCalls.size());
            Assertions.assertEquals("<h1>Docker</h1>", engine.htmlCalls.get(0));
            Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId());
            doc.close();
        } finally {
            Files.deleteIfExists(htmlFile);
        }
    }

    // ---------- E: license key in the environment, never on the command line ----------

    @Test
    public void EngineCommandNeverContainsLicenseKeyTest() {
        Setting_Api.licenseKey = "IRONPDF-SECRET-LICENSE-KEY";
        List<String> cmd = Access.buildEngineCommand("IronPdfEngineConsole", "127.0.0.1", 33350);
        Assertions.assertTrue(cmd.stream().noneMatch(a -> a.startsWith("license_key=")), cmd.toString());
        Assertions.assertTrue(cmd.stream().noneMatch(a -> a.contains("IRONPDF-SECRET-LICENSE-KEY")), cmd.toString());

        Setting_Api.licenseKey = null;
        cmd = Access.buildEngineCommand("IronPdfEngineConsole", "127.0.0.1", 33350);
        Assertions.assertTrue(cmd.stream().noneMatch(a -> a.startsWith("license_key=")), cmd.toString());
    }

    @Test
    public void LicenseKeyGoesToSubprocessEnvironmentTest() {
        Setting_Api.licenseKey = "IRONPDF-SECRET-LICENSE-KEY";
        Assertions.assertEquals("IRONPDF-SECRET-LICENSE-KEY", Access.licenseKey());
        Map<String, String> env = new HashMap<>();
        Access.applyLicenseKeyToEnvironment(env, Access.licenseKey());
        Assertions.assertEquals("IRONPDF-SECRET-LICENSE-KEY", env.get("IRONPDF_ENGINE_LICENSE_KEY"));
    }

    @Test
    public void NoLicenseKeyMeansNoVariableTest() {
        for (String noKey : Arrays.asList(null, "", "   ")) {
            Map<String, String> env = new HashMap<>();
            Access.applyLicenseKeyToEnvironment(env, noKey);
            Assertions.assertFalse(env.containsKey(Access.ENGINE_LICENSE_KEY_ENV), String.valueOf(noKey));
        }
    }

    @Test
    public void ClientLicenseKeyReplacesInheritedValueTest() {
        Map<String, String> env = new HashMap<>();
        env.put(Access.ENGINE_LICENSE_KEY_ENV, "inherited-from-jvm");
        Access.applyLicenseKeyToEnvironment(env, "client-key");
        Assertions.assertEquals("client-key", env.get(Access.ENGINE_LICENSE_KEY_ENV));
    }

    @Test
    public void InheritedLicenseKeyStaysWithoutClientKeyTest() {
        Map<String, String> env = new HashMap<>();
        env.put(Access.ENGINE_LICENSE_KEY_ENV, "inherited-from-jvm");
        Access.applyLicenseKeyToEnvironment(env, null);
        Assertions.assertEquals("inherited-from-jvm", env.get(Access.ENGINE_LICENSE_KEY_ENV));
    }

    // ---------- F: optional TLS to a remote engine ----------

    @Test
    public void NoTlsByDefaultTest() {
        IronPdfEngineConnection c = IronPdfEngineConnection.configure().withHostPort("127.0.0.1", 33350);
        Assertions.assertFalse(c.isTls());
        Assertions.assertNull(c.getTrustedCertificate());
        Assertions.assertFalse(c.toString().contains("tls"), c.toString());
        Assertions.assertFalse(IronPdfEngineConnection.configure().withSubprocess().isTls());
    }

    @Test
    public void TlsSettingsValidationTest() throws Exception {
        Path cert = testResource("tls/engine-test.crt");
        IronPdfEngineConnection c = IronPdfEngineConnection.configure().withHostPort("127.0.0.1", 33350);
        Assertions.assertThrows(IllegalArgumentException.class, () -> c.withTrustedCertificate(null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> c.withTrustedCertificate(cert.resolveSibling("does-not-exist.crt")));
        Assertions.assertFalse(c.isTls());

        c.withTrustedCertificate(cert);
        Assertions.assertTrue(c.isTls());
        Assertions.assertEquals(cert, c.getTrustedCertificate());
        Assertions.assertTrue(c.toString().endsWith(", tls=true"), c.toString());
        Assertions.assertFalse(c.toString().contains("engine-test"), c.toString());
        Assertions.assertFalse(c.toString().contains("BEGIN"), c.toString());

        c.withTls();
        Assertions.assertTrue(c.isTls());
        Assertions.assertNull(c.getTrustedCertificate());
    }

    @Test
    public void TlsDecisionPerModeTest() throws Exception {
        Path cert = testResource("tls/engine-test.crt");
        for (IronPdfEngineConnection plain : Arrays.asList(
                IronPdfEngineConnection.configure().withSubprocess(),
                IronPdfEngineConnection.configure().withHostPort("127.0.0.1", 33350),
                IronPdfEngineConnection.configure().withTarget("127.0.0.1:33350"),
                IronPdfEngineConnection.configure().withOfficialCloud())) {
            Assertions.assertFalse(Access.tlsApplies(plain), plain.toString());
            Assertions.assertNull(Access.tlsWarning(plain), plain.toString());
        }

        Assertions.assertTrue(Access.tlsApplies(IronPdfEngineConnection.configure().withHostPort("h", 1).withTls()));
        Assertions.assertTrue(Access.tlsApplies(IronPdfEngineConnection.configure().withTarget("h:1")
                .withTrustedCertificate(cert)));
        Assertions.assertNull(Access.tlsWarning(IronPdfEngineConnection.configure().withHostPort("h", 1).withTls()));

        // SUBPROCESS: the client starts the engine on 127.0.0.1 without a certificate. Warn and keep plaintext.
        IronPdfEngineConnection subprocess = IronPdfEngineConnection.configure().withSubprocess().withTls();
        Assertions.assertFalse(Access.tlsApplies(subprocess));
        Assertions.assertTrue(Access.tlsWarning(subprocess).contains("SUBPROCESS"), Access.tlsWarning(subprocess));
        Assertions.assertTrue(Access.tlsWarning(subprocess).contains("plaintext"), Access.tlsWarning(subprocess));

        // OFFICIAL_CLOUD keeps its own TLS setting. CUSTOM uses the channel of the user.
        IronPdfEngineConnection cloud = IronPdfEngineConnection.configure().withOfficialCloud().withTls();
        Assertions.assertFalse(Access.tlsApplies(cloud));
        Assertions.assertNotNull(Access.tlsWarning(cloud));
        IronPdfEngineConnection custom = IronPdfEngineConnection.configure()
                .withCustomGrpcConnection(null).withTrustedCertificate(cert);
        Assertions.assertFalse(Access.tlsApplies(custom));
        Assertions.assertNotNull(Access.tlsWarning(custom));
    }

    @Test
    public void TrustedCertificateConnectsToTlsEngineTest() throws Exception {
        startFakeEngine(false, true);
        Path cert = testResource("tls/engine-test.crt");
        for (IronPdfEngineConnection connection : Arrays.asList(
                IronPdfEngineConnection.configure().withHostPort("127.0.0.1", server.getPort()),
                IronPdfEngineConnection.configure().withHostPort("localhost", server.getPort()),
                IronPdfEngineConnection.configure().withTarget("127.0.0.1:" + server.getPort()))) {
            connectWithClientChannel(connection.withTrustedCertificate(cert));
            InternalPdfDocument doc = renderThroughFakeEngine(false);
            Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId(), connection.toString());
            // Close the document here. Else its finalizer can start a real IronPdfEngine during a later test.
            doc.close();
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
        Assertions.assertEquals(3, engine.htmlCalls.size());
    }

    @Test
    public void PlaintextClientCannotCallTlsEngineTest() throws Exception {
        startFakeEngine(false, true);
        RpcClient client = connectWithClientChannel(
                IronPdfEngineConnection.configure().withHostPort("127.0.0.1", server.getPort()));
        StatusRuntimeException e = callFails(client);
        Assertions.assertEquals(Status.Code.UNAVAILABLE, e.getStatus().getCode(), e.toString());
        Assertions.assertTrue(engine.authorizationHeaders.isEmpty(), "the fake engine saw a call");
    }

    @Test
    public void DefaultTrustStoreRefusesSelfSignedEngineTest() throws Exception {
        startFakeEngine(false, true);
        RpcClient client = connectWithClientChannel(
                IronPdfEngineConnection.configure().withHostPort("127.0.0.1", server.getPort()).withTls());
        StatusRuntimeException e = callFails(client);
        Assertions.assertEquals(Status.Code.UNAVAILABLE, e.getStatus().getCode(), e.toString());
        Assertions.assertTrue(engine.authorizationHeaders.isEmpty(), "the fake engine saw a call");
    }

    @Test
    public void OtherCertificateIsNotTrustedTest() throws Exception {
        startFakeEngine(false, true);
        RpcClient client = connectWithClientChannel(IronPdfEngineConnection.configure()
                .withHostPort("127.0.0.1", server.getPort())
                .withTrustedCertificate(testResource("tls/other-test.crt")));
        StatusRuntimeException e = callFails(client);
        Assertions.assertEquals(Status.Code.UNAVAILABLE, e.getStatus().getCode(), e.toString());
        Assertions.assertTrue(engine.authorizationHeaders.isEmpty(), "the fake engine saw a call");
    }

    @Test
    public void TargetChecksTheCertificateByDefaultTest() throws Exception {
        // Before 2026.11.1, TARGET accepted any certificate. Now a self-signed engine is refused.
        startFakeEngine(false, true);
        RpcClient client = connectWithClientChannel(
                IronPdfEngineConnection.configure().withTarget("127.0.0.1:" + server.getPort()));
        StatusRuntimeException e = callFails(client);
        Assertions.assertEquals(Status.Code.UNAVAILABLE, e.getStatus().getCode(), e.toString());
        Assertions.assertTrue(engine.authorizationHeaders.isEmpty(), "the fake engine saw a call");
    }

    @Test
    public void OfficialCloudChecksTheCertificateTest() throws Exception {
        // The channel to the official cloud target is built with the JVM trust store, not with a trust-all manager.
        // A self-signed server at the same address must be refused.
        startFakeEngine(false, true);
        IronPdfEngineConnection cloud = IronPdfEngineConnection.configure().withOfficialCloud();
        Assertions.assertFalse(Access.insecureTlsRequested(cloud));
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = cloud;
        channel = Access.buildChannel(IronPdfEngineConnection.configure().withTarget("127.0.0.1:" + server.getPort()));
        StatusRuntimeException e = callFails(new RpcClient(channel));
        Assertions.assertEquals(Status.Code.UNAVAILABLE, e.getStatus().getCode(), e.toString());
    }

    @Test
    public void InsecureTlsOptOutConnectsToSelfSignedTargetTest() throws Exception {
        startFakeEngine(false, true);
        connectWithClientChannel(
                IronPdfEngineConnection.configure().withTarget("127.0.0.1:" + server.getPort()).withInsecureTls());

        InternalPdfDocument doc = renderThroughFakeEngine(false);

        Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId());
        doc.close();
    }

    @Test
    public void InsecureTlsSystemPropertyConnectsToSelfSignedTargetTest() throws Exception {
        startFakeEngine(false, true);
        System.setProperty("ironpdf.tls.insecure", "true");
        try {
            Assertions.assertTrue(Access.insecureTlsRequested(IronPdfEngineConnection.configure().withTarget("h:1")));
            connectWithClientChannel(IronPdfEngineConnection.configure().withTarget("127.0.0.1:" + server.getPort()));

            InternalPdfDocument doc = renderThroughFakeEngine(false);

            Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId());
            doc.close();
        } finally {
            System.clearProperty("ironpdf.tls.insecure");
        }
        Assertions.assertFalse(Access.insecureTlsRequested(IronPdfEngineConnection.configure().withTarget("h:1")));
    }

    @Test
    public void InsecureTlsSystemPropertyDoesNotOverrideExplicitTlsTest() throws Exception {
        Path cert = Files.createTempFile("engine", ".crt");
        System.setProperty("ironpdf.tls.insecure", "true");
        try {
            Assertions.assertFalse(Access.insecureTlsRequested(
                    IronPdfEngineConnection.configure().withTarget("h:1").withTrustedCertificate(cert)));
            Assertions.assertFalse(Access.insecureTlsRequested(
                    IronPdfEngineConnection.configure().withTarget("h:1").withTls()));
            Assertions.assertTrue(Access.insecureTlsRequested(
                    IronPdfEngineConnection.configure().withTarget("h:1").withInsecureTls()));
            Assertions.assertTrue(Access.insecureTlsRequested(IronPdfEngineConnection.configure().withTarget("h:1")));
        } finally {
            System.clearProperty("ironpdf.tls.insecure");
            Files.deleteIfExists(cert);
        }
    }

    @Test
    public void WithTrustedCertificateTurnsInsecureTlsOffAgainTest() {
        IronPdfEngineConnection connection = IronPdfEngineConnection.configure().withTarget("h:1").withInsecureTls();
        Assertions.assertTrue(connection.isInsecureTls());
        Assertions.assertFalse(connection.withTls().isInsecureTls());
        Assertions.assertTrue(connection.withInsecureTls().isInsecureTls());
        Assertions.assertFalse(connection.isTls());
    }

    @Test
    public void TlsAndAuthTokenTogetherTest() throws Exception {
        startFakeEngine(false, true);
        connectWithClientChannel(IronPdfEngineConnection.configure()
                .withHostPort("127.0.0.1", server.getPort())
                .withTrustedCertificate(testResource("tls/engine-test.crt"))
                .withAuthToken("abc123"));

        InternalPdfDocument doc = renderThroughFakeEngine(false);

        Assertions.assertEquals("from-html", doc.remoteDocument.getDocumentId());
        doc.close();
        Assertions.assertFalse(engine.authorizationHeaders.isEmpty(), "the fake engine saw no call");
        for (String header : engine.authorizationHeaders) {
            Assertions.assertEquals("Bearer abc123", header);
        }
    }

    @Test
    public void UnreadableTrustedCertificateFailsFastTest() throws Exception {
        Path notPem = Files.createTempFile("ironpdf-not-pem-", ".crt");
        try {
            Files.write(notPem, "this is not a certificate".getBytes(StandardCharsets.UTF_8));
            IronPdfEngineConnection connection = IronPdfEngineConnection.configure()
                    .withHostPort("127.0.0.1", 33350).withTrustedCertificate(notPem);
            Assertions.assertThrows(IllegalArgumentException.class, () -> Access.buildChannel(connection));
        } finally {
            Files.deleteIfExists(notPem);
        }
    }

    private List<IronPdfEngineConnection> remoteConnections() {
        return Arrays.asList(
                IronPdfEngineConnection.configure().withTarget("127.0.0.1:33350"),
                IronPdfEngineConnection.configure().withOfficialCloud(),
                IronPdfEngineConnection.configure().withCustomGrpcConnection(channel));
    }

    // ---------- helpers ----------

    private InternalPdfDocument renderThroughFakeEngine(boolean sendPath) {
        return Render_Api.renderHtmlFileByPathOrContent(Access.client, "C:/data/report.html", "<h1>Hello</h1>",
                null, null, sendPath);
    }

    private void startFakeEngine(boolean refusePath) throws Exception {
        startFakeEngine(refusePath, false);
        channel = ManagedChannelBuilder.forAddress("127.0.0.1", server.getPort()).usePlaintext().build();
        Access.client = new RpcClient(channel);
    }

    /**
     * Starts the fake engine. With {@code tls}, the fake engine serves gRPC over TLS with the self-signed
     * test certificate (SAN localhost and 127.0.0.1), as IronPdfEngine 2026.11 does with
     * {@code IRONPDF_ENGINE_TLS_CERT_PATH}. This overload does not create a client channel.
     *
     * <p>The files in {@code src/test/resources/tls} are for tests only. They have no other use.
     * OpenSSL made them (RSA 2048, valid for 100 years):
     * {@code openssl req -x509 -newkey rsa:2048 -nodes -days 36500 -subj "/CN=localhost"
     * -addext "subjectAltName=DNS:localhost,IP:127.0.0.1" -keyout engine-test.key -out engine-test.crt}.
     * {@code other-test.crt} is a second certificate of the same type. The tests do not trust it for the engine.</p>
     */
    private void startFakeEngine(boolean refusePath, boolean tls) throws Exception {
        engine = new FakeEngine(refusePath);
        ServerInterceptor captureAuthorization = new ServerInterceptor() {
            @Override
            public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                         ServerCallHandler<ReqT, RespT> next) {
                engine.authorizationHeaders.add(headers.get(RpcClient.AUTHORIZATION_KEY));
                return next.startCall(call, headers);
            }
        };
        NettyServerBuilder builder = tls
                ? NettyServerBuilder.forAddress(new InetSocketAddress("127.0.0.1", 0),
                TlsServerCredentials.create(testResource("tls/engine-test.crt").toFile(),
                        testResource("tls/engine-test.key").toFile()))
                : NettyServerBuilder.forAddress(new InetSocketAddress("127.0.0.1", 0));
        server = builder
                .addService(ServerInterceptors.intercept(engine, captureAuthorization))
                .build()
                .start();
    }

    /** Builds the channel with the same code as the client, and makes it the active client. */
    private RpcClient connectWithClientChannel(IronPdfEngineConnection connection) throws Exception {
        Setting_Api.useDeprecatedConnectionSettings = false;
        Setting_Api.connectionMode = connection;
        channel = Access.buildChannel(connection);
        Access.client = new RpcClient(channel);
        return Access.client;
    }

    private static StatusRuntimeException callFails(RpcClient client) {
        return Assertions.assertThrows(StatusRuntimeException.class, () -> client.GetBlockingStub("test")
                .withDeadlineAfter(20, TimeUnit.SECONDS)
                .pdfiumDispose(PdfDocumentP.getDefaultInstance()));
    }

    private static Path testResource(String name) throws Exception {
        return Paths.get(EngineHardeningClientTests.class.getClassLoader().getResource(name).toURI());
    }

    private static final class FakeEngine extends IronPdfServiceGrpc.IronPdfServiceImplBase {
        final boolean refusePath;
        final List<String> pathCalls = new CopyOnWriteArrayList<>();
        final List<String> htmlCalls = new CopyOnWriteArrayList<>();
        final List<String> authorizationHeaders = new CopyOnWriteArrayList<>();

        FakeEngine(boolean refusePath) {
            this.refusePath = refusePath;
        }

        @Override
        public StreamObserver<ChromeRenderPdfDocumentFromHtmlFileRequestStreamP> chromeRenderFromHtmlFile(
                StreamObserver<PdfDocumentResultP> responseObserver) {
            return new StreamObserver<ChromeRenderPdfDocumentFromHtmlFileRequestStreamP>() {
                @Override
                public void onNext(ChromeRenderPdfDocumentFromHtmlFileRequestStreamP value) {
                    if (value.hasHtmlPath()) {
                        pathCalls.add(value.getHtmlPath());
                    }
                }

                @Override
                public void onError(Throwable t) {
                }

                @Override
                public void onCompleted() {
                    PdfDocumentResultP result = refusePath
                            ? PdfDocumentResultP.newBuilder().setException(RemoteExceptionP.newBuilder()
                            .setExceptionType("System.InvalidOperationException")
                            .setMessage(REFUSAL)).build()
                            : PdfDocumentResultP.newBuilder()
                            .setResult(PdfDocumentP.newBuilder().setDocumentId("from-path")).build();
                    responseObserver.onNext(result);
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<ChromeRenderPdfDocumentFromHtmlRequestStreamP> chromeRenderFromHtml(
                StreamObserver<PdfDocumentResultP> responseObserver) {
            StringBuilder html = new StringBuilder();
            return new StreamObserver<ChromeRenderPdfDocumentFromHtmlRequestStreamP>() {
                @Override
                public void onNext(ChromeRenderPdfDocumentFromHtmlRequestStreamP value) {
                    if (value.hasHtmlChunk()) {
                        html.append(value.getHtmlChunk());
                    }
                }

                @Override
                public void onError(Throwable t) {
                }

                @Override
                public void onCompleted() {
                    htmlCalls.add(html.toString());
                    responseObserver.onNext(PdfDocumentResultP.newBuilder()
                            .setResult(PdfDocumentP.newBuilder().setDocumentId("from-html")).build());
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public void pdfiumDispose(PdfDocumentP request, StreamObserver<EmptyResultP> responseObserver) {
            responseObserver.onNext(EmptyResultP.newBuilder().setResult(EmptyP.getDefaultInstance()).build());
            responseObserver.onCompleted();
        }
    }
}
