package com.ironsoftware.ironpdf.form;

import java.util.List;

public class FormFieldsSet {

    private final List<CheckBoxField> checkBoxFields;
    private final List<TextField> textFields;
    private final List<ComboBoxField> comboBoxFields;
    private final List<FormField> unknownFields;

    public FormFieldsSet(List<CheckBoxField> checkBoxFields,
                         List<TextField> textFields,
                         List<ComboBoxField> comboBoxFields,
                         List<FormField> unknownFields) {
        this.checkBoxFields = checkBoxFields;
        this.textFields = textFields;
        this.comboBoxFields = comboBoxFields;
        this.unknownFields = unknownFields;
    }

    public List<CheckBoxField> getCheckBoxFields() {
        return checkBoxFields;
    }

    public List<TextField> getTextFields() {
        return textFields;
    }

    public List<ComboBoxField> getComboBoxFields() {
        return comboBoxFields;
    }

    public List<FormField> getUnknownFields() {
        return unknownFields;
    }
}