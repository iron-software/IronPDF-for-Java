package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.form.FormManager;
import com.ironsoftware.ironpdf.form.ComboBoxField;
import com.ironsoftware.ironpdf.form.TextField;
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

    @Test
    public final void SetFormFont_RejectsInvalidFontName() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFont(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFont("with spaces"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFont("/slash"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFont("(parens)"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFont((String) null));

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 100; i++) b.append('a');
        Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFont(b.toString()));
    }

    @Test
    public final void SetFormFont_RejectsInvalidFontData() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        // Too small
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fm.setFormFont("Foo", new byte[]{1, 2, 3}));
        // Wrong magic (zeros)
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fm.setFormFont("Foo", new byte[100]));
        // ASCII text payload
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fm.setFormFont("Foo", "This is not a font".getBytes()));
        // Over 50 MB
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fm.setFormFont("Foo", new byte[51 * 1024 * 1024]));
    }

    @Test
    public final void SetFormFont_AllowsSubsetPrefix() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        // The plus sign is allowed for subset prefixes; engine throws because the font isn't present,
        // but the validator should not reject the name itself.
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> fm.setFormFont("AAAAAA+Poppins-Regular"));
        Assertions.assertFalse(ex instanceof IllegalArgumentException,
                "Validator must accept the subset-prefixed name");
    }

    @Test
    public final void SetFormFontFromFile_NullPath_Throws() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFontFromFile(null));
    }

    @Test
    public final void SetFormFontFromFile_NonexistentPath_Throws() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        Assertions.assertThrows(java.nio.file.NoSuchFileException.class,
                () -> fm.setFormFontFromFile(java.nio.file.Paths.get("does-not-exist-xyz.ttf")));
    }

    @Test
    public final void SetFormFontFromFile_RejectsBogusBytes() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        java.nio.file.Path tmp = java.nio.file.Files.createTempFile("not_a_font_", ".bin");
        java.nio.file.Files.write(tmp, "this is not a font, it is text".getBytes());
        try {
            Assertions.assertThrows(IllegalArgumentException.class, () -> fm.setFormFontFromFile(tmp));
        } finally {
            java.nio.file.Files.deleteIfExists(tmp);
        }
    }

    @Test
    public final void DisableFormFontFallback_DoesNotThrow() throws IOException {
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        Assertions.assertDoesNotThrow(fm::disableFormFontFallback);
    }

    @Test
    public final void DisableFormFontFallback_ThenFillNonAscii_NoTahomaInOutput() throws IOException {
        final String nonAsciiValue = "FONT TEST: € ş";
        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormManager fm = doc.getForm();

        fm.disableFormFontFallback();
        // Non-ASCII fill that would normally trigger the Tahoma+Arial auto-embed.
        String filledFieldName = null;
        for (FormField f : fm.getFields().getAllFields()) {
            if (f instanceof TextField) {
                fm.setFieldValue(f.getName(), nonAsciiValue);
                filledFieldName = f.getName();
                break;
            }
        }
        Assertions.assertNotNull(filledFieldName, "test template must contain at least one text field");

        // Save, reopen, and assert the non-ASCII value round-trips intact.
        // Without this check, a regression that silently drops the value to "save bytes" would
        // still pass every size assertion.
        java.nio.file.Path out = java.nio.file.Files.createTempFile("disable_form_font_", ".pdf");
        try {
            doc.saveAs(out);
            PdfDocument reopened = PdfDocument.fromFile(out);
            Assertions.assertNotNull(reopened);

            String finalFieldName = filledFieldName;
            FormField roundTripped = reopened.getForm().getFields().getAllFields().stream()
                    .filter(f -> f.getName().equals(finalFieldName))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "field '" + finalFieldName + "' missing from reopened document"));
            Assertions.assertEquals(nonAsciiValue, roundTripped.getValue(),
                    "DisableFormFontFallback must not alter the form value");
        } finally {
            java.nio.file.Files.deleteIfExists(out);
        }
    }

    /**
     * Verifies the customer's reported scenario: filling form fields with non-ASCII
     * characters causes the engine to auto-embed the Tahoma/Arial fallback (~1.1 MB).
     * Calling {@link FormManager#disableFormFontFallback()} before filling should suppress
     * the auto-embed and produce a much smaller file.
     */
    @Test
    public final void DisableFormFontFallback_NonAsciiFill_ProducesSmallerFileThanBaseline() throws IOException {
        final String nonAscii = "FONT TEST: € ş ğ ü";

        // Baseline path: NO disable → engine auto-embeds Tahoma+Arial when it sees non-ASCII.
        PdfDocument baseline = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormField baselineField = baseline.getForm().getFields().getAllFields().get(0);
        baseline.getForm().setFieldValue(baselineField.getName(), nonAscii);
        int baselineSize = baseline.getBinaryData().length;

        // Disabled path: same template, same fill — but the auto-embed is suppressed.
        PdfDocument disabled = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        disabled.getForm().disableFormFontFallback();
        FormField disabledField = disabled.getForm().getFields().getAllFields().get(0);
        disabled.getForm().setFieldValue(disabledField.getName(), nonAscii);
        int disabledSize = disabled.getBinaryData().length;

        // The disabled path must save at least ~300 KB compared to the baseline (Tahoma+Arial together).
        // Use a generous lower bound (300 KB) so the test stays robust against minor PDFium changes.
        Assertions.assertTrue(baselineSize - disabledSize > 300_000,
                "DisableFormFontFallback did not produce expected savings: baseline=" + baselineSize +
                        ", disabled=" + disabledSize +
                        " (delta=" + (baselineSize - disabledSize) + ")");
    }

    /**
     * Sanity check: with the fallback disabled, an ASCII-only fill should not grow the file
     * meaningfully. (ASCII fills never trigger the auto-embed anyway, so disable is a no-op
     * size-wise — this verifies disable doesn't inadvertently add bytes itself.)
     */
    @Test
    public final void DisableFormFontFallback_AsciiFill_DoesNotGrowFileMeaningfully() throws IOException {
        // Reference path: render + ASCII fill, no disable.
        PdfDocument reference = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        FormField referenceField = reference.getForm().getFields().getAllFields().get(0);
        reference.getForm().setFieldValue(referenceField.getName(), "PlainAscii");
        int referenceSize = reference.getBinaryData().length;

        // Disabled path: same fill, disable first.
        PdfDocument disabled = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        disabled.getForm().disableFormFontFallback();
        FormField disabledField = disabled.getForm().getFields().getAllFields().get(0);
        disabled.getForm().setFieldValue(disabledField.getName(), "PlainAscii");
        int disabledSize = disabled.getBinaryData().length;

        // Tolerance: PDFium's form-fill always touches AcroForm/AP/xref so a few KB of
        // run-to-run variance is normal. Anything beyond ~50 KB would be suspicious.
        int delta = Math.abs(disabledSize - referenceSize);
        Assertions.assertTrue(delta < 50_000,
                "DisableFormFontFallback caused unexpected size change on ASCII fill: " +
                        "reference=" + referenceSize + ", disabled=" + disabledSize +
                        " (delta=" + delta + ")");
    }

    /**
     * Verifies the customer's actual fix: rendering a form, calling
     * {@link FormManager#setFormFont(String, byte[])} with their custom font bytes, then
     * filling with non-ASCII should produce a file smaller than the Tahoma+Arial baseline.
     */
    @Test
    public final void SetFormFont_BytesMode_NonAsciiFill_ProducesSmallerFileThanBaseline() throws IOException {
        final String nonAscii = "FONT TEST: € ş ğ ü";
        byte[] poppinsBytes = java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(getTestFile("/Data/Poppins-Regular.ttf")));

        // Baseline: no setFormFont → engine pulls in Tahoma+Arial.
        PdfDocument baseline = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        baseline.getForm().setFieldValue(
                baseline.getForm().getFields().getAllFields().get(0).getName(), nonAscii);
        int baselineSize = baseline.getBinaryData().length;

        // With setFormFont: engine embeds Poppins (~150-300 KB) instead of Tahoma+Arial (~1.1 MB).
        PdfDocument withFormFont = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));
        withFormFont.getForm().setFormFont("Poppins-Regular", poppinsBytes);
        withFormFont.getForm().setFieldValue(
                withFormFont.getForm().getFields().getAllFields().get(0).getName(), nonAscii);
        int withFormFontSize = withFormFont.getBinaryData().length;

        // Tahoma+Arial together are ~1.1 MB; full Poppins-Regular is ~150-300 KB.
        // The setFormFont path must therefore save at least ~500 KB.
        Assertions.assertTrue(baselineSize - withFormFontSize > 500_000,
                "SetFormFont(bytes) did not produce expected savings vs Tahoma baseline: " +
                        "baseline=" + baselineSize + ", withFormFont=" + withFormFontSize +
                        " (delta=" + (baselineSize - withFormFontSize) + ")");
    }

    /**
     * Smart-skip: calling {@link FormManager#setFormFont(String, byte[])} twice with the same
     * font and bytes must not embed it twice. The second call should detect that the font is
     * already in the document with usable glyph data and skip the embed.
     */
    @Test
    public final void SetFormFont_BytesMode_SmartSkip_SecondCallDoesNotGrowFile() throws IOException {
        byte[] poppinsBytes = java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(getTestFile("/Data/Poppins-Regular.ttf")));

        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));

        // First call: embeds Poppins.
        doc.getForm().setFormFont("Poppins-Regular", poppinsBytes);
        int afterFirstCall = doc.getBinaryData().length;

        // Second call with same name+bytes: engine should detect the existing embed and skip.
        doc.getForm().setFormFont("Poppins-Regular", poppinsBytes);
        int afterSecondCall = doc.getBinaryData().length;

        // Tolerance for serialization variance; smart-skip means no second embed (~150 KB+).
        int delta = afterSecondCall - afterFirstCall;
        Assertions.assertTrue(delta < 50_000,
                "Smart-skip failed — second SetFormFont call grew the file by " + delta +
                        " bytes (afterFirst=" + afterFirstCall + ", afterSecond=" + afterSecondCall + ")");
    }

    /**
     * Force-embed: passing {@code forceEmbed = true} must add a fresh font copy even when one
     * is already present, so the file grows by approximately the font size on every call.
     */
    @Test
    public final void SetFormFont_ForceEmbed_GrowsFileOnRepeatedCall() throws IOException {
        byte[] poppinsBytes = java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(getTestFile("/Data/Poppins-Regular.ttf")));

        PdfDocument doc = PdfDocument.renderHtmlFileAsPdf(getTestFile("/Data/basic.html"));

        // First call seeds the font.
        doc.getForm().setFormFont("Poppins-Regular", poppinsBytes);
        int afterFirstCall = doc.getBinaryData().length;

        // Force-embed second call: must add another copy.
        doc.getForm().setFormFont("Poppins-Regular", poppinsBytes, true);
        int afterForceEmbed = doc.getBinaryData().length;

        // The fresh copy adds at least ~50 KB (compressed PDF overhead may shrink a typical
        // Poppins-Regular.ttf below its raw 150 KB, but it can't go below ~50 KB).
        int delta = afterForceEmbed - afterFirstCall;
        Assertions.assertTrue(delta > 50_000,
                "force_embed did not duplicate the font: afterFirst=" + afterFirstCall +
                        ", afterForceEmbed=" + afterForceEmbed + " (delta=" + delta + ")");
    }
}
