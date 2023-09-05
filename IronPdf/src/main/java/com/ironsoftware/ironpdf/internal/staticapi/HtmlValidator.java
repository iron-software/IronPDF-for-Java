package com.ironsoftware.ironpdf.internal.staticapi;
import java.util.regex.*;
public class HtmlValidator {
    /**
     Validate whether the specified HTML name or id is valid according to the following rule.
     <p>Name or ID token begins with a letter ([A-Za-z]), and maybe followed by any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").</p>

     @param nameOrId Name of Id to validate according to HTML's Name and Id naming rule.
     @return Return true if the specified name or Id is valid, otherwise return false.
     */
    public static boolean ValidateHtmlNameAndIdToken(String nameOrId)
    {
        String pattern = "^[A-Za-z][A-Za-z0-9-_:\\.]*$";
        return Pattern.matches(pattern, nameOrId);
    }

    /**
     Validate whether the specified HTML class attribute name is valid according to the following rule.
     <p>Name or ID token begins with a letter ([A-Za-z]), and maybe followed by any number of letters, digits ([0-9]), hyphens ("-"), and underscores ("_").</p>

     @param classAttribName Class attribute name to validate according to HTML's class attribute name naming rule.
     @return Return true if the specified class attribute name is valid, otherwise return false.
     */
    public static boolean ValidateHtmlClassAttribName(String classAttribName)
    {
        String pattern = "^[A-Za-z][A-Za-z0-9-_]*$";
        return Pattern.matches(pattern, classAttribName);
    }

}
