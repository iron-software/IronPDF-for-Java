package com.ironsoftware.ironpdf.render;

import com.ironsoftware.ironpdf.internal.staticapi.Render_Api;

/**
 * Html To PDF output options for {@link Render_Api}. Specify options such as Paper-Size, DPI,
 * and other Chromium specific browser setup options.
 */
public class ChromePdfRenderOptions implements Cloneable {

    /**
     * Turns all Html forms elements into editable PDF forms.
     */
    private boolean createPdfFormsFromHtml = true;
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
    private String customCssUrl = "";
    private double customPaperHeight = 297;
    private double customPaperWidth = 210;
    /**
     * Enables JavaScript and Json to be executed  before the page is rendered.  Ideal for printing
     * from Ajax / Angular Applications. <p>Also see {@link #waitFor}</p>
     */
    private boolean enableJavaScript = true;

    private FitToPaperModes fitToPaperMode = FitToPaperModes.Zoom;

    private int viewPortWidth = 1024;

    private int viewPortHeight = 1280;

    private int zoom = 100;

    /**
     * Outputs a black-and-white PDF
     */
    private boolean grayScale = false;
    /**
     * The input character encoding as a string;
     */
    private String inputEncoding = "utf-8"; //Now  Disable this function for JAVA
    /**
     * Bottom Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double marginBottom = 25;
    /**
     * Left Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double marginLeft = 25;
    /**
     * Right Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double marginRight = 25;
    /**
     * Top Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     */
    private double marginTop = 25;
    /**
     * The PDF paper orientation. E.g. Portrait or Landscape.
     */
    private com.ironsoftware.ironpdf.render.PaperOrientation paperOrientation = com.ironsoftware.ironpdf.render.PaperOrientation.PORTRAIT;
    /**
     * Set an output paper size for PDF pages. <p>Use
     * {@link #setCustomPaperSizeInMillimeters(double, double)}, {@link #setCustomPaperSizeInPixelsOrPoints(double, double)}, etc... for custom sizes.</p>
     */
    private com.ironsoftware.ironpdf.render.PaperSize paperSize = com.ironsoftware.ironpdf.render.PaperSize.A4;
    /**
     * Prints background-colors and images from Html.
     */
    private boolean printHtmlBackgrounds = true;

    private String title;

    /**
     * A custom javascript string to be executed after all HTML has loaded but before PDf rendering.
     */
    private String javascript;

    /**
     * A wrapper object that holds configuration for wait-for mechanism for user to wait for certain events before rendering.
     * By default, it will wait for nothing.
     */
    private WaitFor waitFor = new WaitFor();

    /**
     * Is create pdf forms from html. Turns all Html forms elements into editable PDF forms.
     *
     * @return the boolean
     */
    public boolean isCreatePdfFormsFromHtml() {
        return createPdfFormsFromHtml;
    }

    /**
     * Sets create pdf forms from html. Turns all Html forms elements into editable PDF forms.
     *
     * @param value the value
     */
    public void setCreatePdfFormsFromHtml(boolean value) {
        createPdfFormsFromHtml = value;
    }

    /**
     * Gets css media type. Enables Media="screen" CSS Styles  and StyleSheets <p>Note: By setting AllowScreenCss=false,
     * IronPdf renders PDFs from HTML using CSS for media="print" as if printing a web page in a
     * browser print dialog.</p>
     *
     * @return the css media type
     */
    public com.ironsoftware.ironpdf.render.CssMediaType getCssMediaType() {
        return CssMediaType;
    }

    /**
     * Sets css media type. Enables Media="screen" CSS Styles  and StyleSheets <p>Note: By setting AllowScreenCss=false,
     * IronPdf renders PDFs from HTML using CSS for media="print" as if printing a web page in a
     * browser print dialog.</p>
     *
     * @param value the value
     */
    public void setCssMediaType(com.ironsoftware.ironpdf.render.CssMediaType value) {
        CssMediaType = value;
    }

    /**
     * Gets custom css url. Allows a custom CSS style-sheet  to be applied to Html before rendering.  Maybe a local file
     * path,  or remote url.
     *
     * @return the custom css url
     */
    public String getCustomCssUrl() {
        return customCssUrl;
    }

    /**
     * Sets custom css url. Allows a custom CSS style-sheet  to be applied to Html before rendering.  Maybe a local file
     * path,  or remote url.
     *
     * @param value the value
     */
    public void setCustomCssUrl(String value) {
        customCssUrl = value;
    }

    /**
     * Gets custom paper height.
     *
     * @return the custom paper height
     */
    public double getCustomPaperHeight() {
        return customPaperHeight;
    }

