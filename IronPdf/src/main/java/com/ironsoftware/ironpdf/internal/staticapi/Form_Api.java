package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.font.FontTypes;
import com.ironsoftware.ironpdf.form.FormField;
import com.ironsoftware.ironpdf.internal.proto.*;
import com.ironsoftware.ironpdf.page.PageInfo;

import java.util.List;
import java.util.stream.Collectors;

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

        PdfiumGetFormRequestP.Builder request = PdfiumGetFormRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);

        PdfiumGetFormResultP response = client.blockingStub.pdfiumFormGetForm(request.build());
        if (response.getResultOrExceptionCase() == PdfiumGetFormResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(response.getException());
        }

        return Form_Converter.fromProto(response.getResult());
    }

    /**
     * Rename a {@link FormField}
     *
     * @param internalPdfDocument the internal pdf document
     * @param currentFieldName    current fully qualified field name
     * @param newFieldName        new partial field name Please use a fully qualified field name for
     *                            CurrentFieldName, and a partial field name for NewFieldName
     * @return newFieldName;
     */
    public static String renameField(InternalPdfDocument internalPdfDocument, String currentFieldName,
                                   String newFieldName) {
        RpcClient client = Access.ensureConnection();

        PdfiumRenameFieldRequestP.Builder request = PdfiumRenameFieldRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setCurrentFieldName(currentFieldName);
        request.setNewFieldName(newFieldName);
        PdfiumRenameFormFieldResultP response = client.blockingStub.pdfiumFormRenameField(
                request.build());
        if (response.getResultOrExceptionCase() == PdfiumRenameFormFieldResultP.ResultOrExceptionCase.EXCEPTION) {
            throw Exception_Converter.fromProto(response.getException());
        }

        return response.getResult();
    }

    /**
     * Set the value of a {@link FormField}
     *
     * @param internalPdfDocument the internal pdf document
     * @param annotationIndex     fully qualified field annotationIndex
     * @param value               new value
     */
    public static void setFieldValue(InternalPdfDocument internalPdfDocument, int annotationIndex, String value) {
        RpcClient client = Access.ensureConnection();

        PdfiumSetFormFieldValueRequestP.Builder request = PdfiumSetFormFieldValueRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setAnnotationIndex(annotationIndex);
        request.setFormFieldValue(value);
        EmptyResultP response = client.blockingStub.pdfiumFormSetFieldValue(
                request.build());
        Utils_Util.handleEmptyResult(response);
    }


    /**
     * @deprecated This method is deprecated and no longer has any effect.
     * Sets text field font.
     *
     * @param internalPdfDocument the internal pdf document
     * @param textFieldName       the text field name
     * @param font                the font
     * @param fontSize            the font size
     */
    public static void setTextFieldFont(InternalPdfDocument internalPdfDocument, String textFieldName,
                                        FontTypes font, int fontSize) {

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
    public static void flattenPdfFrom(InternalPdfDocument internalPdfDocument, List<Integer> pageIndexes) {
        RpcClient client = Access.ensureConnection();

        PdfiumFlattenFormRequestP.Builder req = PdfiumFlattenFormRequestP.newBuilder();
        req.setDocument(internalPdfDocument.remoteDocument);

        if(pageIndexes == null || pageIndexes.isEmpty()){
            pageIndexes = Page_Api.getPagesInfo(internalPdfDocument).stream().map(PageInfo::getPageIndex).collect(Collectors.toList());
        }
        req.addAllPageIndexes(pageIndexes);

        EmptyResultP res = client.blockingStub.pdfiumFormFlattenForm(req.build());
        Utils_Util.handleEmptyResult(res);
    }

    /**
     * Sets text field font.
     *
     * @param internalPdfDocument the internal pdf document
     * @param annotationIndex      the form field annotation Index
     * @param isReadOnly          the read only value
     */
    public static void setFormFieldIsReadOnly(InternalPdfDocument internalPdfDocument, int annotationIndex,
                                        boolean isReadOnly) {
        RpcClient client = Access.ensureConnection();

        PdfiumSetFormFieldIsReadOnlyRequestP.Builder request = PdfiumSetFormFieldIsReadOnlyRequestP.newBuilder();
        request.setDocument(internalPdfDocument.remoteDocument);
        request.setAnnotationIndex(annotationIndex);
        request.setIsReadOnly(isReadOnly);
        EmptyResultP response = client.blockingStub.pdfiumFormSetFormFieldIsReadOnly(
                request.build());
        Utils_Util.handleEmptyResult(response);
    }
}
