package com.ironsoftware.ironpdf.exception;


import com.ironsoftware.ironpdf.staticapi.Exception_RemoteException;

/**
 * Represents errors that occur during IronPDF Asset deployment.
 */
public final class IronPdfNativeException extends Exception_RemoteException {

    /**
     * Initializes a new instance of the {@link IronPdfNativeException} class.
     */
    IronPdfNativeException() {
    }

    /**
     * Initializes a new instance of the {@link IronPdfNativeException} class with a specified error
     * message.
     *
     * @param message The message that describes the error.
     */
    public IronPdfNativeException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    public IronPdfNativeException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.StackTraceString, ex.getExceptionType());
    }


}