    /**
     * Sets custom paper height (mm).
     *
     * @param value the value
     */
    public void setCustomPaperHeight(double value) {
        customPaperHeight = value;
    }

    /**
     * Gets custom paper width (mm).
     *
     * @return the custom paper width
     */
    public double getCustomPaperWidth() {
        return customPaperWidth;
    }

    /**
     * Sets custom paper width (mm).
     *
     * @param value the value
     */
    public void setCustomPaperWidth(double value) {
        customPaperWidth = value;
    }

    /**
     * Is enable JavaScript. Enables JavaScript and Json to be executed  before the page is rendered.  Ideal for printing
     * from Ajax / Angular Applications. <p>Also see {@link #waitFor}</p>
     *
     * @return the boolean
     */
    public boolean isEnableJavaScript() {
        return enableJavaScript;
    }

    /**
     * Sets enable JavaScript. Enables JavaScript and Json to be executed  before the page is rendered.  Ideal for printing
     * from Ajax / Angular Applications. <p>Also see {@link #waitFor}</p>
     *
     * @param value the value
     */
    public void setEnableJavaScript(boolean value) {
        enableJavaScript = value;
    }

    /**
     * use one of these method instead
     * {@link #UseChromeDefaultRendering()}
     * {@link #UseScaledRendering()}
     * {@link #UseResponsiveCssRendering()}
     * {@link #UseFitToPageRendering()}
     */
    public FitToPaperModes getFitToPaperMode() {
        return fitToPaperMode;
    }

    /**
     * @deprecated use one of these method instead
     * {@link #UseChromeDefaultRendering()}
     * {@link #UseScaledRendering()}
     * {@link #UseResponsiveCssRendering()}
     * {@link #UseFitToPageRendering()}
     */
    public void setFitToPaperMode(FitToPaperModes value) {
        fitToPaperMode = value;
    }

    /**
     * Is gray scale boolean. Outputs a black-and-white PDF.
     *
     * @return the boolean
     */
    public boolean isGrayScale() {
        return grayScale;
    }

    /**
     * Sets gray scale. Outputs a black-and-white PDF.
     *
     * @param value the value
     */
    public void setGrayScale(boolean value) {
        grayScale = value;
    }

    /**
     * Gets input character encoding as a string;
     *
     * @return the input encoding
     */
    public String getInputEncoding() {
        return inputEncoding;
    }

    //Disable setInputEncoding in JAVA

    /**
     * Gets margin bottom. Bottom Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     *
     * @return the margin bottom
     */
    public double getMarginBottom() {
        return marginBottom;
    }

    /**
     * Sets margin bottom. Bottom Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     *
     * @param value the value
     */
    public void setMarginBottom(double value) {
        marginBottom = value;
    }

    /**
     * Gets margin left. Left Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     *
     * @return the margin left
     */
    public double getMarginLeft() {
        return marginLeft;
    }

    /**
     * Sets margin left. Left Pdf "paper" margin in millimeters. Set to zero for border-less and commercial printing
     * applications.
     *
     * @param value the value
     */
    public void setMarginLeft(double value) {
        marginLeft = value;
    }

    /**
     * Gets margin right. Right Pdf "paper" margin in millimeters. Set to zero for border-less and commercial printing
     * applications.
     *
     * @return the margin right
     */
    public double getMarginRight() {
        return marginRight;
    }

    /**
     * Sets margin right. Right Pdf "paper" margin in millimeters. Set to zero for border-less and commercial printing
     * applications.
     *
     * @param value the value
     */
    public void setMarginRight(double value) {
        marginRight = value;
    }

    /**
     * Gets margin top. Top Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     *
     * @return the margin top
     */
    public double getMarginTop() {
        return marginTop;
    }

    /**
     * Sets margin top. Top Pdf "paper" margin in millimeters.  Set to zero for border-less and commercial printing
     * applications.
     *
     * @param value the value
     */
    public void setMarginTop(double value) {
        marginTop = value;
    }

    /**
     * Gets paper orientation. The PDF paper orientation. E.g. Portrait or Landscape.
     *
     * @return the paper orientation
     */
    public com.ironsoftware.ironpdf.render.PaperOrientation getPaperOrientation() {
        return paperOrientation;
    }

    /**
     * Sets paper orientation. The PDF paper orientation. E.g. Portrait or Landscape.
     *
     * @param value the value
     */
    public void setPaperOrientation(com.ironsoftware.ironpdf.render.PaperOrientation value) {
        paperOrientation = value;
    }

