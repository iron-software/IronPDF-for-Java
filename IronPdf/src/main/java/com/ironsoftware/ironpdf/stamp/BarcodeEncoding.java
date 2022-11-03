package com.ironsoftware.ironpdf.stamp;

/**
 * Barcode Encoding Types. Please check the supported characters for each encoding type as some do
 * not support all symbols.
 * <p>See: {@link BarcodeStamper}</p>
 */
public enum BarcodeEncoding {

    /**
     * Code 128 (1D barcode format). Code 128 is a high-density linear barcode symbology defined in
     * ISO/IEC 15417:2007. Supported characters include: All alphabetic and numeric
     * characters.
     */
    Code128,

    /**
     * Code 39 (1D barcode format). Code 39 is a variable length, discrete barcode symbology. The Code
     * 39 specification defines 43 characters. Supported characters include: Digits from (0-9),
     * Uppercase (A through Z), and these symbols: (-.$/+% space)
     */
    Code39,

    /**
     * QR Code (2D barcode format).  QR code (abbreviated from Quick Response Code) is a
     * machine-readable optical label that contains information about the item to which it is
     * attached. A QR code uses four standardized encoding modes to efficiently store data.
     * Supported characters include: All numeric, alphanumeric, byte/binary, and Japanese kanji.
     */
    QRCode
}
