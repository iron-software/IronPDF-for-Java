package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.Engine_Api;

/**
 * Manages the lifecycle of the {@code IronPdfEngine}.
 *
 * <p>Normally the engine is started automatically on the first IronPDF call and
 * stopped when your application shuts down, so most applications never need this
 * class. It exists for long-running services that must <strong>recover</strong>
 * after the engine is interrupted by something outside IronPDF's control; for
 * example a remote IronPdfEngine host restart, an OS {@code kill}, or a native
 * crash. After such an event the cached connection is stale and subsequent
 * IronPDF calls hang or fail; {@link #restartEngine()} clears that state and
 * reconnects.</p>
 *
 * <p>A typical watchdog loop:</p>
 * <pre>{@code
 * if (!IronPdfEngineManager.isEngineActive()) {
 *     IronPdfEngineManager.restartEngine();
 * }
 * PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>Hello World</h1>");
 * }</pre>
 *
 * <p>All methods are static and thread-safe.</p>
 */
public final class IronPdfEngineManager {

    private IronPdfEngineManager() {
    }

    /**
     * Checks whether IronPdfEngine is currently reachable and responsive.
     *
     * <p>Against a running engine this is a lightweight, fast check: a single
     * 5s-deadline handshake against the existing connection. It will
     * <strong>not</strong> start the engine, so calling it before the first
     * IronPDF call (or after {@link #stopEngine()}) returns {@code false}.</p>
     *
     * <p>Note: this method shares a lock with {@link #startEngine()}/
     * {@link #restartEngine()}, so if a connect or restart is in progress on
     * another thread, the check waits for that operation to finish first.</p>
     *
     * @return {@code true} if a connection exists and the engine answers within
     * the deadline; {@code false} if the engine was never started, has been
     * stopped, or is unresponsive (crashed / killed / host restarted).
     */
    public static boolean isEngineActive() {
        return Engine_Api.isEngineActive();
    }

    /**
     * Ensures IronPdfEngine is started and connected.
     *
     * <p>Equivalent to what happens implicitly on the first IronPDF call: in
     * {@code SUBPROCESS} mode it launches the local engine (downloading the
     * binaries if necessary); in remote modes it connects and handshakes. If a
     * healthy connection already exists this is a no-op.</p>
     *
     * <p>Note: this does not recover a stale connection left behind by a crashed
     * engine. Use {@link #restartEngine()} for recovery.</p>
     */
    public static void startEngine() {
        Engine_Api.startEngine();
    }

    /**
     * Stops IronPdfEngine and releases the connection.
     *
     * <p>In {@code SUBPROCESS} mode the local engine process is terminated. In
     * remote modes the gRPC channel is closed but the remote server is left
     * running. The engine will be started again automatically on the next
     * IronPDF call, or explicitly via {@link #startEngine()}.</p>
     *
     * @throws UnsupportedOperationException in {@code CUSTOM} connection mode,
     * where the gRPC channel is owned by the caller and cannot be rebuilt once
     * shut down. Manage the lifecycle of your custom channel yourself.
     */
    public static void stopEngine() {
        Engine_Api.stopEngine();
    }

    /**
     * Restarts IronPdfEngine by stopping it (if running) and establishing a
     * fresh connection.
     *
     * <p>This is the recommended recovery action when {@link #isEngineActive()}
     * reports {@code false} on a previously working engine. It fully resets the
     * cached connection state, which a plain IronPDF call would not do, so a
     * dead subprocess is relaunched and a restarted remote host is re-handshaked.</p>
     *
     * @throws UnsupportedOperationException in {@code CUSTOM} connection mode,
     * where the gRPC channel is owned by the caller and cannot be rebuilt once
     * shut down. Manage the lifecycle of your custom channel yourself.
     */
    public static void restartEngine() {
        Engine_Api.restartEngine();
    }
}