    /**
     * Gets paper size. Set an output paper size for PDF pages. <p>Use
     * {@link #setCustomPaperSizeInMillimeters(double, double)}, {@link #setCustomPaperSizeInPixelsOrPoints(double, double)}, etc... for custom sizes.</p>
     *
     * @return the paper size
     */
    public com.ironsoftware.ironpdf.render.PaperSize getPaperSize() {
        return paperSize;
    }

    /**
     * Sets paper size. Set an output paper size for PDF pages. <p>Use
     * {@link #setCustomPaperSizeInMillimeters(double, double)}, {@link #setCustomPaperSizeInPixelsOrPoints(double, double)}, etc... for custom sizes.</p>
     *
     * @param value the value
     */
    public void setPaperSize(com.ironsoftware.ironpdf.render.PaperSize value) {
        paperSize = value;
    }

    /**
     * Is print html backgrounds boolean. Prints background-colors and images from Html.
     *
     * @return the boolean
     */
    public boolean isPrintHtmlBackgrounds() {
        return printHtmlBackgrounds;
    }

    /**
     * Sets print html backgrounds. Prints background-colors and images from Html.
     *
     * @param value the value
     */
    public void setPrintHtmlBackgrounds(boolean value) {
        printHtmlBackgrounds = value;
    }

    /**
     * Gets title. PDF Document Name and Title meta-data.  Not required.  Useful for mail-merge and file naming.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title. PDF Document Name and Title meta-data.  Not required.  Useful for mail-merge and file naming.
     *
     * @param value the value
     */
    public void setTitle(String value) {
        title = value;
    }

    /**
     * Get A custom javascript string to be executed after all HTML has loaded but before PDf rendering.
     *
     * @return the javascript string
     */
    public String getJavascript() {
        return javascript;
    }

    /**
     * Supports {@link Cloneable}.  Creates a deep copy of this class instance.
     *
     * @return A deep clone of this instance.  Use explicit casting to convert object back to the intended type.
     * @throws CloneNotSupportedException the clone not supported exception
     */
    public Object Clone() throws CloneNotSupportedException {
        return (ChromePdfRenderOptions) clone();
    }

