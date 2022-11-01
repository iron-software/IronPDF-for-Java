package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.internal.proto.*;

import java.io.IOException;
import java.util.List;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.HandleEmptyResult;

public final class Form_Api {

    /**
     * Get a collection of the user-editable form fields within a PDF document
     */
    public static List<FormField> GetFields(InternalPdfDocument pdfDocument) throws IOException {
        RpcClient client = Access.EnsureConnection();

        GetFormRequest.Builder request = GetFormRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);

        GetFormResult response = client.BlockingStub.pdfDocumentFormGetForm(request.build());
        if (response.getResultOrExceptionCase() == GetFormResult.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.FromProto(response.getException());
        }

        return Form_Converter.FromProto(response.getResult());
    }

    /**
     * Rename a {@link FormField}
     *
     * @param currentFieldName Current fully qualified field name
     * @param newFieldName     New partial field name Please use a fully qualified field name for
     *                         CurrentFieldName, and a partial field name for NewFieldName
     */
    public static void RenameField(InternalPdfDocument pdfDocument, String currentFieldName,
                                   String newFieldName) throws IOException {
        RpcClient client = Access.EnsureConnection();

        RenameFieldRequest.Builder request = RenameFieldRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        request.setCurrentFieldName(currentFieldName);
        request.setNewFieldName(newFieldName);
        EmptyResult response = client.BlockingStub.pdfDocumentFormRenameField(
                request.build());
        HandleEmptyResult(response);
    }

    /**
     * Set the value of a {@link FormField}
     *
     * @param fieldName Fully qualified field name
     * @param value     New value
     */
    public static void SetFieldValue(InternalPdfDocument pdfDocument, String fieldName, String value)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        SetFromFieldValueRequest.Builder request = SetFromFieldValueRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        request.setFieldName(fieldName);
        request.setFormFieldValue(value);
        EmptyResult response = client.BlockingStub.pdfDocumentFormSetFieldValue(
                request.build());
        HandleEmptyResult(response);
    }


    public static void SetTextFieldFont(InternalPdfDocument pdfDocument, String textFieldName,
                                        FontTypes font, int fontSize) throws IOException {
        RpcClient client = Access.EnsureConnection();

        SetFromFieldFontRequest.Builder request = SetFromFieldFontRequest.newBuilder();
        request.setDocument(pdfDocument.remoteDocument);
        request.setFieldName(textFieldName);
        request.setFontType(FontTypes_Converter.ToProto(font));
        request.setFontSize(fontSize);
        EmptyResult response = client.BlockingStub.pdfDocumentFormSetFieldFont(
                request.build());
        HandleEmptyResult(response);
    }


    /**
     * Flattens a document (make the fields non-editable).
     */
    public static void FlattenPdfFrom(InternalPdfDocument pdfDocument) throws IOException {
        FlattenPdfFrom(pdfDocument, null);
    }

    /**
     * Flattens a document (make the fields non-editable).
     *
     * @param pageIndexes page indexes to flatten (defaults to all pages)
     */
    public static void FlattenPdfFrom(InternalPdfDocument pdfDocument, Iterable<Integer> pageIndexes)
            throws IOException {
        RpcClient client = Access.EnsureConnection();

        FlattenFormRequest.Builder req = FlattenFormRequest.newBuilder();
        req.setDocument(pdfDocument.remoteDocument);
        if (pageIndexes != null) {
            req.addAllPageIndexes(pageIndexes);
        }

        EmptyResult res = client.BlockingStub.pdfDocumentFormFlattenForm(req.build());
        HandleEmptyResult(res);
    }
}
