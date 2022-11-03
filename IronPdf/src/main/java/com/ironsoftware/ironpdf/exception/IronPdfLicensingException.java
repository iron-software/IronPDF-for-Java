package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.internal.staticapi.Exception_RemoteException;

/**
 * <p>Get a free development license at <a href="https://ironpdf.com/licensing/#trial-license">https://ironpdf.com/licensing/#trial-license</a> or purchase a deployment license from 
 * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>.</p>
 */
public final class IronPdfLicensingException extends IronPdfProductException {

    public IronPdfLicensingException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    public IronPdfLicensingException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.stackTraceString, ex.getExceptionType());

    }

    String getTargetSite() {
        return this.getMessage();
    }
}