    /**
     * Set an output paper size for PDF pages.  Dimensions are in Centimeters.
     *
     * @param width  Custom paper width in cm.
     * @param height Custom paper height in cm.
     */
    public void setCustomPaperSizeInCentimeters(double width, double height) {
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
    public void setCustomPaperSizeInMillimeters(double width, double height) {
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
    public void setCustomPaperSizeInPixelsOrPoints(double width, double height) {
        setCustomPaperSizeInPixelsOrPoints(width, height, 96);
    }

    /**
     * Set an output paper size for PDF pages.  Dimensions are in screen Pixels or printer Points.
     *
     * @param width  Custom paper width in pixels/points.
     * @param height Custom paper height in pixels/points.
     * @param DPI    Intended print resolution of the PDF.  To be clear PDFs have no fixed DPI/PPI               value for rendering. 72 and 96 are common onscreen values.  300 is a common value               used in commercial printing.
     */
    public void setCustomPaperSizeInPixelsOrPoints(double width, double height, int DPI) {
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

    /**
     * Set A custom javascript string to be executed after all HTML has loaded but before PDf rendering.
     *
     * @param javascript a javascript string.
     */
    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }

    /**
     * Gets a wrapper object that holds configuration for wait-for mechanism for user to wait for certain events before rendering.
     * By default, it will wait for nothing.
     */
    public WaitFor getWaitFor() {
        return waitFor;
    }

    /**
     * Sets a wrapper object that holds configuration for wait-for mechanism for user to wait for certain events before rendering.
     * By default, it will wait for nothing.
     */
    public void setWaitFor(WaitFor waitFor) {
        this.waitFor = waitFor;
    }

    /**
     * internal use
     */
    public int getViewPortWidth() {
        return viewPortWidth;
    }

    /**
     * internal use
     */
    public int getViewPortHeight() {
        return viewPortHeight;
    }

    /**
     * internal use
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * Lays out PDF pages in the same way as when viewed from Google Chrome's print preview.
     * Responsive CSS viewport is interpreted based on the width of the Specified Paper Size {@link #setPaperSize(PaperSize)}.
     * To change this responsive behavior use {@link #UseResponsiveCssRendering}
     */
    public void UseChromeDefaultRendering() {
        fitToPaperMode = FitToPaperModes.Zoom;
        zoom = 100;
    }


    /**
     * Adopts a layout which behaves in the same way the 'Chrome Print Preview' does for a given paper size, with an additional zoom level applied to allow content to be manually scaled by the developer.
     * Responsive CSS is interpreted based on the width of the {@link ChromePdfRenderOptions#setPaperSize(PaperSize)} Specified Paper Size
     **/
    public void UseScaledRendering() {
        UseScaledRendering(100);
    }

    /**
     * Adopts a layout which behaves in the same way the 'Chrome Print Preview' does for a given paper size, with an additional zoom level applied to allow content to be manually scaled by the developer.
     * Responsive CSS is interpreted based on the width of the {@link ChromePdfRenderOptions#setPaperSize(PaperSize)} Specified Paper Size
     *
     * @param zoomPercentage A percentage based scale factor on the HTML document.
     **/
    public void UseScaledRendering(int zoomPercentage) {
        fitToPaperMode = FitToPaperModes.Zoom;
        zoom = zoomPercentage;
    }


    /**
     * Uses Responsive CSS to define the rendering of the HTML based on the ViewPortWidth parameter.
     * Content will attempt to scale the rendered content to fill the width of the {@link #setPaperSize(PaperSize)} Specified Paper Size
     * Set {@link #setCssMediaType} to choose between paper and screen CSS interpretations.
     **/
    public void UseResponsiveCssRendering() {
        UseResponsiveCssRendering(1280);
    }

    /**
     * Uses Responsive CSS to define the rendering of the HTML based on the ViewPortWidth parameter.
     * Content will attempt to scale the rendered content to fill the width of the {@link #setPaperSize(PaperSize)} Specified Paper Size
     * Set {@link #setCssMediaType} to choose between paper and screen CSS interpretations.
     *
     * @param viewPortWidthValue A pixel based virtual browser viewport for responsive CSS designs.
     **/
    public void UseResponsiveCssRendering(int viewPortWidthValue) {
        fitToPaperMode = FitToPaperModes.FixedPixelWidth;
        zoom = 100;
        viewPortWidth = viewPortWidthValue;
    }


    /**
     * Scales content to fit the specified {@link #setPaperSize(PaperSize)}. This mode measures minimum HTML content width after it is rendered by the browser, and then scales that content to fit to 1 sheet of paper wide where possible.
     * A minimum width can be set to control scaling and also to ensure that responsive CSS rules are correctly applied.
     **/
    public void UseFitToPageRendering() {
        UseFitToPageRendering(0);
    }

    /**
     * Scales content to fit the specified {@link #setPaperSize(PaperSize)}. This mode measures minimum HTML content width after it is rendered by the browser, and then scales that content to fit to 1 sheet of paper wide where possible.
     * A minimum width can be set to control scaling and also to ensure that responsive CSS rules are correctly applied.
     *
     * @param minimumPixelWidth A pixel based minimum with for the document.  Can help HTML elements to display correctly and respond appropriately to CSS3 responsive layout rules.
     **/
    public void UseFitToPageRendering(int minimumPixelWidth) {
        fitToPaperMode = FitToPaperModes.FixedPixelWidth;
        zoom = 100;
        viewPortWidth = minimumPixelWidth;
    }

    /**
     * Creates a single page PDF which will force its entire content's width and height to fit into one page. Can be used for a consumer bill or receipt.
     */
    public void UseContinuousFeedRendering() {
        UseContinuousFeedRendering(80, 5);
    }

    /**
     * Creates a single page PDF which will force its entire content's width and height to fit into one page. Can be used for a consumer bill or receipt.
     *
     * @param margin The margin in millimeters to apply to the PDF page. Default is 5
     */
    public void UseContinuousFeedRendering(int margin) {
        UseContinuousFeedRendering(80, margin);
    }

    /**
     * Creates a single page PDF which will force its entire content's width and height to fit into one page. Can be used for a consumer bill or receipt.
     *
     * @param width The width in millimeters to apply to the PDF page. Default is 80
     */
    public void UseContinuousFeedRendering(double width) {
        UseContinuousFeedRendering(width, 5);
    }

    /**
     * Creates a single page PDF which will force its entire content's width and height to fit into one page. Can be used for a consumer bill or receipt.
     *
     * @param width  The width in millimeters to apply to the PDF page. Default is 80
     * @param margin The margin in millimeters to apply to the PDF page. Default is 5
     */
    public void UseContinuousFeedRendering(double width, int margin) {
        fitToPaperMode = FitToPaperModes.ContinuousFeed;
        paperSize = PaperSize.Custom;
        customPaperWidth = width;
        marginTop = 0;
        marginBottom = 0;
        marginLeft = margin;
        marginRight = margin;
    }
}
