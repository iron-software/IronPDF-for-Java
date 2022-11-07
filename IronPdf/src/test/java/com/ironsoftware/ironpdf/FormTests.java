package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.form.FormManager;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class FormTests extends TestBase {

    @Test
    public final void FlattenFormAllPagesTest() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager formManager = doc.getForm();
        List<FormField> formsBefore = formManager.getFields().getAllFields();
        Assertions.assertEquals(2, formsBefore.size());

        formManager.flatten();
        List<FormField> formsAfter = formManager.getFields().getAllFields();
        Assertions.assertEquals(0, formsAfter.size());

    }

    @Test
    public final void FlattenFormSelectPageTest() throws IOException {;
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager formManager = doc.getForm();
        List<FormField> formsBefore = formManager.getFields().getAllFields();
        Assertions.assertEquals(2, formsBefore.size());

        formManager.flatten(PageSelection.singlePage(0));
        List<FormField> formsAfter = formManager.getFields().getAllFields();
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
        FormField form = forms.stream().filter(x -> x.getName().equals("fname")).findFirst().get();
        // TODO Maybe add Assertions for Font
        Assertions.assertNotNull(form);
    }

}
