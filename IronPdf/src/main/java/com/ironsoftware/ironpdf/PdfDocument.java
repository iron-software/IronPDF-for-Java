package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.annotation.AnnotationManager;
import com.ironsoftware.ironpdf.attachment.AttachmentManager;
import com.ironsoftware.ironpdf.bookmark.BookmarkManager;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.form.FormManager;
import com.ironsoftware.ironpdf.headerfooter.HeaderFooterOptions;
import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import com.ironsoftware.ironpdf.image.DrawImageOptions;
import com.ironsoftware.ironpdf.image.ImageBehavior;
import com.ironsoftware.ironpdf.image.ToImageOptions;
import com.ironsoftware.ironpdf.internal.staticapi.*;
import com.ironsoftware.ironpdf.metadata.MetadataManager;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;
import com.ironsoftware.ironpdf.render.ChromeHttpLoginCredentials;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.render.PaperSize;
import com.ironsoftware.ironpdf.security.SecurityManager;
import com.ironsoftware.ironpdf.signature.SignatureManager;
import com.ironsoftware.ironpdf.stamp.HorizontalAlignment;
import com.ironsoftware.ironpdf.stamp.HtmlStamper;
import com.ironsoftware.ironpdf.stamp.Stamper;
import com.ironsoftware.ironpdf.stamp.VerticalAlignment;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a PDF document. Allows: loading, editing, manipulating, merging, signing printing and saving PDFs.
 */
public class PdfDocument implements Printable {
    /**
     * The Logger.
     */
    static final Logger logger = LoggerFactory.getLogger(PdfDocument.class);
    private final InternalPdfDocument internalPdfDocument;

    //region Constructor
    private final BookmarkManager bookmarkManager;
    private final MetadataManager metadataManager;
    private final AnnotationManager annotationManager;
    private final FormManager formManager;
    private final AttachmentManager attachmentManager;
    private final SecurityManager securityManager;
    private final SignatureManager signatureManager;

