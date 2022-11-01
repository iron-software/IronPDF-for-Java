package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.staticapi.Exception_RemoteException;

/**
 * Represents errors that occur when invalid variables are passed to IronPDF.
 */
public final class IronPdfInputException extends Exception_RemoteException {

    public IronPdfInputException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    public IronPdfInputException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.StackTraceString, ex.getExceptionType());
    }

}
