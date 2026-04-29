package com.ironsoftware.ironpdf.internal.staticapi;
 
import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.BooleanResultP;
import com.ironsoftware.ironpdf.internal.proto.BytesResultStreamP;
import com.ironsoftware.ironpdf.internal.proto.QPdfIsLinearizedRequestStreamP;
import com.ironsoftware.ironpdf.internal.proto.QPdfLinearizeInMemoryRequestIdP;
import com.ironsoftware.ironpdf.internal.proto.QPdfLinearizeInMemoryRequestStreamP;
import com.ironsoftware.ironpdf.internal.proto.QPdfSaveAsLinearizedFromBytesRequestStreamP;
import com.ironsoftware.ironpdf.internal.proto.QPdfSaveAsLinearizedRequestP;
import com.ironsoftware.ironpdf.render.LinearizationMode;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
 
/**
 * The type Linearize api.
 * <p>Provides linearization (a.k.a. "fast web view") operations backed by QPdf.
 * Linearized PDFs can be streamed and displayed incrementally in web browsers.</p>
 */
public final class Linearize_Api {
 
    private static final Logger logger = LoggerFactory.getLogger(Linearize_Api.class);
 
    private Linearize_Api() {
        // static-only
    }
 
    /**
     * Core linearization logic shared across all linearization methods. Implements the
     * {@link LinearizationMode} strategy pattern. Mirrors {@code PdfDocument.LinearizePdfCore}
     * on the C# side.
     */
    public static byte[] linearizeCoreFromBytes(byte[] pdfBytes, String password, LinearizationMode mode) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("The PDF bytes array cannot be null or empty.");
        }
        LinearizationMode selectedMode = mode == null ? LinearizationMode.Automatic : mode;
 
        if (selectedMode == LinearizationMode.InMemory) {
            return linearizeInMemoryFromBytes(pdfBytes, password);
        }
 
        if (selectedMode == LinearizationMode.FileBased) {
            // User explicitly requested FileBased. If disk fails, let exception bubble up.
            return linearizeViaTempFile(pdfBytes, password);
        }
 
        // Automatic mode
        if (canWriteToTemp()) {
            try {
                return linearizeViaTempFile(pdfBytes, password);
            } catch (Exception ex) {
                logger.info("Automatic Linearization: Disk attempt failed ({}). "
                        + "Falling back to Memory linearization.", ex.getMessage());
                return linearizeInMemoryFromBytes(pdfBytes, password);
            }
        }
        logger.info("Automatic Linearization: No write permission detected. Using Memory linearization.");
        return linearizeInMemoryFromBytes(pdfBytes, password);
    }
 
    /**
     * Variant of {@link #linearizeCoreFromBytes(byte[], String, LinearizationMode)} that starts
     * from an open document on the engine. The {@link LinearizationMode#FileBased} and
     * {@link LinearizationMode#Automatic} paths fall through to the bytes-based core after
     * fetching the document binary, since the file-based RPC needs raw bytes anyway.
     */
    public static byte[] linearizeCoreFromDocument(InternalPdfDocument internalPdfDocument,
                                            String password,
                                            LinearizationMode mode) {
        LinearizationMode selectedMode = mode == null ? LinearizationMode.Automatic : mode;
 
        if (selectedMode == LinearizationMode.InMemory) {
            return linearizeInMemory(internalPdfDocument, password);
        }
        // For FileBased / Automatic, fetch the document bytes once and delegate.
        byte[] pdfBytes = PdfDocument_Api.getBytes(internalPdfDocument, false);
        return linearizeCoreFromBytes(pdfBytes, password, selectedMode);
    }
 
    /**
     * Linearize via the engine's file-based RPC and persist the result through a client-side
     * temporary file. The client-side disk write is the difference between this and
     * {@link #linearizeInMemoryFromBytes(byte[], String)} — when the client filesystem is
     * read-only, {@code FileBased} mode fails here so {@link LinearizationMode#Automatic}
     * can fall back. Mirrors C# {@code PdfDocument.LinearizeViaTempFile}.
     */
    private static byte[] linearizeViaTempFile(byte[] pdfBytes, String password) {
        // Engine-side file-based RPC retrieves the linearized bytes.
        byte[] linearized = saveAsLinearizedToBytes(pdfBytes, password);
 
        // Persist to a client-side temp file as the disk-touching part of the contract.
        Path tempPath = null;
        try {
            tempPath = Files.createTempFile("ironpdf-linearize-", ".pdf");
            Files.write(tempPath, linearized);
            return Files.readAllBytes(tempPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to perform file-based linearization: " + e.getMessage(), e);
        } finally {
            if (tempPath != null) {
                try {
                    Files.deleteIfExists(tempPath);
                } catch (IOException ignored) {
                    // best-effort cleanup
                }
            }
        }
    }
 
    /**
     * Internal helper that calls the file-based bytes-streaming RPC and returns the
     * accumulated response bytes (the engine writes its own temp file and streams the
     * result back; we keep it in memory).
     */
    private static byte[] saveAsLinearizedToBytes(byte[] pdfBytes, String password) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("The PDF bytes array cannot be null or empty.");
        }
 
        RpcClient client = Access.ensureConnection();
 
        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();
 
        StreamObserver<QPdfSaveAsLinearizedFromBytesRequestStreamP> requestStream = client
                .GetStub("saveAsLinearizedToBytes")
                .qPdfLinearizationSaveAsLinearizedFromBytes(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));
 
        QPdfSaveAsLinearizedFromBytesRequestStreamP.InfoP.Builder info =
                QPdfSaveAsLinearizedFromBytesRequestStreamP.InfoP.newBuilder();
        info.setOutputPath(""); // engine ignores this and uses its own temp file
        info.setPassword(password == null ? "" : password);
        requestStream.onNext(QPdfSaveAsLinearizedFromBytesRequestStreamP.newBuilder().setInfo(info).build());
 
        for (Iterator<byte[]> it = Utils_Util.chunk(pdfBytes); it.hasNext(); ) {
            byte[] chunkData = it.next();
            requestStream.onNext(QPdfSaveAsLinearizedFromBytesRequestStreamP.newBuilder()
                    .setPdfBytesChunk(ByteString.copyFrom(chunkData))
                    .build());
        }
 
        requestStream.onCompleted();
 
        Utils_Util.waitAndCheck(finishLatch, resultChunks);
 
        return Utils_Util.handleByteChunks(resultChunks);
    }
 
    /**
     * Probe whether the JVM can create files in the system temp directory.
     */
    private static boolean canWriteToTemp() {
        Path probe = null;
        try {
            probe = Files.createTempFile("ironpdf-probe-", ".tmp");
            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            if (probe != null) {
                try {
                    Files.deleteIfExists(probe);
                } catch (IOException ignored) {
                    // best-effort cleanup
                }
            }
        }
    }
 
    /**
     * Check whether the PDF file at the given path is linearized.
     *
     * @param filePath the pdf file path
     * @return true if the file is linearized, false otherwise
     * @throws IOException if the file cannot be read
     */
    public static boolean isLinearized(String filePath) throws IOException {
        return isLinearized(filePath, "");
    }
 
    /**
     * Check whether the PDF file at the given path is linearized.
     *
     * @param filePath the pdf file path
     * @param password the pdf password (empty string if none)
     * @return true if the file is linearized, false otherwise
     * @throws IOException if the file cannot be read
     */
    public static boolean isLinearized(String filePath, String password) throws IOException {
        if (Utils_StringHelper.isNullOrWhiteSpace(filePath)) {
            throw new IllegalArgumentException("Value 'filePath' cannot be null or empty.");
        }
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
 
        RpcClient client = Access.ensureConnection();
 
        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BooleanResultP> results = new ArrayList<>();
 
        StreamObserver<QPdfIsLinearizedRequestStreamP> requestStream = client.GetStub("isLinearized")
                .qPdfLinearizationIsLinearized(new Utils_ReceivingCustomStreamObserver<>(finishLatch, results));
 
        QPdfIsLinearizedRequestStreamP.InfoP.Builder info = QPdfIsLinearizedRequestStreamP.InfoP.newBuilder();
        info.setPassword(password == null ? "" : password);
        requestStream.onNext(QPdfIsLinearizedRequestStreamP.newBuilder().setInfo(info).build());
 
        for (Iterator<byte[]> it = Utils_Util.chunk(fileBytes); it.hasNext(); ) {
            byte[] chunkData = it.next();
            requestStream.onNext(QPdfIsLinearizedRequestStreamP.newBuilder()
                    .setPdfBytesChunk(ByteString.copyFrom(chunkData))
                    .build());
        }
 
        requestStream.onCompleted();
 
        Utils_Util.waitAndCheck(finishLatch, results);
 
        BooleanResultP res = results.get(0);
        if (res.getResultOrExceptionCase() == BooleanResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }
        return res.getResult();
    }
 
    /**
     * Linearize the PDF in memory and return the linearized bytes.
     *
     * @param internalPdfDocument the internal pdf document
     * @return the linearized pdf byte array
     */
    public static byte[] linearizeInMemory(InternalPdfDocument internalPdfDocument) {
        return linearizeInMemory(internalPdfDocument, "");
    }
 
    /**
     * Linearize the PDF in memory and return the linearized bytes.
     *
     * @param internalPdfDocument the internal pdf document
     * @param password            the pdf password (empty string if none)
     * @return the linearized pdf byte array
     */
    public static byte[] linearizeInMemory(InternalPdfDocument internalPdfDocument, String password) {
        RpcClient client = Access.ensureConnection();
 
        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();
 
        QPdfLinearizeInMemoryRequestIdP.Builder req = QPdfLinearizeInMemoryRequestIdP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setPassword(password == null ? "" : password);
 
        client.GetStub("linearizeInMemory").qPdfLinearizationLinearizeInMemoryFromId(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );
 
        Utils_Util.waitAndCheck(finishLatch, resultChunks);
 
        return Utils_Util.handleByteChunks(resultChunks);
    }
 
    /**
     * Linearize the given PDF bytes in memory and return the linearized bytes.
     *
     * @param pdfBytes the pdf bytes
     * @return the linearized pdf byte array
     */
    public static byte[] linearizeInMemoryFromBytes(byte[] pdfBytes) {
        return linearizeInMemoryFromBytes(pdfBytes, "");
    }
 
    /**
     * Linearize the given PDF bytes in memory and return the linearized bytes.
     *
     * @param pdfBytes the pdf bytes
     * @param password the pdf password (empty string if none)
     * @return the linearized pdf byte array
     */
    public static byte[] linearizeInMemoryFromBytes(byte[] pdfBytes, String password) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("The PDF bytes array cannot be null or empty.");
        }
 
        RpcClient client = Access.ensureConnection();
 
        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();
 
        StreamObserver<QPdfLinearizeInMemoryRequestStreamP> requestStream = client.GetStub("linearizeInMemoryFromBytes")
                .qPdfLinearizationLinearizeInMemory(new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));
 
        QPdfLinearizeInMemoryRequestStreamP.InfoP.Builder info = QPdfLinearizeInMemoryRequestStreamP.InfoP.newBuilder();
        info.setPassword(password == null ? "" : password);
        requestStream.onNext(QPdfLinearizeInMemoryRequestStreamP.newBuilder().setInfo(info).build());
 
        for (Iterator<byte[]> it = Utils_Util.chunk(pdfBytes); it.hasNext(); ) {
            byte[] chunkData = it.next();
            requestStream.onNext(QPdfLinearizeInMemoryRequestStreamP.newBuilder()
                    .setPdfBytesChunk(ByteString.copyFrom(chunkData))
                    .build());
        }
 
        requestStream.onCompleted();
 
        Utils_Util.waitAndCheck(finishLatch, resultChunks);
 
        return Utils_Util.handleByteChunks(resultChunks);
    }
 
    /**
     * Linearize the PDF and save it to the given output file path (via the file-based RPC).
     *
     * @param internalPdfDocument the internal pdf document
     * @param outputFilePath      the output file path
     */
    public static void saveAsLinearized(InternalPdfDocument internalPdfDocument, String outputFilePath) {
        saveAsLinearized(internalPdfDocument, outputFilePath, "");
    }
 
    /**
     * Linearize the PDF and save it to the given output file path (via the file-based RPC).
     *
     * @param internalPdfDocument the internal pdf document
     * @param outputFilePath      the output file path
     * @param password            the pdf password (empty string if none)
     */
    public static void saveAsLinearized(InternalPdfDocument internalPdfDocument, String outputFilePath, String password) {
        if (Utils_StringHelper.isNullOrWhiteSpace(outputFilePath)) {
            throw new IllegalArgumentException("Value 'outputFilePath' cannot be null or empty.");
        }
 
        RpcClient client = Access.ensureConnection();
 
        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();
 
        QPdfSaveAsLinearizedRequestP.Builder req = QPdfSaveAsLinearizedRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        req.setOutputPath(outputFilePath);
        req.setPassword(password == null ? "" : password);
 
        client.GetStub("saveAsLinearized").qPdfLinearizationSaveAsLinearized(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );
 
        Utils_Util.waitAndCheck(finishLatch, resultChunks);
 
        byte[] linearizedBytes = Utils_Util.handleByteChunks(resultChunks);
 
        // Persist the linearized bytes to the output path (matches Compress_Api.compressAndSaveAs behavior).
        try {
            Files.write(Paths.get(outputFilePath), linearizedBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write linearized PDF to " + outputFilePath, e);
        }
    }
 
    /**
     * Linearize the given PDF bytes and save the result to the given output file path
     * (via the bytes-streaming file-based RPC).
     *
     * @param pdfBytes       the pdf bytes
     * @param outputFilePath the output file path
     */
    public static void saveAsLinearizedFromBytes(byte[] pdfBytes, String outputFilePath) {
        saveAsLinearizedFromBytes(pdfBytes, outputFilePath, "");
    }
 
    /**
     * Linearize the given PDF bytes and save the result to the given output file path
     * (via the bytes-streaming file-based RPC).
     *
     * @param pdfBytes       the pdf bytes
     * @param outputFilePath the output file path
     * @param password       the pdf password (empty string if none)
     */
    public static void saveAsLinearizedFromBytes(byte[] pdfBytes, String outputFilePath, String password) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("The PDF bytes array cannot be null or empty.");
        }
        if (Utils_StringHelper.isNullOrWhiteSpace(outputFilePath)) {
            throw new IllegalArgumentException("Value 'outputFilePath' cannot be null or empty.");
        }
 
        RpcClient client = Access.ensureConnection();
 
        final CountDownLatch finishLatch = new CountDownLatch(1);
        List<BytesResultStreamP> resultChunks = new ArrayList<>();
 
        StreamObserver<QPdfSaveAsLinearizedFromBytesRequestStreamP> requestStream = client.GetStub("saveAsLinearizedFromBytes")
                .qPdfLinearizationSaveAsLinearizedFromBytes(new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));
 
        QPdfSaveAsLinearizedFromBytesRequestStreamP.InfoP.Builder info = QPdfSaveAsLinearizedFromBytesRequestStreamP.InfoP.newBuilder();
        info.setOutputPath(outputFilePath);
        info.setPassword(password == null ? "" : password);
        requestStream.onNext(QPdfSaveAsLinearizedFromBytesRequestStreamP.newBuilder().setInfo(info).build());
 
        for (Iterator<byte[]> it = Utils_Util.chunk(pdfBytes); it.hasNext(); ) {
            byte[] chunkData = it.next();
            requestStream.onNext(QPdfSaveAsLinearizedFromBytesRequestStreamP.newBuilder()
                    .setPdfBytesChunk(ByteString.copyFrom(chunkData))
                    .build());
        }
 
        requestStream.onCompleted();
 
        Utils_Util.waitAndCheck(finishLatch, resultChunks);
 
        byte[] linearizedBytes = Utils_Util.handleByteChunks(resultChunks);
 
        try {
            Files.write(Paths.get(outputFilePath), linearizedBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write linearized PDF to " + outputFilePath, e);
        }
    }
}