package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.TableOfContentsTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PdfATests extends TestBase {

    @Test
    public final void PdfATest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        int originalBytesSize = doc.getBinaryData().length;

        int pdfAByteSize = doc.convertToPdfA().getBinaryData().length;

        Assertions.assertTrue(originalBytesSize != pdfAByteSize);
    }

    @Test
    public final void PdfUATest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        int originalBytesSize = doc.getBinaryData().length;

        int pdfUAByteSize = doc.convertToPdfUA(NaturalLanguages.English).getBinaryData().length;

        Assertions.assertTrue(originalBytesSize != pdfUAByteSize);
    }
}
