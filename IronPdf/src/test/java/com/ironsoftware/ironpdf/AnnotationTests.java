package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.annotation.AnnotationIcon;
import com.ironsoftware.ironpdf.annotation.AnnotationManager;
import com.ironsoftware.ironpdf.annotation.AnnotationOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class AnnotationTests extends TestBase {

    @Test
    public final void AddTextAnnotationTest() throws IOException {
//        Settings.setDebug(true);
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        AnnotationOptions options = new AnnotationOptions("This is a text annotation 1");
        options.setRgbColor(null);
        options.setHidden(false);
        options.setOpacity(0);
        options.setOpen(false);
        options.setPrintable(false);
        options.setReadonly(false);
        options.setRotateable(false);
        options.setSubject("Subject");
        options.setTitle("Title");
        options.setIcon(AnnotationIcon.NO_ICON);

        AnnotationManager annotationManager = doc.getAnnotation();
        annotationManager.addTextAnnotation(options, 0);

        annotationManager.addTextAnnotation(new AnnotationOptions("This is a Text annotation 2"), 0);

        Assertions.assertEquals(2, annotationManager.getAnnotationCount(0));
        doc.saveAs(Paths.get(String.format("TestOutput/%1$s.pdf", "AddTextAnnotationTest")));
    }

    @Test
    public final void GetAnnotationCountTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        Assertions.assertEquals(0, doc.getAnnotation().getAnnotationCount(0));
    }
}
