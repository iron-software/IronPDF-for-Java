package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.IronPdfEngineConnection;

/**
 * Internal static API for IronPdfEngine lifecycle management.
 *
 * <p>Public bridge between the user-facing {@code com.ironsoftware.ironpdf}
 * package and the package-private {@link Access} class</p>
 */
public final class Engine_Api {

    private Engine_Api() {
    }

    /**
     * @see com.ironsoftware.ironpdf.IronPdfEngineManager#isEngineActive()
     * @return true if the engine is reachable and responsive
     */
    public static boolean isEngineActive() {
        return Access.isIronPdfEngineActive();
    }

    /**
     * @see com.ironsoftware.ironpdf.IronPdfEngineManager#startEngine()
     */
    public static void startEngine() {
        Access.ensureConnection();
    }

    /**
     * @see com.ironsoftware.ironpdf.IronPdfEngineManager#stopEngine()
     */
    public static void stopEngine() {
        rejectCustomMode("stop");
        Access.stopIronPdfEngine();
    }

    /**
     * @see com.ironsoftware.ironpdf.IronPdfEngineManager#restartEngine()
     */
    public static void restartEngine() {
        rejectCustomMode("restart");
        Access.restartIronPdfEngine();
    }

    /**
     * Guards lifecycle operations that tear the channel down.
     *
     * <p>In {@code CUSTOM} mode the gRPC channel is supplied by the caller and
     * stored once (see {@link IronPdfEngineConnection#withCustomGrpcConnection}).
     * IronPDF cannot rebuild it: after {@code stop}/{@code restart} shuts that
     * channel down, the connection cannot be re-established. Rather than fail with
     * an opaque connect-timeout, reject the operation up front and let the caller
     * manage the lifecycle of their own channel.</p>
     */
    private static void rejectCustomMode(String action) {
        if (!Setting_Api.useDeprecatedConnectionSettings
                && Setting_Api.connectionMode != null
                && Setting_Api.connectionMode.getMode() == IronPdfEngineConnection.ConnectionMode.CUSTOM) {
            throw new UnsupportedOperationException(
                    "Cannot " + action + " IronPdfEngine in CUSTOM connection mode: the gRPC channel is "
                            + "supplied by you and IronPDF cannot rebuild it once it is shut down. "
                            + "Manage the lifecycle of your custom channel yourself.");
        }
    }
}
