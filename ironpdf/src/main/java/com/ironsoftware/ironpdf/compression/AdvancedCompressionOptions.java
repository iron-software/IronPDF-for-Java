package com.ironsoftware.ironpdf.compression;

/**
 * Configuration for the advanced compression pipeline.
 *
 * <p>Recommended starting points:</p>
 * <pre>{@code
 * // Web/email — strongest size reduction
 * AdvancedCompressionOptions web = new AdvancedCompressionOptions();
 * web.setJpegQuality(70);
 * web.setTargetImageDpi(150);
 * web.setRemoveStructureTree(true);
 *
 * // Print quality
 * AdvancedCompressionOptions print = new AdvancedCompressionOptions();
 * print.setJpegQuality(90);
 * print.setTargetImageDpi(300);
 * }</pre>
 *
 * <p>Mirrors the .NET {@code AdvancedCompressionOptions} class shipped in
 * IronPdf 2026.6.</p>
 */
public class AdvancedCompressionOptions {

    private Integer jpegQuality = null;
    private Integer targetImageDpi = 150;
    private boolean highQualityImageSubsampling = true;
    private boolean removeStructureTree = false;
    private boolean compressStreams = true;
    private boolean recompressFlate = true;
    private int compressionLevel = 9;
    private ObjectStreamMode objectStreams = ObjectStreamMode.GENERATE;
    private boolean removeUnreferencedResources = true;
    private boolean coalesceContents = true;
    private boolean decodeGeneralizedStreams = true;
    private int optimizeImagesMinWidth = 0;
    private int optimizeImagesMinHeight = 0;
    private int optimizeImagesMinArea = 0;
    private BitonalCompressionMode bitonalMode = BitonalCompressionMode.OFF;
    private Integer bitonalResolutionDpi = null;
    private Integer bitonalThreshold = null;

    /**
     * JPEG quality used when re-encoding images during optimization (1-100).
     * When set, automatically enables image optimization.
     *
     * <p><strong>Recommended settings:</strong></p>
     * <ul>
     *     <li>{@code null} — No image re-encoding (default)</li>
     *     <li>{@code 95} — Archival, minimal artifacts</li>
     *     <li>{@code 85} — High quality</li>
     *     <li>{@code 70} — Balanced, good for general use</li>
     *     <li>{@code 50} — Web/email; visible artifacts, smaller files</li>
     * </ul>
     *
     * <p><strong>Where the re-encoding happens depends on {@link #getTargetImageDpi()}:</strong></p>
     * <ul>
     *     <li>{@code targetImageDpi} set (default {@code 150}): each image is downsampled and re-encoded
     *         at this quality. {@code optimizeImages} is skipped to avoid double JPEG encoding.</li>
     *     <li>{@code targetImageDpi = null}: {@code optimizeImages} re-encodes images at this quality
     *         without changing pixel dimensions.</li>
     * </ul>
     */
    public Integer getJpegQuality() { return jpegQuality; }

    public AdvancedCompressionOptions setJpegQuality(Integer jpegQuality) {
        this.jpegQuality = jpegQuality;
        return this;
    }

    /**
     * Target DPI for image downsampling during advanced compression.
     * Images whose effective rendered DPI exceeds this value are box-filter
     * downsampled to {@code targetImageDpi} before re-encoding.
     *
     * <p><strong>Recommended values:</strong></p>
     * <ul>
     *     <li>{@code 300} — Print quality, lossless to the eye at normal viewing distance</li>
     *     <li>{@code 200} — Crisp on high-DPI / retina screens</li>
     *     <li>{@code 150} — Default; best size/quality balance for screen + email</li>
     *     <li>{@code 96} — Aggressive; soft text but still readable</li>
     *     <li>{@code null} — Disabled; preserve original resolution (largest file)</li>
     * </ul>
     *
     * <p>Lossy. Once an image is downsampled it cannot be restored to its
     * original resolution.</p>
     */
    public Integer getTargetImageDpi() { return targetImageDpi; }

