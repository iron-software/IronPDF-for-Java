package com.ironsoftware.ironpdf.internal.staticapi.form;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.internal.staticapi.Form_Api;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Render_Api;
import com.ironsoftware.ironpdf.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class FormApiTests extends TestBase {

    @Test
    public final void FlattenFormTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        List<FormField> formBefore = Form_Api.getFields(doc);
        Assertions.assertEquals(2, formBefore.size());
        Form_Api.flattenPdfFrom(doc);
        List<FormField> formAfter = Form_Api.getFields(doc);
        Assertions.assertEquals(2, formAfter.size());

    }

    @Test
    public final void GetFormTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));

        List<FormField> form = Form_Api.getFields(doc);
        Assertions.assertEquals(2, form.size());
        Assertions.assertTrue(form.stream().map(FormField::getName).anyMatch(x -> x.equals("fname")));
        Assertions.assertTrue(form.stream().map(FormField::getName).anyMatch(x -> x.equals("lname")));
    }

    @Test
    public final void RenameFieldTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Form_Api.renameField(doc, "fname", "newfName");
        List<FormField> form = Form_Api.getFields(doc);
        Assertions.assertEquals(2, form.size());
        Assertions.assertTrue(
                form.stream().map(FormField::getName).anyMatch(x -> x.equals("newfName")));
        Assertions.assertTrue(form.stream().map(FormField::getName).anyMatch(x -> x.equals("lname")));
    }

    @Test
    public final void SetFieldValueTest() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        List<FormField> formBefore = Form_Api.getFields(doc);
        Form_Api.setFieldValue(doc, formBefore.get(0).getAnnotationIndex(), "myFirstName");
        List<FormField> form = Form_Api.getFields(doc);
        Assertions.assertEquals(2, form.size());
        Assertions.assertTrue(
                form.stream().map(FormField::getValue).anyMatch(x -> x.equals("myFirstName")));
    }

    @Test
    public final void SetTextFieldFontTest() {
        //todo
    }
}
