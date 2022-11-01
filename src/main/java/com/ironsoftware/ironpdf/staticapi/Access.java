package com.ironsoftware.ironpdf.staticapi;

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

    static synchronized RpcClient EnsureConnection() throws IOException {
        if (client != null) {
            return client;
        }

        StartServer();

        if (channel == null) {
            channel = ManagedChannelBuilder.forAddress(Setting_Api.SubProcessIp,
                    Setting_Api.SubProcessPort).usePlaintext().build();
        }

        RpcClient newClient = new RpcClient(
                IronPdfServiceGrpc.newBlockingStub(channel),
                IronPdfServiceGrpc.newFutureStub(channel),
                IronPdfServiceGrpc.newStub(channel));

        logger.debug("Handshaking, Expected IronPdfEngine Version : " + Setting_Api.IronPdfEngineVersion);

        HandshakeRequest.Builder handshakeRequest_firstTry = HandshakeRequest.newBuilder();
        handshakeRequest_firstTry.setExpectedVersion(Setting_Api.IronPdfEngineVersion);

        HandshakeResponse res_firstTry = newClient.BlockingStub.handshake(
                handshakeRequest_firstTry.build());

        logger.debug("Handshake result:" + res_firstTry);

        switch (res_firstTry.getResultOrExceptionCase()) {
            case SUCCESS:
                client = newClient;
                return client;
            case REQUIREDVERSION:

                logger.error("Mismatch IronPdfEngine version expected: "
                        + Setting_Api.IronPdfEngineVersion + " but found:" + res_firstTry);
                //todo download new Binary
                if (tryAgain) {
                    StopIronPdfEngine();
                    DownloadIronPdfEngine();
                    EnsureConnection();
                    tryAgain = false;
                }
            case EXCEPTION:
                throw new IOException(res_firstTry.getException().getMessage());
            default:
                throw new IOException("Unexpected result from handshake");
        }
    }

    static synchronized void DownloadIronPdfEngine() {
        try {
            if (isTryDownloaded) {
                return;
            }
            Path zipFilePath = Paths.get(Setting_Api.DefaultPathToIronPdfEngineFolder + ".zip");
            logger.info("Download IronPdfEngine to default dir: " + zipFilePath.toAbsolutePath());
            isTryDownloaded = true;
            Files.copy(
                    new URL("https://ironpdfengine.azurewebsites.net/api/IronPdfEngineDownload?version="
                            + Setting_Api.IronPdfEngineVersion +
                            "&platform=" + Setting_Api.CurrentOsFullName() +
                            "&architect=" + Setting_Api.CurrentOsArch()).openStream(),
                    zipFilePath,
                    StandardCopyOption.REPLACE_EXISTING);
            logger.info(
                    "Unzipping IronPdfEngine to dir: " + Setting_Api.IronPdfEngineFolder.toAbsolutePath());
            unzip(zipFilePath.toAbsolutePath().toString(),
                    Setting_Api.IronPdfEngineFolder.toAbsolutePath().toString());
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
                    //noinspection ResultOfMethodCallIgnored
                    entryDestination.mkdirs();
                } else {
                    //noinspection ResultOfMethodCallIgnored
                    entryDestination.getParentFile().mkdirs();
                    try (InputStream in = zipFile.getInputStream(entry);
                         OutputStream out = Files.newOutputStream(entryDestination.toPath())) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        }
    }

    static synchronized void StartServer() throws IOException {
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
                cmdList.add(String.format("port=%1$s", Setting_Api.SubProcessPort));
                cmdList.add(String.format("enable_debug=%1$s", Setting_Api.EnableDebug));

                if (!Utils_StringHelper.isNullOrWhiteSpace(Setting_Api.LicenseKey)) {
                    cmdList.add(String.format("license_key=%1$s", Setting_Api.LicenseKey));
                }

                ProcessBuilder pb = new ProcessBuilder(cmdList);
                Process proc = pb.start();

                CatchServerErrors(proc);

                //This addShutdownHook will run when ...
                // * When all of JVM threads have completed execution
                // * System.exit()
                // * CTRL-C
                // * System level shutdown or User Log-Off
                //BUT IT WILL ***NOT*** RUN WHEN
                // * VM crashes
                // * OS kill command
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    ShutdownChanel();
                    logger.info("Killing IronPdfEngine process");
                    proc.destroy();
                }));

                ironPdfProcess = proc;

            } else {
                logger.info(
                        "Cannot find IronPdfEngine at: " + Setting_Api.getDefaultIronPdfEnginePath()
                                .toAbsolutePath());
                if (tryAgain) {
                    DownloadIronPdfEngine();
                    logger.info(
                            "Try to start IronPdfEngine again from: " + Setting_Api.getDefaultIronPdfEnginePath()
                                    .toAbsolutePath());
                    tryAgain = false;
                    StartServer();
                } else {
                    throw new RuntimeException(String.format("Cannot locate IronPdfEngine. at %1$s",
                            Setting_Api.getDefaultIronPdfEnginePath().toAbsolutePath()));
                }

            }
        } catch (RuntimeException e) {
            throw new RuntimeException(String.format("Cannot start IronPdfEngine at %1$s.",
                    Setting_Api.getDefaultIronPdfEnginePath()), e);
        }
    }

    private static void CatchServerErrors(Process proc) {

        stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        Thread threadInput = new Thread(() -> {
            // Read the output from the command
            if (Setting_Api.EnableDebug)
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
            if (Setting_Api.EnableDebug)
            {
                stdError.lines().forEach(line -> logger.error("[IronPdfEngine][Error]" + line));
            }
        });

        //Note: Whenever the last non-daemon thread terminates, all the daemon threads will be terminated automatically.
        threadError.setDaemon(true);
        threadError.start();
    }

    private static void ShutdownChanel() {
        if (channel != null && !channel.isShutdown() && !channel.isTerminated()) {
            channel.shutdown();
            channel = null;
        }
    }

    /**
     * Stop IronPdfEngine process
     */
    public static void StopIronPdfEngine() {
        ShutdownChanel();

        if (ironPdfProcess != null) {
            ironPdfProcess.destroy();
        }
        ironPdfProcess = null;

        client = null;
    }
}
