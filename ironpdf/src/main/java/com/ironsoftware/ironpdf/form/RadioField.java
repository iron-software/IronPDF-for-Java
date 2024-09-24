package com.ironsoftware.ironpdf.form;

import java.util.List;

/**
 * The type Radio field.
 */
public class RadioField extends FormField {

    private final List<String> options;
    private final int selectedIndex;

    /**
     * (INTERNAL) Please Get RadioFormField from {@link FormFieldsSet#getRadioFields()} from {@link FormManager#getFields()}
     *
     * @param annotationIndex the annotation index
     * @param name            the name
     * @param pageIndex       the page index
     * @param x               the x
     * @param y               the y
     * @param width           the width
     * @param height          the height
     * @param value           the value
     * @param options         the options
     * @param selectedIndex   the selected index
     * @param readOnly        the read only
     */
    public RadioField(int annotationIndex, String name, int pageIndex,
                      double x, double y, double width, double height, String value, List<String> options,
                      int selectedIndex, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.RADIO_BUTTON.getFormType(), x, y, width, height, value, readOnly);
        this.options = options;
        this.selectedIndex = selectedIndex;
    }

    /**
     * Gets options. The available options for the radio form in zero based index order.
     *
     * @return the option names as strings.
     */
    public final List<String> getOptions() {
        return options;
    }

    /**
     * Gets The index of the selected option.
     *
     * @return the index of the selected. 0 based.
     */
    public final int getSelectedIndex() {
        return selectedIndex;
    }
}
