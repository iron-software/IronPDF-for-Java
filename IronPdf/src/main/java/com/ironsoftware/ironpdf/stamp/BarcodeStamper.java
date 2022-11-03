package com.ironsoftware.ironpdf.stamp;

/**
 * Allows the developers to add Barcode(s) and QR code(s) to PDF documents elegantly and easily.
 * <p>An implementation of {@link Stamper}.</p>
 * <p>See: {@link com.ironsoftware.ironpdf.PdfDocument#applyStamp(Stamper)}</p>
 */
public class BarcodeStamper extends Stamper {

    /**
     * The value of the barcode as a string.
     */
    private String value;
    /**
     * Barcode encoding type to use for this Stamper. Supported encoding types include: QRCode,
     * Code128, and Code39. Please see:
     * {@link com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding}.
     * <p>Default is QRCode</p>
     */
    private BarcodeEncoding barcodeType = BarcodeEncoding.QRCode;
    /**
     * The width of the rendered barcode in pixels. Default is 250px
     */
    private int width = 250;
    /**
     * The height of the rendered barcode in pixels. Default is 250px
     */
    private int height = 250;

    /**
     * Initializes a new instance of the  {@link BarcodeStamper} class.
     * <p>Width and Height are 250px each by default unless explicitly set.</p>
     *
     * @param Value       The value of the barcode as a string.
     * @param BarcodeType Barcode encoding type to use for this Stamper. Supported encoding types                    include: QRCode, Code128, and Code39. {@link BarcodeEncoding} .
     */
    public BarcodeStamper(String Value, BarcodeEncoding BarcodeType) {
        this.setValue(Value);
        this.setBarcodeType(BarcodeType);
    }

    /**
     * Initializes a new instance of the  {@link BarcodeStamper} class.
     *
     * @param Value       The value of the barcode as a string.
     * @param BarcodeType Barcode encoding type to use for this Stamper. Supported encoding types                    include: QRCode, Code128, and Code39. {@link BarcodeEncoding} .
     * @param Width       The width of the rendered barcode in pixels.
     * @param Height      The height of the rendered barcode in pixels.
     */
    public BarcodeStamper(String Value, BarcodeEncoding BarcodeType, int Width, int Height) {
        this.setValue(Value);
        this.setBarcodeType(BarcodeType);
        this.setWidth(Width);
        this.setHeight(Height);
    }

    /**
     * Gets value. The value of the barcode as a string.
     *
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * Sets value. The value of the barcode as a string.
     *
     * @param value the value
     */
    public final void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets barcode type. Barcode encoding type to use for this Stamper. Supported encoding types include: QRCode,
     * Code128, and Code39. Please see:
     * {@link com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding}.
     * <p>Default is QRCode</p>
     *
     * @return the barcode type
     */
    public final BarcodeEncoding getBarcodeType() {
        return barcodeType;
    }

    /**
     * Sets barcode type. Barcode encoding type to use for this Stamper. Supported encoding types include: QRCode,
     * Code128, and Code39. Please see:
     * {@link com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding}.
     * <p>Default is QRCode</p>
     *
     * @param value the value
     */
    public final void setBarcodeType(BarcodeEncoding value) {
        barcodeType = value;
    }

    /**
     * Gets width. The width of the rendered barcode in pixels. Default is 250px
     *
     * @return the width
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Sets width. The width of the rendered barcode in pixels. Default is 250px
     *
     * @param value the value
     */
    public final void setWidth(int value) {
        width = value;
    }

    /**
     * Gets height. The height of the rendered barcode in pixels. Default is 250px
     *
     * @return the height
     */
    public final int getHeight() {
        return height;
    }

    /**
     * Sets height. The height of the rendered barcode in pixels. Default is 250px
     *
     * @param value the value
     */
    public final void setHeight(int value) {
        height = value;
    }

}
