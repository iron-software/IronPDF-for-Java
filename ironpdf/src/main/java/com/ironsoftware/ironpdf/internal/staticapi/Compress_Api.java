package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.compression.AdvancedCompressionOptions;
import com.ironsoftware.ironpdf.internal.proto.BytesResultStreamP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumCompressImagesRequestP;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumRemoveStructTreeRequestP;
import com.ironsoftware.ironpdf.internal.proto.QPdfCompressAndSaveAsAdvancedFromBytesRequestStreamP;
import com.ironsoftware.ironpdf.internal.proto.QPdfCompressAndSaveAsAdvancedRequestP;
import com.ironsoftware.ironpdf.internal.proto.QPdfCompressAndSaveAsRequestP;
import com.ironsoftware.ironpdf.internal.proto.QPdfCompressInMemoryRequestIdP;
import com.ironsoftware.ironpdf.internal.proto.QPdfCompressionFlagsP;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * The type Compress api.
 */
public final class Compress_Api {

    /**
     * Compress existing images using JPG encoding and the specified settings
     *
     * @param internalPdfDocument the internal pdf document
     * @param quality             (1 - 100) to use during compression
     * @param scaleToVisibleSize  Scale down the image resolution according to its visible size in the PDF document; may cause distortion with some image configurations. Default is false.
     */
    public static void compressImages(InternalPdfDocument internalPdfDocument, int quality,
                                      boolean scaleToVisibleSize) {
        compressImages(internalPdfDocument, quality, scaleToVisibleSize, false, 0);
    }

