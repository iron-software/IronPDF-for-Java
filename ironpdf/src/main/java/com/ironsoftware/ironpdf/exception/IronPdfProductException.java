package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.internal.staticapi.Exception_RemoteException;

/**
 * Represents meaningful errors that are thrown explicitly by IronPDF.
 */
public class IronPdfProductException extends Exception_RemoteException {

    /**
     * Initializes a new instance of the {@link IronPdfProductException} class.
     */
    IronPdfProductException() {
    }

    /**
     * Initializes a new instance of the {@link IronPdfProductException} class with a specified error
     * message.
     *
     * @param message       The message that describes the error.
     * @param stackTrace    the stack trace
     * @param exceptionType the exception type
     */
    public IronPdfProductException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    /**
     * Instantiates a new Iron pdf product exception.
     *
     * @param ex the ex
     */
    public IronPdfProductException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.stackTraceString, ex.getExceptionType());
    }


}
