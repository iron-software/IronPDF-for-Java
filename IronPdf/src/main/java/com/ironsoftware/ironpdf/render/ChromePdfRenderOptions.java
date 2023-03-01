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
     * from Ajax / Angular Applications. <p>Also see {@link #renderDelay}</p>
     */
    private boolean enableJavaScript = true;

    /**
     * Behavior when fitting HTML content to a physical paper size.
     * Determines {@link #getZoom()} and  {@link #getViewPortWidth()}.
     * <p>See {@link FitToPaperModes} for a detailed description of each mode.</p>
     * <p>{@link FitToPaperModes#Zoom} disables automatic fitting behavior.</p>
     * <p>{@link FitToPaperModes#Automatic} automatically measures and fits HTML content onto each PDF page.</p>
     * <p>Default value is FitToPaperModes.None.</p>
     */
    private FitToPaperModes fitToPaperMode = FitToPaperModes.Zoom;
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
    /**
     * Milliseconds to wait after Html is rendered before printing.  This can use useful when
     * considering the rendering of JavaScript, Ajax or animations.
     */
    private int renderDelay = 0;

    /**
     * Render timeout in seconds
     */
    private int timeout = 60;
    /**
     * PDF Document Name and Title meta-data.  Not required.  Useful for mail-merge and file naming
     */

    private String title;
    /**
     * Defines a virtual screen height used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p> <p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many websites.</p>
     */
    private int viewPortHeight = 1280;
    /**
     * Defines a virtual screen width used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p><p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many website</p>
     */
    private int viewPortWidth = 1024;
    /**
     * The zoom level in %. Enlarges the rendering size of Html documents.
     */
    private int zoom = 100;

    /**
     * A custom javascript string to be executed after all HTML has loaded but before PDf rendering.
     */
    private String javascript;

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
     * from Ajax / Angular Applications. <p>Also see {@link #renderDelay}</p>
     *
     * @return the boolean
     */
    public boolean isEnableJavaScript() {
        return enableJavaScript;
    }

    /**
     * Sets enable JavaScript. Enables JavaScript and Json to be executed  before the page is rendered.  Ideal for printing
     * from Ajax / Angular Applications. <p>Also see {@link #renderDelay}</p>
     *
     * @param value the value
     */
    public void setEnableJavaScript(boolean value) {
        enableJavaScript = value;
    }

    /**
     * Behavior when fitting HTML content to a physical paper size.
     * Determines {@link #getZoom()} and {@link #getViewPortWidth()}.
     *
     * @return the boolean
     */
    public FitToPaperModes getFitToPaperMode() {
        return fitToPaperMode;
    }

    /**
     * Behavior when fitting HTML content to a physical paper size.
     * Determines {@link #setZoom(int)} and {@link #setViewPortWidth(int)}.
     *
     * @param value the value
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
     * Gets render delay. Milliseconds to wait after Html is rendered before printing.  This can use useful when
     * considering the rendering of JavaScript, Ajax or animations.
     *
     * @return the render delay
     */
    public int getRenderDelay() {
        return renderDelay;
    }

    /**
     * Sets render delay. Milliseconds to wait after Html is rendered before printing.  This can use useful when
     * considering the rendering of JavaScript, Ajax or animations.
     *
     * @param value the value
     */
    public void setRenderDelay(int value) {
        renderDelay = value;
    }

    /**
     * Gets timeout. Render timeout in seconds.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout. Render timeout in seconds.
     *
     * @param value the value
     */
    public void setTimeout(int value) {
        timeout = value;
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
     * Gets view port height. Defines a virtual screen height used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p> <p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many websites.</p>
     *
     * @return the view port height
     */
    public int getViewPortHeight() {
        return viewPortHeight;
    }

    /**
     * Sets view port height. Defines a virtual screen height used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p> <p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many websites.</p>
     *
     * @param value the value
     */
    public void setViewPortHeight(int value) {
        viewPortHeight = value;
    }

    /**
     * Gets view port width. Defines a virtual screen width used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p><p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many website</p>
     *
     * @return the view port width
     */
    public int getViewPortWidth() {
        return viewPortWidth;
    }

    /**
     * Sets view port width. Defines a virtual screen width used to render HTML to PDF in IronPdf. Measured in pixels.
     * <p>Viewport size is important in modern responsive HTML5 + CSS3 websites (e.g. Bootstrap
     * framework websites) because the rendering and order of elements on the screen is dependent on
     * viewport size.</p><p>The default viewport is 1280px wide by 1024px high to ensure the desktop
     * version of a website is rendered unless otherwise specified.   Smaller sizes (particularly
     * width) will render responsive versions of many website</p>
     *
     * @param value the value
     */
    public void setViewPortWidth(int value) {
        viewPortWidth = value;
    }

    /**
     * Get A custom javascript string to be executed after all HTML has loaded but before PDf rendering.
     * @return the javascript string
     */
    public String getJavascript() {
        return javascript;
    }

    /**
     * Gets zoom. The zoom level in %. Enlarges the rendering size of Html documents.
     *
     * @return the zoom
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * Sets zoom. The zoom level in %. Enlarges the rendering size of Html documents.
     *
     * @param value the value
     */
    public void setZoom(int value) {
        zoom = value;
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
     * @param javascript a javascript string.
     */
    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }
}
