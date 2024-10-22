package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.form.FormManager;
import com.ironsoftware.ironpdf.form.ComboBoxField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FormTests extends TestBase {

    @Test
    public final void FlattenFormAllPagesTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        List<FormField> formsBefore = doc.getForm().getFields().getAllFields();
        Assertions.assertEquals(2, formsBefore.size());

        doc.getForm().flatten();
        List<FormField> formsAfter = doc.getForm().getFields().getAllFields();
        // Starting from IronPdfEngine v2024.7.8 Flatten will remove Form Field out completely
        Assertions.assertEquals(0, formsAfter.size());

    }

    @Test
    public final void FlattenFormSelectPageTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        List<FormField> formsBefore = doc.getForm().getFields().getAllFields();
        Assertions.assertEquals(2, formsBefore.size());

        doc.getForm().flatten(PageSelection.singlePage(0));
        List<FormField> formsAfter = doc.getForm().getFields().getAllFields();
        // Starting from IronPdfEngine v2024.7.8 Flatten will remove Form Field out completely
        Assertions.assertEquals(0, formsAfter.size());
    }

    @Test
    public final void GetFormTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager formManager = doc.getForm();

        List<FormField> forms = formManager.getFields().getAllFields();
        Assertions.assertEquals(2, forms.size());
        Assertions.assertTrue(forms.stream().map(FormField::getName).anyMatch(x -> x.equals("fname")));
        Assertions.assertTrue(forms.stream().map(FormField::getName).anyMatch(x -> x.equals("lname")));
    }

    @Test
    public final void RenameFieldTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager formManager = doc.getForm();
        formManager.renameField("fname", "newfName");
        List<FormField> forms = formManager.getFields().getAllFields();
        Assertions.assertEquals(2, forms.size());
        Assertions.assertTrue(
                forms.stream().map(FormField::getName).anyMatch(x -> x.equals("newfName")));
        Assertions.assertTrue(forms.stream().map(FormField::getName).anyMatch(x -> x.equals("lname")));
    }

    @Test
    public final void SetFieldValueTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager formManager = doc.getForm();
        formManager.setFieldValue("fname", "myFirstName");
        List<FormField> forms = formManager.getFields().getAllFields();
        Assertions.assertEquals(2, forms.size());
        Assertions.assertTrue(
                forms.stream().map(FormField::getValue).anyMatch(x -> x.equals("myFirstName")));
    }

    @Test
    public final void SetTextFieldFontTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager formManager = doc.getForm();
        formManager.setTextFieldFont("fname", FontTypes.getArial(), 20);
        List<FormField> forms = formManager.getFields().getAllFields();
        Assertions.assertEquals(2, forms.size());
        Optional<FormField> expected = forms.stream().filter(x -> x.getName().equals("fname")).findFirst();
        Assertions.assertTrue(expected.isPresent());
        FormField form = expected.get();
        // TODO Maybe add Assertions for Font
        Assertions.assertNotNull(form);
    }

    @Test
    public final void GetFormTypeTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/advance.html"));
        FormManager formManager = doc.getForm();

        List<FormField> forms = formManager.getFields().getAllFields();
        Assertions.assertEquals(3, forms.size());

        // Assert for TextField
        Assertions.assertTrue(forms.stream().map(FormField::getName).anyMatch(x -> x.equals("fname")));
        Assertions.assertTrue(forms.stream().map(FormField::getName).anyMatch(x -> x.equals("lname")));

        // // Assert for CheckBoxField
        // Assertions.assertTrue(forms.stream().anyMatch(CheckBoxField.class::isInstance));
        // CheckBoxField checkBoxField = (CheckBoxField) forms.stream()
        //         .filter(CheckBoxField.class::isInstance)
        //         .findFirst()
        //         .orElse(null);
        // Assertions.assertNotNull(checkBoxField);
        // Assertions.assertEquals("subscribe", checkBoxField.getName());
        // Assertions.assertEquals("yes", checkBoxField.getValue());

        // Assert ComboBoxFields
        Assertions.assertTrue(forms.stream().anyMatch(ComboBoxField.class::isInstance));
        ComboBoxField comboBoxField = (ComboBoxField) forms.stream()
                .filter(ComboBoxField.class::isInstance)
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(comboBoxField);
        Assertions.assertEquals("country", comboBoxField.getName());
        //Assertions.assertEquals("uk", comboBoxField.getValue());
    }

}
