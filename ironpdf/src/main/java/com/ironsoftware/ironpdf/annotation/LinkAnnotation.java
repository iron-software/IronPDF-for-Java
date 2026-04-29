package com.ironsoftware.ironpdf.annotation;
 
import com.ironsoftware.ironpdf.bookmark.BookmarkDestinations;
 
import java.awt.Rectangle;
 
/**
 * Defines a clickable link annotation that navigates to a specific page within the same PDF
 * document. Useful for building custom tables of contents, cross-references, and in-document
 * navigation.
 *
 * <p>Mirrors {@code IronPdf.Annotations.LinkAnnotation} on the C# side.</p>
 *
 * <pre>{@code
 * // Add a link on page 0 that navigates to page 5:
 * LinkAnnotation link = new LinkAnnotation(0, 5);
 * link.setX(50);
 * link.setY(700);
 * link.setWidth(200);
 * link.setHeight(20);
 * pdf.getAnnotation().addLinkAnnotation(link);
 *
 * // Custom TOC link with contents text:
 * LinkAnnotation tocLink = new LinkAnnotation(0, 3, "Chapter 1 - Introduction");
 * tocLink.setX(72);
 * tocLink.setY(600);
 * tocLink.setWidth(300);
 * tocLink.setHeight(16);
 * pdf.getAnnotation().addLinkAnnotation(tocLink);
 * }</pre>
 *
 * <p>The link annotation creates a clickable region on the page. By default the region is
 * invisible — set {@link #setShowBorder(boolean)} to {@code true} for a visible border.</p>
 *
 * <p><b>Coordinate note:</b> {@link #setY(int)} sets the <em>bottom</em> edge of the clickable
 * area using PDF coordinates (origin at bottom-left of page).</p>
 */
public class LinkAnnotation {
 
    private int pageIndex;
    private int destinationPageIndex;
    private BookmarkDestinations destinationType = BookmarkDestinations.PAGE;
    private int destinationLeft = 0;
    private int destinationRight = 0;
    private int destinationTop = 0;
    private int destinationBottom = 0;
    private int destinationZoom = 0;
 
    private int annotationIndex = -1;
 
    private Rectangle rectangle;
    private String colorCode;
    private boolean hidden = false;
    private String contents = "";
    private String title = "";
    private boolean showBorder = false;
 
    /**
     * Creates a link annotation on the specified page that navigates to a destination page.
     *
     * @param pageIndex            Zero-based index of the page where the link is placed.
     * @param destinationPageIndex Zero-based index of the page to navigate to when clicked.
     */
    public LinkAnnotation(int pageIndex, int destinationPageIndex) {
        this.pageIndex = pageIndex;
        this.destinationPageIndex = destinationPageIndex;
    }
 
    /**
     * Creates a link annotation on the specified page that navigates to a destination page,
     * with descriptive text for identification.
     *
     * @param pageIndex            Zero-based index of the page where the link is placed.
     * @param destinationPageIndex Zero-based index of the page to navigate to when clicked.
     * @param contents             Descriptive text for the link annotation.
     */
    public LinkAnnotation(int pageIndex, int destinationPageIndex, String contents) {
        this(pageIndex, destinationPageIndex);
        this.contents = contents == null ? "" : contents;
    }
 
    /**
     * @return zero-based page index where this link annotation is placed
     */
    public int getPageIndex() {
        return pageIndex;
    }
 
    /**
     * @param pageIndex zero-based page index where this link annotation is placed
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
 
    /**
     * @return zero-based page index this link navigates to when clicked
     */
    public int getDestinationPageIndex() {
        return destinationPageIndex;
    }
 
    /**
     * @param destinationPageIndex zero-based page index this link navigates to when clicked
     */
    public void setDestinationPageIndex(int destinationPageIndex) {
        this.destinationPageIndex = destinationPageIndex;
    }
 
    /**
     * @return the destination type controlling how the target page is displayed
     */
    public BookmarkDestinations getDestinationType() {
        return destinationType;
    }
 
    /**
     * @param destinationType destination type controlling how the target page is displayed
     */
    public void setDestinationType(BookmarkDestinations destinationType) {
        this.destinationType = destinationType == null ? BookmarkDestinations.PAGE : destinationType;
    }
 
    public int getDestinationLeft() {
        return destinationLeft;
    }
 
    public void setDestinationLeft(int destinationLeft) {
        this.destinationLeft = destinationLeft;
    }
 
    public int getDestinationRight() {
        return destinationRight;
    }
 
    public void setDestinationRight(int destinationRight) {
        this.destinationRight = destinationRight;
    }
 
    public int getDestinationTop() {
        return destinationTop;
    }
 
    public void setDestinationTop(int destinationTop) {
        this.destinationTop = destinationTop;
    }
 
    public int getDestinationBottom() {
        return destinationBottom;
    }
 
    public void setDestinationBottom(int destinationBottom) {
        this.destinationBottom = destinationBottom;
    }
 
    public int getDestinationZoom() {
        return destinationZoom;
    }
 
    public void setDestinationZoom(int destinationZoom) {
        this.destinationZoom = destinationZoom;
    }
 
    /**
     * @return annotation index on its page, or -1 if not yet added
     */
    public int getAnnotationIndex() {
        return annotationIndex;
    }
 
    public void setAnnotationIndex(int annotationIndex) {
        this.annotationIndex = annotationIndex;
    }
 
    /**
     * @return horizontal position from the LEFT edge of the page, in points, or -1 if not set
     */
    public int getX() {
        return rectangle == null ? -1 : rectangle.x;
    }
 
    public void setX(int x) {
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        rectangle.x = x;
    }
 
    /**
     * @return vertical position from the BOTTOM edge of the page, in points, or -1 if not set
     */
    public int getY() {
        return rectangle == null ? -1 : rectangle.y;
    }
 
    public void setY(int y) {
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        rectangle.y = y;
    }
 
    public int getWidth() {
        return rectangle == null ? -1 : rectangle.width;
    }
 
    public void setWidth(int width) {
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        rectangle.width = width;
    }
 
    public int getHeight() {
        return rectangle == null ? -1 : rectangle.height;
    }
 
    public void setHeight(int height) {
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        rectangle.height = height;
    }
 
    /**
     * @return the clickable area of the link (may be null if not set)
     */
    public Rectangle getRectangle() {
        return rectangle;
    }
 
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
 
    /**
     * @return color code for the link border (CSS {@code #RRGGBB} format) or null
     */
    public String getColorCode() {
        return colorCode;
    }
 
    /**
     * @param colorCode color code for the link border (CSS {@code #RRGGBB} format) or null
     */
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
 
    /**
     * @return whether the link annotation is hidden from users
     */
    public boolean isHidden() {
        return hidden;
    }
 
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
 
    /**
     * @return descriptive text content for this link annotation
     */
    public String getContents() {
        return contents;
    }
 
    public void setContents(String contents) {
        this.contents = contents == null ? "" : contents;
    }
 
    /**
     * @return title of the link annotation
     */
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }
 
    /**
     * @return whether the link annotation displays a visible border (default {@code false})
     */
    public boolean isShowBorder() {
        return showBorder;
    }
 
    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }
 
    /**
     * @return a string representation of this link annotation, e.g.
     * {@code "Link on page 2 -> page 5: My Link"}
     */
    @Override
    public String toString() {
        return "Link on page " + pageIndex + " -> page " + destinationPageIndex + ": " + contents;
    }
}