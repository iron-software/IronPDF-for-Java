package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.exception.*;
import com.ironsoftware.ironpdf.internal.proto.RemoteException;

import static com.ironsoftware.ironpdf.staticapi.Utils_Util.NullIfEmpty;

final class Exception_Converter {

    static RuntimeException FromProto(RemoteException exception) {
        Exception_RemoteException ex = new Exception_RemoteException(exception.getMessage(),
                NullIfEmpty(exception.getRemoteStackTrace()),
                NullIfEmpty(exception.getExceptionType()));
        String[] exceptionTypes = ex.getExceptionType().split("[.]", -1);
        String cs = exceptionTypes[exceptionTypes.length - 1];

        switch (cs) {
            case "IronPdfDeploymentException":
                throw new IronPdfDeploymentException(ex);
            case "IronPdfInputException":
                throw new IronPdfInputException(ex);
            case "IronPdfLicensingException":
                throw new IronPdfLicensingException(ex);
            case "IronPdfNativeException":
                throw new IronPdfNativeException(ex);
            case "IronPdfProductException":
                throw new IronPdfProductException(ex);

            case "NotSupportedException":
                throw new UnsupportedOperationException(ex.getMessage() + ex.StackTraceString, ex);
            case "IndexOutOfRangeException":
            case "ArgumentOutOfRangeException":
                throw new IndexOutOfBoundsException(ex.getMessage() + ex.StackTraceString);
            case "ArgumentException":
                throw new IllegalArgumentException(ex.getMessage() + ex.StackTraceString, ex);
            case "InvalidCastException":
                throw new ClassCastException(ex.getMessage() + ex.StackTraceString);
            case "NullReferenceException":

                throw new NullPointerException(ex.getMessage() + ex.StackTraceString);
            case "AggregateException":
            default:
                throw ex;
        }
    }
}
