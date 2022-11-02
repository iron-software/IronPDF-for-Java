package com.ironsoftware.ironpdf.render;

import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import com.ironsoftware.ironpdf.staticapi.Render_Api;

/**
 * Html To PDF output options for {@link Render_Api}. Specify options such as Paper-Size, DPI,
 * Headers and Footers and other Chromium specific browser setup options.
 */
public class ChromePdfRenderOptions implements Cloneable {

    /**
     * Turns all Html forms elements into editable PDF forms.
     */
    private boolean CreatePdfFormsFromHtml = true;
    /**
     * Enables Media="screen" CSS Styles  and StyleSheets <p>Note: By setting AllowScreenCss=false,
     * IronPdf renders PDFs from HTML using CSS for media="print" as if printing a web page in a
     * browser print dialog.</p>
     */
    private com.ironsoftware.ironpdf.render.CssMediaType CssMediaType = com.ironsoftware.ironpdf.render.CssMediaType.SCREEN;
    /**
     * Allows a custom CSS style-sheet  to be applied to Html before rendering.  Maybe a local file
     * path,  or remote url.
     */
    private String CustomCssUrl = "";
    private double CustomPaperHeight = 297;
    private double CustomPaperWidth = 210;
    /**
     * Enables JavaScript and Json to be executed  before the page is rendered.  Ideal for printing
     * from Ajax / Angular Applications. <p>Also see {@link #RenderDelay}</p>
     */
    private boolean EnableJavaScript = true;
    /**
     * First page number to be used in PDF Headers and Footers.
     * <p>
     * {@link #TextHeader} {@link #HtmlHeader} {@link #TextFooter} {@link #HtmlFooter}
     */
    private int FirstPageNumber = 1;
    /**
     * Where possible, shrinks the PDF content to 1 page of paper width. <p>This applies when the PDF
     * content is too wide for the given PaperSize (given that 1 HTML pixel = 1 DPI).</p>
     */
    private boolean FitToPaperWidth = true;
    /**
     * Outputs a black-and-white PDF
     */
    private boolean GrayScale = false;
    /**
     * Sets the header content for every PDF page as Html.  Supports 'mail-merge'.
     */
    private HtmlHeaderFooter HtmlFooter = new HtmlHeaderFooter();
    /**
     * Sets the footer content for every PDF page as Html.  Supports 'mail-merge'.
     */
    private HtmlHeaderFooter HtmlHeader = new HtmlHeaderFooter();
    /**
     * The input character encoding as a string;
     */
    private String InputEncoding = "utf-8"; //Now  Disable this function for JAVA
    /**
     * Bottom Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double MarginBottom = 25;
    /**
     * Left Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double MarginLeft = 25;
    /**
     * Right Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double MarginRight = 25;
    /**
     * Top Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double MarginTop = 25;
    /**
     * The PDF paper orientation. E.g. Portrait or Landscape.
     */
    private com.ironsoftware.ironpdf.render.PaperOrientation PaperOrientation = com.ironsoftware.ironpdf.render.PaperOrientation.PORTRAIT;
    /**
     * Set an output paper size for PDF pages.  System.Drawing.Printing.PaperKind. <p>Use
     * SetCustomPaperSize(int width, int height) for custom sizes.</p>
     */
    private com.ironsoftware.ironpdf.render.PaperSize PaperSize = com.ironsoftware.ironpdf.render.PaperSize.A4;
    /**
     * Prints background-colors and images from Html.
     */
    private boolean PrintHtmlBackgrounds = true;
    /**
     * Milliseconds to wait after Html is rendered before printing.  This can use useful when
     * considering the rendering of JavaScript, Ajax or animations.
     */
    private int RenderDelay = 0;
    /**
     * Sets the header content for every PDF page as text.  Supports 'mail-merge' and automatically
     * turns urls into hyperlinks..
     */
    private TextHeaderFooter TextFooter = new TextHeaderFooter();
    /**
     * Sets the footer content for every PDF page as text.  Supports 'mail-merge' and automatically
     * turns urls into hyperlinks..
     */
    private TextHeaderFooter TextHeader = new TextHeaderFooter();
    /**
     * Render timeout in seconds
     */
    private int Timeout = 60;
    /**
     * PDF Document Name and Title meta-data.  Not required.  Useful for mail-merge and file naming
     */

