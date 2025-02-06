package com.ironsoftware.ironpdf.signature;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

/**
 * A class that represents a PDF signing certificate (.PFX or .p12) format which can be used to digitally sign a
 * PDF. This protecting it from alteration.
 */
public class Signature {

    int internalIndex = -1;

    private byte[] certificateRawData;

    /**
     * The date and time of the digital signature. If left null, the signature will be timestamped at the
     * millisecond that the PdfDocument is saved to Disk or Stream.
     */
    private Instant signatureDate = null;

    /**
     * A visual image for the sign, often a PNG of a human signature or company stamp (optional). <p>This
     * appends a visual signature in addition to  cryptographic signing.</p>
     */
    private byte[] signatureImage = null;

    private Rectangle signatureImageRectangle = null;

    /**
     * The contact person or email address for signing related inquiries (optional).
     */
    private String signingContact = null;

    /**
     * The physical location the PDF was signed (optional).
     */
    private String signingLocation = null;

    /**
     * The reason the PDF was signed (optional).
     */
    private String signingReason = null;

    private String password = null;

    /**
     * Url to be used when time-stamping the signature
     */
    private String timeStampUrl = null;

    /**
     * Initializes a new instance of the {@link Signature} class using a .pfx or .p12 digital signature
     * file.
     *
     * @param filePathToCertificate The file path to certificate.
     * @param password              The certificate's password.
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public Signature(String filePathToCertificate, String password) throws IOException {
        this(Files.readAllBytes(Paths.get(filePathToCertificate)), password);
    }

    /**
     * Initializes a new instance of the {@link Signature} class using a .pfx or .p12 digital signature
     * file.
     *
     * @param filePathToCertificate The file path to certificate.
     * @param password              The certificate's password.
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public Signature(Path filePathToCertificate, String password) throws IOException {
        this(Files.readAllBytes(filePathToCertificate), password);
    }

    /**
     * Initializes a new instance of the {@link Signature} class.
     *
     * @param certificateRawData The certificate as a binary data (byte array).
     * @param password           The certificate's password.
     */
    public Signature(byte[] certificateRawData, String password) {

        this.certificateRawData = certificateRawData;
        this.password = password;
    }

    /**
     * Initializes a new instance of the {@link Signature} class.
     *
     * @param certificateRawData the certificate as a binary data (byte array).
     * @param password           the certificate's password.
     * @param signatureDate      the date and time of the digital signature. If left null, the signature will be timestamped at the
     *                           millisecond that the PdfDocument is saved to Disk or Stream.
     * @param signatureImage     a visual image for the sign, often a PNG of a human signature or company stamp (optional). <p>This
     *                           appends a visual signature in addition to  cryptographic signing.</p>
     * @param signingContact     the contact person or email address for signing related inquiries (optional).
     * @param signingLocation    the physical location the PDF was signed (optional).
     * @param signingReason      the reason the PDF was signed (optional).
     */
    public Signature(byte[] certificateRawData, String password, Instant signatureDate, byte[] signatureImage,
                     String signingContact, String signingLocation, String signingReason, Rectangle signatureImageRectangle) {

        this.certificateRawData = certificateRawData;
        this.password = password;
        this.signatureDate = signatureDate;
        this.signatureImage = signatureImage;
        this.signingContact = signingContact;
        this.signingLocation = signingLocation;
        this.signingReason = signingReason;
        this.signatureImageRectangle = signatureImageRectangle;
    }

    /**
     * Get certificate raw data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getCertificateRawData() {
        return certificateRawData;
    }

    /**
     * Sets certificate raw data.
     *
     * @param certificateRawData the certificate raw data
     */
    public void setCertificateRawData(byte[] certificateRawData) {
        this.certificateRawData = certificateRawData;
    }

    /**
     * Gets signature date. The date and time of the digital signature. If left null, the signature will be timestamped at the
     * millisecond that the PdfDocument is saved to Disk or Stream.
     *
     * @return the signature date
     */
    public Instant getSignatureDate() {
        return signatureDate;
    }

    /**
     * Sets signature date. The date and time of the digital signature. If left null, the signature will be timestamped at the
     * millisecond that the PdfDocument is saved to Disk or Stream.
     *
     * @param signatureDate the signature date
     */
    public void setSignatureDate(Instant signatureDate) {
        this.signatureDate = signatureDate;
    }

    /**
     * Get signature image. A visual image for the sign, often a PNG of a human signature or company stamp (optional). <p>This
     * appends a visual signature in addition to  cryptographic signing.</p>
     *
     * @return the image byte[]
     */
    public byte[] getSignatureImage() {
        return signatureImage;
    }

    /**
     * Get signature image Rectangle. A position of visual image for the sign</p>
     *
     * @return Rectangle
     */
    public Rectangle getSignatureImageRectangle() {
        return signatureImageRectangle;
    }

    /**
     * Set signature image Rectangle. A position of visual image for the sign</p>
     *
     * @return Rectangle
     */
    public void setSignatureImageRectangle(Rectangle rectangle) {
        this.signatureImageRectangle = rectangle;
    }

    /**
     * Sets signature image. A visual image for the sign, often a PNG of a human signature or company stamp (optional). <p>This
     * appends a visual signature in addition to  cryptographic signing.</p>
     *
     * @param signatureImage the signature image
     * @param rectangle Rectangle to display image
     */
    public void setSignatureImage(byte[] signatureImage, Rectangle rectangle) {
        this.signatureImage = signatureImage;
        this.signatureImageRectangle = rectangle;
    }

    /**
     * Gets signing contact. The contact person or email address for signing related inquiries (optional).
     *
     * @return the signing contact
     */
    public String getSigningContact() {
        return signingContact;
    }

    /**
     * Sets signing contact. The contact person or email address for signing related inquiries (optional).
     *
     * @param signingContact the signing contact
     */
    public void setSigningContact(String signingContact) {
        this.signingContact = signingContact;
    }

    /**
     * Gets signing location. The physical location the PDF was signed (optional).
     *
     * @return the signing location
     */
    public String getSigningLocation() {
        return signingLocation;
    }

    /**
     * Sets signing location. The physical location the PDF was signed (optional).
     *
     * @param signingLocation the signing location
     */
    public void setSigningLocation(String signingLocation) {
        this.signingLocation = signingLocation;
    }

    /**
     * Gets signing reason. The reason the PDF was signed (optional).
     *
     * @return the signing reason
     */
    public String getSigningReason() {
        return signingReason;
    }

    /**
     * Sets signing reason. The reason the PDF was signed (optional).
     *
     * @param signingReason the signing reason
     */
    public void setSigningReason(String signingReason) {
        this.signingReason = signingReason;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Gets Url to be used when time-stamping the signature
     *
     * @return the timestamp url
     */
    public String getTimeStampUrl() {
        return timeStampUrl;
    }

    /**
     * Sets Url to be used when time-stamping the signature
     *
     * @param timeStampUrl the timestamp url
     */
    public void setTimeStampUrl(String timeStampUrl) {
        this.timeStampUrl = timeStampUrl;
    }

    /**
     * Internal using only
     */
    public int getInternalIndex() {
        return internalIndex;
    }

}
