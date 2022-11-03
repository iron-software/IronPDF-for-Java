package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.Setting_Api;

import java.nio.file.Path;

/** Important global settings for configuration of IronPDF for Java
*/

public final class Settings {

    /**
     * @return The current IronPdfEngine version
     */
    @SuppressWarnings("SameReturnValue")
    public static String getIronPdfEngineVersion() {
        return Setting_Api.IRON_PDF_ENGINE_VERSION;
    }


    /**
     * @return true if debug mode is enabled.
     */
    public static boolean isDebug() {
        return Setting_Api.enableDebug;
    }

    /**
     * Enable or disable debug mode.
     */
    public static void setDebug(boolean isDebug) {
        Setting_Api.enableDebug = isDebug;
    }

    /**
     * We will look for IronPdfEngine (binaries) in this folder first. 
     * If binaries are not present or are of an older version, the software will automatically try to download all the latest required binaries into this folder.
     *
     * @return custom IronPdfEngine folder.
     */
    public static Path getIronPdfEngineFolder() {
        return Setting_Api.ironPdfEngineFolder;
    }

    /**
     * Sets a custom IronPdfEngine folder path. We will look for IronPdfEngine (binaries) in this folder first.  
     * If binaries are not present or are of an older version, the software will automatically try to download all the latest required binaries into this folder.
     */
    public static void setIronPdfEngineFolder(Path path) {
        Setting_Api.ironPdfEngineFolder = path;
    }
}
