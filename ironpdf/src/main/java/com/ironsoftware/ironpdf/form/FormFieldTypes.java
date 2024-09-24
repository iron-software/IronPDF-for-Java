package com.ironsoftware.ironpdf.form;

/**
 * Form field types
 */
public enum FormFieldTypes {
    UNKNOWN("Unknown"),
    PUSH_BUTTON("Unknown"),
    CHECK_BOX("Btn"),
    RADIO_BUTTON("Btn"),
    COMBO_BOX("Ch"),
    LIST_BOX("Unknown"),
    TEXT_FIELD("Tx"),
    SIGNATURE("Sig"),
    XFA("Unknown"),
    XFA_CHECK_BOX("Unknown"),
    XFA_COMBO_BOX("Unknown"),
    XFA_IMAGE_FIELD("Unknown"),
    XFA_LIST_BOX("Unknown"),
    XFA_PUSH_BUTTON("Unknown"),
    XFA_SIGNATURE("Unknown"),
    XFA_TEXT_FIELD("Unknown"),
    IMAGE("Btn");

    private final String formType;

    FormFieldTypes(String formType) {
        this.formType = formType;
    }

    public String getFormType() {
        return formType;
    }

    public static FormFieldTypes fromFormType(String formType) {
        for (FormFieldTypes fieldType : values()) {
            if (fieldType.formType.equals(formType)) {
                return fieldType;
            }
        }
        throw new IllegalArgumentException("No enum constant for formType: " + formType);
    }
}
