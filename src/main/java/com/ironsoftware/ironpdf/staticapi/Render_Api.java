package com.ironsoftware.ironpdf.staticapi;

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
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.ironsoftware.ironpdf.staticapi.Utils_StringHelper.isNullOrWhiteSpace;
import static com.ironsoftware.ironpdf.staticapi.Utils_Util.*;

public final class Render_Api {

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath Path to a Html to be rendered as a PDF.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlFileAsPdf(String htmlFilePath) throws IOException {
        return RenderHtmlFileAsPdf(htmlFilePath, null, null);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlFileAsPdf(String htmlFilePath,
                                                          ChromePdfRenderOptions renderOptions,
                                                          ChromeHttpLoginCredentials loginCredentials) throws IOException {
        if (isNullOrWhiteSpace(htmlFilePath)) {
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

        return RenderHtmlAsPdf(String.join("", htmlList), renderOptions, loginCredentials, "");
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or
     *                         URL context for hyperlinks, images, CSS and JavaScript files.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html,
                                                      ChromePdfRenderOptions renderOptions,
                                                      ChromeHttpLoginCredentials loginCredentials,
                                                      String baseUrl) throws IOException {
        RpcClient client = Access.EnsureConnection();

        java.util.Iterator<char[]> htmlChunks = Utils_Util.Chunk(html.toCharArray());

        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<RenderPdfDocumentFromStringSnippetRequestStream> requestStream =
                client.Stub.renderFromStringSnippet(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        RenderPdfDocumentFromStringSnippetRequestStream.Info.Builder info =
                RenderPdfDocumentFromStringSnippetRequestStream.Info.newBuilder();

        com.ironsoftware.ironpdf.internal.proto.ChromePdfRenderOptions renderOp = Render_Converter.ToProto(
                renderOptions);
        if (renderOp != null) {
            info.setRenderOptions(renderOp);
        }

        com.ironsoftware.ironpdf.internal.proto.ChromeHttpLoginCredentials login = Render_Converter.ToProto(
                loginCredentials);
        if (login != null) {
            info.setHttpOptions(login);
        }
        info.setBaseUrl(baseUrl != null ? baseUrl : "");

        RenderPdfDocumentFromStringSnippetRequestStream.Builder infoMsg =
                RenderPdfDocumentFromStringSnippetRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        while (htmlChunks.hasNext()) {
            char[] chunk = htmlChunks.next();
            RenderPdfDocumentFromStringSnippetRequestStream.Builder dataMsg = RenderPdfDocumentFromStringSnippetRequestStream.newBuilder();
            dataMsg.setHtmlChunk(String.valueOf(chunk));
            requestStream.onNext(dataMsg.build());
        }
        requestStream.onCompleted();

        WaitAndCheck(finishLatch, resultChunks);

        return HandlePdfDocumentChunks(resultChunks);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlFileAsPdf(String htmlFilePath,
                                                          ChromeHttpLoginCredentials loginCredentials
    ) throws IOException {
        return RenderHtmlFileAsPdf(htmlFilePath, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link InternalPdfDocument}.
     *
     * @param htmlFilePath  Path to a Html to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlFileAsPdf(String htmlFilePath,
                                                          ChromePdfRenderOptions renderOptions
    ) throws IOException {
        return RenderHtmlFileAsPdf(htmlFilePath, renderOptions, null);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url An absolute (fully formed) Uri.  Points to the Html document to be rendered as a
     *            PDF.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderUriAsPdf(String url) throws IOException {
        return RenderUriAsPdf(url, null, null);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url              An absolute (fully formed) Uri.  Points to the Html document to be
     *                         rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderUriAsPdf(String url,
                                                     ChromePdfRenderOptions renderOptions,
                                                     ChromeHttpLoginCredentials loginCredentials) throws IOException {
        RpcClient client = Access.EnsureConnection();
        RenderPdfDocumentFromUriRequest.Builder request = RenderPdfDocumentFromUriRequest.newBuilder();
        request.setUri(url);

        //optional;
        if (renderOptions != null) {
            request.setRenderOptions(Render_Converter.ToProto(renderOptions));
        }

        //optional;
        if (loginCredentials != null) {
            request.setHttpOptions(Render_Converter.ToProto(loginCredentials));
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);

        List<PdfDocumentResult> msgChunks = new ArrayList<>();

        PdfDocumentResult res = client.BlockingStub.renderFromUri(request.build());

        return HandlePdfDocumentResult(res);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url              An absolute (fully formed) Uri.  Points to the Html document to be
     *                         rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderUriAsPdf(String url,
                                                     ChromeHttpLoginCredentials loginCredentials) throws IOException {
        return RenderUriAsPdf(url, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link InternalPdfDocument}.
     *
     * @param url           An absolute (fully formed) Uri.  Points to the Html document to be
     *                      rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderUriAsPdf(String url,
                                                     ChromePdfRenderOptions renderOptions) throws IOException {
        return RenderUriAsPdf(url, renderOptions, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html The Html to be rendered as a PDF.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html) throws IOException {
        return RenderHtmlAsPdf(html, null, null, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or
     *                         URL context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html,
                                                      String baseUrl,
                                                      ChromeHttpLoginCredentials loginCredentials) throws IOException {
        return RenderHtmlAsPdf(html, null, loginCredentials, baseUrl);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html          The Html to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @param baseUrl       Optional. Setting the BaseURL property gives the relative file path or URL
     *                      context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html,
                                                      String baseUrl,
                                                      ChromePdfRenderOptions renderOptions) throws IOException {
        return RenderHtmlAsPdf(html, renderOptions, null, baseUrl);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html,
                                                      ChromePdfRenderOptions renderOptions,
                                                      ChromeHttpLoginCredentials loginCredentials) throws IOException {
        return RenderHtmlAsPdf(html, renderOptions, loginCredentials, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html    The Html to be rendered as a PDF.
     * @param baseUrl Optional. Setting the BaseURL property gives the relative file path or URL
     *                context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html,
                                                      String baseUrl) throws IOException {
        return RenderHtmlAsPdf(html, null, null, baseUrl);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html,
                                                      ChromeHttpLoginCredentials loginCredentials) throws IOException {
        return RenderHtmlAsPdf(html, null, loginCredentials, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link InternalPdfDocument}.
     *
     * @param html          The Html to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link InternalPdfDocument}
     */
    public static InternalPdfDocument RenderHtmlAsPdf(String html,
                                                      ChromePdfRenderOptions renderOptions) throws IOException {
        return RenderHtmlAsPdf(html, renderOptions, null, null);
    }
}
