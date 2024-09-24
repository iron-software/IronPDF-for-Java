package com.ironsoftware.ironpdf.render;

import com.ironsoftware.ironpdf.exception.IronPdfInputException;
import com.ironsoftware.ironpdf.internal.staticapi.HtmlValidator;

public class WaitFor {

    /**
     * Default maximum wait time in milliseconds
     */
    public static final int DefaultMaxWaitTime = 10000;

    /**
     * Default render delay duration in milliseconds.
     */
    public static final int DefaultRenderDelayDuration = 20;

    /**
     * Get type of wait-for.
     * <p>
     * Read-only, its value is meant to be set via factory method this class provides.
     */
    private WaitForType Type;

    public final WaitForType getType() {
        return Type;
    }

    private void setType(WaitForType value) {
        Type = value;
    }

    /**
     * Get timeout duration in milliseconds.
     * <p>
     * Read-only, its value is meant to be set via factory method this class provides.
     */
    private int Timeout;

    public final int getTimeout() {
        return Timeout;
    }

    private void setTimeout(int value) {
        Timeout = value;
    }

    /**
     * Get network idle duration in milliseconds to be regarded as part of network idle event.
     * <p>
     * Read-only, its value is meant to be set via factory method this class provides.
     */
    private int NetworkIdleDuration;

    public final int getNetworkIdleDuration() {
        return NetworkIdleDuration;
    }

    private void setNetworkIdleDuration(int value) {
        NetworkIdleDuration = value;
    }

    /**
     * Get number of allowed inflight network request to be regarded as part of network idle event.
     * <p>
     * Read-only, its value is meant to be set via factory method this class provides.
     */
    private int NumAllowedInFlight;

    public final int getNumAllowedInFlight() {
        return NumAllowedInFlight;
    }

    private void setNumAllowedInFlight(int value) {
        NumAllowedInFlight = value;
    }

    /**
     * Get amount of delay in milliseconds before rendering.
     * <p>
     * Read-only, its value is meant to be set via factory method this class provides.
     */
    private int RenderDelayDuration;

    public final int getRenderDelayDuration() {
        return RenderDelayDuration;
    }

    private void setRenderDelayDuration(int value) {
        RenderDelayDuration = value;
    }

    /**
     * Get HTML element query string.
     */
    private String HtmlElementQueryStr;

    public final String getHtmlElementQueryStr() {
        return HtmlElementQueryStr;
    }

    private void setHtmlElementQueryStr(String value) {
        HtmlElementQueryStr = value;
    }

    /**
     * Default construct WaitFor object.
     * Convenient method to construct a WaitFor object with default values.
     *
     * @param maxWaitTime Maximum wait time in milliseconds until it forces rendering. Default value is {@link WaitFor#DefaultMaxWaitTime}
     */
    public WaitFor(int maxWaitTime) {
        this(maxWaitTime, WaitFor.DefaultRenderDelayDuration);
    }

    /**
     * Default construct WaitFor object.
     * Convenient method to construct a WaitFor object with default values.
     */
    public WaitFor() {
        this(WaitFor.DefaultMaxWaitTime, WaitFor.DefaultRenderDelayDuration);
    }

    /**
     * Default construct WaitFor object.
     * Convenient method to construct a WaitFor object with default values.
     *
     * @param maxWaitTime Maximum wait time in milliseconds until it forces rendering. Default value is {@link WaitFor#DefaultMaxWaitTime}
     * @param renderDelay Render delay in milliseconds. It will delay before rendering. Default value is {@link WaitFor#DefaultRenderDelayDuration}
     *                    This will set to {@link WaitFor#RenderDelay}.
     */
    public WaitFor(int maxWaitTime, int renderDelay) {
        setType(WaitForType.RenderDelay);
        setTimeout(maxWaitTime);
        setNetworkIdleDuration(500); // doesn't matter
        setNumAllowedInFlight(0);
        setRenderDelayDuration(renderDelay);
        setHtmlElementQueryStr(null);
    }

