package com.ironsoftware.ironpdf.staticapi.annotation;

import com.ironsoftware.ironpdf.annotation.AnnotationIcon;
import com.ironsoftware.ironpdf.annotation.TextAnnotation;
import com.ironsoftware.ironpdf.staticapi.Annotation_Api;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AnnotationApiTests extends TestBase {

    @Test
    public final void AddTextAnnotationTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        TextAnnotation textAnnotation1 = new TextAnnotation();
        textAnnotation1.setColorCode(null);
        textAnnotation1.setContents("This is a text annotation");
        textAnnotation1.setHidden(false);
        textAnnotation1.setOpacity(0);
        textAnnotation1.setOpenByDefault(false);
        textAnnotation1.setPrintable(false);
        textAnnotation1.setReadOnly(false);
        textAnnotation1.setRotateable(false);
        textAnnotation1.setSubject("subject");
        textAnnotation1.setTitle("title");
        textAnnotation1.setIcon(AnnotationIcon.NO_ICON);

        Annotation_Api.AddTextAnnotation(doc, textAnnotation1, 0, 0, 0);

        TextAnnotation textAnnotation2 = new TextAnnotation();
        Annotation_Api.AddTextAnnotation(doc, textAnnotation2, 0, 20, 20);

        Assertions.assertEquals(2, Annotation_Api.GetAnnotationCount(doc, 0));
        PdfDocument_Api.SaveAs(doc, String.format("TestOutput/%1$s.pdf", "AddTextAnnotationTest"));
    }

    @Test
    public final void GetAnnotationCountTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        Assertions.assertEquals(0, Annotation_Api.GetAnnotationCount(doc, 0));
    }
}
