package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.HandshakeRequestP;
import com.ironsoftware.ironpdf.internal.proto.HandshakeResponseP;
import com.ironsoftware.ironpdf.internal.proto.IronPdfServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.ironsoftware.ironpdf.internal.staticapi.Setting_Api.*;
import static com.ironsoftware.ironpdf.internal.staticapi.Utils_Util.logInfoOrSystemOut;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

final class Access {
    static final Logger logger = LoggerFactory.getLogger(Access.class);
    static final Logger engineLogger = LoggerFactory.getLogger("com.ironsoftware.ironpdf.IronPdfEngine");
    static RpcClient client = null;
    static boolean isTryDownloaded = false;
    private static ManagedChannel channel = null;
    private static Process ironPdfProcess = null;
    private static boolean tryAgain = true;
    private static BufferedReader stdInput;
    private static BufferedReader stdError;
    private static CountDownLatch serverReady;

    static synchronized RpcClient ensureConnection() {
        if (client != null) {
            return client;
        }

        if (!isIronPdfEngineDocker) {
            startServer();
        }

        if (channel == null) {
            channel = ManagedChannelBuilder.forAddress(Setting_Api.subProcessHost,
                    Setting_Api.subProcessPort).usePlaintext().build();
        }

        RpcClient newClient = new RpcClient(
                IronPdfServiceGrpc.newBlockingStub(channel),
                IronPdfServiceGrpc.newFutureStub(channel),
                IronPdfServiceGrpc.newStub(channel));

        logger.debug("Handshaking, Expected IronPdfEngine Version : " + Setting_Api.IRON_PDF_ENGINE_VERSION);

        HandshakeRequestP.Builder handshakeRequest = HandshakeRequestP.newBuilder();
        handshakeRequest.setExpectedVersion(Setting_Api.IRON_PDF_ENGINE_VERSION);
        handshakeRequest.setProgLang("java");

        HandshakeResponseP res_firstTry = newClient.blockingStub.handshake(
                handshakeRequest.build());

        logger.debug("Handshake result:" + res_firstTry);

        switch (res_firstTry.getResultOrExceptionCase()) {
            case SUCCESS:
                client = newClient;
                setLicenseKey();
                return client;
            case REQUIREDVERSION:
                logger.error("Mismatch IronPdfEngine version expected: "
                        + Setting_Api.IRON_PDF_ENGINE_VERSION + " but found:" + res_firstTry);
//                //todo download new Binary
                if (!isIronPdfEngineDocker && tryAgain) {
                    tryAgain = false;
                    stopIronPdfEngine();
                    downloadIronPdfEngine();
                    Setting_Api.subProcessPort = Setting_Api.getDefaultPort();
                    return ensureConnection();
                }
                client = newClient;
                setLicenseKey();
                return client;

            case EXCEPTION:
                throw Exception_Converter.fromProto(res_firstTry.getException());
            default:
                throw new RuntimeException("Unexpected result from handshake");
        }
    }

    static void setLicenseKey() {
        //Set License
        String lk = Utils_StringHelper.isNullOrWhiteSpace(Setting_Api.licenseKey) ?
                new ConfigLoader().getProperty("IRONPDF_LICENSE_KEY") : Setting_Api.licenseKey;
        if (!Utils_StringHelper.isNullOrWhiteSpace(lk))
            License_Api.SetLicensed(lk);
    }

