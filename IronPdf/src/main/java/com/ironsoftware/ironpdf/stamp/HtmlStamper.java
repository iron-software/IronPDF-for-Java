package com.ironsoftware.ironpdf.stamp;

import com.ironsoftware.ironpdf.render.WaitFor;

import java.nio.file.Path;

/**
 * Defines an HTML stamper allowing developers to edit a {@link com.ironsoftware.ironpdf.PdfDocument} by adding new content designed in HTML, CSS and JavaScript.
 * <p>An implementation of {@link Stamper}.</p>
 * <p>See: {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper)}</p>
 */
public class HtmlStamper extends Stamper {

    @Deprecated
    private int renderDelay = 0;

    /**
     * Render timeout in seconds <p>Default value is 60.</p>
     */
    private int timeout = 60;
    /**
     * Enables Media="screen" CSS Styles  and StyleSheets <p>Note: By setting CssMediaType=PRINT,
     * IronPdf renders Stamp from HTML using CSS for media="print" as if printing a web page in a
     * browser print dialog. It renders exactly as per Google Chrome.</p>
     * <p>Default value is CssMediaType.SCREEN.</p>
     */
    private com.ironsoftware.ironpdf.render.CssMediaType cssMediaType = com.ironsoftware.ironpdf.render.CssMediaType.SCREEN;

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
     * @param baseUrlString The HTML base URL for which references to external CSS, Javascript and                      Image files will be relative.
     */
    public HtmlStamper(String html, String baseUrlString) {
        super(html);
        setHtmlBaseUrl(baseUrlString);
    }

    /**
     * Initializes a new instance of the  {@link HtmlStamper} class.
     *
     * @param html    The HTML string.
     * @param baseUrl The HTML base URL for which references to external CSS, Javascript and Image                files will be relative.
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
     * Gets the HTML base URL for which references to external CSS, Javascript and Image files will be
     * relative.
     *
     * @return the html base url
     */
    public final String getHtmlBaseUrl() {
        return getInnerHtmlBaseUrl();
    }

    /**
     * Sets the HTML base URL for which references to external CSS, Javascript and Image files will be
     * relative.
     *
     * @param value the value
     */
    public final void setHtmlBaseUrl(String value) {
        setInnerHtmlBaseUrl(value);
    }

    /**
     * @deprecated use {@link #getWaitFor} instead
     */
    @Deprecated
    public final int getRenderDelay() {
        return renderDelay;
    }

    /**
     * @deprecated use {@link #setWaitFor} instead
     */
    @Deprecated
    public final void setRenderDelay(int value) {
        renderDelay = value;
    }


    /**
     * Gets timeout. Render timeout in seconds <p>Default value is 60.</p>
     *
     * @return the timeout
     */
    public final int getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout. Render timeout in seconds <p>Default value is 60.</p>
     *
     * @param value the value
     */
    public final void setTimeout(int value) {
        timeout = value;
    }

    /**
     * Gets css media type. Enables Media="screen" CSS Styles  and StyleSheets <p>Note: By setting CssMediaType=PRINT,
     * IronPdf renders Stamp from HTML using CSS for media="print" as if printing a web page in a
     * browser print dialog. It renders exactly as per Google Chrome.</p>
     * <p>Default value is CssMediaType.SCREEN.</p>
     *
     * @return the css media type
     */
    public final com.ironsoftware.ironpdf.render.CssMediaType getCssMediaType() {
        return cssMediaType;
    }

    /**
     * Sets css media type. Enables Media="screen" CSS Styles  and StyleSheets <p>Note: By setting CssMediaType=PRINT,
     * IronPdf renders Stamp from HTML using CSS for media="print" as if printing a web page in a
     * browser print dialog. It renders exactly as per Google Chrome.</p>
     * <p>Default value is CssMediaType.SCREEN.</p>
     *
     * @param value the value
     */
    public final void setCssMediaType(com.ironsoftware.ironpdf.render.CssMediaType value) {
        cssMediaType = value;
    }
}