    /**
     * Compress images with optional DPI downsampling and high-quality subsampling toggle.
     *
     * @param internalPdfDocument   the internal pdf document
     * @param quality               JPEG quality (1-100), or 0 to skip quality recompression
     * @param scaleToVisibleSize    scale images down to their visible rendered size
     * @param useHqSampling         use 4:4:4 chroma subsampling when true; 4:1:1 when false
     * @param targetDpi             downsample images whose effective DPI exceeds this value;
     *                              pass {@code 0} to disable downsampling
     */
    public static void compressImages(InternalPdfDocument internalPdfDocument, int quality,
                                      boolean scaleToVisibleSize, boolean useHqSampling, int targetDpi) {
        RpcClient client = Access.ensureConnection();

        PdfiumCompressImagesRequestP.Builder req = PdfiumCompressImagesRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setQuality(quality);
        req.setScaleToVisibleSize(scaleToVisibleSize);
        req.setUseHqSampling(useHqSampling);
        if (targetDpi > 0) {
            req.setTargetDpi(targetDpi);
        }

        EmptyResultP res = client.GetBlockingStub("compressImages").pdfiumCompressCompressImages(req.build());

        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Compress the PDF in memory using QPdf smart compression and return the compressed bytes.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the compressed pdf byte array
     */
    public static byte[] compressInMemory(InternalPdfDocument internalPdfDocument) {
        return compressInMemory(internalPdfDocument, "");
    }

    /**
     * Compress the PDF in memory using QPdf smart compression and return the compressed bytes.
     *
     * @param internalPdfDocument the internal pdf document
     * @param password            the pdf password (empty string if none)
     * @return the compressed pdf byte array
     */
    public static byte[] compressInMemory(InternalPdfDocument internalPdfDocument, String password) {
        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();

        QPdfCompressInMemoryRequestIdP.Builder req = QPdfCompressInMemoryRequestIdP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPassword(password);

        client.GetStub("compressInMemory").qPdfCompressionCompressInMemoryFromId(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handleByteChunks(resultChunks);
    }

    /**
     * Compress the PDF using QPdf smart compression and save to a file path.
     *
     * @param internalPdfDocument the internal pdf document
     * @param outputFilePath      the output file path
     */
    public static void compressAndSaveAs(InternalPdfDocument internalPdfDocument, String outputFilePath) {
        compressAndSaveAs(internalPdfDocument, outputFilePath, "", -1);
    }

    /**
     * Compress the PDF using QPdf smart compression and save to a file path.
     *
     * @param internalPdfDocument the internal pdf document
     * @param outputFilePath      the output file path
     * @param jpegQuality         JPEG quality (1-100) for image recompression, or -1 to skip image recompression
     */
    public static void compressAndSaveAs(InternalPdfDocument internalPdfDocument, String outputFilePath, int jpegQuality) {
        compressAndSaveAs(internalPdfDocument, outputFilePath, "", jpegQuality);
    }

    /**
     * Compress the PDF using QPdf smart compression and save to a file path.
     *
     * @param internalPdfDocument the internal pdf document
     * @param outputFilePath      the output file path
     * @param password            the pdf password (empty string if none)
     * @param jpegQuality         JPEG quality (1-100) for image recompression, or -1 to skip image recompression
     */
    public static void compressAndSaveAs(InternalPdfDocument internalPdfDocument, String outputFilePath, String password, int jpegQuality) {
        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();

        QPdfCompressAndSaveAsRequestP.Builder req = QPdfCompressAndSaveAsRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setOutputPath(outputFilePath);
        req.setPassword(password);
        if (jpegQuality >= 1 && jpegQuality <= 100) {
            req.setJpeg(jpegQuality);
        }

        client.GetStub("compressAndSaveAs").qPdfCompressionCompressAndSaveAs(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        byte[] compressedBytes = Utils_Util.handleByteChunks(resultChunks);

        // Write the compressed bytes to the output file
        try {
            Files.write(Paths.get(outputFilePath), compressedBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write compressed PDF to " + outputFilePath, e);
        }
    }

    public static void compressStructTree(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();

        PdfiumRemoveStructTreeRequestP.Builder req = PdfiumRemoveStructTreeRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        EmptyResultP res = client.GetBlockingStub("compressStructTree").pdfiumCompressRemoveStructTree(req.build());

        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Apply the advanced compression pipeline and save the result to a file.
     *
     * <p>Runs an optional pdfium-side image DPI downsampling pass (driven by
     * {@link AdvancedCompressionOptions#getTargetImageDpi()} +
     * {@link AdvancedCompressionOptions#getJpegQuality()}) followed by a qpdf
     * pass configured by the remaining structural flags.</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param outputFilePath      the output file path
     * @param options             advanced compression options; pass a default-constructed
     *                            instance to reproduce the legacy {@code compressAndSaveAs}
     *                            behaviour with no image downsampling
     */
    public static void compressAndSaveAs(InternalPdfDocument internalPdfDocument,
                                         String outputFilePath,
                                         AdvancedCompressionOptions options) {
        compressAndSaveAs(internalPdfDocument, outputFilePath, "", options);
    }

    /**
     * Apply the advanced compression pipeline and save the result to a file.
     *
     * @param internalPdfDocument the internal pdf document
     * @param outputFilePath      the output file path
     * @param password            the pdf password (empty string if none)
     * @param options             advanced compression options
     */
    public static void compressAndSaveAs(InternalPdfDocument internalPdfDocument,
                                         String outputFilePath,
                                         String password,
                                         AdvancedCompressionOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("options must not be null");
        }

        // Step 1: Optional pdfium image downsampling + re-encode. When a target DPI is
        // set, every image is downsampled and re-encoded, with JpegQuality defaulting to
        // 85 when unset.
        if (options.pdfiumWillReEncode()) {
            int jpegQuality = options.getJpegQuality() != null ? options.getJpegQuality() : 85;
            compressImages(internalPdfDocument,
                    jpegQuality,
                    true, // scaleToVisibleSize — matches .NET useVisible: true
                    options.isHighQualityImageSubsampling(),
                    options.getTargetImageDpi());
        }

        // Step 2 — optional struct-tree removal before qpdf passes.
        if (options.isRemoveStructureTree()) {
            compressStructTree(internalPdfDocument);
        }

        // Step 3 — qpdf advanced pass.
        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();

        QPdfCompressAndSaveAsAdvancedRequestP.Builder req = QPdfCompressAndSaveAsAdvancedRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setOutputPath(outputFilePath);
        req.setPassword(password == null ? "" : password);
        req.setFlags(toFlagsProto(options));

        client.GetStub("compressAndSaveAsAdvanced")
                .qPdfCompressionCompressAndSaveAsAdvanced(req.build(),
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        byte[] compressedBytes = Utils_Util.handleByteChunks(resultChunks);

        try {
            Files.write(Paths.get(outputFilePath), compressedBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write compressed PDF to " + outputFilePath, e);
        }
    }

    /**
     * Apply the advanced compression pipeline to a byte[] input and save to disk.
     *
     * <p>Streams the input PDF bytes to the engine, runs the advanced qpdf pass with
     * the supplied {@link AdvancedCompressionOptions}, and writes the resulting bytes
     * to {@code outputFilePath}.</p>
     */
    public static void compressAndSaveAs(byte[] pdfBytes,
                                         String outputFilePath,
                                         String password,
                                         AdvancedCompressionOptions options) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("pdfBytes must not be null or empty");
        }
        if (options == null) {
            throw new IllegalArgumentException("options must not be null");
        }
        // Validate the output path once, up front, so this overload behaves consistently
        // regardless of options: the delegate path below writes via the instance overload
        // (Files.write + protobuf setOutputPath, both of which reject null/empty), so a
        // null/empty path must be rejected for the fast path too rather than silently
        // diverging.
        if (outputFilePath == null || outputFilePath.isEmpty()) {
            throw new IllegalArgumentException("outputFilePath must not be null or empty");
        }

        String pwd = password == null ? "" : password;

        // Image DPI downsampling and struct-tree removal require a loaded document. Open
        // the bytes into a temporary document (decrypting with the password) and delegate
        // to the instance overload so the image -> struct-tree -> qpdf orchestration
        // (Steps 1-3) lives in a single place, and the temporary document is released
        // deterministically via try-with-resources.
        //
        // This assumes the loaded-document qpdf RPC (used by the instance overload) and
        // the from-bytes qpdf RPC (fast path below) behave identically — both receive the
        // same toFlagsProto(options). The static-vs-instance parity test guards against
        // any gross divergence between the two engine RPCs.
        if (options.pdfiumWillReEncode() || options.isRemoveStructureTree()) {
            try (InternalPdfDocument doc = PdfDocument_Api.fromBytes(pdfBytes, pwd)) {
                // The document is already decrypted in the engine, so no password here.
                compressAndSaveAs(doc, outputFilePath, "", options);
            }
            return;
        }

        // Fast path: nothing to do pdfium-side — stream the raw bytes straight to the
        // qpdf from-bytes RPC (this also lets the engine decrypt encrypted input).
        RpcClient client = Access.ensureConnection();

        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<QPdfCompressAndSaveAsAdvancedFromBytesRequestStreamP> requestStream =
                client.GetStub("compressAndSaveAsAdvancedFromBytes")
                        .qPdfCompressionCompressAndSaveAsAdvancedFromBytes(
                                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        // Send Info first.
        QPdfCompressAndSaveAsAdvancedFromBytesRequestStreamP.InfoP info =
                QPdfCompressAndSaveAsAdvancedFromBytesRequestStreamP.InfoP.newBuilder()
                        .setOutputPath(outputFilePath == null ? "" : outputFilePath)
                        .setPassword(pwd)
                        .setFlags(toFlagsProto(options))
                        .build();
        requestStream.onNext(QPdfCompressAndSaveAsAdvancedFromBytesRequestStreamP.newBuilder()
                .setInfo(info).build());

        // Stream input bytes.
        for (Iterator<byte[]> it = Utils_Util.chunk(pdfBytes); it.hasNext(); ) {
            byte[] chunk = it.next();
            requestStream.onNext(QPdfCompressAndSaveAsAdvancedFromBytesRequestStreamP.newBuilder()
                    .setPdfBytesChunk(ByteString.copyFrom(chunk)).build());
        }
        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        byte[] compressedBytes = Utils_Util.handleByteChunks(resultChunks);

        if (outputFilePath != null && !outputFilePath.isEmpty()) {
            try {
                Files.write(Paths.get(outputFilePath), compressedBytes);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write compressed PDF to " + outputFilePath, e);
            }
        }
    }

    /**
     * Apply the advanced compression pipeline to an {@link InputStream} input
     * and save to disk.
     */
    public static void compressAndSaveAs(InputStream pdfStream,
                                         String outputFilePath,
                                         String password,
                                         AdvancedCompressionOptions options) throws IOException {
        if (pdfStream == null) {
            throw new IllegalArgumentException("pdfStream must not be null");
        }
        byte[] bytes = readAllBytes(pdfStream);
        compressAndSaveAs(bytes, outputFilePath, password, options);
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int read;
        while ((read = in.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toByteArray();
    }

    private static QPdfCompressionFlagsP toFlagsProto(AdvancedCompressionOptions options) {
        boolean pdfiumWillReEncode = options.pdfiumWillReEncode();

        QPdfCompressionFlagsP.Builder b = QPdfCompressionFlagsP.newBuilder()
                .setCompressStreams(options.isCompressStreams())
                .setCoalesceContents(options.isCoalesceContents())
                .setRemoveUnreferencedResources(options.isRemoveUnreferencedResources())
                .setRecompressFlate(options.isRecompressFlate())
                .setCompressionLevel(options.getCompressionLevel())
                .setObjectStreams(options.getObjectStreams().toWire())
                .setDecodeGeneralizedStreams(options.isDecodeGeneralizedStreams())
                .setOptimizeImagesMinWidth(options.getOptimizeImagesMinWidth())
                .setOptimizeImagesMinHeight(options.getOptimizeImagesMinHeight())
                .setOptimizeImagesMinArea(options.getOptimizeImagesMinArea())
                .setOptimizeImages(!pdfiumWillReEncode && options.getJpegQuality() != null);

        if (!pdfiumWillReEncode && options.getJpegQuality() != null) {
            b.setJpegQuality(options.getJpegQuality());
        }

        return b.build();
    }
}
