package com.ironsoftware;

import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.Settings;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Smoke Test from Staging!");

        ChromePdfRenderOptions tempVar = new ChromePdfRenderOptions();
        tempVar.setPaperSize(PaperSize.A4);

        Settings.setDebug(true);

        try {
            PdfDocument doc = PdfDocument.renderUrlAsPdf("https://google.com", tempVar);
            List<PageInfo> info = doc.getPagesInfo();
            System.out.println("Size: " + info.size());
            System.out.println("Width: " + info.get(0).getWidth());
            doc.saveAs(Paths.get("TestOutput/test.pdf"));
        }
        catch (Exception e) {
            throw new Exception("Cannot RenderUriAsPdf.", e);
        }

        File savedFile = new File(Paths.get("TestOutput/test.pdf").toString());
        if (!savedFile.exists()) {
            throw new FileNotFoundException("Cannot find saved file from RenderUriAsPdf.");
        }
    }
}