    /**
     * Intended to be used internally as our API recommends user to use convenient methods e.g. {@link WaitFor#JavaScript(int)},
     * {@link WaitFor#NetworkIdle0(int)} and others to construct the WaitFor object instead of manually setting each data fields.
     * We use this internally as part of parsing ProtoBuf structure.
     *
     * @param type                WaitFor type
     * @param maxWaitTime         Maximum wait time in milliseconds until it forces rendering.
     * @param networkIdleDuration Network idle duration to be regarded as network idle event.
     * @param numAllowedInflight  Number of inflight network request allowed to have while still regarded it as network idle event.
     * @param renderDelayDuration Amount of time to delay before rendering (in ms)
     * @param htmlElementQueryStr HTML element query string that will be passed to JavaScript's document#querySelector().
     */
    public WaitFor(WaitForType type, int maxWaitTime, int networkIdleDuration, int numAllowedInflight, int renderDelayDuration, String htmlElementQueryStr) {
        setType(type);
        setTimeout(maxWaitTime);
        setNetworkIdleDuration(networkIdleDuration);
        setNumAllowedInFlight(numAllowedInflight);
        setRenderDelayDuration(renderDelayDuration);
        setHtmlElementQueryStr(htmlElementQueryStr);
    }

    /**
     * Basically it waits for nothing, but will render as soon as the page loaded.
     * <p>There is no need to call this method if user desires to normally render the page. It is mostly useful to reset WaitFor configurations back to wait for nothing.</p>
     * Page loaded in this case means only loaded in DOM, not the resource loaded.
     */
    public final void PageLoad() {
        setType(WaitForType.None);
        setTimeout(DefaultMaxWaitTime); // doesn't matter for its value to be set
        setNetworkIdleDuration(500);
        setNumAllowedInFlight(0);
        setRenderDelayDuration(0);
        setHtmlElementQueryStr(null);
    }

    /**
     * This method proceeds rendering by introducing an initial delay before rendering.
     *
     * @param delay Delay time in milliseconds before rendering
     */
    public final void RenderDelay(int delay) {
        setType(WaitForType.RenderDelay);
        setTimeout(DefaultMaxWaitTime); // doesn't matter for its value to be set
        setNetworkIdleDuration(500);
        setNumAllowedInFlight(0);
        setRenderDelayDuration(delay);
        setHtmlElementQueryStr(null);
    }

