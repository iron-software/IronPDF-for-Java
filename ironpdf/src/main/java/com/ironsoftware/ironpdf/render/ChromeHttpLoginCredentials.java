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
    private HashMap<String, String> customCookies = new HashMap<>();
    private boolean enableCookies = true;
    /**
     * Optional: Password credential for Windows / Linux network security authentication.
     */
    private String networkPassword = null;
    /**
     * Optional: User-name credential for Windows / Linux network security authentication.
     */
    private String networkUsername = null;

    /**
     * Gets network password. Optional: Password credential for Windows / Linux network security authentication.
     *
     * @return the network password
     */
    public String getNetworkPassword() {
        return networkPassword;
    }

    /**
     * Sets network password. Optional: Password credential for Windows / Linux network security authentication.
     *
     * @param value the value
     */
    public void setNetworkPassword(String value) {
        networkPassword = value;
    }

    /**
     * Gets network username. Optional: User-name credential for Windows / Linux network security authentication.
     *
     * @return the network username
     */
    public String getNetworkUsername() {
        return networkUsername;
    }

    /**
     * Sets network username. Optional: User-name credential for Windows / Linux network security authentication.
     *
     * @param value the value
     */
    public void setNetworkUsername(String value) {
        networkUsername = value;
    }

    /**
     * Is enable cookies boolean. Using with {@link #setCustomCookies(HashMap)}
     *
     * @return the boolean
     */
    public boolean isEnableCookies() {
        return enableCookies;
    }

    /**
     * Sets enable cookies. Using with {@link #setCustomCookies(HashMap)}
     *
     * @param enableCookies enable cookies
     */
    public void setEnableCookies(boolean enableCookies) {
        this.enableCookies = enableCookies;
    }

    /**
     * Gets custom cookies. A Dictionary which allows custom cookies to be posted with every login request, and HTTP
     * request made by RenderUriToHml methods.
     *
     * @return the new HashMap of custom cookies
     */
    public HashMap<String, String> getCustomCookies() {
        return new HashMap<>(customCookies);
    }

    /**
     * Sets custom cookies. A Dictionary which allows custom cookies to be posted with every login request, and HTTP
     * request made by RenderUriToHml methods.
     *
     * @param customCookies the custom cookies
     */
    public void setCustomCookies(HashMap<String, String> customCookies) {
        this.customCookies = customCookies;
    }
}
