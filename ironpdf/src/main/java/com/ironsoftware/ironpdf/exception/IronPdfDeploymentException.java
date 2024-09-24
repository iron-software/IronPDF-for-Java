package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.internal.staticapi.Exception_RemoteException;

/**
 * Represents errors that occur during IronPDF binary asset deployment.
 */
public final class IronPdfDeploymentException extends Exception_RemoteException {

    /**
     * Instantiates a new Iron pdf deployment exception.
     *
     * @param message       the message
     * @param stackTrace    the stack trace
     * @param exceptionType the exception type
     */
    public IronPdfDeploymentException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    /**
     * Instantiates a new Iron pdf deployment exception.
     *
     * @param ex the ex
     */
    public IronPdfDeploymentException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.stackTraceString, ex.getExceptionType());
    }

}
