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
     * Returns true if pdfium-side image re-encoding is engaged (because a
     * positive {@code targetImageDpi} was set). The qpdf {@code optimizeImages}
     * step is skipped in that case to avoid double JPEG encoding.
     */
    public boolean pdfiumWillReEncode() {
        return targetImageDpi != null && targetImageDpi > 0;
    }
}
