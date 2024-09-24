package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.IronPdfEngineConnection;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Setting api.
 */
public final class Setting_Api {
    static final Logger logger = LoggerFactory.getLogger(Setting_Api.class);

    public static String licenseKey = "";

    public static IronPdfEngineConnection connectionMode = IronPdfEngineConnection.configure().withOfficialCloud();

    public static final String IRON_PDF_ENGINE_VERSION = "2024.10.2";

    public static int ironPdfEngineTimeout = 120;

    //region ---------------------SubProcess options---------------------

    public static boolean enableDebug = false;

    public static Path logPath = Paths.get("ironpdfengine.log");

    public static Path ironPdfEngineWorkingDirectory = Paths.get(System.getProperty("user.dir"));

    public static Path tempFolderPath = null;

    public static boolean singleProcess = false;

    public static int chromeBrowserLimit = 30;

    public static Path chromeBrowserCachePath = null;

    public static int chromeGpuMode = 0;

    public static boolean linuxAndDockerAutoConfig = true;

    public static String getIronPdfEngineZipName() {
        return getIronPdfEngineFolderName() + ".zip";
    }

    public static String getIronPdfEngineExecutableFileName() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "IronPdfEngineConsole.exe";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "IronPdfEngineConsole";
        } else if (SystemUtils.IS_OS_MAC) {
            //todo also check for M1
            return "IronPdfEngineConsole";
        } else {
            //default, should not reach
            return "IronPdfEngineConsole";
        }
    }

    /**
     * Gets custom iron pdf engine path.
     *
     * @return the custom iron pdf engine path
     */
    public static Path getIronPdfEngineExecutablePath(Path workingDir) {
        return Paths.get(workingDir.toAbsolutePath().toString()
                , getIronPdfEngineFolderName()
                , getIronPdfEngineExecutableFileName());
    }

    /**
     * Current os full name string.
     *
     * @return the string
     */
    static String currentOsFullName() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "Windows";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "Linux";
        } else if (SystemUtils.IS_OS_MAC) {
            return "MacOS";
        }
        throw new RuntimeException("unknown OS:" + SystemUtils.OS_NAME);
    }

    /**
     * Current os arch string.
     *
     * @return the string
     */
    static String currentOsArch() {
        if (SystemUtils.IS_OS_WINDOWS) {
            if (System.getProperty("os.arch").equalsIgnoreCase("x86")) {
                return "x86";
            } else {
                return "x64";
            }
        } else if (SystemUtils.IS_OS_LINUX) {
            return "x64";
        } else if (SystemUtils.IS_OS_MAC) {
            if(System.getProperty("os.arch").toLowerCase().contains("arm") )
                return "arm64";
            return "x64";
        }
        throw new RuntimeException("unknown OS:" + SystemUtils.OS_NAME);
    }

    public static String getIronPdfEngineFolderName() {
        return "IronPdfEngine." +
                IRON_PDF_ENGINE_VERSION +
                "." +
                currentOsFullName() +
                "." +
                currentOsArch();
    }


    //endregion

    //region ---------------------Deprecated---------------------

    /**
     * just for support deprecated host port configuration.
     * should be removed in the future.
     */
    public static boolean useDeprecatedConnectionSettings = false;

    static boolean isIronPdfEngineDocker = false;

    public static String subProcessHost = "127.0.0.1";

    //will use unique port numbers to avoid conflicts with other instances of IronPdf
    public static int subProcessPort = getDefaultPort();

    /**
     * Find free port int.
     *
     * @return the int
     */
    public static int getDefaultPort() {
        return 33350;
    }

    public static void useIronPdfEngineDocker(int port){
        logger.info("Using IronPdfEngine Docker port:" + port);
        subProcessPort = port;
        isIronPdfEngineDocker = true;
    }
    //endregion
}
