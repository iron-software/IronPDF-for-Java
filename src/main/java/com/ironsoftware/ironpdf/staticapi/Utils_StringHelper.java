package com.ironsoftware.ironpdf.staticapi;

final class Utils_StringHelper {

    static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    static boolean isNullOrWhiteSpace(String string) {
        if (string == null) {
            return true;
        }

        for (int index = 0; index < string.length(); index++) {
            if (!Character.isWhitespace(string.charAt(index))) {
                return false;
            }
        }

        return true;
    }
}
