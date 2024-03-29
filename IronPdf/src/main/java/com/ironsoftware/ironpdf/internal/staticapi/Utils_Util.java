package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;
import org.slf4j.Logger;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ironsoftware.ironpdf.internal.staticapi.Utils_StringHelper.isNullOrEmpty;

/**
 * The type Utils util.
 */
final class Utils_Util {

    /**
     * The Chunk size.
     */
    static final int CHUNK_SIZE = 65536; // 64 * 1024 // 65.53600 kilobytes // GRPC msg size limit 4MB

    /**
     * Enum comparable string.
     *
     * @param input the input
     * @return the string
     */
    static String enumComparable(String input) {
        return input.toUpperCase().trim().replace("_", "").replace("-", "");
    }

    /**
     * Null guard string.
     *
     * @param input the input
     * @return the string
     */
    static String nullGuard(String input) {
        if (isNullOrEmpty(input)) {
            return "";
        }

        return input;
    }

    /**
     * Null if empty string.
     *
     * @param input the input
     * @return the string
     */
    static String nullIfEmpty(String input) {
        return isNullOrEmpty(input) ? null : input;
    }

    /**
     * Chunk list.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the list
     */
    static <T> List<T[]> chunk(final T[] data) {
        return chunk(data, CHUNK_SIZE);
    }

    /**
     * Chunk list.
     *
     * @param <T>       the type parameter
     * @param data      the data
     * @param chunkSize the chunk size
     * @return the list
     */
    @SuppressWarnings("SameParameterValue")
    static <T> List<T[]> chunk(T[] data, int chunkSize) {
        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((data.length + chunkSize - 1) / chunkSize)
                .mapToObj(i -> Arrays.copyOfRange(data, i, Math.min(i + chunkSize, data.length)))
                .collect(Collectors.toList());

    }

    /**
     * Chunk iterator.
     *
     * @param data the data
     * @return the iterator
     */
    static Iterator<byte[]> chunk(final byte[] data) {
        return chunk(data, CHUNK_SIZE);
    }

    /**
     * Chunk iterator.
     *
     * @param data      the data
     * @param chunkSize the chunk size
     * @return the iterator
     */
    @SuppressWarnings("SameParameterValue")
    static Iterator<byte[]> chunk(byte[] data, int chunkSize) {
        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((data.length + chunkSize - 1) / chunkSize)
                .mapToObj(i -> Arrays.copyOfRange(data, i, Math.min(i + chunkSize, data.length)))
                .iterator();
    }

    /**
     * Chunk iterator.
     *
     * @param data the data
     * @return the iterator
     */
    static Iterator<char[]> chunk(final char[] data) {
        return chunk(data, CHUNK_SIZE);
    }

    /**
     * Chunk iterator.
     *
     * @param data      the data
     * @param chunkSize the chunk size
     * @return the iterator
     */
    @SuppressWarnings("SameParameterValue")
    static Iterator<char[]> chunk(char[] data, int chunkSize) {
        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((data.length + chunkSize - 1) / chunkSize)
                .mapToObj(i -> Arrays.copyOfRange(data, i, Math.min(i + chunkSize, data.length)))
                .iterator();
    }

    /**
     * Handle empty result.
     *
     * @param emptyResult the empty result
     */
    static void handleEmptyResult(EmptyResultP emptyResult) {
        if (emptyResult.getResultOrExceptionCase() == EmptyResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(emptyResult.getException());
        }
    }

    /**
     * Handle empty result chunks.
     *
     * @param emptyResultChunks the empty result chunks
     */
    static void handleEmptyResultChunks(List<EmptyResultP> emptyResultChunks) {
        if (emptyResultChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdf.");
        }

        EmptyResultP res = emptyResultChunks.stream().findFirst().get();

        handleEmptyResult(res);
    }

    /**
     * Handle boolean result boolean.
     *
     * @param booleanResult the boolean result
     * @return the boolean
     */
    static boolean handleBooleanResult(BooleanResultP booleanResult) {
        if (booleanResult.getResultOrExceptionCase() == BooleanResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(booleanResult.getException());
        }
        return booleanResult.getResult();
    }

