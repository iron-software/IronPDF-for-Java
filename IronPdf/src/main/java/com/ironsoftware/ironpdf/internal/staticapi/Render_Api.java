package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.PdfDocumentResult;
import com.ironsoftware.ironpdf.internal.proto.RenderPdfDocumentFromStringSnippetRequestStream;
import com.ironsoftware.ironpdf.internal.proto.RenderPdfDocumentFromUriRequest;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * The type Render api.
 */
public final class Render_Api {

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

        String absoluteFilePath = (new File(htmlFilePath)).getAbsolutePath();
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

        return renderHtmlAsPdf(String.join("", htmlList), renderOptions, loginCredentials, "");
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or                         URL context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html,
                                                      ChromePdfRenderOptions renderOptions,
                                                      ChromeHttpLoginCredentials loginCredentials,
                                                      String baseUrl) {
        RpcClient client = Access.ensureConnection();


        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<RenderPdfDocumentFromStringSnippetRequestStream> requestStream =
                client.stub.renderFromStringSnippet(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        RenderPdfDocumentFromStringSnippetRequestStream.Info.Builder info =
                RenderPdfDocumentFromStringSnippetRequestStream.Info.newBuilder();

        com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions renderOp = Render_Converter.toProto(
                renderOptions);
        if (renderOp != null) {
            info.setRenderOptions(renderOp);
        }

        com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials login = Render_Converter.toProto(
                loginCredentials);
        if (login != null) {
            info.setHttpOptions(login);
        }
        info.setBaseUrl(baseUrl != null ? baseUrl : "");

        RenderPdfDocumentFromStringSnippetRequestStream.Builder infoMsg =
                RenderPdfDocumentFromStringSnippetRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (Iterator<char[]> it = Utils_Util.chunk(html.toCharArray()); it.hasNext(); ) {
            char[] htmlChunk = it.next();
            RenderPdfDocumentFromStringSnippetRequestStream.Builder dataMsg = RenderPdfDocumentFromStringSnippetRequestStream.newBuilder();
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
        RenderPdfDocumentFromUriRequest.Builder request = RenderPdfDocumentFromUriRequest.newBuilder();
        request.setUri(url);

        //optional;
        if (renderOptions != null) {
            request.setRenderOptions(Render_Converter.toProto(renderOptions));
        }

        //optional;
        if (loginCredentials != null) {
            request.setHttpOptions(Render_Converter.toProto(loginCredentials));
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);

        List<PdfDocumentResult> msgChunks = new ArrayList<>();

        PdfDocumentResult res = client.blockingStub.renderFromUri(request.build());

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
        return renderHtmlAsPdf(html, null, null, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or                         URL context for hyperlinks, images, CSS and JavaScript files.
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html,
                                                      String baseUrl,
                                                      ChromeHttpLoginCredentials loginCredentials) {
        return renderHtmlAsPdf(html, null, loginCredentials, baseUrl);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html          The Html to be rendered as a PDF.
     * @param baseUrl       Optional. Setting the BaseURL property gives the relative file path or URL                      context for hyperlinks, images, CSS and JavaScript files.
     * @param renderOptions Rendering options
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html,
                                                      String baseUrl,
                                                      ChromePdfRenderOptions renderOptions) {
        return renderHtmlAsPdf(html, renderOptions, null, baseUrl);
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
        return renderHtmlAsPdf(html, renderOptions, loginCredentials, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html    The Html to be rendered as a PDF.
     * @param baseUrl Optional. Setting the BaseURL property gives the relative file path or URL                context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument renderHtmlAsPdf(String html,
                                                      String baseUrl) {
        return renderHtmlAsPdf(html, null, null, baseUrl);
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
        return renderHtmlAsPdf(html, null, loginCredentials, null);
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
        return renderHtmlAsPdf(html, renderOptions, null, null);
    }
}