    private String Title;
    /**
     * Defines a virtual screen height used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p> <p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many websites.</p>
     */
    private int ViewPortHeight = 1280;
    /**
     * Defines a virtual screen width used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p><p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many website</p>
     */
    private int ViewPortWidth = 1024;
    /**
     * The zoom level in %.  Enlarges the rendering size of Html documents.
     */
    private int Zoom = 100;

    public boolean getCreatePdfFormsFromHtml() {
        return CreatePdfFormsFromHtml;
    }

    public void setCreatePdfFormsFromHtml(boolean value) {
        CreatePdfFormsFromHtml = value;
    }

    public com.ironsoftware.ironpdf.render.CssMediaType getCssMediaType() {
        return CssMediaType;
    }

    public void setCssMediaType(com.ironsoftware.ironpdf.render.CssMediaType value) {
        CssMediaType = value;
    }

    public String getCustomCssUrl() {
        return CustomCssUrl;
    }

    public void setCustomCssUrl(String value) {
        CustomCssUrl = value;
    }

    public double getCustomPaperHeight() {
        return CustomPaperHeight;
    }

    public void setCustomPaperHeight(double value) {
        CustomPaperHeight = value;
    }

    public double getCustomPaperWidth() {
        return CustomPaperWidth;
    }

    public void setCustomPaperWidth(double value) {
        CustomPaperWidth = value;
    }

    public boolean getEnableJavaScript() {
        return EnableJavaScript;
    }

    public void setEnableJavaScript(boolean value) {
        EnableJavaScript = value;
    }

    public int getFirstPageNumber() {
        return FirstPageNumber;
    }

    public void setFirstPageNumber(int value) {
        FirstPageNumber = value;
    }

    public boolean getFitToPaperWidth() {
        return FitToPaperWidth;
    }

    public void setFitToPaperWidth(boolean value) {
        FitToPaperWidth = value;
    }

    public boolean getGrayScale() {
        return GrayScale;
    }

    public void setGrayScale(boolean value) {
        GrayScale = value;
    }

    public String getInputEncoding() {
        return InputEncoding;
    }

    void setInputEncoding(String value) {
        InputEncoding = value;
    }

    public double getMarginBottom() {
        return MarginBottom;
    }

    public void setMarginBottom(double value) {
        MarginBottom = value;
    }

    public double getMarginLeft() {
        return MarginLeft;
    }

    public void setMarginLeft(double value) {
        MarginLeft = value;
    }

    public double getMarginRight() {
        return MarginRight;
    }

    public void setMarginRight(double value) {
        MarginRight = value;
    }

    public double getMarginTop() {
        return MarginTop;
    }

    public void setMarginTop(double value) {
        MarginTop = value;
    }

    public com.ironsoftware.ironpdf.render.PaperOrientation getPaperOrientation() {
        return PaperOrientation;
    }

    public void setPaperOrientation(com.ironsoftware.ironpdf.render.PaperOrientation value) {
        PaperOrientation = value;
    }

    public com.ironsoftware.ironpdf.render.PaperSize getPaperSize() {
        return PaperSize;
    }

    public void setPaperSize(com.ironsoftware.ironpdf.render.PaperSize value) {
        PaperSize = value;
    }

    public boolean getPrintHtmlBackgrounds() {
        return PrintHtmlBackgrounds;
    }

    public void setPrintHtmlBackgrounds(boolean value) {
        PrintHtmlBackgrounds = value;
    }

    public int getRenderDelay() {
        return RenderDelay;
    }

    public void setRenderDelay(int value) {
        RenderDelay = value;
    }

    public int getTimeout() {
        return Timeout;
    }

    public void setTimeout(int value) {
        Timeout = value;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String value) {
        Title = value;
    }

    public int getViewPortHeight() {
        return ViewPortHeight;
    }

    public void setViewPortHeight(int value) {
        ViewPortHeight = value;
    }

    public int getViewPortWidth() {
        return ViewPortWidth;
    }

    public void setViewPortWidth(int value) {
        ViewPortWidth = value;
    }

