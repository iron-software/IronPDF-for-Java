package com.ironsoftware.ironpdf.render;
 
/**
 * Specifies the linearization strategy to use when producing a linearized
 * (web-optimized) PDF.
 *
 * <p>Different linearization modes balance disk I/O requirements and system resource
 * constraints. Choose the mode that best fits your use case and system environment.</p>
 *
 * <p>Mirrors {@code IronPdf.LinearizationMode} on the C# side.</p>
 */
public enum LinearizationMode {
    /**
     * Automatically selects the optimal linearization method based on system capabilities.
     * This is the default and recommended option for most scenarios.
     *
     * <p><b>Behavior:</b></p>
     * <ol>
     *   <li>First attempts {@link #FileBased} linearization.</li>
     *   <li>If disk access is restricted (e.g., limited permissions, sandboxed environment),
     *       automatically falls back to {@link #InMemory}.</li>
     * </ol>
     *
     * <p><b>When to Use:</b> Recommended for most applications where you want optimal
     * linearization but need to handle varying system environments gracefully.</p>
     */
    Automatic,
 
    /**
     * Performs linearization entirely in memory without any disk I/O operations.
     *
     * <p><b>Characteristics:</b></p>
     * <ul>
     *   <li><b>Speed:</b> Fastest linearization method</li>
     *   <li><b>Security:</b> No temporary files written to disk</li>
     *   <li><b>Disk I/O:</b> Zero disk access required</li>
     * </ul>
     *
     * <p><b>When to Use:</b></p>
     * <ul>
     *   <li>Compliance requirements mandate zero disk I/O (e.g., HIPAA, PCI-DSS)</li>
     *   <li>Processing sensitive documents that must not touch the filesystem</li>
     *   <li>Sandboxed or restricted environments without disk write permissions</li>
     *   <li>Cloud-based environments where disk I/O introduces latency</li>
     * </ul>
     */
    InMemory,
 
    /**
     * Performs linearization using temporary file operations on disk.
     *
     * <p><b>Characteristics:</b></p>
     * <ul>
     *   <li><b>Disk I/O:</b> Requires writing temporary files to the system temp directory</li>
     * </ul>
     *
     * <p><b>System Requirements:</b> Requires write permissions to the system temporary directory.</p>
     */
    FileBased
}