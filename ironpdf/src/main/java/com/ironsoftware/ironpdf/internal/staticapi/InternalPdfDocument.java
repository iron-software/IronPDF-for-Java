package com.ironsoftware.ironpdf.internal.staticapi;


import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.internal.proto.EmptyResultP;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.signature.Signature;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Internal pdf document. For users, please use {@link com.ironsoftware.ironpdf.PdfDocument} instead.
 */
public final class InternalPdfDocument implements AutoCloseable , Printable {

    /**
     * The Remote document.
     */
    final com.ironsoftware.ironpdf.internal.proto.PdfDocumentP remoteDocument;
    /**
     * The Temp pages info.
     */
//todo to optimize some code we may use this more often
    List<PageInfo> tempPagesInfo = Collections.emptyList();
    private boolean disposed = false;

    public List<Signature> signatures = new ArrayList<>();

    public String userPassword = "";
    public String ownerPassword = "";

    /**
     * Instantiates a new Internal pdf document.
     *
     * @param remoteDocument the remote document
     */
    InternalPdfDocument(com.ironsoftware.ironpdf.internal.proto.PdfDocumentP remoteDocument) {
        this.remoteDocument = remoteDocument;
    }

    @Override
    protected void finalize() {
        this.close();
    }

    @Override
    public synchronized void close() {
        if (this.disposed) {
            return;
        }
        disposed = true;
        try {
            RpcClient client = Access.ensureConnection();

            EmptyResultP res = client.GetBlockingStub("close").pdfiumDispose(remoteDocument);

            Utils_Util.handleEmptyResult(res);
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

    /**
     * Gets page list.
     * @param pageSelection PageSelection
     * @return the page list
     */
    public List<Integer> getPageList(PageSelection pageSelection) {
        List<PageInfo> pageInfos =  Page_Api.getPagesInfo(this);

        if(pageSelection.pagesList.isEmpty()){
            //default to all pages
            return pageInfos.stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }

        return pageSelection.pagesList.stream().map(i->{
            if(i == -1){
                return pageInfos.size() - 1;
            }
            return i;
        }).collect(Collectors.toList());
    }

    public List<PageInfo> getPageInfoList(PageSelection pageSelection) {
        List<PageInfo> pageInfos =  Page_Api.getPagesInfo(this);

        if(pageSelection.pagesList.isEmpty()){
            //default to all pages
            return pageInfos;
        }

        return pageSelection.pagesList.stream().map(i->{
            if(i == -1){
                return pageInfos.get(pageInfos.size() - 1);
            }
            return pageInfos.get(i);
        }).collect(Collectors.toList());
    }
}
