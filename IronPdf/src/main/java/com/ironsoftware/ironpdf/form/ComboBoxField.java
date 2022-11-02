package com.ironsoftware.ironpdf.form;

import java.util.Iterator;

public class ComboBoxField extends FormField {

    private final Iterator<String> Options;
    private final int SelectedIndex;

    public ComboBoxField(int annotationIndex, String name, int pageIndex,
                         double x, double y, double width, double height, String value, Iterator<String> options,
                         int selectedIndex, boolean readOnly) {
        super(annotationIndex, name, pageIndex, FormFieldTypes.COMBO_BOX, x, y, width, height, value, readOnly);
        Options = options;
        SelectedIndex = selectedIndex;
    }

    public final Iterator<String> getOptions() {
        return Options;
    }

    public final int getSelectedIndex() {
        return SelectedIndex;
    }
}
