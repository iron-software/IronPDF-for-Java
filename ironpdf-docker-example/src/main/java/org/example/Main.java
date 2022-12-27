package org.example;

import com.ironsoftware.ironpdf.License;
import com.ironsoftware.ironpdf.PdfDocument;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("---START---");
        //License.setLicenseKey("MY-LICENSE-KEY");
        PdfDocument pdfDocument = PdfDocument.renderUrlAsPdf("https://www.ironpdf.com/java");
        System.out.println("Render finished");
        try {
            pdfDocument.saveAs("pdfFromDocker.pdf");
        } catch (IOException e) {
            System.out.println("cannot save file");
            throw new RuntimeException(e);
        }
        System.out.println("---END---");
    }
}