    static synchronized void downloadIronPdfEngine() {
        try {
            if (isTryDownloaded) {
                return;
            }
            Path zipFilePath = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(),
                    Setting_Api.getIronPdfEngineZipName());

            logInfoOrSystemOut(logger, "Download IronPdfEngine to working dir: " + zipFilePath.toAbsolutePath());
            logInfoOrSystemOut(logger, "You can skip this downloading step by adding IronPdfEngine as a Maven dependency. see: https://github.com/iron-software/IronPDF-for-Java#install-ironpdf-engine-as-a-maven-dependency");
            isTryDownloaded = true;

            URL downloadUrl = new URL("https://ironpdfengine.azurewebsites.net/api/IronPdfEngineDownload?version="
                    + Setting_Api.IRON_PDF_ENGINE_VERSION +
                    "&platform=" + Setting_Api.currentOsFullName() +
                    "&architect=" + Setting_Api.currentOsArch());
            try (InputStream stream = downloadUrl.openStream()) {

                long downloadSize = stream.available();
                try (DownloadInputStream pis = new DownloadInputStream(stream, downloadSize, logger)) {
                    copyInputStreamToFile(pis, zipFilePath.toFile());
                }
            }

            Path unzippedFolder = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(), Setting_Api.getIronPdfEngineFolderName());
            logInfoOrSystemOut(logger, "Unzipping IronPdfEngine to dir: " + unzippedFolder);
            unzip(zipFilePath.toAbsolutePath().toString(),
                    unzippedFolder.toAbsolutePath().toString());
            logger.info("Delete zip file: " + zipFilePath.toAbsolutePath());
            Files.deleteIfExists(zipFilePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to download IronPdfEngine binary", e);
            throw new RuntimeException("Failed to download IronPdfEngine binary", e);
        }
    }

    static void unzip(String zipFilePath, String destDir) throws IOException {
        try (java.util.zip.ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(entryDestination.toPath());
                    setPermission(entryDestination);
                } else {
                    File parentDir = entryDestination.getParentFile();
                    Files.createDirectories(parentDir.toPath());
                    setPermission(parentDir);
                    try (InputStream in = zipFile.getInputStream(entry);
                         OutputStream out = Files.newOutputStream(entryDestination.toPath())) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        }
    }

    static synchronized void startServer() {
        try {
            Optional<File> selectedFile = getAvailableIronPdfEngineFile();
            if (selectedFile.isPresent()) {
                logger.info("Using IronPdfEngine from: " + selectedFile.get().getAbsolutePath());

                try {
//                    logger.info("Try Setting IronPdfEngine permission...");
                    try {
                        File parentDir = selectedFile.get().getParentFile();
                        Files.createDirectories(parentDir.toPath());
                        setPermission(parentDir);
                    } catch (Exception e) {
                        logger.warn("Cannot set IronPdfEngine parent folder permission ", e);
                    }

                    Arrays.stream(Objects.requireNonNull(selectedFile.get().getParentFile().listFiles()))
                            .forEach(file -> {
                                try {
                                    setPermission(file);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (Exception e) {
                    logger.warn("Cannot set IronPdfEngine files permission ", e);
                }

                List<String> cmdList = new ArrayList<>();

                cmdList.add(selectedFile.get().toPath().toAbsolutePath().toString());
                cmdList.add("host=" + Setting_Api.subProcessHost);
                cmdList.add("port=" + Setting_Api.subProcessPort);
                cmdList.add("enable_debug=" + Setting_Api.enableDebug);
                cmdList.add("log_path=" + Setting_Api.logPath);
                cmdList.add("programming_language=" + "java");
                cmdList.add("single_process=" + (Setting_Api.singleProcess || currentOsFullName().equalsIgnoreCase("MacOS")));
                cmdList.add("docker_build=false");
                cmdList.add("linux_and_docker_auto_config=" + Setting_Api.linuxAndDockerAutoConfig);
                cmdList.add("skip_initialization=false");
                cmdList.add("chrome_browser_limit=" + Setting_Api.chromeBrowserLimit);
                if (chromeBrowserCachePath != null)
                    cmdList.add("chrome_cache_path=" + Setting_Api.chromeBrowserCachePath.toAbsolutePath());
                cmdList.add("chrome_gpu_mode=" + Setting_Api.chromeGpuMode);

                if (Setting_Api.tempFolderPath != null)
                    cmdList.add("temp_folder_path=" + Setting_Api.tempFolderPath.toAbsolutePath());

                if (!Utils_StringHelper.isNullOrWhiteSpace(Setting_Api.licenseKey)) {
                    cmdList.add("license_key=" + Setting_Api.licenseKey);
                } else {
                    cmdList.add("license_key=" + new ConfigLoader().getProperty("IRONPDF_LICENSE_KEY"));
                }
                serverReady = new CountDownLatch(1);
                ProcessBuilder pb = new ProcessBuilder(cmdList);

                logger.info("Start IronPdfEngine");
                if (enableDebug) {
                    logger.debug("options: " + cmdList);
                }

                Process proc = pb.start();
                catchServerMessage(proc);

                //This addShutdownHook will run when ...
                // * When all of JVM threads have completed execution
                // * System.exit()
                // * CTRL-C
                // * System level shutdown or User Log-Off
                //BUT IT WILL ***NOT*** RUN WHEN
                // * VM crashes
                // * OS kill command
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    shutdownChanel();
                    logger.info("Shutdown IronPdfEngine process");
                    proc.destroy();
                }));

                //wait for IronPdfEngine finish initialize
                //not throw timeout exception because we will try to connect IronPdfEngine on first method called.
                boolean ignored = serverReady.await(60, TimeUnit.SECONDS);

                ironPdfProcess = proc;

            } else {
                logger.debug(
                        "Cannot find IronPdfEngine from ironPdf working dir:" + Setting_Api.ironPdfEngineWorkingDirectory
                                .toAbsolutePath());
                if (tryAgain) {
                    downloadIronPdfEngine();
                    logger.info("Try to start IronPdfEngine again");
                    tryAgain = false;
                    startServer();
                } else {
                    throw new RuntimeException(String.format("Cannot locate IronPdfEngine. at %1$s",
                            Setting_Api.getIronPdfEngineExecutablePath(ironPdfEngineWorkingDirectory)
                                    .toAbsolutePath())
                            + " An alternative approach is to install one of ironpdf-engine packages https://search.maven.org/search?q=ironpdf%20engine, more information: https://github.com/iron-software/IronPDF-for-Java#install-ironpdf-engine-as-a-maven-dependency");
                }

            }
        } catch (Exception e) {
            logger.error("Cannot start IronPdfEngine (working dir: " + ironPdfEngineWorkingDirectory.toAbsolutePath() + ")", e);
            e.printStackTrace();
            throw new RuntimeException("Cannot start IronPdfEngine due to " + e.getMessage(), e);
        }
    }

    private static void catchServerMessage(Process proc) {

        stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        Thread threadInput = new Thread(() -> {
            engineLogger.debug("listening IronPdfEngine");
            // Read the output from the command
            stdInput.lines().forEach(line -> {
                if (line.trim().equalsIgnoreCase("IronPdfEngine is up")) {
                    serverReady.countDown();
                }
                if (Setting_Api.enableDebug)
                    engineLogger.info("[IronPdfEngine]" + line);
            });

        });
        //Note: Whenever the last non-daemon thread terminates, all the daemon threads will be terminated automatically.
        threadInput.setDaemon(true);
        threadInput.start();

        stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        Thread threadError = new Thread(() -> {
            // Read the error from the command
            stdError.lines().forEach(line -> {
                if (Setting_Api.enableDebug) {
                    engineLogger.error("[IronPdfEngine][Error]" + line);
                } else {
                    com.ironsoftware.ironpdf.Settings.setDebug(true);
                    logger.warn("IronPdfEngine Error! For more information, Please enable Debug mode by adding this line before calling any IronPDF methods `com.ironsoftware.ironpdf.Settings.setDebug(true);`");
                    logger.info("Current log file path: " + Setting_Api.logPath.toAbsolutePath());
                }
            });
        });

        //Note: Whenever the last non-daemon thread terminates, all the daemon threads will be terminated automatically.
        threadError.setDaemon(true);
        threadError.start();
    }

    private static void shutdownChanel() {
        if (channel != null && !channel.isShutdown() && !channel.isTerminated()) {
            channel.shutdown();
            channel = null;
        }
    }

    /**
     * Stop IronPdfEngine process
     */
    public static void stopIronPdfEngine() {
        shutdownChanel();

        if (ironPdfProcess != null) {
            ironPdfProcess.destroy();
        }
        ironPdfProcess = null;

        client = null;
    }

    private static void setPermission(File file) {
        try {
            boolean setExecutablePermissionResult = file.setExecutable(true, false);
            boolean setWritablePermissionResult = file.setWritable(true, false);
            boolean setReadablePermissionResult = file.setReadable(true, false);
            logger.debug(file.getAbsolutePath() + " permission status:" +
                    System.lineSeparator() +
                    " Executable:" + setExecutablePermissionResult +
                    System.lineSeparator() +
                    " Writable:" + setWritablePermissionResult +
                    System.lineSeparator() +
                    " Readable:" + setReadablePermissionResult
            );
        } catch (Exception exception) {
            logger.warn("Set permission failed : " + file.getAbsolutePath(), exception);
        }
    }

    /**
     * Gets available iron pdf engine file.
     *
     * @return the available iron pdf engine file
     */
    private static Optional<File> getAvailableIronPdfEngineFile() {

        Path binFromWorkingDir = getIronPdfEngineExecutablePath(ironPdfEngineWorkingDirectory);
        try {
            if (Files.exists(binFromWorkingDir)) {
                logger.info("IronPdfEngine found at: " + binFromWorkingDir);
                return Optional.of(binFromWorkingDir.toFile());
            }
        } catch (Exception ignored) {
        }
        logger.debug("IronPdfEngine not found at IronPdfEngine working directory: " + binFromWorkingDir.toAbsolutePath());

        //check for ironpdf-engine package
        try {
            String engineResourceClassName = "com.ironsoftware.ironpdf.internal.EngineResource" + currentOsFullName() + currentOsArch();
            try (InputStream inputStream = Class.forName(engineResourceClassName)
                    .getResourceAsStream("/" + getIronPdfEngineFolderName() + ".zip")) {

                Path zipFilePath = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(), Setting_Api.getIronPdfEngineFolderName() + ".zip");
                copyInputStreamToFile(inputStream, zipFilePath.toFile());

                Path unzipTargetPath = Paths.get(ironPdfEngineWorkingDirectory.toAbsolutePath().toString(), Setting_Api.getIronPdfEngineFolderName() + "/");
                logInfoOrSystemOut(logger, "Unzipping IronPdfEngine (from dependency) to dir: " + unzipTargetPath);

                Access.unzip(zipFilePath.toAbsolutePath().toString(),
                        unzipTargetPath.toAbsolutePath().toString());
                Files.deleteIfExists(zipFilePath.toAbsolutePath());
            }

            if (Files.exists(binFromWorkingDir)) {
                logger.info(
                        "IronPdfEngine found (extracted from ironpdf-engine package) (EngineResource) at: " + binFromWorkingDir);
                return Optional.of(binFromWorkingDir.toFile());
            }
        } catch (ClassNotFoundException ignored) {
            //it is ok if engineResourceClassName not exists (e.g. ironpdf-engine-linux-x64 package)
            logger.debug("Cannot detect IronPdfEngine from ironpdf-engine package, skipped");
        } catch (Exception e) {
            logger.debug("IronPdfEngine from ironpdf-engine package not found", e);
        }

        //backup by look at System.getProperty("user.dir")
        Path binFromUserDir = getIronPdfEngineExecutablePath(Paths.get(System.getProperty("user.dir")));
        try {
            if (Files.exists(binFromUserDir)) {
                logger.info("IronPdfEngine found at: " + binFromUserDir);
                return Optional.of(binFromUserDir.toFile());
            }
        } catch (Exception ignored) {
        }
        logger.debug("IronPdfEngine not found at: " + "(System.getProperty(\"user.dir\")): " + binFromUserDir.toAbsolutePath());

        //backup by look at current dir
        Path binFromCurrentDir = getIronPdfEngineExecutablePath(Paths.get("."));
        try {
            if (Files.exists(binFromCurrentDir)) {
                logger.info("IronPdfEngine found at: " + binFromCurrentDir);
                return Optional.of(binFromCurrentDir.toFile());
            }
        } catch (Exception ignored) {
        }
        logger.debug("IronPdfEngine not found at current dir: " + binFromCurrentDir.toAbsolutePath());


        return Optional.empty();
    }
}
