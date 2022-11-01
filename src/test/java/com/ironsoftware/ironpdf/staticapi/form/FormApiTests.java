package com.ironsoftware.ironpdf.staticapi.form;

import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.staticapi.Form_Api;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.Render_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class FormApiTests extends TestBase {

    @Test
    public final void FlattenFormTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        List<FormField> formBefore = Form_Api.GetFields(doc);
        Assertions.assertEquals(2, formBefore.size());
        Form_Api.FlattenPdfFrom(doc);
        List<FormField> formAfter = Form_Api.GetFields(doc);
        Assertions.assertEquals(0, formAfter.size());

    }

    @Test
    public final void GetFormTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlFileAsPdf(getTestFile("/Data/basic.html"));

        List<FormField> form = Form_Api.GetFields(doc);
        Assertions.assertEquals(2, form.size());
        Assertions.assertTrue(form.stream().map(FormField::getName).anyMatch(x -> x.equals("fname")));
        Assertions.assertTrue(form.stream().map(FormField::getName).anyMatch(x -> x.equals("lname")));
    }

    @Test
    public final void RenameFieldTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Form_Api.RenameField(doc, "fname", "newfName");
        List<FormField> form = Form_Api.GetFields(doc);
        Assertions.assertEquals(2, form.size());
        Assertions.assertTrue(
                form.stream().map(FormField::getName).anyMatch(x -> x.equals("newfName")));
        Assertions.assertTrue(form.stream().map(FormField::getName).anyMatch(x -> x.equals("lname")));
    }

    @Test
    public final void SetFieldValueTest() throws IOException {
        InternalPdfDocument doc = Render_Api.RenderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Form_Api.SetFieldValue(doc, "fname", "myFirstName");
        List<FormField> form = Form_Api.GetFields(doc);
        Assertions.assertEquals(2, form.size());
        Assertions.assertTrue(
                form.stream().map(FormField::getValue).anyMatch(x -> x.equals("myFirstName")));
    }

    @Test
    public final void SetTextFieldFontTest() {
        //todo: check font, now it still not working
    }
}
