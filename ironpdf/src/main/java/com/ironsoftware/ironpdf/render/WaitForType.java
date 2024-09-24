package com.ironsoftware.ironpdf.render;

/**
 Type of wait-for for the user to have a chance to control when to commit rendering.
 */
public enum WaitForType
{
    /**
     Default type, no condition, no special need to do anything prior to rendering.
     */
    None(0),

    /**
     This mode will wait until user calls internal IronPdf's JavaScript function `window.ironpdf.notifyRender()` that will trigger the rendering.
     */
    ManualTrigger(1),

    /**
     This mode will delay for specified amount of time before begin rendering.
     */
    RenderDelay(2),

    /**
     This mode will begin rendering when there is no new network request for 500ms as well as no inflight (outstanding) network requests.

     This mode is suitable for SPA (Single-Page Application) or web page that that doesn't have any on-going long polling network request.
     */
    NetworkIdle0(3),

    /**
     This mode will begin rendering when there is no new network request for 500ms but allowed to have no more than 2 inflight (outstanding)
     network requests.

     This mode is suitable for web application or web page that has on-going long-polling or heartbeart ping request.
     */
    NetworkIdle2(4),

    /**
     This mode will begin rendering when there is no new network request as per specified by user according to networkidle duration (in millisecond),
     and number of maximum allowed inflight (oustanding) network requests.

     This mode is suitable for customized type of web application or web page as per user's requirement that doesn't fit by using
     {@link WaitForType#NetworkIdle0}
     {@link WaitForType#NetworkIdle2}
     */
    NetworkIdleN(5),

    /**
     This mode will begin rendering when the specified HTML element becomes existent.
     */
    HtmlElement(6),

    /**
     This mode will begin rendering after all fonts have been loaded; be it local, remote, or google web fonts.
     */
    AllFontsLoaded(7);

    public static final int SIZE = java.lang.Integer.SIZE;

    private int intValue;
    private static java.util.HashMap<Integer, WaitForType> mappings;
    private static java.util.HashMap<Integer, WaitForType> getMappings()
    {
        if (mappings == null)
        {
            synchronized (WaitForType.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, WaitForType>();
                }
            }
        }
        return mappings;
    }

    private WaitForType(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return intValue;
    }

    public static WaitForType forValue(int value)
    {
        return getMappings().get(value);
    }
}
