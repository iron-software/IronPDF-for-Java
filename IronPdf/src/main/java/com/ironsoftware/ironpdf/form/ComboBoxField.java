package com.ironsoftware.ironpdf.form;

import java.util.List;

/**
 * The type Combo box field.
 */
public class ComboBoxField extends FormField {

    private final List<String> options;
    private final int selectedIndex;

    /**
     * (INTERNAL) Please Get ComboBoxField from {@link FormFieldsSet#getComboBoxFields()} from {@link FormManager#getFields()}
     */
    public ComboBoxField(int annotationIndex, String name, int pageIndex,
                         double x, double y, double width, double height, String value, List<String> options,
                         int selectedIndex, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.COMBO_BOX, x, y, width, height, value, readOnly);
        this.options = options;
        this.selectedIndex = selectedIndex;
    }

    /**
     * Gets options. The available options for the combo-box in zero based index order.
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
