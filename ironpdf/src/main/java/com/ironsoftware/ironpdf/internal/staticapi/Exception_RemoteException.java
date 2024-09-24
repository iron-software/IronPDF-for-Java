package com.ironsoftware.ironpdf.internal.staticapi;

import java.util.Arrays;

public class Exception_RemoteException extends RuntimeException {

    private final String exceptionType;
    public String stackTraceString;

    public Exception_RemoteException() {
        throw new UnsupportedOperationException("RemoteException should not be manually initialized.");
    }

    public Exception_RemoteException(String message, String exceptionType) {
        super(message);
        this.stackTraceString = "";
        this.exceptionType = exceptionType;
    }

    public Exception_RemoteException(String message, String stackTrace, String exceptionType) {
        super(message);
        this.stackTraceString = stackTrace;
        this.exceptionType = exceptionType;
    }

    public final String getExceptionType() {
        return exceptionType;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return Arrays.stream(stackTraceString.split("\n"))
                .map(s -> new StackTraceElement(stackTraceString, "ironpdf", "ironpdf", 0))
                .toArray(StackTraceElement[]::new);
    }
}
