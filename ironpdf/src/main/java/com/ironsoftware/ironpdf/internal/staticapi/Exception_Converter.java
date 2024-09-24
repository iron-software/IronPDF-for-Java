package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.exception.*;
import com.ironsoftware.ironpdf.internal.proto.RemoteExceptionP;

final class Exception_Converter {

    static RuntimeException fromProto(RemoteExceptionP exception) {
        Exception_RemoteException ex = new Exception_RemoteException(exception.getMessage(),
                Utils_Util.nullIfEmpty(exception.getRemoteStackTrace()),
                Utils_Util.nullIfEmpty(exception.getExceptionType()));
        String[] exceptionTypes = ex.getExceptionType().split("[.]", -1);
        String cs = exceptionTypes[exceptionTypes.length - 1];

        switch (cs) {
            case "IronPdfDeploymentException":
                throw new IronPdfDeploymentException(ex);
            case "IronPdfInputException":
                throw new IronPdfInputException(ex);
            case "IronPdfLicensingException":
            case "LicensingException":
                throw new IronPdfLicensingException(ex);
            case "IronPdfNativeException":
                throw new IronPdfNativeException(ex);
            case "IronPdfProductException":
                throw new IronPdfProductException(ex);

            case "NotSupportedException":
                throw new UnsupportedOperationException(ex.getMessage() + ex.stackTraceString, ex);
            case "IndexOutOfRangeException":
            case "ArgumentOutOfRangeException":
                throw new IndexOutOfBoundsException(ex.getMessage() + ex.stackTraceString);
            case "ArgumentException":
                throw new IllegalArgumentException(ex.getMessage() + ex.stackTraceString, ex);
            case "InvalidCastException":
                throw new ClassCastException(ex.getMessage() + ex.stackTraceString);
            case "NullReferenceException":

                throw new NullPointerException(ex.getMessage() + ex.stackTraceString);
            case "AggregateException":
            default:
                throw ex;
        }
    }
}
