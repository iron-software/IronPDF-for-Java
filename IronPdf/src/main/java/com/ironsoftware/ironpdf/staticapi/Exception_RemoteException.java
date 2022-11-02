package com.ironsoftware.ironpdf.staticapi;

import java.util.Arrays;

public class Exception_RemoteException extends RuntimeException {

    private final String ExceptionType;
    public String StackTraceString;

    public Exception_RemoteException() {
        throw new UnsupportedOperationException("RemoteException should not be manually initialized.");
    }

    public Exception_RemoteException(String message, String stackTrace, String exceptionType) {
        super(message);
        this.StackTraceString = stackTrace;
        this.ExceptionType = exceptionType;
    }

    public final String getExceptionType() {
        return ExceptionType;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return Arrays.stream(StackTraceString.split("\n"))
                .map(s -> new StackTraceElement(StackTraceString, "ironpdf", "ironpdf", 0))
                .toArray(StackTraceElement[]::new);
    }
}
