package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.*;

/**
 * The type Text api.
 */
public final class License_Api {

    /**
     * Determines whether a string license key is valid. <p>See https://ironpdf.com/licensing/ for trial,
     * development and commercial deployment licensing options.</p>
     *
     * @param licenseKey IronPDF license key as a string
     * @return True if the license key given is valid.
     */
    public static Boolean isValidLicensed(String licenseKey) {
        RpcClient client = Access.ensureConnection();

        SystemIsValidLicenseRequestP.Builder req = SystemIsValidLicenseRequestP.newBuilder();
        req.setLicenseKey(licenseKey);

        BooleanResultP res = client.GetBlockingStub("isValidLicensed").systemLicenseIsValidLicense(req.build());

        return Utils_Util.handleBooleanResult(res);
    }

    /**
     * Determines whether this instance of IronPDF is Licensed. <p>Will return false unless a {@link com.ironsoftware.ironpdf.License#setLicenseKey(String)}
     * is set to a valid trial or full license key in IronPdf.License.LicenseKey,
     * Web.Config , App.Config or appsettings.json in .Net Core.</p>
     * <p>See https://ironpdf.com/licensing/ and https://ironpdf.com/docs/license/license-keys/</p>
     *
     * @return True if the license key given is valid.
     */
    public static Boolean isLicensed() {
        RpcClient client = Access.ensureConnection();

        SystemIsLicensedRequestP.Builder req = SystemIsLicensedRequestP.newBuilder();

        BooleanResultP res = client.GetBlockingStub("isLicensed").systemLicenseIsLicensed(req.build());

        return Utils_Util.handleBooleanResult(res);
    }

    public static void SetLicensed(String licenseKey) {
        if(Access.client == null)
            return;

        RpcClient client = Access.ensureConnection();

        SystemSetLicenseKeyRequestP.Builder req = SystemSetLicenseKeyRequestP.newBuilder();
        req.setLicenseKey(licenseKey);

        EmptyResultP res = client.GetBlockingStub("SetLicensed").systemLicenseSetLicenseKey(req.build());

        Utils_Util.handleEmptyResult(res);
    }
}