    public int getZoom() {
        return Zoom;
    }

    public void setZoom(int value) {
        Zoom = value;
    }

    /**
     * Supports {@link Cloneable}.  Creates a deep copy of this class instance.
     *
     * @return A deep clone of this instance.  Use explicit casting to convert object back to the
     * intended type.
     */
    public Object Clone() throws CloneNotSupportedException {
        ChromePdfRenderOptions output = (ChromePdfRenderOptions) clone();
        output.setTextFooter(
                (TextHeaderFooter) (getTextFooter() == null ? null : getTextFooter().Clone()));
        output.setTextHeader(
                (TextHeaderFooter) (getTextHeader() == null ? null : getTextHeader().Clone()));
        output.setHtmlFooter(
                (HtmlHeaderFooter) (getHtmlFooter() == null ? null : getHtmlFooter().Clone()));
        output.setHtmlHeader(
                (HtmlHeaderFooter) (getHtmlHeader() == null ? null : getHtmlHeader().Clone()));

        return output;
    }

    public TextHeaderFooter getTextFooter() {
        return TextFooter;
    }

    public void setTextFooter(TextHeaderFooter value) {
        TextFooter = value;
    }

    public TextHeaderFooter getTextHeader() {
        return TextHeader;
    }

    public void setTextHeader(TextHeaderFooter value) {
        TextHeader = value;
    }

    public HtmlHeaderFooter getHtmlFooter() {
        return HtmlFooter;
    }

    public void setHtmlFooter(HtmlHeaderFooter value) {
        HtmlFooter = value;
    }

    public HtmlHeaderFooter getHtmlHeader() {
        return HtmlHeader;
    }

    public void setHtmlHeader(HtmlHeaderFooter value) {
        HtmlHeader = value;
    }

    /**
     * Set an output paper size for PDF pages.  Dimensions are in Centimeters.
     *
     * @param width  Custom paper width in cm.
     * @param height Custom paper height in cm.
     */
    public void SetCustomPaperSizeInCentimeters(double width, double height) {
        setCustomPaperWidth(width * 10.0);
        setCustomPaperHeight(height * 10.0);
        setPaperSize(com.ironsoftware.ironpdf.render.PaperSize.Custom);
    }

    /**
     * Set an output paper size for PDF pages.  Dimensions are in millimeters.
     *
     * @param width  Custom paper width in millimeters.
     * @param height Custom paper height in millimeters.
     */
    public void SetCustomPaperSizeInMillimeters(double width, double height) {
        setCustomPaperWidth(width);
        setCustomPaperHeight(height);
        setPaperSize(com.ironsoftware.ironpdf.render.PaperSize.Custom);
    }

    /**
     * Set an output paper size for PDF pages.  Dimensions are in screen Pixels or printer Points.
     *
     * @param width  Custom paper width in pixels/points.
     * @param height Custom paper height in pixels/points..
     */

    public void SetCustomPaperSizeInPixelsOrPoints(double width, double height) {
        SetCustomPaperSizeInPixelsOrPoints(width, height, 96);
    }

    /**
     * Set an output paper size for PDF pages.  Dimensions are in screen Pixels or printer Points.
     *
     * @param width  Custom paper width in pixels/points.
     * @param height Custom paper height in pixels/points.
     * @param DPI    Intended print resolution of the PDF.  To be clear PDFs have no fixed DPI/PPI
     *               value for rendering. 72 and 96 are common onscreen values.  300 is a common value
     *               used in commercial printing.
     */
    public void SetCustomPaperSizeInPixelsOrPoints(double width, double height, int DPI) {
        SetCustomPaperSizeInInches(width / DPI, height / DPI);
    }

    /**
     * Set an output paper size for PDF pages.  Dimensions are in Inches.
     *
     * @param width  Custom paper width in Inches.
     * @param height Custom paper height in Inches.
     */
    public void SetCustomPaperSizeInInches(double width, double height) {
        setCustomPaperWidth(width * 25.400);
        setCustomPaperHeight(height * 25.400);
        setPaperSize(com.ironsoftware.ironpdf.render.PaperSize.Custom);
    }

}
