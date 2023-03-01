package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.internal.proto.*;

import java.util.List;

/**
 * The type Form api.
 */
public final class Form_Api {

    /**
     * Get a collection of the user-editable form fields within a PDF document
     *
     * @param internalPdfDocument the internal pdf document
     * @return the fields
     */
    public static List<FormField> getFields(InternalPdfDocument internalPdfDocument) {
        RpcClient client = Access.ensureConnection();

        GetFormRequest.Builder request = GetFormRequest.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);

        GetFormResult response = client.blockingStub.pdfDocumentFormGetForm(request.build());
        if (response.getResultOrExceptionCase() == GetFormResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(response.getException());
        }

        return Form_Converter.fromProto(response.getResult());
    }

    /**
     * Rename a {@link FormField}
     *
     * @param internalPdfDocument the internal pdf document
     * @param currentFieldName    current fully qualified field name
     * @param newFieldName        new partial field name Please use a fully qualified field name for                         CurrentFieldName, and a partial field name for NewFieldName
     */
    public static void renameField(InternalPdfDocument internalPdfDocument, String currentFieldName,
                                   String newFieldName) {
        RpcClient client = Access.ensureConnection();

        RenameFieldRequest.Builder request = RenameFieldRequest.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setCurrentFieldName(currentFieldName);
        request.setNewFieldName(newFieldName);
        EmptyResult response = client.blockingStub.pdfDocumentFormRenameField(
                request.build());
        Utils_Util.handleEmptyResult(response);
    }

    /**
     * Set the value of a {@link FormField}
     *
     * @param internalPdfDocument the internal pdf document
     * @param fieldName           fully qualified field name
     * @param value               new value
     */
    public static void setFieldValue(InternalPdfDocument internalPdfDocument, String fieldName, String value) {
        RpcClient client = Access.ensureConnection();

        SetFromFieldValueRequest.Builder request = SetFromFieldValueRequest.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setFieldName(fieldName);
        request.setFormFieldValue(value);
        EmptyResult response = client.blockingStub.pdfDocumentFormSetFieldValue(
                request.build());
        Utils_Util.handleEmptyResult(response);
    }


    /**
     * Sets text field font.
     *
     * @param internalPdfDocument the internal pdf document
     * @param textFieldName       the text field name
     * @param font                the font
     * @param fontSize            the font size
     */
    public static void setTextFieldFont(InternalPdfDocument internalPdfDocument, String textFieldName,
                                        FontTypes font, int fontSize) {
        RpcClient client = Access.ensureConnection();

        SetFromFieldFontRequest.Builder request = SetFromFieldFontRequest.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setFieldName(textFieldName);
        request.setFontType(FontTypes_Converter.toProto(font));
        request.setFontSize(fontSize);
        EmptyResult response = client.blockingStub.pdfDocumentFormSetFieldFont(
                request.build());
        Utils_Util.handleEmptyResult(response);
    }


    /**
     * Flattens a document (make the fields non-editable).
     *
     * @param internalPdfDocument the internal pdf document
     */
    public static void flattenPdfFrom(InternalPdfDocument internalPdfDocument) {
        flattenPdfFrom(internalPdfDocument, null);
    }

    /**
     * Flattens a document (make the fields non-editable).
     *
     * @param internalPdfDocument the internal pdf document
     * @param pageIndexes         page indexes to flatten (defaults to all pages)
     */
    public static void flattenPdfFrom(InternalPdfDocument internalPdfDocument, Iterable<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();

        FlattenFormRequest.Builder req = FlattenFormRequest.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);
        if (pageIndexes != null) {
            req.addAllPageIndexes(pageIndexes);
        }

        EmptyResult res = client.blockingStub.pdfDocumentFormFlattenForm(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Sets text field font.
     *
     * @param internalPdfDocument the internal pdf document
     * @param formFieldName       the form field name
     * @param isReadOnly          the read only value
     */
    public static void setFormFieldIsReadOnly(InternalPdfDocument internalPdfDocument, String formFieldName,
                                        boolean isReadOnly) {
        RpcClient client = Access.ensureConnection();

        SetFormFieldIsReadOnlyRequest.Builder request = SetFormFieldIsReadOnlyRequest.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setFieldName(formFieldName);
        request.setIsReadOnly(isReadOnly);
        EmptyResult response = client.blockingStub.pdfDocumentFormSetFormFieldIsReadOnly(
                request.build());
        Utils_Util.handleEmptyResult(response);
    }
}
