package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.staticapi.Exception_RemoteException;

/**
 * Represents errors that occur during IronPDF execution.
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
     * @param message The message that describes the error.
     */
    public IronPdfProductException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    public IronPdfProductException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.StackTraceString, ex.getExceptionType());
    }


}
