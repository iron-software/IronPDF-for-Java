package com.ironsoftware.ironpdf.staticapi;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;

public final class Print_Api {

    public static void Print(InternalPdfDocument document, boolean isShowDialog)
            throws IOException, PrinterException {
        document.tempPagesInfo = Page_Api.GetPagesInfo(document);
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
