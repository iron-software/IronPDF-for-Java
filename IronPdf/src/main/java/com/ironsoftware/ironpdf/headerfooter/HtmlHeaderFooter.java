package com.ironsoftware.ironpdf.headerfooter;

import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;

/**
 * A Html Header or Footer which will be printed onto every page of the PDF.
 * <p>When using HtmlHeaderFooter it is important to set {@link #htmlFragment}</p>
 * <p>Merge meta-data into your html using any of these placeholder strings: {page} {total-pages}
 * {url} {date} {time} {html-title} {pdf-title}</p>
 * <p>See:{@link com.ironsoftware.ironpdf.PdfDocument#addHtmlHeader(HtmlHeaderFooter)}  &amp; {@link com.ironsoftware.ironpdf.PdfDocument#addHtmlFooter(HtmlHeaderFooter)} </p>
 */
public class HtmlHeaderFooter implements Cloneable {

    private boolean drawDividerLine = false;
    private String baseUrl = null;
    private Integer maxHeight = null;
    private String htmlFragment = "";
    private boolean loadStylesAndCSSFromMainHtmlDocument = false;

    /**
     * Maximum Height of the Html Header / Footer in millimeters.  This value must be set sufficiently
     * high to display the full html header / footer content.
     *
     * @return the max height
     */
    public Integer getMaxHeight() {
        return maxHeight;
    }

    /**
     * Maximum Height of the Html Header / Footer in millimeters.  This value must be set sufficiently
     * high to display the full html header / footer content.
     *
     * @param value the value
     */
    public void setMaxHeight(Integer value) {
        maxHeight = value;
    }

    /**
     * A horizontal line divider between the header / footer and the page content on every page of the
     * PDF document.
     *
     * @return the boolean
     */
    public boolean isDrawDividerLine() {
        return drawDividerLine;
    }

    /**
     * Adds a horizontal line divider between the header / footer and the page content on every page
     * of the PDF document.
     *
     * @param value the value
     */
    public void setDrawDividerLine(boolean value) {
        drawDividerLine = value;
    }

    /**
     * The Base URL all URLS in the {@link #htmlFragment}" will be relative to. This includes 'src'
     * attributes on images, scripts, style-sheets and also hrefs on hyperlinks. <p>Note: A base URL
     * that points to a directory should end with a slash.</p> <p>Base URL accepts file paths as well
     * as URLS. If no BaseUrl is given, the HtmlHeaderFooter BaseUrl will be inherited from the main
     * HTML document where possible.</p>
     *
     * @return the base url
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * The Base URL all URLS in the {@link #htmlFragment}" will be relative to. This includes 'src'
     * attributes on images, scripts, style-sheets and also hrefs on hyperlinks. <p>Note: A base URL
     * that points to a directory should end with a slash.</p> <p>Base URL accepts file paths as well
     * as URLS. If no BaseUrl is given, the HtmlHeaderFooter BaseUrl will be inherited from the main
     * HTML document where possible.</p>
     *
     * @param value the value
     */
    public void setBaseUrl(String value) {
        baseUrl = value;
    }

    /**
     * The Html which will be used to render the Header / Footer.  Should be  a Html snippet rather
     * than a complete document.  May contain styles &amp; images. <p>Merge meta-data into the
     * HtmlFragment by putting any of these placeholder strings into the text: {page} {total-pages}
     * {url} {date} {time} {html-title} {pdf-title}. An alternative mail-merge style using the pattern
     * &lt;span class='total-pages'&gt;&lt;/span&gt; also work</p>
     * <p>HtmlFragment is a stand alone HTML document which does not inherit styles or settings from
     * your
     * main HTML content unless {@link #loadStylesAndCSSFromMainHtmlDocument} is set true</p>
     *
     * @return the html fragment
     */
    public String getHtmlFragment() {
        return htmlFragment;
    }

    /**
     * The Html which will be used to render the Header / Footer.  Should be  a Html snippet rather
     * than a complete document.  May contain styles &amp; images. <p>Merge meta-data into the
     * HtmlFragment by putting any of these placeholder strings into the text: {page} {total-pages}
     * {url} {date} {time} {html-title} {pdf-title}. An alternative mail-merge style using the pattern
     * &lt;span class='total-pages'&gt;&lt;/span&gt; also work</p>
     * <p>HtmlFragment is a stand alone HTML document which does not inherit styles or settings from
     * your
     * main HTML content unless {@link #loadStylesAndCSSFromMainHtmlDocument} is set true</p>
     *
     * @param value the value
     */
    public void setHtmlFragment(String value) {
        htmlFragment = value;
    }

    /**
     * Loads style code blocks and links to CSS style sheets from the main HTML document (which
     * provides the PDF content) into the {@link HtmlHeaderFooter} . <p>By default, Html Headers and
     * Footers are stand- alone HTML documents with their own default styles.  Setting
     * {@link #loadStylesAndCSSFromMainHtmlDocument} to true will attempt to load all STYLE and LINK
     * tags from the main HTML document (which renders teh PDF) into the {@link HtmlHeaderFooter}
     * .</p> <p>If your main HTML document contains complex CSS frameworks,  styles the HEAD or BODY
     * element heavily or loads CSS from javascript then this method may not work as intended.</p>
     * <p>This feature is not available for  RenderUrlAsPdf methods.  It works for HTMLToPdf and
     * HtmlFileToPdf conversions only.</p> <p>It is often preferable to load style sheets explicitly
     * into your HTML Headers and Footers as STYLE and LINK tags within the {@link #htmlFragment} for
     * granular control</p>
     *
     * @return the boolean
     */
    public boolean isLoadStylesAndCSSFromMainHtmlDocument() {
        return loadStylesAndCSSFromMainHtmlDocument;
    }

    /**
     * Loads style code blocks and links to CSS style sheets from the main HTML document (which
     * provides the PDF content) into the {@link HtmlHeaderFooter} . <p>By default, Html Headers and
     * Footers are stand- alone HTML documents with their own default styles.  Setting
     * {@link #loadStylesAndCSSFromMainHtmlDocument} to true will attempt to load all STYLE and LINK
     * tags from the main HTML document (which renders teh PDF) into the {@link HtmlHeaderFooter}
     * .</p> <p>If your main HTML document contains complex CSS frameworks,  styles the HEAD or BODY
     * element heavily or loads CSS from javascript then this method may not work as intended.</p>
     * <p>This feature is not available for  RenderUrlAsPdf methods.  It works for HTMLToPdf and
     * HtmlFileToPdf conversions only.</p> <p>It is often preferable to load style sheets explicitly
     * into your HTML Headers and Footers as STYLE and LINK tags within the {@link #htmlFragment} for
     * granular control</p>
     *
     * @param value the value
     */
    public void setLoadStylesAndCSSFromMainHtmlDocument(boolean value) {
        loadStylesAndCSSFromMainHtmlDocument = value;
    }

    /**
     * Supports {@link Cloneable}.  Creates a deep copy of this class instance.
     *
     * @return A deep clone of this instance.  Use explicit casting to convert object back to the intended type.
     * @throws CloneNotSupportedException the clone not supported exception
     */
    public Object Clone() throws CloneNotSupportedException {
        return this.clone();
    }

    /**
     * Instantiates a new Html header footer.
     */
    public HtmlHeaderFooter(){

    }

    /**
     * Instantiates a new Html header footer.
     *
     * @param htmlFragment the html fragment
     */
    public HtmlHeaderFooter(String htmlFragment){
        this.htmlFragment = htmlFragment;
    }
}
