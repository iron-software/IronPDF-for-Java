package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.internal.proto.EmptyResult;
import com.ironsoftware.ironpdf.page.PageInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * The type Internal pdf document. For users, please use {@link com.ironsoftware.ironpdf.PdfDocument} instead.
 */
public final class InternalPdfDocument implements AutoCloseable, Printable {

    /**
     * The Remote document.
     */
    final com.ironsoftware.ironpdf.internal.proto.PdfDocument remoteDocument;
    /**
     * The Temp pages info.
     */
//todo to optimize some code we may use this more often
    List<PageInfo> tempPagesInfo = Collections.emptyList();
    private boolean disposed = false;

    /**
     * Instantiates a new Internal pdf document.
     *
     * @param remoteDocument the remote document
     */
    InternalPdfDocument(com.ironsoftware.ironpdf.internal.proto.PdfDocument remoteDocument) {
        this.remoteDocument = remoteDocument;
    }

    @Override
    public void close() {
        try {
            if (this.disposed) {
                return;
            }

            RpcClient client = Access.ensureConnection();

            EmptyResult res = client.blockingStub.pdfDocumentDispose(remoteDocument);

            Utils_Util.handleEmptyResult(res);

            disposed = true;
        } catch (Exception ignored) {

        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        try {
            if (pageIndex >= tempPagesInfo.size()) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D graphics2D = (Graphics2D) graphics;

            byte[] bytes = Image_Api.pdfToImage(this, Collections.singletonList(pageIndex), 300).get(0);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            BufferedImage image = ImageIO.read(byteArrayInputStream);

            graphics2D.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
            graphics2D.drawImage(image, 0, 0, (int) pageFormat.getPaper().getWidth(),
                    (int) pageFormat.getPaper().getHeight(), null);

            return PAGE_EXISTS;
        } catch (IOException e) {
            throw new PrinterException(e.getMessage());
        }
    }
}