    /**
     * Handle images result list.
     *
     * @param resultChunks the result chunks
     * @return the list
     * @throws IOException the io exception
     */
    static List<byte[]> handleImagesResult(List<ImagesResultStreamP> resultChunks) throws IOException {
        if (resultChunks.size() == 0) {
            throw new IOException("No response from IronPdf.");
        }
        resultChunks.stream().filter(ImagesResultStreamP::hasException).findFirst().ifPresent(x -> {
            throw Exception_Converter.fromProto(x.getException());
        });

        java.util.Map<Integer, List<byte[]>> grouped = resultChunks.stream()
                .map(ImagesResultStreamP::getRawImagesChunk)
                .collect(Collectors.groupingBy(RawImageChunkWithIndexP::getImageIndex,
                        Collectors.mapping(c -> c.getRawImageChunk().toByteArray(), Collectors.toList())));

        return grouped.values().stream().map(Utils_Util::combineChunk).collect(Collectors.toList());
    }

    /**
     * Handle image result list.
     *
     * @param resultChunks the result chunks
     * @return the list
     * @throws IOException the io exception
     */
    static byte[] handleImageResult(List<ImageResultStreamP> resultChunks) throws IOException {
        if (resultChunks.size() == 0) {
            throw new IOException("No response from IronPdf.");
        }
        resultChunks.stream().filter(ImageResultStreamP::hasException).findFirst().ifPresent(x -> {
            throw Exception_Converter.fromProto(x.getException());
        });
        List<byte[]> chunks = resultChunks.stream()
                .map(c -> c.getRawImageChunk().toByteArray()).collect(Collectors.toList());
        return Utils_Util.combineChunk(chunks);
    }

    /**
     * Combine chunk byte [ ].
     *
     * @param chunks the chunks
     * @return the byte [ ]
     */
    static byte[] combineChunk(List<byte[]> chunks) {
        int totalLength = 0;
        for (byte[] chunk : chunks) {
            totalLength += chunk.length;
        }
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, result, offset, chunk.length);
            offset += chunk.length;
        }
        return result;
    }

    /**
     * Handle pdf document result internal pdf document.
     *
     * @param res the res
     * @return the internal pdf document
     */
    static InternalPdfDocument handlePdfDocumentResult(PdfDocumentResultP res) {
        if (res.getResultOrExceptionCase() == PdfDocumentResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(res.getException());
        }

        return new InternalPdfDocument(res.getResult());
    }

    /**
     * Handle pdf document chunks internal pdf document.
     *
     * @param resChunks the res chunks
     * @return the internal pdf document
     */
    static InternalPdfDocument handlePdfDocumentChunks(List<PdfDocumentResultP> resChunks) {
        if (resChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdf.");
        }

        PdfDocumentResultP res = resChunks.stream().findFirst().get();

        return handlePdfDocumentResult(res);
    }

    static byte[] handleByteChunks(List<BytesResultStreamP> resultChunks){
        List<byte[]> bytesChunks = resultChunks.stream().map(res -> {
                    if (res.getResultOrExceptionCase() == BytesResultStreamP.ResultOrExceptionCase.EXCEPTION) {
                        throw Exception_Converter.fromProto(res.getException());
                    }
                    return res.getResultChunk().toByteArray();
                }
        ).collect(Collectors.toList());

        return Utils_Util.combineChunk(bytesChunks);
    }

    /**
     * Wait and check.
     *
     * @param <T>          the type parameter
     * @param finishLatch  the finish latch
     * @param resultChunks the result chunks
     */
    static <T> void waitAndCheck(CountDownLatch finishLatch, List<T> resultChunks) {
        try {
            finishLatch.await(Setting_Api.ironPdfEngineTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (resultChunks.size() == 0) {
            throw new RuntimeException("No response from IronPdfEngine.");
        }
    }

    static void logInfoOrSystemOut(Logger logger, String msg) {
        if (logger.isInfoEnabled())
            logger.info(msg);
        else
            System.out.println(msg);
    }
}

