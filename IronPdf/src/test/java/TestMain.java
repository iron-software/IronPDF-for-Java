import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.internal.staticapi.Render_Api;

import java.io.IOException;
import java.nio.file.Paths;

public class TestMain {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        com.ironsoftware.ironpdf.Settings.setDebug(true);
        try {
            System.out.println("A !");
            PdfDocument doc = PdfDocument.renderUrlAsPdf("https://www.google.com");
            doc.saveAs(Paths.get("C:/tmp/FROM_JAVA.pdf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }
}
