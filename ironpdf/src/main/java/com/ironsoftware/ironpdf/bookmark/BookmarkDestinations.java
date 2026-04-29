package com.ironsoftware.ironpdf.bookmark;

/**
 * Destination type that controls how a PDF reader displays the target page when a
 * bookmark or internal hyperlink is followed.
 *
 * <p>Mirrors {@code IronPdf.Bookmarks.BookmarkDestinations} on the C# side. The
 * {@link #ordinal()} of each value is what the engine expects on the wire, so
 * the order of entries below must match the C# enum.</p>
 *
 * <p>Corresponds to the PDF 1.7 specification §12.3.2 "Explicit Destinations".</p>
 */
public enum BookmarkDestinations {
    /**
     * Fit the entire destination page within the window (PDF {@code /Fit}).
     * No additional coordinates are used.
     */
    PAGE,
 
    /**
     * Scroll to a specific Y coordinate on the destination page while fitting the
     * page width to the window (PDF {@code /FitH}). Uses
     * {@code destinationTop}.
     */
    PAGE_Y,
 
    /**
     * Scroll to a specific X coordinate on the destination page while fitting the
     * page height to the window (PDF {@code /FitV}). Uses
     * {@code destinationLeft}.
     */
    PAGE_X,
 
    /**
     * Scroll to a specific position and zoom level (PDF {@code /XYZ}). Uses
     * {@code destinationLeft}, {@code destinationTop}, and {@code destinationZoom}.
     * A {@code destinationZoom} of 0 means "inherit the current zoom".
     */
    PAGE_ZOOM,
 
    /**
     * Fit the specified rectangle of the destination page into the window
     * (PDF {@code /FitR}). Uses {@code destinationLeft}, {@code destinationBottom},
     * {@code destinationRight}, and {@code destinationTop}.
     */
    PAGE_RECT,
 
    /**
     * Fit the bounding box of the destination page into the window (PDF {@code /FitB}).
     * No additional coordinates are used.
     */
    PAGE_BOUNDS,
 
    /**
     * Scroll to a Y coordinate while fitting the bounding box width to the window
     * (PDF {@code /FitBH}). Uses {@code destinationTop}.
     */
    PAGE_BOUNDS_Y,
 
    /**
     * Scroll to an X coordinate while fitting the bounding box height to the window
     * (PDF {@code /FitBV}). Uses {@code destinationLeft}.
     */
    PAGE_BOUNDS_X
}
