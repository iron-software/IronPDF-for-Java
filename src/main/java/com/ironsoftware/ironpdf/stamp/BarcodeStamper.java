package com.ironsoftware.ironpdf.stamp;

/**
 * Allows the developers to add Barcode(s) and QR code(s) to PDf documents with ease.
 */
public class BarcodeStamper extends Stamper {

    /**
     * The value of the barcode as a string.
     */
    private String Value;
    /**
     * Barcode encoding type to use for this Stamper. Supported encoding types include: QRCode,
     * Code128, and Code39. Please see:
     * {@link com.ironsoftware.ironpdf.internal.proto.BarcodeEncoding}.
     * <p>Default is QRCode</p>
     */
    private BarcodeEncoding BarcodeType = BarcodeEncoding.QRCode;
    /**
     * The width of the rendered barcode in pixels. Default is 250px
     */
    private int Width = 250;
    /**
     * The height of the rendered barcode in pixels. Default is 250px
     */
    private int Height = 250;

    /**
     * Initializes a new instance of the  {@link BarcodeStamper} class.
     * <p>Width and Height are 250px each by default unless explicitly set.</p>
     *
     * @param Value       The value of the barcode as a string.
     * @param BarcodeType Barcode encoding type to use for this Stamper. Supported encoding types
     *                    include: QRCode, Code128, and Code39. {@link BarcodeEncoding} .
     */
    public BarcodeStamper(String Value, BarcodeEncoding BarcodeType) {
        this.setValue(Value);
        this.setBarcodeType(BarcodeType);
    }

    /**
     * Initializes a new instance of the  {@link BarcodeStamper} class.
     *
     * @param Value       The value of the barcode as a string.
     * @param BarcodeType Barcode encoding type to use for this Stamper. Supported encoding types
     *                    include: QRCode, Code128, and Code39. {@link BarcodeEncoding} .
     * @param Width       The width of the rendered barcode in pixels.
     * @param Height      The height of the rendered barcode in pixels.
     */
    public BarcodeStamper(String Value, BarcodeEncoding BarcodeType, int Width, int Height) {
        this.setValue(Value);
        this.setBarcodeType(BarcodeType);
        this.setWidth(Width);
        this.setHeight(Height);
    }

    public final String getValue() {
        return Value;
    }

    public final void setValue(String value) {
        Value = value;
    }

    public final BarcodeEncoding getBarcodeType() {
        return BarcodeType;
    }

    public final void setBarcodeType(BarcodeEncoding value) {
        BarcodeType = value;
    }

    public final int getWidth() {
        return Width;
    }

    public final void setWidth(int value) {
        Width = value;
    }

    public final int getHeight() {
        return Height;
    }

    public final void setHeight(int value) {
        Height = value;
    }

}
