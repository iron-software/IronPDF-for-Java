import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.Render_Api;

import java.io.IOException;

public class TestMain {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        try {
            System.out.println("A !");
//            PdfDocument cc = PdfDocument_Api.FromFile(TestBase.getTestFile("/Data/google.pdf"));
            ChromePdfRenderOptions xx = new ChromePdfRenderOptions();
            xx.setRenderDelay(2000);
            InternalPdfDocument cc = Render_Api.RenderUriAsPdf("https://www.google.com", xx);
//            (TestBase.getTestFile("/Data/google.pdf"));

            System.out.println("B !" + cc);
            byte[] cs = PdfDocument_Api.GetBytes(cc);

            System.out.println("C !" + cs.length);
            PdfDocument_Api.SaveAs(cs, "TestOutput/FROM_JAVA.pdf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }
}
