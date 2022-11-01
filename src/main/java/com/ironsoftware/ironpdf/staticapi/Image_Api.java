package com.ironsoftware.ironpdf.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.image.ImageBehavior;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.ironsoftware.ironpdf.staticapi.Render_Converter.ToProto;
import static com.ironsoftware.ironpdf.staticapi.Utils_Util.*;

public final class Image_Api {

    /**
     * Converts multiple image files to a PDF document.  Each image creates 1 page which matches the
     * image dimensions. The default PaperSize is A4. You can set it via
     * ImageToPdfConverter.PaperSize. Note: Imaging.ImageBehavior.CropPage will set PaperSize equal to
     * ImageSize.
     *
     * @param imagesBytes   The image file as bytes.
     * @param imageBehavior Describes how image should be placed on the PDF pages
     */
    public static InternalPdfDocument ImageToPdf(Iterator<byte[]> imagesBytes,
                                                 ImageBehavior imageBehavior) throws IOException {
        return ImageToPdf(imagesBytes, imageBehavior, null);
    }

    /**
     * Converts multiple image files to a PDF document.  Each image creates 1 page which matches the
     * image dimensions. The default PaperSize is A4. You can set it via
     * ImageToPdfConverter.PaperSize. Note: Imaging.ImageBehavior.CropPage will set PaperSize equal to
     * ImageSize.
     *
     * @param imagesBytes   The image file as bytes.
     * @param imageBehavior Describes how image should be placed on the PDF page
     * @param renderOptions Rendering options
     */
    public static InternalPdfDocument ImageToPdf(Iterator<byte[]> imagesBytes,
                                                 ImageBehavior imageBehavior, ChromePdfRenderOptions renderOptions) throws IOException {
        RpcClient client = Access.EnsureConnection();

        ImageToPdfRequestStream.Info.Builder info = ImageToPdfRequestStream.Info.newBuilder();
        info.setRenderOptions(
                ToProto((renderOptions != null ? renderOptions : new ChromePdfRenderOptions())));
        info.setImageBehavior(Image_Converter.ToProto(imageBehavior));

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<PdfDocumentResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<ImageToPdfRequestStream> requestStream =
                client.Stub.pdfDocumentImageImageToPdf(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        ImageToPdfRequestStream.Builder infoMsg =
                ImageToPdfRequestStream.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        int imageIndex = 0;
        while (imagesBytes.hasNext()) {
            byte[] image = imagesBytes.next();
            Iterator<byte[]> chunks = Chunk(image);
            while (chunks.hasNext()) {
                byte[] chunk = chunks.next();
                ImageToPdfRequestStream.Builder msg = ImageToPdfRequestStream.newBuilder();
                RawImageChunkWithIndex.Builder rawImageChunkWithIndex = RawImageChunkWithIndex.newBuilder();
                rawImageChunkWithIndex.setRawImageChunk(ByteString.copyFrom(chunk));
                rawImageChunkWithIndex.setImageIndex(imageIndex);
                msg.setRawImagesChunk(rawImageChunkWithIndex);
                requestStream.onNext(msg.build());
            }
            imageIndex++;
        }

        requestStream.onCompleted();

        WaitAndCheck(finishLatch, resultChunks);

        return HandlePdfDocumentChunks(resultChunks);
    }

    /**
     * Converts multiple image files to a PDF document.  Each image creates 1 page which matches the
     * image dimensions. The default PaperSize is A4. You can set it via
     * ImageToPdfConverter.PaperSize. Note: Imaging.ImageBehavior.CropPage will set PaperSize equal to
     * ImageSize.
     *
     * @param imagesBytes The image file as bytes.
     */
    public static InternalPdfDocument ImageToPdf(Iterator<byte[]> imagesBytes) throws IOException {
        return ImageToPdf(imagesBytes, ImageBehavior.CENTERED_ON_PAGE, null);
    }

    /**
     * Draw a bitmap multiple times according to the specified parameters; all occurrences of the
     * bitmap will share a single data stream
     *
     * @param imageBytes    images to draw
     * @param pageIndexes   Target page indexes
     * @param x             X coordinate
     * @param y             Y coordinate
     * @param desiredWidth  Desired widths
     * @param desiredHeight Desired heights
     */
    public static void DrawBitmap(InternalPdfDocument pdfDocument, byte[] imageBytes,
                                  Iterable<Integer> pageIndexes, double x,
                                  double y, double desiredWidth, double desiredHeight) throws IOException {
        RpcClient client = Access.EnsureConnection();

        DrawBitmapRequestStream.Info.Builder info = DrawBitmapRequestStream.Info.newBuilder();
        info.setDocument(pdfDocument.remoteDocument);
        info.setX(x);
        info.setY(y);
        info.setDesiredWidth(desiredWidth);
        info.setDesiredHeight(desiredHeight);
        info.addAllPageIndexes(pageIndexes);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<EmptyResult> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<DrawBitmapRequestStream> requestStream = client.Stub.pdfDocumentImageDrawBitmap(
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        requestStream.onNext(DrawBitmapRequestStream.newBuilder().setInfo(info).build());

        for (Iterator<byte[]> it = Chunk(imageBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            DrawBitmapRequestStream.Builder msg = DrawBitmapRequestStream.newBuilder();
            msg.setRawImageChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(msg.build());
        }
        requestStream.onCompleted();

        WaitAndCheck(finishLatch, resultChunks);

        HandleEmptyResultChunks(resultChunks);
    }

    /**
     * Finds all embedded Images from within the PDF and returns as list of image bytes
     */

    public static List<byte[]> ExtractAllImages(InternalPdfDocument pdfDocument) throws IOException {
        return ExtractAllImages(pdfDocument, null);
    }

    /**
     * Finds all embedded Images from within the PDF and returns then as System.Drawing.Image objects
     *
     * @param pageIndexes Index of the page.  Note: Page 1 has index 0. Defaults to all pages
     */
    public static List<byte[]> ExtractAllImages(InternalPdfDocument pdfDocument,
                                                Iterable<Integer> pageIndexes) throws IOException {
        RpcClient client = Access.EnsureConnection();

        ExtractAllRawImagesRequest.Builder request = ExtractAllRawImagesRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        if (pageIndexes != null) {
            request.addAllPageIndexes(pageIndexes);
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<ImagesResultStream> resultChunks = new ArrayList<>();

        client.Stub.pdfDocumentImageExtractAllRawImages(request.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        WaitAndCheck(finishLatch, resultChunks);

        return HandleImagesResult(resultChunks);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param pageIndexes   A list of the specific zero based page number to render as images.
     * @param imageMaxWidth The target maximum width(in pixel) of the output images.
     * @param dpi           The desired resolution of the output Images.
     * @return An array of the file paths of the image files created.
     * <p>
     * The DPI will be ignored under Linux and macOS.
     */

    public static List<byte[]> PdfToImage(InternalPdfDocument pdfDocument,
                                          Iterable<Integer> pageIndexes, int dpi, Integer imageMaxWidth) throws IOException {
        return PdfToImage(pdfDocument, pageIndexes, dpi, imageMaxWidth, null);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param pageIndexes    A list of the specific zero based page number to render as images.
     * @param imageMaxWidth  The target maximum width(in pixel) of the output images.
     * @param imageMaxHeight The target maximum height(in pixel) of the output images.
     * @param dpi            The desired resolution of the output Images.
     * @return An array of the file paths of the image files created.
     */
    public static List<byte[]> PdfToImage(InternalPdfDocument pdfDocument,
                                          Iterable<Integer> pageIndexes, int dpi, Integer imageMaxWidth, Integer imageMaxHeight)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        PdfToImagesRequest.Builder request = PdfToImagesRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        request.setDpi(dpi);

        if (pageIndexes != null) {
            request.addAllPageIndexes(pageIndexes);
        }

        if (imageMaxWidth != null) {
            request.setMaxWidth(imageMaxWidth);
        }

        if (imageMaxHeight != null) {
            request.setMaxHeight(imageMaxHeight);
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<ImagesResultStream> resultChunks = new ArrayList<>();

        client.Stub.pdfDocumentImagePdfToImages(request.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        WaitAndCheck(finishLatch, resultChunks);

        return HandleImagesResult(resultChunks);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param pageIndexes A list of the specific zero based page number to render as images.
     * @param dpi         The desired resolution of the output Images.
     * @return An array of the file paths of the image files created.
     */
    public static List<byte[]> PdfToImage(InternalPdfDocument pdfDocument,
                                          Iterable<Integer> pageIndexes, int dpi) throws IOException {
        return PdfToImage(pdfDocument, pageIndexes, dpi, null, null);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param pageIndexes A list of the specific zero based page number to render as images.
     * @return An array of the file paths of the image files created.
     */
    public static List<byte[]> PdfToImage(InternalPdfDocument pdfDocument,
                                          Iterable<Integer> pageIndexes) throws IOException {
        return PdfToImage(pdfDocument, pageIndexes, 92, null, null);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @return An array of the file paths of the image files created.
     */
    public static List<byte[]> PdfToImage(InternalPdfDocument pdfDocument) throws IOException {
        return PdfToImage(pdfDocument, null, 92, null, null);
    }
}
