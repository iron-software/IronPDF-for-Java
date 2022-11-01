package com.ironsoftware.ironpdf.edit;

/**
 * A length value with {@link MeasurementUnit}
 */
public class Length {

    private double Value;
    private MeasurementUnit Unit = MeasurementUnit.values()[0];

    public Length(double value) {
        this(value, MeasurementUnit.PERCENTAGE);
    }

    public Length(double value, MeasurementUnit unit) {
        setValue(value);
        setUnit(unit);
    }

    /**
     * Default length as 0%
     */
    public Length() {
        this(0, MeasurementUnit.PERCENTAGE);
    }

    public static double ToPt(Length length) {
        switch (length.getUnit()) {
            case INCH:
                return length.getValue() * 72;
            case MILLIMETER:
                return length.getValue() * 2.8346456693;
            case CENTIMETER:
                return length.getValue() * 28.346456693;
            case PERCENTAGE:
                return length.getValue() * 0.72;
            case PIXEL:
                return length.getValue() * 0.75;
            case POINTS:
                return length.getValue();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public final MeasurementUnit getUnit() {
        return Unit;
    }

    public final void setUnit(MeasurementUnit value) {
        Unit = value;
    }

    public final double getValue() {
        return Value;
    }

    public final void setValue(double value) {
        Value = value;
    }
}