    public AdvancedCompressionOptions setTargetImageDpi(Integer targetImageDpi) {
        this.targetImageDpi = targetImageDpi;
        return this;
    }

    /** 4:4:4 chroma subsampling when true (better color), 4:1:1 when false (smaller). */
    public boolean isHighQualityImageSubsampling() { return highQualityImageSubsampling; }

    public AdvancedCompressionOptions setHighQualityImageSubsampling(boolean v) {
        this.highQualityImageSubsampling = v;
        return this;
    }

    /** Remove the document structure tree before compression. */
    public boolean isRemoveStructureTree() { return removeStructureTree; }

    public AdvancedCompressionOptions setRemoveStructureTree(boolean v) {
        this.removeStructureTree = v;
        return this;
    }

    /** Compress content streams using zlib/Flate. */
    public boolean isCompressStreams() { return compressStreams; }

    public AdvancedCompressionOptions setCompressStreams(boolean v) {
        this.compressStreams = v;
        return this;
    }

    /** Re-compress already Flate-encoded streams using {@link #getCompressionLevel()}. */
    public boolean isRecompressFlate() { return recompressFlate; }

    public AdvancedCompressionOptions setRecompressFlate(boolean v) {
        this.recompressFlate = v;
        return this;
    }

    /** zlib compression level (0-9). Default 9. */
    public int getCompressionLevel() { return compressionLevel; }

    public AdvancedCompressionOptions setCompressionLevel(int v) {
        this.compressionLevel = v;
        return this;
    }

    /** Object stream mode used when writing the output PDF. */
    public ObjectStreamMode getObjectStreams() { return objectStreams; }

    public AdvancedCompressionOptions setObjectStreams(ObjectStreamMode v) {
        this.objectStreams = v;
        return this;
    }

    /** Drop indirect objects that are not referenced from the document catalog. */
    public boolean isRemoveUnreferencedResources() { return removeUnreferencedResources; }

    public AdvancedCompressionOptions setRemoveUnreferencedResources(boolean v) {
        this.removeUnreferencedResources = v;
        return this;
    }

    /** Merge a page's separate content streams so they Flate-compress together. */
    public boolean isCoalesceContents() { return coalesceContents; }

    public AdvancedCompressionOptions setCoalesceContents(boolean v) {
        this.coalesceContents = v;
        return this;
    }

    /** Decode generalized filters (FlateDecode, LZW, ASCII85, ASCIIHex) before re-encoding. */
    public boolean isDecodeGeneralizedStreams() { return decodeGeneralizedStreams; }

    public AdvancedCompressionOptions setDecodeGeneralizedStreams(boolean v) {
        this.decodeGeneralizedStreams = v;
        return this;
    }

    /** Minimum image width (in pixels) for image optimization to apply. */
    public int getOptimizeImagesMinWidth() { return optimizeImagesMinWidth; }

    public AdvancedCompressionOptions setOptimizeImagesMinWidth(int v) {
        this.optimizeImagesMinWidth = v;
        return this;
    }

    /** Minimum image height (in pixels) for image optimization to apply. */
    public int getOptimizeImagesMinHeight() { return optimizeImagesMinHeight; }

    public AdvancedCompressionOptions setOptimizeImagesMinHeight(int v) {
        this.optimizeImagesMinHeight = v;
        return this;
    }

    /** Minimum image area (width × height pixels) for image optimization to apply. */
    public int getOptimizeImagesMinArea() { return optimizeImagesMinArea; }

    public AdvancedCompressionOptions setOptimizeImagesMinArea(int v) {
        this.optimizeImagesMinArea = v;
        return this;
    }

    /**
     * Bitonal (1-bit CCITT Group 4) compression mode for scanned / black-and-white content.
     *
     * @return the bitonal mode; {@link BitonalCompressionMode#OFF} by default
     */
    public BitonalCompressionMode getBitonalMode() { return bitonalMode; }

