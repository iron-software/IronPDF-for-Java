package com.ironsoftware.ironpdf.render;

import java.util.HashMap;

/**
 * Provides credentials for IronPdf's embedded Chrome browser to log in to an intranet, extranet or
 * website, impersonating a user.   This allows a unique ability to render web-pages as PDFs even on
 * secure intranets, extranets and websites.
 */
public final class ChromeHttpLoginCredentials {

    /**
     * A Dictionary which allows custom cookies to be posted with every login request, and HTTP
     * request made by RenderUriToHml methods.
     */
    private HashMap<String, String> CustomCookies = new HashMap<>();
    private boolean EnableCookies = true;
    /**
     * Optional: Password credential for Windows / Linux network security authentication.
     */
    private String NetworkPassword = null;
    /**
     * Optional: User-name credential for Windows / Linux network security authentication.
     */
    private String NetworkUsername = null;

    public String getNetworkPassword() {
        return NetworkPassword;
    }

    public void setNetworkPassword(String value) {
        NetworkPassword = value;
    }

    public String getNetworkUsername() {
        return NetworkUsername;
    }

    public void setNetworkUsername(String value) {
        NetworkUsername = value;
    }

    public boolean isEnableCookies() {
        return EnableCookies;
    }

    public void setEnableCookies(boolean enableCookies) {
        EnableCookies = enableCookies;
    }

    public HashMap<String, String> getCustomCookies() {
        return CustomCookies;
    }

    public void setCustomCookies(HashMap<String, String> customCookies) {
        CustomCookies = customCookies;
    }
}
