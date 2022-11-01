package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.staticapi.Exception_RemoteException;

/**
 * Represents errors that occur during IronPDF execution due to the library not being appropriately
 * licensed.
 * <p>https: //ironpdf.com/licensing/</p>
 */
public final class IronPdfLicensingException extends IronPdfProductException {

    public IronPdfLicensingException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    public IronPdfLicensingException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.StackTraceString, ex.getExceptionType());

    }

    String getTargetSite() {
        return this.getMessage();
    }
}
