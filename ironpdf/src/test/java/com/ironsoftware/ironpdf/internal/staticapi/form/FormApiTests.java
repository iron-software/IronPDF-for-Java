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
        // Starting from IronPdfEngine v2024.7.8 Flatten will remove Form Field out completely
        Assertions.assertEquals(0, formAfter.size());

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

    @Test
    public final void SetFormFont_RejectsNullFontName() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, null, null, false));
    }

    @Test
    public final void SetFormFont_RejectsEmptyFontName() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "", null, false));
    }

    @Test
    public final void SetFormFont_RejectsInjectionAttempts() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "/Bad", null, false));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "Foo Bar", null, false));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "(parens)", null, false));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "back\\slash", null, false));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "null\0byte", null, false));
    }

    @Test
    public final void SetFormFont_RejectsOversizeName() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 100; i++) b.append('a');
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, b.toString(), null, false));
    }

    @Test
    public final void SetFormFont_AllowsSubsetPrefixCharacter() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        // The plus sign is allowed (subset prefix). The font won't actually be in the document,
        // so the engine throws — but the validator must accept the name.
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> Form_Api.setFormFont(doc, "AAAAAA+Poppins-Regular", null, false));
        Assertions.assertFalse(ex instanceof IllegalArgumentException,
                "Validator must accept the subset-prefixed name; engine should be the one that throws");
    }

    @Test
    public final void SetFormFont_RejectsBogusFontPayload() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));

        // Too small.
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "Foo", new byte[]{1, 2, 3}, false));

        // 100 bytes of zeros — wrong magic.
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "Foo", new byte[100], false));

        // Plain ASCII text — wrong magic.
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "Foo",
                        "This is not a font, it is text".getBytes(), false));

        // Over 50 MB.
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Form_Api.setFormFont(doc, "Foo", new byte[51 * 1024 * 1024], false));
    }

    @Test
    public final void SetFormFont_NameOnly_ThrowsWhenFontNotInDocument() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> Form_Api.setFormFont(doc, "DefinitelyNotInThisDocument12345", null, false));
        // Validator passes (name is fine), engine rejects because no such font is embedded.
        Assertions.assertFalse(ex instanceof IllegalArgumentException);
    }

    @Test
    public final void DisableFormFontFallback_DoesNotThrow() throws IOException {
        InternalPdfDocument doc = Render_Api.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        Assertions.assertDoesNotThrow(() -> Form_Api.disableFormFontFallback(doc));
    }
}
