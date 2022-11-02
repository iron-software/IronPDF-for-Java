package com.ironsoftware;

import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.Page_Api;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.Render_Api;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Smoke Test from Staging!");

        ChromePdfRenderOptions tempVar = new ChromePdfRenderOptions();
        tempVar.setPaperSize(PaperSize.A4);

        try {
            InternalPdfDocument doc = Render_Api.RenderUriAsPdf("https://google.com", tempVar);
            List<PageInfo> info = Page_Api.GetPagesInfo(doc);
            System.out.println("Size: " + info.size());
            System.out.println("Width: " + info.get(0).getWidth());
            PdfDocument_Api.SaveAs(doc, "test.pdf");
        }
        catch (Exception e) {
            throw new Exception("Cannot RenderUriAsPdf.", e);
        }

        File savedFile = new File("test.pdf");
        if (!savedFile.exists()) {
            throw new FileNotFoundException("Cannot find saved file from RenderUriAsPdf.");
        }
    }
}