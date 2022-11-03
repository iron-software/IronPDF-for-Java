package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.internal.staticapi.Exception_RemoteException;

/**
 * Represents errors that occur during IronPDF binary asset deployment.
 */
public final class IronPdfDeploymentException extends Exception_RemoteException {

    public IronPdfDeploymentException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    public IronPdfDeploymentException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.stackTraceString, ex.getExceptionType());
    }

}
