package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.exception.IronPdfLicensingException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EngineManagerTests extends TestBase {

    /**
     * Test 01: startEngine() brings the engine up and isEngineActive() reports true.
     */
    @Test
    public final void Test01_StartEngine_ReportsActive() {
        IronPdfEngineManager.startEngine();
        Assertions.assertTrue(IronPdfEngineManager.isEngineActive(),
                "engine should be active after startEngine()");
        System.out.println("Test01 (PDF-2144): engine active after startEngine");
    }

    /**
     * Test 02: stopEngine() makes the engine report inactive.
     */
    @Test
    public final void Test02_StopEngine_ReportsInactive() {
        IronPdfEngineManager.startEngine();
        Assertions.assertTrue(IronPdfEngineManager.isEngineActive());

        IronPdfEngineManager.stopEngine();
        Assertions.assertFalse(IronPdfEngineManager.isEngineActive(),
                "engine should be inactive after stopEngine()");
        System.out.println("Test02 (PDF-2144): engine inactive after stopEngine");
    }

    /**
     * Test 03: restartEngine() recovers an interrupted engine.
     * This is the core acceptance criterion. After the engine is stopped
     * (simulating an external interruption), restartEngine() makes it active again.
     */
    @Test
    public final void Test03_RestartEngine_RecoversAfterStop() {
        IronPdfEngineManager.startEngine();

        // Simulate the engine being interrupted externally.
        IronPdfEngineManager.stopEngine();
        Assertions.assertFalse(IronPdfEngineManager.isEngineActive());

        // Recover.
        IronPdfEngineManager.restartEngine();
        Assertions.assertTrue(IronPdfEngineManager.isEngineActive(),
                "engine should be active after restartEngine()");
        System.out.println("Test03 (PDF-2144): engine recovered via restartEngine");
    }

    /**
     * Test 04: restartEngine() is also a valid cold start (no prior connection).
     */
    @Test
    public final void Test04_RestartEngine_ColdStart() {
        IronPdfEngineManager.stopEngine();
        IronPdfEngineManager.restartEngine();
        Assertions.assertTrue(IronPdfEngineManager.isEngineActive(),
                "engine should be active after a cold restartEngine()");
        System.out.println("Test04 (PDF-2144): cold restartEngine brought engine up");
    }

    /**
     * Test 05: startEngine() is idempotent. Calling it on a healthy engine is a no-op.
     */
    @Test
    public final void Test05_StartEngine_Idempotent() {
        IronPdfEngineManager.startEngine();
        IronPdfEngineManager.startEngine();
        Assertions.assertTrue(IronPdfEngineManager.isEngineActive(),
                "engine should remain active after repeated startEngine()");
        System.out.println("Test05 (PDF-2144): startEngine idempotent");
    }

    /**
     * Test 06: end-to-end. After a restart, the engine actually processes a render
     * request. A licensing exception is acceptable here (no valid license is
     * configured in this environment); it still proves the restarted engine is
     * reachable and handling requests.
     */
    @Test
    public final void Test06_RenderWorksAfterRestart() {
        IronPdfEngineManager.restartEngine();
        Assertions.assertTrue(IronPdfEngineManager.isEngineActive());

        try {
            PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>after restart</h1>");
            AssertNotNullOrEmpty(pdf.getBinaryData());
            System.out.println("Test06 (PDF-2144): render succeeded after restart");
        } catch (IronPdfLicensingException licensing) {
            // Engine responded with a license error -> it is alive and serving.
            System.out.println("Test06 (PDF-2144): restarted engine reachable "
                    + "(render reported a licensing error, as expected without a valid key)");
        }
    }

    /**
     * Test 07: in CUSTOM connection mode, stopEngine()/restartEngine() reject the
     * call with UnsupportedOperationException instead of shutting down the
     * user-owned channel (which IronPDF cannot rebuild). startEngine() and
     * isEngineActive() remain available in all modes.
     */
    @Test
    public final void Test07_CustomMode_StopAndRestart_Throw() {
        IronPdfEngineConnection previous = Settings.getConnectionMode();
        // A real channel is required by withCustomGrpcConnection; the guard rejects
        // the operation before the channel is ever used, so it stays a lazy no-op.
        ManagedChannel customChannel =
                ManagedChannelBuilder.forAddress("localhost", 33399).usePlaintext().build();
        try {
            Settings.setConnectionMode(
                    IronPdfEngineConnection.configure().withCustomGrpcConnection(customChannel));

            Assertions.assertThrows(UnsupportedOperationException.class,
                    IronPdfEngineManager::stopEngine,
                    "stopEngine() must be rejected in CUSTOM mode");
            Assertions.assertThrows(UnsupportedOperationException.class,
                    IronPdfEngineManager::restartEngine,
                    "restartEngine() must be rejected in CUSTOM mode");

            // isEngineActive() is allowed in CUSTOM mode and must not throw.
            Assertions.assertDoesNotThrow(IronPdfEngineManager::isEngineActive,
                    "isEngineActive() must remain available in CUSTOM mode");

            System.out.println("Test07 (PDF-2144): CUSTOM mode stop/restart rejected with UnsupportedOperationException");
        } finally {
            customChannel.shutdownNow();
            // Restore the prior connection mode so the shared static engine state
            // does not leak into other tests.
            Settings.setConnectionMode(
                    previous != null ? previous : IronPdfEngineConnection.configure().withSubprocess());
        }
    }
}
