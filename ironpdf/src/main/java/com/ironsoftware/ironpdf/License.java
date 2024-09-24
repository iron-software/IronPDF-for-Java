package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.License_Api;
import com.ironsoftware.ironpdf.internal.staticapi.Setting_Api;

/**
 * Allows developers to apply license keys for of IronPDF for Java
 */
public final class License {

    /**
     * Gets your current license key. Removes PDF watermarks.
     * <p>Get a free development license at <a href="https://ironpdf.com/licensing/#trial-license">https://ironpdf.com/licensing/#trial-license</a> or purchase a deployment license from
     * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>.</p>
     *
     * @return the license key
     */
    public static String getLicenseKey() {
        return Setting_Api.licenseKey;
    }

    /**
     * Sets your license key.
     * <p>***Recommended to set license key before calling any IronPdf function.***
     * <p>You will need to add a valid license key to perform any IronPdf operations beyond basic testing.
     * <p>Adding a valid license key removes PDF watermarks and allows advanced functionality.
     * <p>Get a free development license at <a href="https://ironpdf.com/licensing/#trial-license">https://ironpdf.com/licensing/#trial-license</a> or purchase a deployment license from
     * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>.</p>
     *
     * @param licenseKey the license key
     */
    public static void setLicenseKey(String licenseKey) {
        Setting_Api.licenseKey = licenseKey;
        License_Api.SetLicensed(licenseKey);
    }

    /**
     * Determines whether this instance of IronPDF is Licensed. <p>Will return false unless a {@link com.ironsoftware.ironpdf.License#setLicenseKey(String)}
     * is set to a valid trial or full license key in IronPdf.License.LicenseKey,
     * Web.Config , App.Config or appsettings.json in .Net Core.</p>
     * <p>See https://ironpdf.com/licensing/ and https://ironpdf.com/docs/license/license-keys/</p>
     *
     * @return True if the license key given is valid.
     */
    public static boolean isLicensed() {
        return License_Api.isLicensed();
    }

    /**
     * Determines whether a string license key is valid. <p>See https://ironpdf.com/licensing/ for trial,
     * development and commercial deployment licensing options.</p>
     *
     * @param licenseKey IronPDF license key as a string
     * @return True if the license key given is valid.
     */
    public static boolean isValidLicensed(String licenseKey) {
        return License_Api.isValidLicensed(licenseKey);
    }
}
