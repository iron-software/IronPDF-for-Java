package com.ironsoftware.ironpdf.stamp;

import java.nio.file.Path;

/**
 * A subclass of {@link Stamper}. Defines an HTML PDF Stamper.
 * {@link com.ironsoftware.ironpdf.PdfDocument#ApplyStamp(Stamper)}
 */
public class HtmlStamper extends Stamper {

    /**
     * Milliseconds to wait after Html is rendered before printing.  This can use useful when
     * considering the rendering of JavaScript, Ajax or animations.
     * <p>Default value is 0.</p>
     */
    private int RenderDelay = 500;
    /**
     * Render timeout in seconds
     * <p>Default value is 60.</p>
     */
    private int Timeout = 60;
    /**
     * Enables Media="screen" CSS Styles  and StyleSheets <p>Note: By setting AllowScreenCss=false,
     * IronPdf renders Stamp from HTML using CSS for media="print" as if printing a web page in a
     * browser print dialog.</p>
     * <p>Default value is PdfCssMediaType.Screen.</p>
     */
    private com.ironsoftware.ironpdf.render.CssMediaType CssMediaType = com.ironsoftware.ironpdf.render.CssMediaType.SCREEN;

    /**
     * Initializes a new instance of the  {@link HtmlStamper} class.
     *
     * @param html The HTML string.
     */
    public HtmlStamper(String html) {
        super(html);
    }

    /**
     * Initializes a new instance of the  {@link HtmlStamper} class.
     *
     * @param html          The HTML string.
     * @param baseUrlString The HTML base URL for which references to external CSS, Javascript and
     *                      Image files will be relative.
     */
    public HtmlStamper(String html, String baseUrlString) {
        super(html);
        setHtmlBaseUrl(baseUrlString);
    }

    /**
     * Initializes a new instance of the  {@link HtmlStamper} class.
     *
     * @param html    The HTML string.
     * @param baseUrl The HTML base URL for which references to external CSS, Javascript and Image
     *                files will be relative.
     */
    public HtmlStamper(String html, Path baseUrl) {
        super(html);
        setHtmlBaseUrl(baseUrl.toAbsolutePath().toString());
    }

    /**
     * Initializes a new instance of the  {@link HtmlStamper} class.
     */
    public HtmlStamper() {
    }

    /**
     * The HTML base URL for which references to external CSS, Javascript and Image files will be
     * relative.
     */
    public final String getHtmlBaseUrl() {
        return getInnerHtmlBaseUrl();
    }

    public final void setHtmlBaseUrl(String value) {
        setInnerHtmlBaseUrl(value);
    }

    public final int getRenderDelay() {
        return RenderDelay;
    }

    public final void setRenderDelay(int value) {
        RenderDelay = value;
    }

    public final int getTimeout() {
        return Timeout;
    }

    public final void setTimeout(int value) {
        Timeout = value;
    }

    public final com.ironsoftware.ironpdf.render.CssMediaType getCssMediaType() {
        return CssMediaType;
    }

    public final void setCssMediaType(com.ironsoftware.ironpdf.render.CssMediaType value) {
        CssMediaType = value;
    }

}
