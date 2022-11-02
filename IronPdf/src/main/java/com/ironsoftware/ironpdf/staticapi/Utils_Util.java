package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.internal.proto.ImagesResultStream;
import com.ironsoftware.ironpdf.internal.proto.PdfDocumentResult;
import com.ironsoftware.ironpdf.internal.proto.RawImageChunkWithIndex;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ironsoftware.ironpdf.staticapi.Utils_StringHelper.isNullOrEmpty;

final class Utils_Util {

    static final int CHUNK_SIZE = 65536; // 64 * 1024 // 65.53600 kilobytes // GRPC msg size limit 4MB

    static String EnumComparable(String input) {
        return input.toUpperCase().trim().replace("_", "").replace("-", "");
    }

    static String NullGuard(String input) {
        if (isNullOrEmpty(input)) {
            return "";
        }

        return input;
    }

    static String NullIfEmpty(String input) {
        return isNullOrEmpty(input) ? null : input;
    }

    static <T> Iterator<T[]> Chunk(final T[] data) {
        return Chunk(data, CHUNK_SIZE);
    }

    @SuppressWarnings("SameParameterValue")
    static <T> Iterator<T[]> Chunk(T[] data, int chunkSize) {
        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((data.length + chunkSize - 1) / chunkSize)
                .mapToObj(i -> Arrays.copyOfRange(data, i, Math.min(i + chunkSize, data.length)))
                .iterator();
    }

    static Iterator<byte[]> Chunk(final byte[] data) {
        return Chunk(data, CHUNK_SIZE);
    }

    @SuppressWarnings("SameParameterValue")
    static Iterator<byte[]> Chunk(byte[] data, int chunkSize) {
        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((data.length + chunkSize - 1) / chunkSize)
                .mapToObj(i -> Arrays.copyOfRange(data, i, Math.min(i + chunkSize, data.length)))
                .iterator();
    }

    static Iterator<char[]> Chunk(final char[] data) {
        return Chunk(data, CHUNK_SIZE);
    }

    @SuppressWarnings("SameParameterValue")
    static Iterator<char[]> Chunk(char[] data, int chunkSize) {
        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((data.length + chunkSize - 1) / chunkSize)
                .mapToObj(i -> Arrays.copyOfRange(data, i, Math.min(i + chunkSize, data.length)))
                .iterator();
    }

    static void HandleEmptyResult(EmptyResult emptyResult) {
        if (emptyResult.getResultOrExceptionCase() == EmptyResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(emptyResult.getException());
        }
    }

    static void HandleEmptyResultChunks(List<EmptyResult> emptyResultChunks) throws IOException {
        if (emptyResultChunks.size() == 0) {
            throw new IOException("No response from IronPdf.");
        }

        EmptyResult res = emptyResultChunks.stream().findFirst().get();

        HandleEmptyResult(res);
    }

    static List<byte[]> HandleImagesResult(List<ImagesResultStream> resultChunks) throws IOException {
        if (resultChunks.size() == 0) {
            throw new IOException("No response from IronPdf.");
        }
        resultChunks.stream().filter(ImagesResultStream::hasException).findFirst().ifPresent(x -> {
            throw Exception_Converter.FromProto(x.getException());
        });

        java.util.Map<Integer, List<byte[]>> grouped = resultChunks.stream()
                .map(ImagesResultStream::getRawImagesChunk)
                .collect(Collectors.groupingBy(RawImageChunkWithIndex::getImageIndex,
                        Collectors.mapping(c -> c.getRawImageChunk().toByteArray(), Collectors.toList())));

        return grouped.values().stream().map(Utils_Util::CombineChunk).collect(Collectors.toList());
    }

    static byte[] CombineChunk(List<byte[]> chunks) {
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

    static InternalPdfDocument HandlePdfDocumentResult(PdfDocumentResult res) {
        if (res.getResultOrExceptionCase() == PdfDocumentResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(res.getException());
        }

        return new InternalPdfDocument(res.getResult());
    }

    static InternalPdfDocument HandlePdfDocumentChunks(List<PdfDocumentResult> resChunks) throws IOException {
        if (resChunks.size() == 0) {
            throw new IOException("No response from IronPdf.");
        }

        PdfDocumentResult res = resChunks.stream().findFirst().get();

        return HandlePdfDocumentResult(res);
    }

    static <T> void WaitAndCheck(CountDownLatch finishLatch, List<T> resultChunks)
            throws IOException {
        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (resultChunks.size() == 0) {
            throw new IOException("No response from IronPdf.");
        }
    }
}

