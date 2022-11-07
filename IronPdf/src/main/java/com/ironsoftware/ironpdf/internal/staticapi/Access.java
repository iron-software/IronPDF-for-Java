package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.HandshakeRequest;
import com.ironsoftware.ironpdf.internal.proto.HandshakeResponse;
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
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

    static synchronized RpcClient ensureConnection() {
        if (client != null) {
            return client;
        }

        startServer();

        if (channel == null) {
            channel = ManagedChannelBuilder.forAddress(Setting_Api.subProcessIp,
                    Setting_Api.subProcessPort).usePlaintext().build();
        }

        RpcClient newClient = new RpcClient(
                IronPdfServiceGrpc.newBlockingStub(channel),
                IronPdfServiceGrpc.newFutureStub(channel),
                IronPdfServiceGrpc.newStub(channel));

        logger.debug("Handshaking, Expected IronPdfEngine Version : " + Setting_Api.IRON_PDF_ENGINE_VERSION);

        HandshakeRequest.Builder handshakeRequest_firstTry = HandshakeRequest.newBuilder();
        handshakeRequest_firstTry.setExpectedVersion(Setting_Api.IRON_PDF_ENGINE_VERSION);

        HandshakeResponse res_firstTry = newClient.blockingStub.handshake(
                handshakeRequest_firstTry.build());

        logger.debug("Handshake result:" + res_firstTry);

        switch (res_firstTry.getResultOrExceptionCase()) {
            case SUCCESS:
                client = newClient;
                return client;
            case REQUIREDVERSION:

                logger.error("Mismatch IronPdfEngine version expected: "
                        + Setting_Api.IRON_PDF_ENGINE_VERSION + " but found:" + res_firstTry);
                //todo download new Binary
                if (tryAgain) {
                    stopIronPdfEngine();
                    downloadIronPdfEngine();
                    Setting_Api.subProcessPort = Setting_Api.findFreePort();
                    ensureConnection();
                    tryAgain = false;
                }
            case EXCEPTION:
                throw Exception_Converter.fromProto(res_firstTry.getException());
            default:
                throw new RuntimeException("Unexpected result from handshake");
        }
    }

    static synchronized void downloadIronPdfEngine() {
        try {
            if (isTryDownloaded) {
                return;
            }
            Path zipFilePath = Paths.get(Setting_Api.defaultPathToIronPdfEngineFolder + ".zip");
            logger.info("Download IronPdfEngine to default dir: " + zipFilePath.toAbsolutePath());
            isTryDownloaded = true;
            Files.copy(
                    new URL("https://ironpdfengine.azurewebsites.net/api/IronPdfEngineDownload?version="
                            + Setting_Api.IRON_PDF_ENGINE_VERSION +
                            "&platform=" + Setting_Api.currentOsFullName() +
                            "&architect=" + Setting_Api.currentOsArch()).openStream(),
                    zipFilePath,
                    StandardCopyOption.REPLACE_EXISTING);
            logger.info(
                    "Unzipping IronPdfEngine to dir: " + Setting_Api.ironPdfEngineFolder.toAbsolutePath());
            unzip(zipFilePath.toAbsolutePath().toString(),
                    Setting_Api.ironPdfEngineFolder.toAbsolutePath().toString());
            logger.info("Delete zip file: " + zipFilePath.toAbsolutePath());
            Files.deleteIfExists(zipFilePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to download IronPdfEngine binary", e);
            throw new RuntimeException("Failed to download IronPdfEngine binary", e);
        }
    }

    private static void unzip(String zipFilePath, String destDir) throws IOException {
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
            Optional<File> selectedFile = Setting_Api.getAvailableIronPdfEngineFile();
            if (selectedFile.isPresent()) {
                logger.info("Using IronPdfEngine from: " + selectedFile.get().getAbsolutePath());

                try {
                    logger.info("Try Setting IronPdfEngine permission...");
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
                cmdList.add(String.format("port=%1$s", Setting_Api.subProcessPort));
                cmdList.add(String.format("enable_debug=%1$s", Setting_Api.enableDebug));

                if (!Utils_StringHelper.isNullOrWhiteSpace(Setting_Api.licenseKey)) {
                    cmdList.add(String.format("license_key=%1$s", Setting_Api.licenseKey));
                }

                ProcessBuilder pb = new ProcessBuilder(cmdList);

                logger.info("Start IronPdfEngine");

                Process proc = pb.start();
                catchServerErrors(proc);

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
                    logger.info("Killing IronPdfEngine process");
                    proc.destroy();
                }));

                //wait for IronPdfEngine finish initialize
                Thread.sleep(1000);
                ironPdfProcess = proc;

            } else {
                logger.info(
                        "Cannot find IronPdfEngine at: " + Setting_Api.getDefaultIronPdfEnginePath()
                                .toAbsolutePath());
                if (tryAgain) {
                    downloadIronPdfEngine();
                    logger.info(
                            "Try to start IronPdfEngine again from: " + Setting_Api.getDefaultIronPdfEnginePath()
                                    .toAbsolutePath());
                    tryAgain = false;
                    startServer();
                } else {
                    throw new RuntimeException(String.format("Cannot locate IronPdfEngine. at %1$s",
                            Setting_Api.getDefaultIronPdfEnginePath().toAbsolutePath()));
                }

            }
        } catch (Exception e) {
            logger.error(String.format("Cannot start IronPdfEngine at %1$s.",
                    Setting_Api.getDefaultIronPdfEnginePath()), e);
            throw new RuntimeException(String.format("Cannot start IronPdfEngine at %1$s.",
                    Setting_Api.getDefaultIronPdfEnginePath()), e);
        }
    }

    private static void catchServerErrors(Process proc) {

        stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        Thread threadInput = new Thread(() -> {
            engineLogger.info("[IronPdfEngine] Start");
            // Read the output from the command
            stdInput.lines().forEach(line -> {
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
                if (Setting_Api.enableDebug)
                    engineLogger.error("[IronPdfEngine][Error]" + line);
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
}
