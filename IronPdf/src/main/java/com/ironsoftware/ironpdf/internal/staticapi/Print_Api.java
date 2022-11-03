package com.ironsoftware.ironpdf.internal.staticapi;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public final class Print_Api {

    public static void print(InternalPdfDocument document, boolean isShowDialog)
            throws PrinterException {
        document.tempPagesInfo = Page_Api.getPagesInfo(document);
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);

        printerJob.setPrintable(document);
        if (isShowDialog) {
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } else {
            printerJob.print();
        }

    }
}
