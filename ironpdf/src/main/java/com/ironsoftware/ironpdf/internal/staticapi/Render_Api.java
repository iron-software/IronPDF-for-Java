package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.IronPdfEngineConnection;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * The type Render api.
 */
public final class Render_Api {

    static final Logger logger = LoggerFactory.getLogger(Render_Api.class);

    /**
     * Text in the error that IronPdfEngine 2026.11 and later return when the engine refuses
     * to read a local file by path.
     */
    static final String LOCAL_FILE_ACCESS_DISABLED_MARKER = "allow_local_file_access=false";

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath Path to a Html to be rendered as a PDF.
     * @return A {@link InternalPdfDocument}
     * @throws IOException the io exception
     */
    public static InternalPdfDocument renderHtmlFileAsPdf(String htmlFilePath) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, null, null);
    }

    public static boolean isIronPdfProcessRunning() {
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            String[] cmd = isWindows ? new String[]{"cmd", "/c", "tasklist"} : new String[]{"bash", "-c", "ps aux"};
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().contains("IronPdfEngineConsole")) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return false;
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     * @throws IOException the io exception
     */
    public static InternalPdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                          ChromePdfRenderOptions renderOptions,
                                                          ChromeHttpLoginCredentials loginCredentials) throws IOException {
        if (Utils_StringHelper.isNullOrWhiteSpace(htmlFilePath)) {
            throw new IllegalArgumentException("Value 'htmlFilePath' cannot be null or empty.");
        }

        File htmlFile = new File(htmlFilePath);
        String absoluteFilePath = htmlFile.getAbsolutePath();
        if (!(new File(absoluteFilePath)).isFile()) {
            throw new IOException(
                    String.format("%1$s is not a valid Html file path. That file does not exist.",
                            absoluteFilePath));
        }

        List<String> htmlList = Files.readAllLines(Paths.get(htmlFilePath));
        if (htmlList.size() == 0) {
            throw new IOException(String.format(
                    "html data is null or empty.  Can not create a PDF document from invalid data. %1$s",
                    htmlFilePath));
        }

        RpcClient client = Access.ensureConnection();

        // Send the path only to an IronPdfEngine on the same machine: a subprocess that this client started,
        // or a HOST_PORT connection to a loopback address. Any other engine (Docker, a remote host and port,
        // cloud, custom channel) gets the HTML content.
        // Send the absolute path, as the .NET client does. IronPdfEngine can have a different working directory.
        boolean sendPath = sendsPath(isIronPdfProcessRunning());
        return renderHtmlFileByPathOrContent(client, absoluteFilePath, String.join(System.lineSeparator(), htmlList),
                renderOptions, loginCredentials, sendPath);
    }

    /**
     * Tells if the connection goes to an IronPdfEngine subprocess that this client starts on the same machine.
     *
     * <p>The result is {@code false} for IronPdfEngine in Docker ({@code useIronPdfEngineDocker}),
     * and for the {@code HOST_PORT}, {@code TARGET}, {@code OFFICIAL_CLOUD} and {@code CUSTOM} modes.
     * Such an engine cannot read the files of the client machine.</p>
     *
     * @return {@code true} only for the {@code SUBPROCESS} mode.
     */
    static boolean isClientStartedSubprocess() {
        if (Setting_Api.useDeprecatedConnectionSettings) {
            return !Setting_Api.isIronPdfEngineDocker;
        }
        return Setting_Api.connectionMode != null
                && Setting_Api.connectionMode.getMode() == IronPdfEngineConnection.ConnectionMode.SUBPROCESS;
    }

    /**
     * Tells if the file path goes to IronPdfEngine: the engine is on this machine and a local engine process runs.
     */
    static boolean sendsPath(boolean localEngineProcessRunning) {
        return (isClientStartedSubprocess() || isLoopbackHostPort()) && localEngineProcessRunning;
    }

    /**
     * Tells if the connection is a {@code HOST_PORT} connection to a loopback address ({@code 127.0.0.1},
     * {@code localhost} or {@code ::1}): an IronPdfEngine that the user started on the same machine.
     * If that engine cannot read the file, the render falls back to the HTML content.
     */
    static boolean isLoopbackHostPort() {
        if (Setting_Api.useDeprecatedConnectionSettings || Setting_Api.connectionMode == null
                || Setting_Api.connectionMode.getMode() != IronPdfEngineConnection.ConnectionMode.HOST_PORT) {
            return false;
        }
        String host = Setting_Api.connectionMode.getHost();
        return host != null && (host.equals("localhost") || host.equals("::1") || host.equals("[::1]")
                || host.startsWith("127."));
    }

    /**
     * Renders an HTML file. This method sends the file path to IronPdfEngine, or it sends the HTML content.
     *
     * <p>This method has the same behaviour as the .NET client ({@code GrpcChromeClient.RenderPDFFromFile}):</p>
     * <ul>
     *   <li>If IronPdfEngine is not on this machine (IronPdfEngine in Docker, on a remote host, in the cloud,
     *       or on a custom channel; a {@code HOST_PORT} connection to a loopback address counts as this machine),
     *       or if no local IronPdfEngine process runs,
     *       this method sends only the HTML content. It does not send the path.</li>
     *   <li>Otherwise, this method sends the path. If IronPdfEngine returns an error, this method sends
     *       the HTML content. IronPdfEngine 2026.11 and later return an error that contains
     *       {@value #LOCAL_FILE_ACCESS_DISABLED_MARKER} when the engine operator disabled local file access.</li>
     * </ul>
     *
     * @param client           the connection to IronPdfEngine.
     * @param htmlFilePath     the path of the HTML file.
     * @param htmlContent      the content of the HTML file.
     * @param renderOptions    rendering options.
     * @param loginCredentials HTTP login credentials.
     * @param sendPath         {@code true} to send the path first.
     * @return the rendered document.
     */
    static InternalPdfDocument renderHtmlFileByPathOrContent(RpcClient client,
                                                            String htmlFilePath,
                                                            String htmlContent,
                                                            ChromePdfRenderOptions renderOptions,
                                                            ChromeHttpLoginCredentials loginCredentials,
                                                            boolean sendPath) {
        if (!sendPath) {
            return renderHtmlAsPdf(htmlContent, renderOptions, loginCredentials);
        }

        try {
            final CountDownLatch finishLatch = new CountDownLatch(1);

            ArrayList<PdfDocumentResultP> resultChunks = new ArrayList<>();

            io.grpc.stub.StreamObserver<ChromeRenderPdfDocumentFromHtmlFileRequestStreamP> requestStream =
                    client.GetStub("renderHtmlFileAsPdf").chromeRenderFromHtmlFile(
                            new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

            ChromeRenderPdfDocumentFromHtmlFileRequestStreamP.InfoP.Builder info = ChromeRenderPdfDocumentFromHtmlFileRequestStreamP.InfoP.newBuilder();

            com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptionsP renderOp = Render_Converter.toProto(renderOptions);
            if (renderOp != null) {
                info.setRenderOptions(renderOp);
            }

            com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentialsP login = Render_Converter.toProto(loginCredentials);
            if (login != null) {
                info.setHttpOptions(login);
            }

            ChromeRenderPdfDocumentFromHtmlFileRequestStreamP.Builder infoMsg = ChromeRenderPdfDocumentFromHtmlFileRequestStreamP.newBuilder();
            infoMsg.setInfo(info);
            requestStream.onNext(infoMsg.build());

            ChromeRenderPdfDocumentFromHtmlFileRequestStreamP.Builder dataMsg = ChromeRenderPdfDocumentFromHtmlFileRequestStreamP.newBuilder();
            dataMsg.setHtmlPath(htmlFilePath);
            requestStream.onNext(dataMsg.build());

            requestStream.onCompleted();

            Utils_Util.waitAndCheck(finishLatch, resultChunks);

            return Utils_Util.handlePdfDocumentChunks(resultChunks);
        } catch(Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().contains(LOCAL_FILE_ACCESS_DISABLED_MARKER)) {
                logger.info("IronPdfEngine does not render HTML files by path ("
                        + LOCAL_FILE_ACCESS_DISABLED_MARKER + "). Sending the HTML content instead.");
            } else {
                logger.info(ex.getMessage());
            }
            return renderHtmlAsPdf(htmlContent, renderOptions, loginCredentials);
        }
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html,
                                                      ChromePdfRenderOptions renderOptions,
                                                      ChromeHttpLoginCredentials loginCredentials) {
        RpcClient client = Access.ensureConnection();


        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResultP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<ChromeRenderPdfDocumentFromHtmlRequestStreamP> requestStream =
                client.GetStub("renderHtmlAsPdf").chromeRenderFromHtml(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        ChromeRenderPdfDocumentFromHtmlRequestStreamP.InfoP.Builder info =
                ChromeRenderPdfDocumentFromHtmlRequestStreamP.InfoP.newBuilder();

        com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptionsP renderOp = Render_Converter.toProto(
                renderOptions);
        if (renderOp != null) {
            info.setRenderOptions(renderOp);
        }

        com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentialsP login = Render_Converter.toProto(
                loginCredentials);
        if (login != null) {
            info.setHttpOptions(login);
        }

        ChromeRenderPdfDocumentFromHtmlRequestStreamP.Builder infoMsg =
                ChromeRenderPdfDocumentFromHtmlRequestStreamP.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<char[]> it = Utils_Util.chunk(html.toCharArray()); it.hasNext(); ) {
            char[] htmlChunk = it.next();
            ChromeRenderPdfDocumentFromHtmlRequestStreamP.Builder dataMsg = ChromeRenderPdfDocumentFromHtmlRequestStreamP.newBuilder();
            dataMsg.setHtmlChunk(String.valueOf(htmlChunk));
            requestStream.onNext(dataMsg.build());
        }
        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handlePdfDocumentChunks(resultChunks);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     * @throws IOException the io exception
     */
    public static InternalPdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                          ChromeHttpLoginCredentials loginCredentials
    ) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath  Path to a Html to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link InternalPdfDocument}
     * @throws IOException the io exception
     */
    public static InternalPdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                          ChromePdfRenderOptions renderOptions
    ) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, renderOptions, null);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url An absolute (fully formed) Uri.  Points to the Html document to be rendered as a            PDF.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderUrlAsPdf(String url) {
        return renderUrlAsPdf(url, null, null);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url              An absolute (fully formed) Uri.  Points to the Html document to be                         rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderUrlAsPdf(String url,
                                                     ChromePdfRenderOptions renderOptions,
                                                     ChromeHttpLoginCredentials loginCredentials) {
        RpcClient client = Access.ensureConnection();
        ChromeRenderPdfDocumentFromUriRequestP.Builder request = ChromeRenderPdfDocumentFromUriRequestP.newBuilder();
        request.setUri(url);

        if (renderOptions == null) {
            renderOptions = new ChromePdfRenderOptions();
        }
        request.setRenderOptions(Render_Converter.toProto(renderOptions));

        if (loginCredentials == null) {
            loginCredentials = new ChromeHttpLoginCredentials();
        }
        request.setHttpOptions(Render_Converter.toProto(loginCredentials));

        PdfDocumentResultP res = client.GetBlockingStub("renderUrlAsPdf").chromeRenderFromUri(request.build());

        return Utils_Util.handlePdfDocumentResult(res);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url              An absolute (fully formed) Uri.  Points to the Html document to be                         rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderUrlAsPdf(String url,
                                                     ChromeHttpLoginCredentials loginCredentials) {
        return renderUrlAsPdf(url, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url           An absolute (fully formed) Uri.  Points to the Html document to be                      rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderUrlAsPdf(String url,
                                                     ChromePdfRenderOptions renderOptions) {
        return renderUrlAsPdf(url, renderOptions, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html The Html to be rendered as a PDF.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html) {
        return renderHtmlAsPdf(html, null, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html,
                                                      ChromeHttpLoginCredentials loginCredentials) {
        return renderHtmlAsPdf(html, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html          The Html to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html,
                                                      ChromePdfRenderOptions renderOptions) {
        return renderHtmlAsPdf(html, renderOptions, null);
    }

    /**
     * Creates a PDF file from RTF string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param rtfString The RTF string to be rendered as a PDF.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderRtfAsPdf(String rtfString) {
        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResultP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<ChromeRenderPdfDocumentFromRtfStringRequestStreamP> requestStream =
                client.GetStub("renderRtfAsPdf").chromeRenderRtfToPdf(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        ChromeRenderPdfDocumentFromRtfStringRequestStreamP.InfoP.Builder info = ChromeRenderPdfDocumentFromRtfStringRequestStreamP.InfoP.newBuilder();
        info.setRenderOptions(Render_Converter.toProto(new ChromePdfRenderOptions()));
        info.setHttpOptions(Render_Converter.toProto(new ChromeHttpLoginCredentials()));

        requestStream.onNext(ChromeRenderPdfDocumentFromRtfStringRequestStreamP.newBuilder().setInfo(info.build()).build());
        for (Iterator<char[]> it = Utils_Util.chunk(rtfString.toCharArray()); it.hasNext(); ) {
            char[] htmlChunk = it.next();
            ChromeRenderPdfDocumentFromRtfStringRequestStreamP.Builder dataMsg = ChromeRenderPdfDocumentFromRtfStringRequestStreamP.newBuilder();
            dataMsg.setRtfStringChunk(String.valueOf(htmlChunk));
            requestStream.onNext(dataMsg.build());
        }
        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handlePdfDocumentChunks(resultChunks);
    }

    public static InternalPdfDocument renderZipAsPdf(Path zipFilePath, String mainFile,
                                                     ChromePdfRenderOptions renderOptions,
                                                     ChromeHttpLoginCredentials loginCredentials) throws IOException {
        RpcClient client = Access.ensureConnection();


        ChromeRenderPdfDocumentFromZipFileRequestStreamP.InfoP.Builder info =
                ChromeRenderPdfDocumentFromZipFileRequestStreamP.InfoP.newBuilder();

        com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptionsP renderOp = Render_Converter.toProto(
                renderOptions);
        if (renderOp != null) {
            info.setRenderOptions(renderOp);
        }

        com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentialsP login = Render_Converter.toProto(
                loginCredentials);
        if (login != null) {
            info.setHttpOptions(login);
        }
        if (mainFile != null) {
            info.setMainFile(mainFile);
        }

        ChromeRenderPdfDocumentFromZipFileRequestStreamP.Builder infoMsg =
                ChromeRenderPdfDocumentFromZipFileRequestStreamP.newBuilder();
        infoMsg.setInfo(info);


        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResultP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<ChromeRenderPdfDocumentFromZipFileRequestStreamP> requestStream =
                client.GetStub("renderZipAsPdf").chromeRenderFromZipFile(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        requestStream.onNext(infoMsg.build());

        byte[] zipByte = Files.readAllBytes(zipFilePath);
        for (Iterator<byte[]> it = Utils_Util.chunk(zipByte); it.hasNext(); ) {
            byte[] zipChunk = it.next();
            ChromeRenderPdfDocumentFromZipFileRequestStreamP.Builder dataMsg = ChromeRenderPdfDocumentFromZipFileRequestStreamP.newBuilder();
            dataMsg.setZipChunk(ByteString.copyFrom(zipChunk));
            requestStream.onNext(dataMsg.build());
        }

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handlePdfDocumentChunks(resultChunks);
    }
}
