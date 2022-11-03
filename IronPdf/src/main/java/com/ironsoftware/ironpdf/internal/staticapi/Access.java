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
            throw new RuntimeException("Failed to download IronPdfEngine binary: " + e);
        }
    }

    private static void unzip(String zipFilePath, String destDir) throws IOException {
        try (java.util.zip.ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    boolean ignored = entryDestination.mkdirs();
                } else {
                    boolean ignored = entryDestination.getParentFile().mkdirs();
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
                    Arrays.stream(Objects.requireNonNull(selectedFile.get().getParentFile().listFiles()))
                            .forEach(file -> {
                                if (file.setExecutable(true)) {
                                    logger.debug("Set executable of " + file.getName() + " success");
                                } else {
                                    logger.debug("Set executable of " + file.getName() + " failed");
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<String> cmdList = new ArrayList<>();

                cmdList.add(selectedFile.get().toPath().toAbsolutePath().toString());
                cmdList.add(String.format("port=%1$s", Setting_Api.subProcessPort));
                cmdList.add(String.format("enable_debug=%1$s", Setting_Api.enableDebug));

                if (!Utils_StringHelper.isNullOrWhiteSpace(Setting_Api.licenseKey)) {
                    cmdList.add(String.format("license_key=%1$s", Setting_Api.licenseKey));
                }

                ProcessBuilder pb = new ProcessBuilder(cmdList);
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
            throw new RuntimeException(String.format("Cannot start IronPdfEngine at %1$s.",
                    Setting_Api.getDefaultIronPdfEnginePath()), e);
        }
    }

    private static void catchServerErrors(Process proc) {

        stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        Thread threadInput = new Thread(() -> {
            // Read the output from the command
            if (Setting_Api.enableDebug)
            {
                stdInput.lines().forEach(line -> logger.info("[IronPdfEngine]" + line));
            }
        });
        //Note: Whenever the last non-daemon thread terminates, all the daemon threads will be terminated automatically.
        threadInput.setDaemon(true);
        threadInput.start();

        stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        Thread threadError = new Thread(() -> {
            // Read the error from the command
            if (Setting_Api.enableDebug)
            {
                stdError.lines().forEach(line -> logger.error("[IronPdfEngine][Error]" + line));
            }
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
}
