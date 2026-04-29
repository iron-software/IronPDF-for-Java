package com.ironsoftware.ironpdf.render;
 
import java.awt.geom.Rectangle2D;
 
/**
 * Represents the rendered location of an HTML element within a PDF document.
 *
 * <p>Returned by {@code PdfDocument.getElementLocations()} after rendering HTML with
 * {@link ChromePdfRenderOptions#setElementQuerySelectors(String[])} configured.</p>
 *
 * <p>Mirrors {@code IronPdf.RenderedElementLocation} on the C# side.</p>
 */
public class RenderedElementLocation {
 
    private String text;
    private int pageIndex;
    private Rectangle2D.Double rectangle;
    private int elementIndex;
 
    /**
     * Default constructor.
     */
    public RenderedElementLocation() {
    }
 
    /**
     * Full constructor.
     *
     * @param text         The text content of the matched element
     * @param pageIndex    The zero-based page index where the element was rendered
     * @param rectangle    The bounding rectangle of the annotation marker on the page
     * @param elementIndex The original document-order index of the element among all matched elements
     */
    public RenderedElementLocation(String text, int pageIndex, Rectangle2D.Double rectangle, int elementIndex) {
        this.text = text;
        this.pageIndex = pageIndex;
        this.rectangle = rectangle;
        this.elementIndex = elementIndex;
    }
 
    /**
     * @return The text content of the matched element.
     */
    public String getText() {
        return text;
    }
 
    public void setText(String text) {
        this.text = text;
    }
 
    /**
     * @return The zero-based page index where the element was rendered.
     */
    public int getPageIndex() {
        return pageIndex;
    }
 
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
 
    /**
     * @return The bounding rectangle of the annotation marker on the page, in PDF points (1/72 inch).
     * Coordinate origin is the bottom-left corner of the page. The rectangle corresponds to the
     * injected anchor marker placed at the start of the matched element.
     */
    public Rectangle2D.Double getRectangle() {
        return rectangle;
    }
 
    public void setRectangle(Rectangle2D.Double rectangle) {
        this.rectangle = rectangle;
    }
 
    /**
     * @return The original index of the element in document order among all matched elements.
     */
    public int getElementIndex() {
        return elementIndex;
    }
 
    public void setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
    }
 
    @Override
    public String toString() {
        return "RenderedElementLocation{"
                + "elementIndex=" + elementIndex
                + ", pageIndex=" + pageIndex
                + ", rectangle=" + rectangle
                + ", text='" + text + '\''
                + '}';
    }
}