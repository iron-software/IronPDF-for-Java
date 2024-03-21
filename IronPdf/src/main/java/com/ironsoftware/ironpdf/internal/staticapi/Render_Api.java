package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath Path to a Html to be rendered as a PDF.
     * @return A {@link InternalPdfDocument}
     * @throws IOException the io exception
     */
    public static InternalPdfDocument renderHtmlFileAsPdf(String htmlFilePath) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, null, null);
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

        return renderHtmlAsPdf(String.join(System.lineSeparator(), htmlList), renderOptions, loginCredentials);
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
                client.stub.chromeRenderFromHtml(
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

        PdfDocumentResultP res = client.blockingStub.chromeRenderFromUri(request.build());

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
                client.stub.chromeRenderRtfToPdf(
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
                client.stub.chromeRenderFromZipFile(
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
