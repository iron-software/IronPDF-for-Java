package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.Setting_Api;

/** Allows developers to apply license keys for of IronPDF for Java
*/
public final class License {

    /**
     * Gets your current license key. Removes PDF watermarks. 
     * <p>Get a free development license at <a href="https://ironpdf.com/licensing/#trial-license">https://ironpdf.com/licensing/#trial-license</a> or purchase a deployment license from 
     * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>.</p>
     */
    public static String getLicenseKey() {
        return Setting_Api.licenseKey;
    }

    /**
     * Sets your license key.
     * <p>***Please set license key before calling any IronPdf function.***
     * <p>You will need to add a valid license key to perform any IronPdf operations beyond basic testing.
     * <p>Adding a valid license key removes PDF watermarks and allows advanced functionality.
     * <p>Get a free development license at <a href="https://ironpdf.com/licensing/#trial-license">https://ironpdf.com/licensing/#trial-license</a> or purchase a deployment license from 
     * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>.</p>
     */
    public static void setLicenseKey(String licenseKey) {
        Setting_Api.licenseKey = licenseKey;
    }
}
