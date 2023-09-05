package com.ironsoftware.ironpdf.internal.staticapi;

import com.google.protobuf.ByteString;
import com.ironsoftware.ironpdf.image.ImageBehavior;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The type Image api.
 */
public final class Image_Api {

    public static class ImageData {
        byte[] data;
        String fileExtension;

        public ImageData(byte[] data, String fileExtension) {
            this.data = data;
            this.fileExtension = fileExtension;
        }
    }

    /**
     * Converts multiple image files to a PDF document.  Each image creates 1 page which matches the
     * image dimensions. The default PaperSize is A4. You can set it via {@link ChromePdfRenderOptions#setPaperSize(PaperSize)}
     * Note: Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.
     *
     * @param imagesData   The {@link ImageData}
     * @param imageBehavior Describes how image should be placed on the PDF page
     * @param renderOptions Rendering options
     * @return the internal pdf document
     */
    public static InternalPdfDocument imageToPdf(List<ImageData> imagesData,
                                                 ImageBehavior imageBehavior, ChromePdfRenderOptions renderOptions) {
        RpcClient client = Access.ensureConnection();

        ChromeImageFilesToPdfRequestStreamP.InfoP.Builder info = ChromeImageFilesToPdfRequestStreamP.InfoP.newBuilder();
        info.setRenderOptions(
                Render_Converter.toProto((renderOptions != null ? renderOptions : new ChromePdfRenderOptions())));
        info.setImageBehavior(Image_Converter.toProto(imageBehavior));

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<PdfDocumentResultP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<ChromeImageFilesToPdfRequestStreamP> requestStream =
                client.stub.chromeImageImageFilesToPdf(
                        new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        ChromeImageFilesToPdfRequestStreamP.Builder infoMsg =
                ChromeImageFilesToPdfRequestStreamP.newBuilder();
        infoMsg.setInfo(info);
        requestStream.onNext(infoMsg.build());

        for (int imageIndex : IntStream.range(0, imagesData.size()).toArray()) {
            ImageData imageData = imagesData.get(imageIndex);
            for (Iterator<byte[]> it = Utils_Util.chunk(imageData.data); it.hasNext(); ) {
                byte[] chunk = it.next();
                ChromeImageFilesToPdfRequestStreamP.Builder msg = ChromeImageFilesToPdfRequestStreamP.newBuilder();
                RawImageChunkWithIndexAndFileTypeP.Builder rawImageFilesChunkWithIndex = RawImageChunkWithIndexAndFileTypeP.newBuilder();
                rawImageFilesChunkWithIndex.setRawImageChunk(ByteString.copyFrom(chunk));
                rawImageFilesChunkWithIndex.setImageIndex(imageIndex);
                rawImageFilesChunkWithIndex.setFileType(imageData.fileExtension);
                msg.setRawImagesFileChunk(rawImageFilesChunkWithIndex);
                requestStream.onNext(msg.build());
            }
        }

        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handlePdfDocumentChunks(resultChunks);
    }

    /**
     * Draw an image multiple times according to the specified parameters; all occurrences of the
     * image will share a single data stream
     *
     * @param internalPdfDocument the internal pdf document
     * @param imageBytes          images to draw
     * @param pageIndexes         Target page indexes
     * @param x                   X coordinate
     * @param y                   Y coordinate
     * @param desiredWidth        Desired widths
     * @param desiredHeight       Desired heights
     */
    public static void drawImage(InternalPdfDocument internalPdfDocument, byte[] imageBytes,
                                 List<Integer> pageIndexes, double x,
                                 double y, double desiredWidth, double desiredHeight) {
        RpcClient client = Access.ensureConnection();

        PdfiumDrawBitmapRequestStreamP.InfoP.Builder info = PdfiumDrawBitmapRequestStreamP.InfoP.newBuilder();
        info.setDocument(internalPdfDocument.remoteDocument);
        info.setX(x);
        info.setY(y);
        info.setDesiredWidth(desiredWidth);
        info.setDesiredHeight(desiredHeight);
        info.addAllPageIndexes(pageIndexes);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<EmptyResultP> resultChunks = new ArrayList<>();

        io.grpc.stub.StreamObserver<PdfiumDrawBitmapRequestStreamP> requestStream = client.stub.pdfiumImageDrawBitmap(
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        requestStream.onNext(PdfiumDrawBitmapRequestStreamP.newBuilder().setInfo(info).build());

        for (Iterator<byte[]> it = Utils_Util.chunk(imageBytes); it.hasNext(); ) {
            byte[] bytes = it.next();
            PdfiumDrawBitmapRequestStreamP.Builder msg = PdfiumDrawBitmapRequestStreamP.newBuilder();
            msg.setRawImageChunk(ByteString.copyFrom(bytes));
            requestStream.onNext(msg.build());
        }
        requestStream.onCompleted();

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        Utils_Util.handleEmptyResultChunks(resultChunks);
    }

    /**
     * Finds all embedded Images from within the PDF and returns as list of image bytes
     *
     * @param internalPdfDocument the internal pdf document
     * @return the list
     * @throws IOException the io exception
     */
    public static List<byte[]> extractAllImages(InternalPdfDocument internalPdfDocument) throws IOException {
        return extractAllImages(internalPdfDocument, null);
    }

    /**
     * Finds all embedded Images from within the PDF and returns then as image byte[] objects
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         Index of the page.  Note: Page 1 has index 0. Defaults to all pages
     * @return the list
     * @throws IOException the io exception
     */
    public static List<byte[]> extractAllImages(InternalPdfDocument internalPdfDocument,
                                                List<Integer> pageIndexes) throws IOException {
        RpcClient client = Access.ensureConnection();

        PdfiumExtractAllRawImagesRequestP.Builder req = PdfiumExtractAllRawImagesRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        if(pageIndexes == null || pageIndexes.isEmpty()){
            pageIndexes = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        req.addAllPageIndexes(pageIndexes);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<ImagesResultStreamP> resultChunks = new ArrayList<>();

        client.stub.pdfiumImageExtractAllRawImages(req.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handleImagesResult(resultChunks);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         A list of the specific zero based page number to render as images.
     * @param dpi                 The desired resolution of the output Images.
     * @param imageMaxWidth       The target maximum width(in pixel) of the output images.
     * @return An array of the file paths of the image files created. <p> The DPI will be ignored under Linux and macOS.
     * @throws IOException the io exception
     */
    public static List<byte[]> pdfToImage(InternalPdfDocument internalPdfDocument,
                                          List<Integer> pageIndexes, int dpi, Integer imageMaxWidth) throws IOException {
        return pdfToImage(internalPdfDocument, pageIndexes, dpi, imageMaxWidth, null);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         A list of the specific zero based page number to render as images.
     * @param dpi                 The desired resolution of the output Images.
     * @param imageMaxWidth       The target maximum width(in pixel) of the output images.
     * @param imageMaxHeight      The target maximum height(in pixel) of the output images.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public static List<byte[]> pdfToImage(InternalPdfDocument internalPdfDocument,
                                          List<Integer> pageIndexes, int dpi, Integer imageMaxWidth, Integer imageMaxHeight)
            throws IOException {
        RpcClient client = Access.ensureConnection();

        PdfiumPdfToImagesRequestP.Builder request = PdfiumPdfToImagesRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setDpi(dpi);

        if(pageIndexes == null || pageIndexes.isEmpty()){
            pageIndexes = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        request.addAllPageIndexes(pageIndexes);

        if (imageMaxWidth != null) {
            request.setMaxWidth(imageMaxWidth);
        }

        if (imageMaxHeight != null) {
            request.setMaxHeight(imageMaxHeight);
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<ImagesResultStreamP> resultChunks = new ArrayList<>();

        client.stub.pdfiumImagePdfToImages(request.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handleImagesResult(resultChunks);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         A list of the specific zero based page number to render as images.
     * @param dpi                 The desired resolution of the output Images.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public static List<byte[]> pdfToImage(InternalPdfDocument internalPdfDocument,
                                          List<Integer> pageIndexes, int dpi) throws IOException {
        return pdfToImage(internalPdfDocument, pageIndexes, dpi, null, null);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         A list of the specific zero based page number to render as images.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public static List<byte[]> pdfToImage(InternalPdfDocument internalPdfDocument,
                                          List<Integer> pageIndexes) throws IOException {
        return pdfToImage(internalPdfDocument, pageIndexes, 92, null, null);
    }

    /**
     * Renders the PDF and exports image Files in convenient formats.  Page Numbers may be specified.
     * 1 image file is created for each page. <p>FileNamePattern should normally contain an asterisk
     * (*) character which will be substituted for the page numbers</p>
     *
     * @param internalPdfDocument the internal pdf document
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public static List<byte[]> pdfToImage(InternalPdfDocument internalPdfDocument) throws IOException {
        return pdfToImage(internalPdfDocument, null, 92, null, null);
    }

    public static byte[] toMultiPageTiff(InternalPdfDocument internalPdfDocument,
                                          List<Integer> pageIndexes, int dpi, Integer imageMaxWidth, Integer imageMaxHeight) throws IOException {
        RpcClient client = Access.ensureConnection();

        PdfiumPdfToMultiPageTiffImageRequestP.Builder request = PdfiumPdfToMultiPageTiffImageRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setDpi(dpi);

        if(pageIndexes == null || pageIndexes.isEmpty()){
            pageIndexes = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        request.addAllPageIndexes(pageIndexes);

        if (imageMaxWidth != null) {
            request.setMaxWidth(imageMaxWidth);
        }

        if (imageMaxHeight != null) {
            request.setMaxHeight(imageMaxHeight);
        }

        final CountDownLatch finishLatch = new CountDownLatch(1);
        ArrayList<ImageResultStreamP> resultChunks = new ArrayList<>();

        client.stub.pdfiumImagePdfToMultiPageTiffImage(request.build(),
                new Utils_ReceivingCustomStreamObserver<>(finishLatch, resultChunks));

        Utils_Util.waitAndCheck(finishLatch, resultChunks);

        return Utils_Util.handleImageResult(resultChunks);
    }
}
