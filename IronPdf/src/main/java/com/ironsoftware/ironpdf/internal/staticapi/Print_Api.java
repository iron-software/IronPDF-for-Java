package com.ironsoftware.ironpdf.internal.staticapi;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * The type Print api.
 */
public final class Print_Api {

    /**
     * Print.
     *
     * @param internalPdfDocument the internal pdf document
     * @param isShowDialog        the is show dialog
     * @throws PrinterException the printer exception
     */
    public static void print(InternalPdfDocument internalPdfDocument, boolean isShowDialog)
            throws PrinterException {
        internalPdfDocument.tempPagesInfo = Page_Api.getPagesInfo(internalPdfDocument);
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);

        printerJob.setPrintable(internalPdfDocument);
        if (isShowDialog) {
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } else {
            printerJob.print();
        }

    }
}