    /**
     * This method proceeds rendering by waiting until user calls IronPdf's internal JavaScript function <code>window.ironpdf.notifyRender()</code>.
     */
    public final void JavaScript() {
        JavaScript(WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until user calls IronPdf's internal JavaScript function <code>window.ironpdf.notifyRender()</code>.
     *
     * @param maxWaitTime Maximum wait time in milliseconds until it forces rendering. Default value is {@link WaitFor#DefaultMaxWaitTime}
     */
    public final void JavaScript(int maxWaitTime) {
        setType(WaitForType.ManualTrigger);
        setTimeout(maxWaitTime);
        setNetworkIdleDuration(500);
        setNumAllowedInFlight(0);
        setRenderDelayDuration(0);
        setHtmlElementQueryStr(null);
    }

    /**
     * This method proceeds rendering by waiting until it internally detects a network idle event when there is no network activity.
     * after at least 500ms as well as no inflight (outstanding) network requests.
     */
    public final void NetworkIdle0() {
        NetworkIdle0(WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it internally detects a network idle event when there is no network activity.
     * after at least 500ms as well as no inflight (outstanding) network requests.
     *
     * @param maxWaitTime Maximum wait time in milliseconds until it forces rendering. Default value is {@link WaitFor#DefaultMaxWaitTime}
     */
    public final void NetworkIdle0(int maxWaitTime) {
        setType(WaitForType.NetworkIdle0);
        setTimeout(maxWaitTime);
        setNetworkIdleDuration(500);
        setNumAllowedInFlight(0);
        setRenderDelayDuration(0);
        setHtmlElementQueryStr(null);
    }

    /**
     * This method proceeds rendering by waiting until it internally detects a network idle event when there is no network activity
     * after at least 500ms as well as at maximum of 2 inflight (outstanding) network request.
     */
    public final void NetworkIdle2() {
        NetworkIdle2(WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it internally detects a network idle event when there is no network activity
     * after at least 500ms as well as at maximum of 2 inflight (outstanding) network request.
     *
     * @param maxWaitTime Maximum wait time in milliseconds until it forces rendering. Default value is {@link WaitFor#DefaultMaxWaitTime}
     */
    public final void NetworkIdle2(int maxWaitTime) {
        setType(WaitForType.NetworkIdle2);
        setTimeout(maxWaitTime);
        setNetworkIdleDuration(500);
        setNumAllowedInFlight(2);
        setRenderDelayDuration(0);
        setHtmlElementQueryStr(null);
    }

    /**
     * This method proceeds rendering by waiting until it internally detects a network idle event when there is no network activity
     * after at least specified <em>networkIdleDuration</em> as well as at maximum of <em>maxNumAllowedInflight</em> inflight (outstanding) network requests.
     *
     * @param networkIdleDuration   Duration of time in milliseconds to regard as network idle event
     * @param maxNumAllowedInflight Maximum number of allowed inflight network requests to not invalidate network idle event
     */
    public final void NetworkIdle(int networkIdleDuration, int maxNumAllowedInflight) {
        NetworkIdle(networkIdleDuration, maxNumAllowedInflight, WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it internally detects a network idle event when there is no network activity
     * after at least specified <em>networkIdleDuration</em> as well as at maximum of <em>maxNumAllowedInflight</em> inflight (outstanding) network requests.
     *
     * @param networkIdleDuration   Duration of time in milliseconds to regard as network idle event
     * @param maxNumAllowedInflight Maximum number of allowed inflight network requests to not invalidate network idle event
     * @param maxWaitTime           Maximum wait time in milliseconds until it forces rendering. Default value is {@link WaitFor#DefaultMaxWaitTime}
     */
    public final void NetworkIdle(int networkIdleDuration, int maxNumAllowedInflight, int maxWaitTime) {
        Type = WaitForType.NetworkIdleN;
        Timeout = maxWaitTime;
        NetworkIdleDuration = networkIdleDuration;
        NumAllowedInFlight = maxNumAllowedInflight;
        RenderDelayDuration = 0;
        HtmlElementQueryStr = null;
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element via the specified query string which is executed by a JavaScript function <code>document.querySelector()</code>.
     * <p><a href='https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector'>Read querySelector() documentation</a>.</p>
     *
     * @param htmlQueryStr HTML element query string to query for with Javascript's document#querySelector()
     */
    public final void HtmlQuerySelector(String htmlQueryStr) {
        HtmlQuerySelector(htmlQueryStr, WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element via the specified query string which is executed by a JavaScript function <code>document.querySelector()</code>.
     * <p><a href='https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector'>Read querySelector() documentation</a>.</p>
     *
     * @param htmlQueryStr HTML element query string to query for with Javascript's document#querySelector()
     * @param maxWaitTime  Maximum wait time (in ms) until it forces rendering. Default is {@link WaitFor#DefaultMaxWaitTime}.
     */
    public final void HtmlQuerySelector(String htmlQueryStr, int maxWaitTime) {
        Type = WaitForType.HtmlElement;
        Timeout = maxWaitTime;
        NetworkIdleDuration = 500; // doesn't matter
        NumAllowedInFlight = 0; // doesn't matter
        RenderDelayDuration = 0;
        HtmlElementQueryStr = htmlQueryStr;
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element that has the same id as specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/getElementsByClassName">getElementsByClassName()</a>
     * but instead it makes a query directly via `document.querySelector()` as it calls {@link WaitFor#HtmlQuerySelector(String, int)} internally.</p>
     *
     * @param id          Target element Id. Id token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     * @throws IronPdfInputException Thrown if input id is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementById(String id) {
        HtmlElementById(id, WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element that has the same id as specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/getElementsByClassName">getElementsByClassName()</a>
     * but instead it makes a query directly via `document.querySelector()` as it calls {@link WaitFor#HtmlQuerySelector(String, int)} internally.</p>
     *
     * @param id          Target element Id. Id token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     * @param maxWaitTime Maximum wait time (in ms) until it forces rendering. Default is {@link WaitFor#DefaultMaxWaitTime}.
     * @throws IronPdfInputException Thrown if input id is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementById(String id, int maxWaitTime) {
        if (!HtmlValidator.ValidateHtmlNameAndIdToken(id)) {
            throw new IronPdfInputException("Invalid input of name or Id.. It must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens (\"-\"), underscores (\"_\"), colons (\":\"), and periods (\".\").");
        }
        HtmlQuerySelector(String.format("#%1$s", id), maxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the element with the attribute name as of the specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementsByName">getElementsByName()</a>
     * but effective only for first found element from the result. Instead it makes a query directly via `document.querySelector()` as it calls {@link WaitFor#HtmlQuerySelector(String, int)} internally.</p>
     *
     * @param name        Target element name# Name token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     * @throws IronPdfInputException Thrown if input name is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementByName(String name) {
        HtmlElementByName(name, WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the element with the attribute name as of the specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementsByName">getElementsByName()</a>
     * but effective only for first found element from the result. Instead it makes a query directly via `document.querySelector()` as it calls {@link WaitFor#HtmlQuerySelector(String, int)} internally.</p>
     *
     * @param name        Target element name# Name token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     * @param maxWaitTime Maximum wait time (in ms) until it forces rendering. Default is {@link WaitFor#DefaultMaxWaitTime}.
     * @throws IronPdfInputException Thrown if input name is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementByName(String name, int maxWaitTime) {
        if (!HtmlValidator.ValidateHtmlNameAndIdToken(name)) {
            throw new IronPdfInputException("Invalid input of name or Id.. It must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens (\"-\"), underscores (\"_\"), colons (\":\"), and periods (\".\").");
        }
        HtmlQuerySelector(String.format("[name='%1$s']", name), maxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element that itself has the same tag name as of specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/getElementsByTagName">getElementsByTagName()</a>
     * but effective only for first found element from the result. Instead it makes a query directly via <code>document.querySelector()</code> as it calls {@link WaitFor#HtmlQuerySelector(String, int)} internally.</p>
     *
     * @param tagName     Target element's tag name# Tag name token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     * @throws IronPdfInputException Thrown if input name is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementByTagName(String tagName) {
        HtmlElementByTagName(tagName, WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element that itself has the same tag name as of specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/getElementsByTagName">getElementsByTagName()</a>
     * but effective only for first found element from the result. Instead it makes a query directly via <code>document.querySelector()</code> as it calls {@link WaitFor#HtmlQuerySelector(String, int)} internally.</p>
     *
     * @param tagName     Target element's tag name# Tag name token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     * @param maxWaitTime Maximum wait time (in ms) until it forces rendering. Default is {@link WaitFor#DefaultMaxWaitTime}.
     * @throws IronPdfInputException Thrown if input name is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementByTagName(String tagName, int maxWaitTime) {
        if (!HtmlValidator.ValidateHtmlNameAndIdToken(tagName)) {
            throw new IronPdfInputException("Invalid input of name or Id.. It must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens (\"-\"), underscores (\"_\"), colons (\":\"), and periods (\".\").");
        }
        HtmlQuerySelector(String.format("%1$s", tagName), maxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element whose the class name as of the specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/getElementsByClassName">getElementsByClassName()</a>
     * but effective only for first found element from the result. Instead it makes a query directly via <code>document.querySelector()</code> as it calls {@link WaitFor#HtmlQuerySelector(String, int)} )} internally.</p>
     *
     * @param classAttribName Target element's class attribute name# Class attribute name token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), and underscores ("_").
     * @throws IronPdfInputException Thrown if input name is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementByClassName(String classAttribName) {
        HtmlElementByClassName(classAttribName, WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until it finds the HTML element whose the class name as of the specified one.
     * <p>Related JavaScript API that works similarly to this method is <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element/getElementsByClassName">getElementsByClassName()</a>
     * but effective only for first found element from the result. Instead it makes a query directly via <code>document.querySelector()</code> as it calls {@link WaitFor#HtmlQuerySelector(String, int)} )} internally.</p>
     *
     * @param classAttribName Target element's class attribute name# Class attribute name token must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens ("-"), and underscores ("_").
     * @param maxWaitTime     Maximum wait time (in ms) until it forces rendering. Default is {@link WaitFor#DefaultMaxWaitTime}.
     * @throws IronPdfInputException Thrown if input name is invalid not according to HTML name and Id naming rule.
     */
    public final void HtmlElementByClassName(String classAttribName, int maxWaitTime) {
        if (!HtmlValidator.ValidateHtmlClassAttribName(classAttribName)) {
            throw new IronPdfInputException("Invalid input of class attribute name. It must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits ([0-9]), hyphens (\"-\"), and underscores (\"_\").");
        }
        HtmlQuerySelector(String.format(".%1$s", classAttribName), maxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until all of its fonts have been loaded. Such font types can local, remote, or google web fonts.
     */
    public final void AllFontsLoaded() {
        AllFontsLoaded(WaitFor.DefaultMaxWaitTime);
    }

    /**
     * This method proceeds rendering by waiting until all of its fonts have been loaded. Such font types can local, remote, or google web fonts.
     *
     * @param maxWaitTime maximum wait time (in ms) until it forces rendering. Default is {@link WaitFor#DefaultMaxWaitTime}.
     */
    public final void AllFontsLoaded(int maxWaitTime) {
        Type = WaitForType.AllFontsLoaded;
        Timeout = maxWaitTime;
        NetworkIdleDuration = 500;
        NumAllowedInFlight = 0;
        RenderDelayDuration = 0;
        HtmlElementQueryStr = null;
    }
}
