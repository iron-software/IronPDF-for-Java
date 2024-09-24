package com.ironsoftware.ironpdf.exception;


import com.ironsoftware.ironpdf.internal.staticapi.Exception_RemoteException;

/**
 * Represents errors that occur from native code within IronPDF.
 * <p>If reporting such issues please attach any .log files from the directory where your application is running..</p>
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
     * @param message       The message that describes the error.
     * @param stackTrace    the stack trace
     * @param exceptionType the exception type
     */
    public IronPdfNativeException(String message, String stackTrace, String exceptionType) {
        super(message, stackTrace, exceptionType);
    }

    /**
     * Instantiates a new Iron pdf native exception.
     *
     * @param ex the ex
     */
    public IronPdfNativeException(Exception_RemoteException ex) {
        super(ex.getMessage(), ex.stackTraceString, ex.getExceptionType());
    }


}



