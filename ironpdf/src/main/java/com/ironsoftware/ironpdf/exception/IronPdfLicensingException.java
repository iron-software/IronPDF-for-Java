package com.ironsoftware.ironpdf.exception;

import com.ironsoftware.ironpdf.internal.staticapi.Exception_RemoteException;

/**
 * <p>Get a free development license at <a href="https://ironpdf.com/licensing/#trial-license">https://ironpdf.com/licensing/#trial-license</a> or purchase a deployment license from
 * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>.</p>
 */
public final class IronPdfLicensingException extends IronPdfProductException {

    /**
     * Instantiates a new Iron pdf licensing exception.
     *
     * @param message       the message
     * @param stackTrace    the stack trace
     * @param exceptionType the exception type
     */
    public IronPdfLicensingException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    /**
     * Instantiates a new Iron pdf licensing exception.
     *
     * @param ex the ex
     */
    public IronPdfLicensingException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.stackTraceString, ex.getExceptionType());

    }

    /**
     * Gets target site.
     *
     * @return the target site
     */
    String getTargetSite() {
        return this.getMessage();
    }
}