    private PdfDocument(InternalPdfDocument static_pdfDocument) {
        internalPdfDocument = static_pdfDocument;
        bookmarkManager = new BookmarkManager(this.internalPdfDocument);
        metadataManager = new MetadataManager(this.internalPdfDocument);
        annotationManager = new AnnotationManager(this.internalPdfDocument);
        formManager = new FormManager(this.internalPdfDocument);
        attachmentManager = new AttachmentManager(this.internalPdfDocument);
        securityManager = new SecurityManager(this.internalPdfDocument);
        signatureManager = new SignatureManager(this.internalPdfDocument);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @param password    Optional user password if the PDF document is encrypted.
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public PdfDocument(Path pdfFilePath, String password) throws IOException {
        this(pdfFilePath, password, "");
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath   The PDF file path.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,                      modifying restrictions etc..)
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public PdfDocument(Path pdfFilePath, String password, String ownerPassword) throws IOException {
        this(Files.readAllBytes(pdfFilePath), password, ownerPassword);
    }


    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfData       The PDF file data as byte array.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,                      modifying restrictions etc..)
     */
    public PdfDocument(byte[] pdfData, String password, String ownerPassword) {
        this(PdfDocument_Api.fromBytes(pdfData,
                password, ownerPassword));
    }


    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @throws IOException the io exception
     */
    public PdfDocument(Path pdfFilePath) throws IOException {
        this(pdfFilePath, "", "");
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfData  The PDF file data as byte array.
     * @param password Optional user password if the PDF document is encrypted.
     */
    public PdfDocument(byte[] pdfData, String password) {
        this(pdfData, password, "");
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfData The PDF file data as byte array.
     */
    public PdfDocument(byte[] pdfData) {
        this(pdfData, "", "");
    }


    //endregion

    // region StaticFactory

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @param password    Optional user password if the PDF document is encrypted.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public static PdfDocument fromFile(Path pdfFilePath, String password) throws IOException {
        return fromFile(pdfFilePath, password, "");
    }


    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath   The PDF file path.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,                      modifying restrictions etc..)
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public static PdfDocument fromFile(Path pdfFilePath, String password, String ownerPassword)
            throws IOException {
        return new PdfDocument(pdfFilePath, password, ownerPassword);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     * @throws IOException if an I/O error occurs reading from the stream
     */
    public static PdfDocument fromFile(Path pdfFilePath) throws IOException {
        return fromFile(pdfFilePath, "", "");
    }


    /**
     * Converts a single image file to an identical PDF document of matching dimensions.
     * <p>The default PaperSize is A4.</p>
     * <p>Note:  Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.</p>
     *
     * @param imagesPath A list of file path of the image file.
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or served over the web.
     */
    public static PdfDocument fromImage(List<Path> imagesPath) {

        return fromImage(imagesPath, ImageBehavior.CENTERED_ON_PAGE);
    }

    /**
     * Converts a single image file to an identical PDF document of matching dimensions.
     * <p>The default PaperSize is A4. You can set it via ImageToPdfConverter.PaperSize.</p>
     * <p>Note:  Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.</p>
     *
     * @param imagesPath A list of file path of the image file.
     * @param paperSize  A target paper size. Default is A4.
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or served over the web.
     */
    public static PdfDocument fromImage(List<Path> imagesPath, PaperSize paperSize) {

        return fromImage(imagesPath, ImageBehavior.CENTERED_ON_PAGE, paperSize);
    }

    /**
     * Converts a single image file to an identical PDF document of matching dimensions.
     * <p>The default PaperSize is A4.</p>
     * <p>Note:  Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.</p>
     *
     * @param imagesPath    A list of file path of the image file.
     * @param imageBehavior Describes how image should be placed on the PDF page
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or served over the web.
     */
    public static PdfDocument fromImage(List<Path> imagesPath, ImageBehavior imageBehavior) {
        return fromImage(imagesPath, imageBehavior, (ChromePdfRenderOptions) null);
    }

    /**
     * Converts a single image file to an identical PDF document of matching dimensions.
     * <p>Note:  Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.</p>
     *
     * @param imagesPath    A list of file path of the image file.
     * @param imageBehavior Describes how image should be placed on the PDF page.
     * @param paperSize     A target paper size. Default is A4.
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or served over the web.
     */
    public static PdfDocument fromImage(List<Path> imagesPath, ImageBehavior imageBehavior, PaperSize paperSize) {
        ChromePdfRenderOptions renderOption = new ChromePdfRenderOptions();
        renderOption.setPaperSize(paperSize);
        return fromImage(imagesPath, imageBehavior, renderOption);
    }


    /**
     * Converts a single image file to an identical PDF document of matching dimensions.
     * <p>The default PaperSize is A4. You can set it via {@link ChromePdfRenderOptions#setPaperSize(PaperSize)}.</p>
     * <p>Note:  Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.</p>
     *
     * @param imagesPath    A list of file path of the image file.
     * @param imageBehavior Describes how image should be placed on the PDF page.
     * @param renderOptions Rendering options.
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or served over the web.
     */
    public static PdfDocument fromImage(List<Path> imagesPath, ImageBehavior imageBehavior,
                                        ChromePdfRenderOptions renderOptions) {

       List<Image_Api.ImageData> imageDataList =  imagesPath.stream().map(x -> {
                    try {
                        return new Image_Api.ImageData(Files.readAllBytes(x),FilenameUtils.getExtension(x.toAbsolutePath().toString()) );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }}).collect(Collectors.toList());

        return new PdfDocument(Image_Api.imageToPdf(imageDataList, imageBehavior, renderOptions));
    }


    //endregion

    //region Managers

    /**
     * Gets the BookmarkManager for this PDF document. BookmarkManager allows reading, removing and editing of bookmarks from the PDF outline.
     * * <p>See: {@link BookmarkManager}.</p>
     *
     * @return the BookmarkManager
     */
    public BookmarkManager getBookmark() {
        return bookmarkManager;
    }

    /**
     * Gets the metadata manager for this PDF document. MetadataManager allows metadata such as Author etc. to be read and set.
     *
     * <p>See: {@link MetadataManager}.
     *
     * @return the MetadataManager
     */
    public MetadataManager getMetadata() {
        return metadataManager;
    }

    /**
     * Gets the annotation manager for this PDF document.  AnnotationManager allows annotation objects to be edited.
     * <p>See: {@link AnnotationManager}.
     *
     * @return the AnnotationManager
     */
    public AnnotationManager getAnnotation() {
        return annotationManager;
    }

    /**
     * Gets the form manager for this PDF document. FormManager allows AcroForm fields to be read and set.
     * <p>See: {@link FormManager}.
     *
     * @return the FormManager
     */
    public FormManager getForm() {
        return formManager;
    }

    /**
     * Gets the attachment manager for this PDF document. AttachmentManager allows attachment objects to be edited.
     * <p>See: {@link AttachmentManager}.
     *
     * @return the AttachmentManager
     */
    public AttachmentManager getAttachment() {
        return attachmentManager;
    }

    /**
     * Gets security.
     *
     * @return the SecurityManager
     */
    public SecurityManager getSecurity() {
        return securityManager;
    }

    /**
     * Gets signature.
     *
     * @return the SignatureManager
     */
    public SignatureManager getSignature() {
        return signatureManager;
    }

    //endregion

    //region PageOperations

    /**
     * Static method that joins (concatenates) 2 PDF documents together into one PDF document.
     * <p>If the second PDF contains form fields, the resulting PDF's form fields  will be appended with an origin index number.  e.g. 'Name' from the first PDF will become 'Name_0' </p>
     *
     * @param A A PDF
     * @param B A Seconds PDF
     * @return A new, merged {@link PdfDocument}
     */
    public static PdfDocument merge(PdfDocument A, PdfDocument B) {
        return merge(Arrays.asList(A, B));
    }

    /**
     * Static method that joins (concatenates) 2 PDF documents together into one PDF document.
     * <p>If the second PDF contains form fields, the resulting PDF's form fields  will be appended with an origin index number.  e.g. 'Name' from the first PDF will become 'Name_0' </p>
     *
     * @param Documents A List of PdfDocument.  To merge existing PDF files you may use the                  PdfDocument.FromFile static method in conjunction with Merge.
     * @return A new, merged {@link PdfDocument}
     */
    public static PdfDocument merge(List<PdfDocument> Documents) {
        return new PdfDocument(Page_Api.mergePage(
                Documents.stream().map(x -> x.internalPdfDocument).collect(Collectors.toList())));
    }

    /**
     * Creates a new PDF by copying a page from this PdfDocument into a new blank document.
     *
     * @param PageIndex Index of the page.  Note: Page 1 has index 0...
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument copyPage(int PageIndex) {
        return copyPages(new ArrayList<>(Collections.singletonList(PageIndex)));
    }

    /**
     * Creates a new PDF by copying a page from this PdfDocument into a new blank document.
     *
     * @param PageIndexes An Iterable of page indexes to copy into the new PDF.
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument copyPages(java.lang.Iterable<Integer> PageIndexes) {
        return new PdfDocument(
                Page_Api.copyPage(internalPdfDocument, PageIndexes));
    }

    /**
     * Creates a new PDF by copying a range of pages from this {@link PdfDocument} into a new blank document.
     *
     * @param StartIndex The index of the first PDF page to copy. Note: Page 1 has index 0
     * @param EndIndex   The index of the last PDF page to copy.
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument copyPages(int StartIndex, int EndIndex) {
        ArrayList<Integer> pageIndexes = new ArrayList<>();
        for (int i = StartIndex; i <= EndIndex; i++) {
            pageIndexes.add(i);
        }

        return copyPages(pageIndexes);
    }

    /**
     * Appends another PDF to the end of the current {@link PdfDocument}. <p>If AnotherPdfFile contains
     * form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name' will become  'Name_'</p>
     *
     * @param AnotherPdfFile PdfDocument to append.
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument appendPdf(PdfDocument AnotherPdfFile) {
        Page_Api.appendPdf(this.internalPdfDocument, AnotherPdfFile.internalPdfDocument);
        return this;
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index.
     * <p> If AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name' will be 'Name_' </p>
     *
     * @param AnotherPdfFile Another PdfDocument...
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument insertPdf(PdfDocument AnotherPdfFile) {
        return insertPdf(AnotherPdfFile, 0);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index.
     * <p> If AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name' will be 'Name_' </p>
     *
     * @param AnotherPdfFile Another PdfDocument.
     * @param AtIndex        Index at which to insert the new content.  Note: Page 1 has index 0...
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument insertPdf(PdfDocument AnotherPdfFile, int AtIndex) {
        Page_Api.insertPage(internalPdfDocument, AnotherPdfFile.internalPdfDocument, AtIndex);
        return this;
    }

    /**
     * Adds another PDF to the beginning of the current PdfDocument.
     * <p> If AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name' will be 'Name_' </p>
     *
     * @param AnotherPdfFile PdfDocument to prepend.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument prependPdf(PdfDocument AnotherPdfFile) {
        Page_Api.insertPage(internalPdfDocument, AnotherPdfFile.internalPdfDocument, 0);
        return this;
    }

    /**
     * Removes a range of pages from the PDF
     *
     * @param pageSelection The selected page index(es). Default is all pages.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument removePages(PageSelection pageSelection) {
        Page_Api.removePage(internalPdfDocument, internalPdfDocument.getPageList(pageSelection));
        return this;
    }

    /**
     * Gets a list of information about pages in this PDF as a List of {@link PageInfo}.
     *
     * @return A list of information about the PDF’s pages.
     */
    public final List<PageInfo> getPagesInfo() {
        return Page_Api.getPagesInfo(internalPdfDocument);
    }

    /**
     * Gets a Map of information a selection of  pages in this PDF as a List of {@link PageInfo}.
     *
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return a map of page index and page information.
     */
    public final Map<Integer, PageInfo> getPagesInfo(PageSelection pageSelection) {
        List<PageInfo> pagesInfo = Page_Api.getPagesInfo(
                internalPdfDocument);
        return internalPdfDocument.getPageList(pageSelection).stream().collect(Collectors.toMap(x -> x, pagesInfo::get));
    }

    /**
     * Rotates all pages of the PdfDocument by a specified number of degrees.
     *
     * @param rotation Degrees of rotation. May be 0,90,180 or 270
     */
    public final void rotateAllPages(PageRotation rotation) {
        Page_Api.setPageRotation(internalPdfDocument, rotation);
    }

    /**
     * Rotates a selection page of the PdfDocument by a specified number of degrees.
     *
     * @param pageRotation  Degrees of rotation. May be 0,90,180 or 270
     * @param pageSelection Selected page indexes. Default is all pages.
     */
    public final void rotatePage(PageRotation pageRotation, PageSelection pageSelection) {
        Page_Api.setPageRotation(internalPdfDocument, pageRotation,
                internalPdfDocument.getPageList(pageSelection));
    }

    /**
     Resize a page to the specified dimensions (in millimeters)

     @param pageWidth Desired page width, in millimeters
     @param pageHeight Desired page height, in millimeters
     @param pageSelection Selected page indexes.
     */
    public final void resizePage(double pageWidth, double pageHeight, PageSelection pageSelection){
        internalPdfDocument.getPageList(pageSelection).forEach(x->{
            Page_Api.resizePage(internalPdfDocument, pageWidth, pageHeight, x);
        });
    }

    //endregion

    // region BackgroundForeground

    /**
     * Adds a background to each page of this PDF. The background is copied from a first page in the
     * backgroundPdf document.
     *
     * @param backgroundPdf The background PDF document.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addBackgroundPdf(PdfDocument backgroundPdf) {
        return this.addBackgroundPdf(backgroundPdf, 0, PageSelection.allPages());
    }

    /**
     * Adds a background to each page of this PDF. The background is copied from a selected
     * page in the backgroundPdf document.
     *
     * @param backgroundPdf          The background PDF document.
     * @param backgroundPdfPageIndex Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addBackgroundPdf(PdfDocument backgroundPdf, int backgroundPdfPageIndex) {
        return this.addBackgroundPdf(backgroundPdf, backgroundPdfPageIndex, PageSelection.allPages());
    }

    /**
     * Adds a background to selected page(s) of this PDF. The background is copied from a selected
     * page in the backgroundPdf document.
     *
     * @param backgroundPdf The background PDF document.
     * @param pageSelection PageSelection to which the background/foreground will be added. Default is                      PageSelection.AllPages().
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addBackgroundPdf(PdfDocument backgroundPdf, PageSelection pageSelection) {
        BackgroundForeground_Api.addBackground(
                this.internalPdfDocument,
                backgroundPdf.internalPdfDocument,
                internalPdfDocument.getPageList(pageSelection));
        return this;
    }


    /**
     * Adds a background to selected page(s) of this PDF. The background is copied from a selected
     * page in the backgroundPdf document.
     *
     * @param backgroundPdf          The background PDF document.
     * @param backgroundPdfPageIndex Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     * @param pageSelection          PageSelection to which the background/foreground will be added. Default is                               PageSelection.AllPages().
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addBackgroundPdf(PdfDocument backgroundPdf, int backgroundPdfPageIndex,
                                              PageSelection pageSelection) {
        BackgroundForeground_Api.addBackground(
                this.internalPdfDocument,
                backgroundPdf.internalPdfDocument,
                internalPdfDocument.getPageList(pageSelection),
                backgroundPdfPageIndex);
        return this;
    }

    /**
     * Adds a foreground to each page of this PDF. The foreground is copied from a first page of
     * the foregroundPdf document.
     *
     * @param foregroundPdf The foreground PDF document.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addForegroundPdf(PdfDocument foregroundPdf) {
        return addForegroundPdf(foregroundPdf, 0, PageSelection.allPages());
    }

    /**
     * Adds a foreground to each page of this PDF. The foreground is copied from a selected
     * page in the foregroundPdf document.
     *
     * @param foregroundPdf          The foreground PDF document.
     * @param foregroundPdfPageIndex Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addForegroundPdf(PdfDocument foregroundPdf, int foregroundPdfPageIndex) {
        return this.addForegroundPdf(foregroundPdf, foregroundPdfPageIndex, PageSelection.allPages());
    }

    /**
     * Adds a foreground to selected page(s) of this PDF. The foreground is copied from a selected
     * page in the foregroundPdf document.
     *
     * @param foregroundPdf The foreground PDF document.
     * @param pageSelection PageSelection to which the background/foreground will be added. Default is                      PageSelection.AllPages().
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addForegroundPdf(PdfDocument foregroundPdf,
                                              PageSelection pageSelection) {
        return this.addForegroundPdf(foregroundPdf, 0, pageSelection);
    }

    /**
     * Adds a foreground to selected page(s) of this PDF. The foreground is copied from a selected
     * page in the foregroundPdf document.
     *
     * @param foregroundPdf          The foreground PDF document.
     * @param foregroundPdfPageIndex Index (zero-based page number) of the page to copy from the Background/Foreground PDF. Default is 0.
     * @param pageSelection          PageSelection to which the background/foreground will be added. Default is                               PageSelection.AllPages().
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument addForegroundPdf(PdfDocument foregroundPdf, int foregroundPdfPageIndex,
                                              PageSelection pageSelection) {
        BackgroundForeground_Api.addForeground(
                this.internalPdfDocument,
                foregroundPdf.internalPdfDocument,
                internalPdfDocument.getPageList(pageSelection),
                foregroundPdfPageIndex);
        return this;
    }

    //endregion

    //region HeadersAndFooters

    /**
     * Renders TEXT page headers onto an existing PDF File
     *
     * @param header A new instance of IronPdf.TextHeaderFooter that defines the header content and               layout.
     * @return the pdf document
     */
    public PdfDocument addTextHeader(TextHeaderFooter header) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addTextHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page headers onto an existing PDF File
     *
     * @param header        A new instance of IronPdf.TextHeaderFooter that defines the header content and                      layout.
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addTextHeader(TextHeaderFooter header, PageSelection pageSelection) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addTextHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page headers onto an existing PDF File
     *
     * @param header              A new instance of IronPdf.TextHeaderFooter that defines the header                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @return the pdf document
     */
    public PdfDocument addTextHeader(TextHeaderFooter header, HeaderFooterOptions headerFooterOptions) {
        HeaderFooter_Api.addTextHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page headers onto an existing PDF File
     *
     * @param header              A new instance of IronPdf.TextHeaderFooter that defines the header                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @param pageSelection       Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addTextHeader(TextHeaderFooter header, HeaderFooterOptions headerFooterOptions, PageSelection pageSelection) {
        HeaderFooter_Api.addTextHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page footers onto an existing PDF File
     *
     * @param footer A new instance of IronPdf.TextHeaderFooter that defines the footer content and               layout.
     * @return the pdf document
     */
    public PdfDocument addTextFooter(TextHeaderFooter footer) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addTextFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page footers onto an existing PDF File
     *
     * @param footer        A new instance of IronPdf.TextHeaderFooter that defines the footer content and                      layout.
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addTextFooter(TextHeaderFooter footer, PageSelection pageSelection) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addTextFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page footer onto an existing PDF File
     *
     * @param footer              A new instance of IronPdf.TextHeaderFooter that defines the footer                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @return the pdf document
     */
    public PdfDocument addTextFooter(TextHeaderFooter footer, HeaderFooterOptions headerFooterOptions) {
        HeaderFooter_Api.addTextFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page footer onto an existing PDF File
     *
     * @param footer              A new instance of IronPdf.TextHeaderFooter that defines the footer                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @param pageSelection       Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addTextFooter(TextHeaderFooter footer, HeaderFooterOptions headerFooterOptions, PageSelection pageSelection) {
        HeaderFooter_Api.addTextFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders HTML page headers onto an existing PDF File
     *
     * @param header A new instance of IronPdf.HtmlHeaderFooter that defines the header content and               layout.
     * @return the pdf document
     */
    public PdfDocument addHtmlHeader(HtmlHeaderFooter header) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addHtmlHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page headers onto an existing PDF File
     *
     * @param header        A new instance of IronPdf.HtmlHeaderFooter that defines the header content and                      layout.
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addHtmlHeader(HtmlHeaderFooter header, PageSelection pageSelection) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addHtmlHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page headers onto an existing PDF File
     *
     * @param header              A new instance of IronPdf.HtmlHeaderFooter that defines the header                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @return the pdf document
     */
    public PdfDocument addHtmlHeader(HtmlHeaderFooter header, HeaderFooterOptions headerFooterOptions) {
        HeaderFooter_Api.addHtmlHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page headers onto an existing PDF File
     *
     * @param header              A new instance of IronPdf.HtmlHeaderFooter that defines the header                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @param pageSelection       Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addHtmlHeader(HtmlHeaderFooter header, HeaderFooterOptions headerFooterOptions, PageSelection pageSelection) {
        HeaderFooter_Api.addHtmlHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }


    /**
     * Renders HTML page footers onto an existing PDF File
     *
     * @param footer A new instance of IronPdf.HtmlHeaderFooter that defines the footer content and               layout.
     * @return the pdf document
     */
    public PdfDocument addHtmlFooter(HtmlHeaderFooter footer) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addHtmlFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page footers onto an existing PDF File
     *
     * @param footer        A new instance of IronPdf.HtmlHeaderFooter that defines the footer content and                      layout.
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addHtmlFooter(HtmlHeaderFooter footer, PageSelection pageSelection) {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        HeaderFooter_Api.addHtmlFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page footer onto an existing PDF File
     *
     * @param footer              A new instance of IronPdf.HtmlHeaderFooter that defines the footer                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @return the pdf document
     */
    public PdfDocument addHtmlFooter(HtmlHeaderFooter footer, HeaderFooterOptions headerFooterOptions) {
        HeaderFooter_Api.addHtmlHeader(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(PageSelection.allPages()),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page footer onto an existing PDF File
     *
     * @param footer              A new instance of IronPdf.HtmlHeaderFooter that defines the footer                            content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     * @param pageSelection       Selected page indexes. Default is all pages.
     * @return the pdf document
     */
    public PdfDocument addHtmlFooter(HtmlHeaderFooter footer, HeaderFooterOptions headerFooterOptions, PageSelection pageSelection) {
        HeaderFooter_Api.addHtmlHeader(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                internalPdfDocument.getPageList(pageSelection),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }
    //endregion

    //region Imaging

    /**
     * Draws an image onto the PDF document
     *
     * @param imagePath The image file path.
     * @param option    the option
     * @throws IOException the io exception
     */
    public final void drawImage(Path imagePath, DrawImageOptions option) throws IOException {
        Image_Api.drawImage(internalPdfDocument,
                Files.readAllBytes(imagePath), internalPdfDocument.getPageList(option.getPageSelection()), option.getX(),
                option.getY(),
                option.getWidth(), option.getHeight());
    }

    /**
     * Draws an image onto the PDF document
     *
     * @param imageBytes image byte array
     * @param option     the option
     */
    public final void drawImage(byte[] imageBytes, DrawImageOptions option) {
        Image_Api.drawImage(internalPdfDocument,
                imageBytes, internalPdfDocument.getPageList(option.getPageSelection()), option.getX(), option.getY(),
                option.getWidth(), option.getHeight());
    }

    /**
     * Rasterizes (renders) the PDF into BufferedImage objects.  1 BufferedImage for each page.
     *
     * @return An array of BufferedImage objects.
     * @throws IOException the io exception
     */
    public final List<BufferedImage> toBufferedImages() throws IOException {
        return toBufferedImages(new ToImageOptions());
    }

    /**
     * Rasterizes (renders) the PDF into BufferedImage objects.  1 BufferedImage for each page.
     *
     * @param options The {@link ToImageOptions}
     * @return An array of BufferedImage objects.
     * @throws IOException the io exception
     */
    public final List<BufferedImage> toBufferedImages(ToImageOptions options) throws IOException {
        return toBufferedImages(options, PageSelection.allPages());
    }

    /**
     * Rasterizes (renders) the PDF into BufferedImage objects.  1 BufferedImage for each page.
     *
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return An array of BufferedImage objects.
     * @throws IOException the io exception
     */
    public final List<BufferedImage> toBufferedImages(PageSelection pageSelection) throws IOException {
        return toBufferedImages(new ToImageOptions(), pageSelection);
    }

    /**
     * Rasterizes (renders) the PDF into BufferedImage objects.  1 BufferedImage for each page.
     *
     * @param options       The {@link ToImageOptions}
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return An array of BufferedImage objects.
     * @throws IOException the io exception
     */
    public final List<BufferedImage> toBufferedImages(ToImageOptions options, PageSelection pageSelection) throws IOException {
        return Image_Api.pdfToImage(internalPdfDocument,
                        internalPdfDocument.getPageList(pageSelection),
                        options.getDpi(), options.getImageMaxWidth(), options.getImageMaxHeight()).stream()
                .map(bytes -> {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    try {
                        return ImageIO.read(byteArrayInputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }


    /**
     * Renders the pages of the PDF as PNG (Portable Network Graphic) files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_pages_*.png
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toPngImages(String fileNamePattern)
            throws IOException {
        return this.toPngImages(fileNamePattern, new ToImageOptions(), PageSelection.allPages());
    }

    /**
     * Renders the pages of the PDF as PNG (Portable Network Graphic) files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_pages_*.png
     * @param options         The {@link ToImageOptions}
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toPngImages(String fileNamePattern, ToImageOptions options)
            throws IOException {
        return this.toPngImages(fileNamePattern, options, PageSelection.allPages());
    }

    /**
     * Renders the pages of the PDF as PNG (Portable Network Graphic) files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_pages_*.png
     * @param pageSelection   Selected page indexes. Default is all pages.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toPngImages(String fileNamePattern, PageSelection pageSelection)
            throws IOException {
        return this.toPngImages(fileNamePattern, new ToImageOptions(), pageSelection);
    }

    /**
     * Renders the pages of the PDF as PNG (Portable Network Graphic) files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_pages_*.png
     * @param options         The {@link ToImageOptions}
     * @param pageSelection   Selected page indexes. Default is all pages.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toPngImages(String fileNamePattern, ToImageOptions options, PageSelection pageSelection)
            throws IOException {
        return toImages(fileNamePattern, "png", options, pageSelection);
    }

    /**
     * Renders the pages of the PDF as specific image files type and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @param imageFileType   a specific image file type without dot.  E.g.  "jpg", "png", "bmp",                        "gif", "tiff"
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public List<String> toImages(String fileNamePattern, String imageFileType)
            throws IOException {
        return this.toImages(fileNamePattern, imageFileType, new ToImageOptions());
    }

    /**
     * Renders the pages of the PDF as specific image files type and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @param imageFileType   a specific image file type without dot.  E.g.  "jpg", "png", "bmp",                        "gif", "tiff"
     * @param options         The {@link ToImageOptions}
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public List<String> toImages(String fileNamePattern, String imageFileType, ToImageOptions options)
            throws IOException {
        return this.toImages(fileNamePattern, imageFileType, options, PageSelection.allPages());
    }

    /**
     * Renders the pages of the PDF as specific image files type and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @param imageFileType   a specific image file type without dot.  E.g.  "jpg", "png", "bmp",                        "gif", "tiff"
     * @param pageSelection   Selected page indexes. Default is all pages.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public List<String> toImages(String fileNamePattern, String imageFileType, PageSelection pageSelection)
            throws IOException {
        return this.toImages(fileNamePattern, imageFileType, new ToImageOptions(), pageSelection);
    }

    /**
     * Renders the pages of the PDF as specific image files type and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @param imageFileType   a specific image file type without dot.  E.g.  "jpg", "png", "bmp",                        "gif", "tiff"
     * @param options         The {@link ToImageOptions}
     * @param pageSelection   Selected page indexes. Default is all pages.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public List<String> toImages(String fileNamePattern, String imageFileType, ToImageOptions options, PageSelection pageSelection)
            throws IOException {

        List<byte[]> dataList = Image_Api.pdfToImage(
                internalPdfDocument, internalPdfDocument.getPageList(pageSelection),
                options.getDpi(), options.getImageMaxWidth(), options.getImageMaxHeight());

        List<String> paths = new ArrayList<>();
        int imageIndex = 1;

        for (byte[] data : dataList) {
            if (imageFileType == null || imageFileType.isEmpty()) {
                imageFileType = FilenameUtils.getExtension(fileNamePattern);
            }

            // dissect file pattern
            String file_nam = FilenameUtils.getBaseName(fileNamePattern);
            String file_ext = FilenameUtils.getExtension(fileNamePattern);
            String file_dir = (new File(fileNamePattern)).getParent();
            String file_new = file_nam;
            // determine current image (page) index
            int current_index = imageIndex++;
            // replace asterisk with page index
            if (file_nam.contains("*")) {
                file_new = file_nam.replace("*", String.format("%1$s", current_index));
            }
            // .. or just append "_pg{index}"
            else if (dataList.size() > 1) {
                file_new = String.format("%1$s_pg%2$s", file_nam, current_index);
            }

            // reconstruct new path
            String path;
            path = java.nio.file.Paths.get(file_dir)
                    .resolve(String.format("%1$s%2$s", file_new, file_ext)).toString();
            // set to specified dpi

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

            // save image
            ImageIO.write(ImageIO.read(byteArrayInputStream), imageFileType, new File(path));
            paths.add(path);

        }

        return paths;
    }

    /**
     * Renders the pages of the PDF as JPEG image files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toJpegImages(String fileNamePattern)
            throws IOException {
        return this.toJpegImages(fileNamePattern, new ToImageOptions());
    }

    /**
     * Renders the pages of the PDF as JPEG image files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @param options         The {@link ToImageOptions}
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toJpegImages(String fileNamePattern, ToImageOptions options)
            throws IOException {
        return this.toJpegImages(fileNamePattern, options, PageSelection.allPages());
    }

    /**
     * Renders the pages of the PDF as JPEG image files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @param pageSelection   Selected page indexes. Default is all pages.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toJpegImages(String fileNamePattern, PageSelection pageSelection)
            throws IOException {
        return toImages(fileNamePattern, "jpg", new ToImageOptions(), pageSelection);
    }

    /**
     * Renders the pages of the PDF as JPEG image files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters.</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers.</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.                        E.g.  C:\images\pdf_page_*.jpg
     * @param options         The {@link ToImageOptions}
     * @param pageSelection   Selected page indexes. Default is all pages.
     * @return An array of the file paths of the image files created.
     * @throws IOException the io exception
     */
    public final List<String> toJpegImages(String fileNamePattern, ToImageOptions options, PageSelection pageSelection)
            throws IOException {
        return toImages(fileNamePattern, "jpg", options, pageSelection);
    }

    /**
     * Renders the pages of the PDF as TIFF (Tagged Image File Format / Tif) file and saves it to disk.
     * Specific image dimensions and page numbers may be given as optional parameters
     * FileNamePattern should normally contain an asterisk (*) character which will be substituted for the
     * page numbers
     *
     * @param filePath A file path for the output file.  E.g.  C:\images\pdf_pages.tiff
     * @return A file path of the image file created.
     * @throws IOException the io exception
     */
    public String toMultiPageTiff(Path filePath)
            throws IOException {

        return toMultiPageTiff(filePath, new ToImageOptions(), PageSelection.allPages());
    }


    /**
     * Renders the pages of the PDF as TIFF (Tagged Image File Format / Tif) file and saves it to disk.
     * Specific image dimensions and page numbers may be given as optional parameters
     * FileNamePattern should normally contain an asterisk (*) character which will be substituted for the
     * page numbers
     *
     * @param filePath      A file path for the output file.  E.g.  C:\images\pdf_pages.tiff
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return A file path of the image file created.
     * @throws IOException the io exception
     */
    public String toMultiPageTiff(Path filePath, PageSelection pageSelection)
            throws IOException {

        return toMultiPageTiff(filePath, new ToImageOptions(), pageSelection);
    }

    /**
     * Renders the pages of the PDF as TIFF (Tagged Image File Format / Tif) file and saves it to disk.
     * Specific image dimensions and page numbers may be given as optional parameters
     * FileNamePattern should normally contain an asterisk (*) character which will be substituted for the
     * page numbers
     *
     * @param filePath A file path for the output file.  E.g.  C:\images\pdf_pages.tiff
     * @param options  The {@link ToImageOptions}
     * @return A file path of the image file created.
     * @throws IOException the io exception
     */
    public String toMultiPageTiff(Path filePath, ToImageOptions options)
            throws IOException {
        return toMultiPageTiff(filePath, options, PageSelection.allPages());
    }

    /**
     * Renders the pages of the PDF as TIFF (Tagged Image File Format / Tif) file and saves it to disk.
     * Specific image dimensions and page numbers may be given as optional parameters
     * FileNamePattern should normally contain an asterisk (*) character which will be substituted for the
     * page numbers
     *
     * @param filePath      A file path for the output file.  E.g.  C:\images\pdf_pages.tiff
     * @param options       The {@link ToImageOptions}
     * @param pageSelection Selected page indexes. Default is all pages.
     * @return A file path of the image file created.
     * @throws IOException the io exception
     */
    public String toMultiPageTiff(Path filePath, ToImageOptions options, PageSelection pageSelection)
            throws IOException {

        byte[] tifData = Image_Api.toMultiPageTiff(
                internalPdfDocument, internalPdfDocument.getPageList(pageSelection),
                options.getDpi(), options.getImageMaxWidth(), options.getImageMaxHeight());

        return Files.write(filePath, tifData).toAbsolutePath().toString();
    }

    /**
     * Reduces the PDF's file size by compressing existing images using JPEG encoding and the specified quality setting.
     *
     * @param quality Quality (1 - 100) to use during compression
     */
    public final void compressImages(int quality) {
        compressImages(quality, false);
    }

    /**
     * Reduces the PDF's file size by compressing existing images using JPEG encoding and the specified quality settings.
     *
     * @param quality            Quality (1 - 100) to use during compression
     * @param scaleToVisibleSize Scale down the image resolution according to its visible size in the                           PDF document; may cause distortion with some image configurations.                           Default is false.
     */
    public final void compressImages(int quality, boolean scaleToVisibleSize) {
        if (quality < 1 || quality > 100) {
            throw new IndexOutOfBoundsException(String.format(
                    "Invalid quality specifier (%1$s) when compressing images. Quality must be between 1 and 100.",
                    quality));
        }
        Compress_Api.compressImages(internalPdfDocument, quality,
                scaleToVisibleSize);
    }

    /**
     * Finds all embedded Images from within the PDF and returns them as a list of {@link BufferedImage} images.
     *
     * @return The extracted images as {@link BufferedImage} objects.
     * @throws IOException the io exception
     */
    public final List<BufferedImage> extractAllImages() throws IOException {
        return extractAllRawImages().stream().map(bytes -> {
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage newBi;
            try {
                newBi = ImageIO.read(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return newBi;
        }).collect(Collectors.toList());
    }


    /**
     * Finds all embedded Images from within the PDF and returns as a list of image bytes
     *
     * @return The extracted images as byte arrays.
     * @throws IOException the io exception
     */
    public final List<byte[]> extractAllRawImages() throws IOException {
        if (Page_Api.getPagesInfo(internalPdfDocument).size()
                == 0) {
            return new ArrayList<>();
        }
        return Image_Api.extractAllImages(internalPdfDocument);
    }

    /**
     * Finds all embedded Images from within the PDF and returns as a list of image bytes
     *
     * @param pageSelection The selected page index(es). Default is all pages.
     * @return The extracted images as {@link BufferedImage} objects.
     * @throws IOException the io exception
     */
    public final List<BufferedImage> extractAllImagesFromPages(PageSelection pageSelection)
            throws IOException {
        return extractAllRawImagesFromPages(pageSelection).stream().map(bytes -> {
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage newBi;
            try {
                newBi = ImageIO.read(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return newBi;
        }).collect(Collectors.toList());
    }


    /**
     * Finds all embedded Images from within the PDF and returns them as raw bytes.
     *
     * @param pageSelection The selected page index(es). Default is all pages.
     * @return The extracted images as byte arrays.
     * @throws IOException the io exception
     */
    public final List<byte[]> extractAllRawImagesFromPages(PageSelection pageSelection)
            throws IOException {
        if (Page_Api.getPagesInfo(internalPdfDocument).size()
                == 0) {
            return new ArrayList<>();
        }
        return Image_Api.extractAllImages(internalPdfDocument,
                internalPdfDocument.getPageList(pageSelection));
    }

    //endregion

    //region IO

    /**
     * Saves this PdfDocument to a file.
     *
     * @param filePath File Path
     * @return This PdfDocument for fluid code notation.
     * @throws IOException the io exception
     */
    public final PdfDocument saveAs(Path filePath) throws IOException {
        PdfDocument_Api.saveAs(internalPdfDocument,
                filePath.toAbsolutePath().toString());
        return this;
    }

    /**
     * Saves this PdfDocument to a file.
     *
     * @param filePath File path string
     * @return This PdfDocument for fluid code notation.
     * @throws IOException the io exception
     */
    public final PdfDocument saveAs(String filePath) throws IOException {
        PdfDocument_Api.saveAs(internalPdfDocument,
                filePath);
        return this;
    }

    /**
     * Saves this PdfDocument to a file.
     *
     * @param filePath File Path
     * @return This PdfDocument for fluid code notation.
     * @throws IOException the io exception
     */
    public final PdfDocument saveAsRevision(Path filePath) throws IOException {
        PdfDocument_Api.saveAsRevision(internalPdfDocument,
                filePath.toAbsolutePath().toString());
        return this;
    }

    /**
     * Saves this PdfDocument to a file.
     *
     * @param filePath File path string
     * @return This PdfDocument for fluid code notation.
     * @throws IOException the io exception
     */
    public final PdfDocument saveAsRevision(String filePath) throws IOException {
        PdfDocument_Api.saveAsRevision(internalPdfDocument,
                filePath);
        return this;
    }

    /**
     * Saves the PDF as byte array, including any changes.
     *
     * @return The PDF file as a byte array.
     */
    public final byte[] getBinaryData() {
        return PdfDocument_Api.getBytes(internalPdfDocument, false);
    }

    /**
     Saves the PDF as byte array with changes appended to the end of the file.

     @return The PDF file as a byte array.
     */
    public final byte[] getBinaryDataIncremental() {
        return PdfDocument_Api.getBytes(internalPdfDocument, true);
    }



    /**
     Creates a copy of this document at the specified revision number. {@link PdfDocument#saveAsRevision}

     @return a {@link PdfDocument} document
     @param index revision index
     */
    public final PdfDocument getRevision(int index) {
        return new PdfDocument(PdfDocument_Api.getRevision(internalPdfDocument, index));
    }

    //endregion

    //region Print

    /**
     * Prints this PDF by sending it to the computer's real world printer(s).
     * <p>For advanced real-world printing options please implement your own java.awt.print code.
     * <p>This class {@link PdfDocument}implements java.awt.print.Printable.</p>
     *
     * @throws PrinterException the printer exception
     */
    public void print() throws PrinterException {
        Print_Api.print(internalPdfDocument, true);
    }

    /**
     * Prints this PDF by sending it to the computer's real world printer(s).
     * <p>For advanced real-world printing options please implement your own java.awt.print code.
     * <p>This class {@link PdfDocument}implements java.awt.print.Printable.</p>
     *
     * @throws PrinterException the printer exception
     */
    public void printWithoutDialog() throws PrinterException {
        Print_Api.print(internalPdfDocument, false);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        return internalPdfDocument.print(graphics, pageFormat, pageIndex);
    }

    //endregion

    //region Edit

    /**
     * Adds a watermark to this PDF.
     * <p>Please use {@link #applyStamp(Stamper)} for more control.</p>
     *
     * @param html              The HTML fragment which will be stamped onto your PDF.
     * @param opacity           Watermark transparent value. 0 is invisible, 100 if fully opaque.
     * @param verticalAlignment The vertical alignment of the watermark relative to the page.
     * @return Returns this {@link PdfDocument}, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument applyWatermark(String html, int opacity,
                                            VerticalAlignment verticalAlignment) {
        return applyWatermark(html, opacity, verticalAlignment, HorizontalAlignment.CENTER);
    }

    /**
     * Adds a watermark to this PDF.
     * <p>Please use {@link #applyStamp(Stamper)} for more control.</p>
     *
     * @param html                The HTML fragment which will be stamped onto your PDF.
     * @param opacity             Watermark transparent value. 0 is invisible, 100 if fully opaque.
     * @param verticalAlignment   The vertical alignment of the watermark relative to the page.
     * @param horizontalAlignment The horizontal alignment of the watermark relative to the page.
     * @return Returns this {@link PdfDocument}, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument applyWatermark(String html, int opacity,
                                            VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment) {
        HtmlStamper stamper = new HtmlStamper(html);
        stamper.setVerticalAlignment(verticalAlignment);
        stamper.setHorizontalAlignment(horizontalAlignment);
        stamper.setOpacity(opacity);
        applyStamp(stamper);
        return this;
    }

    /**
     * Adds Watermark to PDF, Please use {@link #applyStamp(Stamper)} for more control.
     *
     * @param html    The HTML fragment which will be stamped onto your PDF.
     * @param opacity Watermark transparent value. 0 is invisible, 100 if fully opaque.
     * @return Returns this {@link PdfDocument}, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument applyWatermark(String html, int opacity) {
        return applyWatermark(html, opacity, VerticalAlignment.MIDDLE, HorizontalAlignment.CENTER);
    }

    /**
     * Adds Watermark to PDF, Please use {@link #applyStamp(Stamper)} for more control.
     *
     * @param html The HTML fragment which will be stamped onto your PDF.
     * @return Returns this {@link PdfDocument}, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument applyWatermark(String html) {
        return applyWatermark(html, 50, VerticalAlignment.MIDDLE, HorizontalAlignment.CENTER);
    }


    /**
     * Edits the PDF by applying the  {@link Stamper}'s rendered to every page.
     *
     * @param stamper The  {@link Stamper} object that has the content to be stamped onto the PDF.
     * @return Returns this {@link PdfDocument}, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument applyStamp(Stamper stamper) {
        applyStamp(stamper, PageSelection.allPages());
        return this;
    }

    /**
     * Edits the PDF by applying the  {@link Stamper}'s rendered to  only selected page(s).
     *
     * @param stamper       The  {@link Stamper} object that has the content to be stamped onto the PDF.
     * @param pageSelection The selected page index(es). Default is all pages,
     * @return Returns this {@link PdfDocument}, allowing for a 'fluent'  chained in-line code style
     */
    public final PdfDocument applyStamp(Stamper stamper, PageSelection pageSelection) {
        Stamp_Api.applyStamp(internalPdfDocument, stamper, internalPdfDocument.getPageList(pageSelection));
        return this;
    }


    //endregion

    //region Text

    /**
     * Extracts the written text content from the PDF and returns it as a string.
     *
     * @return All text in the PDF as a string. <p>Pages are separated by 4 consecutive line breaks</p>
     */
    public final String extractAllText() {
        return Text_Api.extractAllText(internalPdfDocument);
    }

    /**
     * Extracts the text content from one page of the PDF and returns it as a string.
     *
     * @param pageSelection The selected page index(es). Default is all pages.
     * @return The text extracted from the PDF page as a string.
     */
    public final String extractTextFromPage(PageSelection pageSelection) {
        return Text_Api.extractAllText(internalPdfDocument,
                internalPdfDocument.getPageList(pageSelection));
    }

    /**
     * Replace the specified old text with new text on a given page.
     *
     * @param pageSelection The selected page index(es).
     * @param oldText       Old text to remove
     * @param newText       New text to add
     */
    public final void replaceText(PageSelection pageSelection, String oldText, String newText) {
        internalPdfDocument.getPageList(pageSelection).forEach(page -> {
            Text_Api.replaceTextOnPage(internalPdfDocument, page,
                    oldText, newText);
        });
    }

    //endregion

    //region Render

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath Path to a Html to be rendered as a PDF.
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, null, null, null);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath Path to a Html to be rendered as a PDF.
     * @param baseUrl      Optional. Setting the BaseURL property gives the relative file path or URL context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath, String baseUrl) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, baseUrl, null, null);
    }


    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                  ChromePdfRenderOptions renderOptions,
                                                  ChromeHttpLoginCredentials loginCredentials) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, null, renderOptions, loginCredentials);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or URL context for hyperlinks, images, CSS and JavaScript files.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                  String baseUrl,
                                                  ChromePdfRenderOptions renderOptions,
                                                  ChromeHttpLoginCredentials loginCredentials) throws IOException {
        return new PdfDocument(Render_Api.renderHtmlFileAsPdf(htmlFilePath, baseUrl, renderOptions, loginCredentials));
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                  ChromeHttpLoginCredentials loginCredentials
    ) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, null, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath     Path to a Html to be rendered as a PDF.
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or URL context for hyperlinks, images, CSS and JavaScript files.
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                  String baseUrl,
                                                  ChromeHttpLoginCredentials loginCredentials
    ) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, baseUrl, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath  Path to a Html to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                  ChromePdfRenderOptions renderOptions
    ) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, renderOptions, null);
    }

    /**
     * Creates a PDF file from a local Html file, and returns it as a {@link PdfDocument}.
     *
     * @param htmlFilePath  Path to a Html to be rendered as a PDF.
     * @param baseUrl       Optional. Setting the BaseURL property gives the relative file path or URL context for hyperlinks, images, CSS and JavaScript files.
     * @param renderOptions Rendering options
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderHtmlFileAsPdf(String htmlFilePath,
                                                  String baseUrl,
                                                  ChromePdfRenderOptions renderOptions
    ) throws IOException {
        return renderHtmlFileAsPdf(htmlFilePath, baseUrl, renderOptions, null);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link PdfDocument}.
     *
     * @param url An absolute (fully formed) Uri.  Points to the Html document to be rendered as a PDF.
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderUrlAsPdf(String url) {
        return renderUrlAsPdf(url, null, null);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link PdfDocument}.
     *
     * @param url              An absolute (fully formed) Uri.  Points to the Html document to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderUrlAsPdf(String url,
                                             ChromePdfRenderOptions renderOptions,
                                             ChromeHttpLoginCredentials loginCredentials) {
        return new PdfDocument(Render_Api.renderUrlAsPdf(url, renderOptions, loginCredentials));
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link PdfDocument}.
     *
     * @param url              An absolute (fully formed) Uri.  Points to the Html document to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderUrlAsPdf(String url,
                                             ChromeHttpLoginCredentials loginCredentials) {
        return renderUrlAsPdf(url, null, loginCredentials);
    }

    /**
     * Creates a PDF file from a URL or local file path and returns it as a
     * {@link PdfDocument}.
     *
     * @param url           An absolute (fully formed) Uri.  Points to the Html document to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderUrlAsPdf(String url,
                                             ChromePdfRenderOptions renderOptions) {
        return renderUrlAsPdf(url, renderOptions, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or URL context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html,
                                              ChromePdfRenderOptions renderOptions,
                                              ChromeHttpLoginCredentials loginCredentials,
                                              String baseUrl) {
        return new PdfDocument(Render_Api.renderHtmlAsPdf(html, renderOptions, loginCredentials, baseUrl));
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html The Html to be rendered as a PDF.
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html) {
        return renderHtmlAsPdf(html, null, null, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param baseUrl          Optional. Setting the BaseURL property gives the relative file path or URL context for hyperlinks, images, CSS and JavaScript files.
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html,
                                              String baseUrl,
                                              ChromeHttpLoginCredentials loginCredentials) {
        return renderHtmlAsPdf(html, null, loginCredentials, baseUrl);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html          The Html to be rendered as a PDF.
     * @param baseUrl       Optional. Setting the BaseURL property gives the relative file path or URL                      context for hyperlinks, images, CSS and JavaScript files.
     * @param renderOptions Rendering options
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html,
                                              String baseUrl,
                                              ChromePdfRenderOptions renderOptions) {
        return renderHtmlAsPdf(html, renderOptions, null, baseUrl);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param renderOptions    Rendering options
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html,
                                              ChromePdfRenderOptions renderOptions,
                                              ChromeHttpLoginCredentials loginCredentials) {
        return renderHtmlAsPdf(html, renderOptions, loginCredentials, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html    The Html to be rendered as a PDF.
     * @param baseUrl Optional. Setting the BaseURL property gives the relative file path or URL                context for hyperlinks, images, CSS and JavaScript files.
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html,
                                              String baseUrl) {
        return renderHtmlAsPdf(html, null, null, baseUrl);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html             The Html to be rendered as a PDF.
     * @param loginCredentials Http login credentials
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html,
                                              ChromeHttpLoginCredentials loginCredentials) {
        return renderHtmlAsPdf(html, null, loginCredentials, null);
    }

    /**
     * Creates a PDF file from a Html string, and returns it as a {@link PdfDocument}.
     *
     * @param html          The Html to be rendered as a PDF.
     * @param renderOptions Rendering options
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderHtmlAsPdf(String html,
                                              ChromePdfRenderOptions renderOptions) {
        return renderHtmlAsPdf(html, renderOptions, null, null);
    }

    /**
     * Creates a PDF file from RTF string, and returns it as a {@link PdfDocument}.
     *
     * @param rtfString The RTF string to be rendered as a PDF.
     * @return A {@link PdfDocument}
     */
    public static PdfDocument renderRtfAsPdf(String rtfString) {
        return new PdfDocument(Render_Api.renderRtfAsPdf(rtfString));
    }

    /**
     * Creates a PDF file from RTF file, and returns it as a {@link PdfDocument}.
     *
     * @param rtfFilePath The RTF file path to be rendered as a PDF.
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderRtfFileAsPdf(String rtfFilePath) throws IOException {
        return renderRtfFileAsPdf(Paths.get(rtfFilePath));
    }

    /**
     * Creates a PDF file from RTF file, and returns it as a {@link PdfDocument}.
     *
     * @param rtfFilePath The RTF file path to be rendered as a PDF.
     * @return A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderRtfFileAsPdf(Path rtfFilePath) throws IOException {
        return new PdfDocument(Render_Api.renderRtfAsPdf(String.join("", Files.readAllLines(rtfFilePath))));
    }

    /**
     *Creates a PDF file from a local Zip file, and returns it as a {@link PdfDocument}.
     *<p>IronPDF is a W3C standards compliant HTML rendering based on Google's Chromium browser.  If your output PDF does not look as expected:
     *<p> - Validate your HTML file using  https://validator.w3.org/ &amp; CSS https://jigsaw.w3.org/css-validator/
     *<p> - To debug HTML, view the file in Chrome web browser's print preview which will work almost exactly as IronPDF.
     *<p> - Read our detailed documentation on pixel perfect HTML to PDF: https://ironpdf.com/tutorials/pixel-perfect-html-to-pdf/
     *@param zipFilePath Path to a Zip to be rendered as a PDF.
     *@param mainFile Name of the primary HTML file.
     *@param renderOptions Rendering options
     *@return
     *A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderZipAsPdf(Path zipFilePath, String mainFile, ChromePdfRenderOptions renderOptions) throws IOException {
        return new PdfDocument(Render_Api.renderZipAsPdf(zipFilePath, mainFile, renderOptions, new ChromeHttpLoginCredentials()));
    }

    /**
     *Creates a PDF file from a local Zip file, and returns it as a {@link PdfDocument}.
     *<p>IronPDF is a W3C standards compliant HTML rendering based on Google's Chromium browser.  If your output PDF does not look as expected:
     *<p> - Validate your HTML file using  https://validator.w3.org/ &amp; CSS https://jigsaw.w3.org/css-validator/
     *<p> - To debug HTML, view the file in Chrome web browser's print preview which will work almost exactly as IronPDF.
     *<p> - Read our detailed documentation on pixel perfect HTML to PDF: https://ironpdf.com/tutorials/pixel-perfect-html-to-pdf/
     *@param zipFilePath Path to a Zip to be rendered as a PDF.
     *@param mainFile Name of the primary HTML file.
     *@return
     *A {@link PdfDocument}
     * @throws IOException the io exception
     */
    public static PdfDocument renderZipAsPdf(Path zipFilePath, String mainFile) throws IOException {
        return new PdfDocument(Render_Api.renderZipAsPdf(zipFilePath, mainFile, new ChromePdfRenderOptions(), new ChromeHttpLoginCredentials()));
    }

    //endregion
}
