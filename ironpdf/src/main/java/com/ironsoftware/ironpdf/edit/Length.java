package com.ironsoftware.ironpdf.edit;

/**
 * A length value with {@link MeasurementUnit}
 * <p> Allows use and interchange of units such as inches, mm, pt, percentages, pixels and points when editing a PDF.</p>
 */
public class Length {

    private double value;
    private MeasurementUnit unit = MeasurementUnit.values()[0];

    /**
     * Instantiates a new Length. Default MeasurementUnit is {@link MeasurementUnit#PERCENTAGE}
     *
     * @param value the value
     */
    public Length(double value) {
        this(value, MeasurementUnit.PERCENTAGE);
    }

    /**
     * Instantiates a new Length. A length value with {@link MeasurementUnit}.
     *
     * @param value the value
     * @param unit  the unit
     */
    public Length(double value, MeasurementUnit unit) {
        setValue(value);
        setUnit(unit);
    }

    /**
     * Gets default length : 0%
     */
    public Length() {
        this(0, MeasurementUnit.PERCENTAGE);
    }


    /**
     * Gets unit. {@link MeasurementUnit}
     *
     * @return the unit
     */
    public final MeasurementUnit getUnit() {
        return unit;
    }

    /**
     * Sets unit. {@link MeasurementUnit}
     *
     * @param value the value
     */
    public final void setUnit(MeasurementUnit value) {
        unit = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public final double getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public final void setValue(double value) {
        this.value = value;
    }
}