    /**
     * Opt-in bitonal (1-bit CCITT Group 4) compression. When not {@link BitonalCompressionMode#OFF},
     * eligible images are thresholded to pure black/white and CCITT Group 4 encoded, which is far
     * smaller than JPEG on scanned text (and sharper). The bitonal result is only kept when it is
     * actually smaller than the original; images that are already bitonal are left untouched.
     *
     * <p>Note: when {@link #getBitonalResolutionDpi()} is unset the bitonal pass falls back to
     * {@link #getTargetImageDpi()} (default 150), so setting only this resamples a 300 DPI scan to
     * 150 DPI before thresholding. Set a bitonal resolution, or set {@code targetImageDpi} to
     * {@code null}, to threshold at the source resolution.</p>
     *
     * <p>Enabling bitonal also runs the image pass for images that do not qualify (or whose CCITT
     * encoding is not smaller): those go through the normal JPEG pass at {@link #getJpegQuality()}
     * (85 when unset), and a re-encode is kept only when it is smaller than the original. This matches
     * .NET. Use it on documents that are mostly black-and-white scans.</p>
     *
     * @param bitonalMode the bitonal mode (non-null)
     * @return this instance
     * @throws IllegalArgumentException if {@code bitonalMode} is null
     */
    public AdvancedCompressionOptions setBitonalMode(BitonalCompressionMode bitonalMode) {
        if (bitonalMode == null) {
            throw new IllegalArgumentException("bitonalMode must not be null.");
        }
        this.bitonalMode = bitonalMode;
        return this;
    }

    /**
     * Target DPI for bitonal images when bitonal mode is enabled.
     *
     * @return the bitonal DPI, or {@code null} (default) to fall back to {@link #getTargetImageDpi()}
     */
    public Integer getBitonalResolutionDpi() { return bitonalResolutionDpi; }

    /**
     * Target DPI for bitonal images when bitonal mode is enabled; images above it are downsampled
     * before thresholding. Wins over {@link #getTargetImageDpi()} when set; when {@code null} the
     * bitonal pass falls back to {@code targetImageDpi}, and when both are {@code null} the source
     * resolution is kept. A value {@code <= 0} is treated as unset. No effect when bitonal is off.
     *
     * @param bitonalResolutionDpi the DPI, or {@code null}
     * @return this instance
     */
    public AdvancedCompressionOptions setBitonalResolutionDpi(Integer bitonalResolutionDpi) {
        this.bitonalResolutionDpi = bitonalResolutionDpi;
        return this;
    }

    /**
     * Luminance threshold used to split pixels into black/white when bitonal mode is enabled.
     *
     * @return the threshold, or {@code null} (default) for the automatic (Otsu) threshold
     */
    public Integer getBitonalThreshold() { return bitonalThreshold; }

    /**
     * Luminance threshold (1-255) splitting pixels into black/white when bitonal mode is enabled.
     * {@code null} (default) auto-computes an optimal per-image threshold (Otsu's method,
     * recommended). A value of 0 or outside 1-255 falls back to auto, since a cutoff of 0 would make
     * every pixel white.
     *
     * @param bitonalThreshold the threshold, or {@code null}
     * @return this instance
     */
    public AdvancedCompressionOptions setBitonalThreshold(Integer bitonalThreshold) {
        this.bitonalThreshold = bitonalThreshold;
        return this;
    }

    /**
     * Returns true if pdfium-side image re-encoding is engaged (because a
     * positive {@code targetImageDpi} was set). The qpdf {@code optimizeImages}
     * step is skipped in that case to avoid double JPEG encoding.
     */
    public boolean pdfiumWillReEncode() {
        return targetImageDpi != null && targetImageDpi > 0;
    }

    /**
     * Returns true if the pdfium image pass runs at all: a positive {@code targetImageDpi}, or
     * bitonal mode on. (Mirrors .NET, which widened only this run condition for bitonal; the qpdf
     * flags still key off {@link #pdfiumWillReEncode()}.)
     */
    public boolean pdfiumImagePassWillRun() {
        return pdfiumWillReEncode() || bitonalMode != BitonalCompressionMode.OFF;
    }
}
