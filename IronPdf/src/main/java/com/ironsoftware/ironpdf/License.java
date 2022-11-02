package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.staticapi.Setting_Api;

public class License {

    /**
     * Gets current license key. Removes watermarks. Get Licensed at
     * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>
     * for licensing options.
     */
    public static String getLicenseKey() {
        return Setting_Api.LicenseKey;
    }

    /**
     * Sets license key. Need to set before performing any IronPdf operation. Removes watermarks. Get
     * Licensed at
     * <a href="https://ironpdf.com/licensing/">https://ironpdf.com/licensing/</a>
     * for licensing options.
     */
    public static void setLicenseKey(String licenseKey) {
        Setting_Api.LicenseKey = licenseKey;
    }
}
