package com.ironsoftware.ironpdf.staticapi;


import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.internal.proto.BytesResultStream;
import com.ironsoftware.ironpdf.internal.proto.PdfDocumentConstructorStream;
import com.ironsoftware.ironpdf.internal.proto.PdfDocumentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.ironsoftware.ironpdf.staticapi.Utils_StringHelper.isNullOrWhiteSpace;
import static com.ironsoftware.ironpdf.staticapi.Utils_Util.*;

public final class PdfDocument_Api {

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @param password    Optional user password if the PDF document is encrypted.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException               Exception thrown if it can not be opened.
     * @throws IndexOutOfBoundsException pdfFilePath is null, empty, or consists only of white-space
     *                                   characters.
     */
    public static InternalPdfDocument FromFile(String pdfFilePath, String password)
            throws IOException {
        return FromFile(pdfFilePath, password, null);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath   The PDF file path.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,
     *                      modifying restrictions etc..).
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException               Exception thrown if it can not be opened.
     * @throws IndexOutOfBoundsException pdfFilePath is null, empty, or consists only of white-space
     *                                   characters.
     */
    public static InternalPdfDocument FromFile(String pdfFilePath, String password,
                                               String ownerPassword) throws IOException {
        if (isNullOrWhiteSpace(pdfFilePath)) {
            throw new IllegalArgumentException("Value 'pdfFilePath' cannot be null or empty.");
        }

        String absoluteFilePath = (new File(pdfFilePath)).getAbsolutePath();
        if (!(new File(absoluteFilePath)).isFile()) {
            throw new IOException(
                    String.format("%1$s is not a valid PDF file path. That file does not exist.",
                            absoluteFilePath));
        }

        byte[] pdfData = Files.readAllBytes(Paths.get(pdfFilePath));
        if (pdfData.length == 0) {
            throw new IOException(String.format(
                    "PdfData Stream is null or empty.  Can not create a PDF document from invalid data. %1$s",
                    pdfFilePath));
        }

        return FromBytes(pdfData, password, ownerPassword);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFileBytes  The PDF file data as byte array.
     * @param userPassword  Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,
     *                      modifying restrictions etc..).
     * @throws IOException Exception thrown if it can not be opened.
     */
    public static InternalPdfDocument FromBytes(byte[] pdfFileBytes, String userPassword,
                                                String ownerPassword) throws IOException {
        RpcClient client = Access.EnsureConnection();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ArrayList<PdfDocumentResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<PdfDocumentConstructorStream> requestStream =
                client.Stub.pdfDocumentFromBytes(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        // sending request
        PdfDocumentConstructorStream.Builder pdfDocumentConstructor_info = PdfDocumentConstructorStream.newBuilder();

        PdfDocumentConstructorStream.Info.Builder info = PdfDocumentConstructorStream.Info.newBuilder();

        if (!isNullOrWhiteSpace(userPassword) || !isNullOrWhiteSpace(ownerPassword)) {

            info.setUserPassword(!isNullOrWhiteSpace(userPassword) ? userPassword : "");

            info.setOwnerPassword(!isNullOrWhiteSpace(ownerPassword) ? ownerPassword : "");

            pdfDocumentConstructor_info.setInfo(info);
            requestStream.onNext(pdfDocumentConstructor_info.build());
        }
        for (Iterator<byte[]> it = Chunk(pdfFileBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            PdfDocumentConstructorStream.Builder pdfDocumentConstructor_data = PdfDocumentConstructorStream.newBuilder();

            pdfDocumentConstructor_data.setPdfBytesChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(pdfDocumentConstructor_data.build());
        }

        //finish sending
        requestStream.onCompleted();

        WaitAndCheck(finishLatch, resultChunks);

        return HandlePdfDocumentChunks(resultChunks);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException               Exception thrown if it can not be opened.
     * @throws IndexOutOfBoundsException pdfFilePath is null, empty, or consists only of white-space
     *                                   characters.
     */
    public static InternalPdfDocument FromFile(String pdfFilePath) throws IOException {
        return FromFile(pdfFilePath, null, null);
    }

    public static void SaveAs(InternalPdfDocument pdfDocument, String filePath) throws IOException {
        byte[] data = PdfDocument_Api.GetBytes(pdfDocument);
        SaveAs(data, filePath);
    }

    /**
     * Gets the binary data for the full PDF file as a byte array.
     */
    public static byte[] GetBytes(InternalPdfDocument pdfDocument) throws IOException {
        RpcClient client = Access.EnsureConnection();

        //for checking that the response stream is finished
        final CountDownLatch finishLatch = new CountDownLatch(1);

//        ArrayList<byte[]> chunks = new ArrayList<byte[]>();
        List<BytesResultStream> resultChunks = new ArrayList<>();

        client.Stub.pdfDocumentGetBinaryData(pdfDocument.remoteDocument,
                //response handler
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks)
        );

        WaitAndCheck(finishLatch, resultChunks);

        List<byte[]> bytesChunks = resultChunks.stream().map(res -> {
                    if (res.getResultOrExceptionCase() == BytesResultStream.ResultOrExceptionCase.EXCEPTION) {
                        throw Exception_Converter.FromProto(res.getException());
                    }
                    return res.getResultChunk().toByteArray();
                }
        ).collect(Collectors.toList());

        return CombineChunk(bytesChunks);
    }

    public static void SaveAs(byte[] pdfData, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
        }
        try (FileOutputStream stream = new FileOutputStream(filePath)) {
            stream.write(pdfData);
        }
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFileBytes The PDF file data as byte array.
     * @param userPassword Optional user password if the PDF document is encrypted.
     * @throws IOException Exception thrown if it can not be opened.
     */

    public static InternalPdfDocument FromBytes(byte[] pdfFileBytes, String userPassword)
            throws IOException {
        return FromBytes(pdfFileBytes, userPassword, null);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFileBytes The PDF file data as byte array.
     * @throws IOException Exception thrown if it can not be opened.
     */
    public static InternalPdfDocument FromBytes(byte[] pdfFileBytes) throws IOException {
        return FromBytes(pdfFileBytes, null, null);
    }
}
