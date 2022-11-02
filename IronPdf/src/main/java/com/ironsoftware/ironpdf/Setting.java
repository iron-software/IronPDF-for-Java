package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.staticapi.Setting_Api;

import java.nio.file.Path;

public final class Setting {

    /**
     * @return The current IronPdfEngine version
     */
    @SuppressWarnings("SameReturnValue")
    public static String getIronPdfEngineVersion() {
        return Setting_Api.IronPdfEngineVersion;
    }


    /**
     * @return true if debug mode is enabled.
     */
    public static boolean isDebug() {
        return Setting_Api.EnableDebug;
    }

    /**
     * Enable or disable debug mode.
     */
    public static void setDebug(boolean isDebug) {
        Setting_Api.EnableDebug = isDebug;
    }

    /**
     * We will look for IronPdfEngine (executable) in this folder first, if not exists we will try to
     * download it in to this folder.
     *
     * @return custom IronPdfEngine folder.
     */
    public static Path getIronPdfEngineFolder() {
        return Setting_Api.IronPdfEngineFolder;
    }

    /**
     * Sets custom IronPdfEngine folder. We will look for IronPdfEngine (executable) in this folder
     * first, if not exists we will try to download it in to this folder.
     */
    public static void setIronPdfEngineFolder(Path path) {
        Setting_Api.IronPdfEngineFolder = path;
    }
}
