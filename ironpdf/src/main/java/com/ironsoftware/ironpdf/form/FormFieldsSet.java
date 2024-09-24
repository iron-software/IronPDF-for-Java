package com.ironsoftware.ironpdf.form;

import java.util.ArrayList;
import java.util.List;


/**
 * A container class contains the list of form field separated by the type.
 */
public class FormFieldsSet {

    private final List<CheckBoxField> checkBoxFields;
    private final List<TextField> textFields;
    private final List<ComboBoxField> comboBoxFields;

    private final List<ImageField> imageFields;

    private final List<RadioField> radioFields;

    private final List<SignatureField> signatureFields;
    private final List<FormField> unknownFields;

    /**
     * (INTERNAL) Please Get FormFieldsSet from {@link FormManager#getFields()}
     *
     * @param checkBoxFields the checkbox fields
     * @param textFields     the text fields
     * @param comboBoxFields the combo box fields
     * @param unknownFields  the unknown fields
     */
    public FormFieldsSet(List<CheckBoxField> checkBoxFields,
                         List<TextField> textFields,
                         List<ComboBoxField> comboBoxFields,
                         List<ImageField> imageFields,
                         List<RadioField> radioFields,
                         List<SignatureField> signatureFields,
                         List<FormField> unknownFields) {
        this.checkBoxFields = checkBoxFields;
        this.textFields = textFields;
        this.comboBoxFields = comboBoxFields;
        this.imageFields = imageFields;
        this.radioFields = radioFields;
        this.signatureFields = signatureFields;
        this.unknownFields = unknownFields;
    }

    /**
     * Gets checkbox fields.
     *
     * @return a new list of checkbox form fields
     */
    public List<CheckBoxField> getCheckBoxFields() {
        return new ArrayList<>(checkBoxFields);
    }

    /**
     * Gets text fields.
     *
     * @return a new list of text form fields
     */
    public List<TextField> getTextFields() {
        return new ArrayList<>(textFields);
    }

    /**
     * Gets combobox fields.
     *
     * @return a new list of combobox form fields
     */
    public List<ComboBoxField> getComboBoxFields() {
        return new ArrayList<>(comboBoxFields);
    }

    /**
     * Gets image fields.
     *
     * @return a new list of image fields
     */
    public List<ImageField> getImageFields() {
        return new ArrayList<>(imageFields);
    }

    /**
     * Gets radio fields.
     *
     * @return a new list of radio fields
     */
    public List<RadioField> getRadioFields() {
        return new ArrayList<>(radioFields);
    }

    /**
     * Gets signature fields.
     *
     * @return a new list of signature form fields
     */
    public List<SignatureField> getSignatureFields() {
        return new ArrayList<>(signatureFields);
    }

    /**
     * Gets unknown fields.
     *
     * @return a new list of unknown form fields
     */
    public List<FormField> getUnknownFields() {
        return new ArrayList<>(unknownFields);
    }

    /**
     * Gets all form fields.
     *
     * @return a new list of all form fields
     */
    public List<FormField> getAllFields() {
        List<FormField> newList = new ArrayList<>(checkBoxFields);
        newList.addAll(comboBoxFields);
        newList.addAll(textFields);
        newList.addAll(imageFields);
        newList.addAll(radioFields);
        newList.addAll(signatureFields);
        newList.addAll(unknownFields);
        return newList;
    }


}