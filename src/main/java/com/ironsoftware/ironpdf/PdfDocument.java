package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.annotation.AnnotationManager;
import com.ironsoftware.ironpdf.backgroundforeground.BackgroundForegroundOptions;
import com.ironsoftware.ironpdf.bookmark.BookmarkManager;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.form.FormManager;
import com.ironsoftware.ironpdf.headerfooter.HeaderFooterOptions;
import com.ironsoftware.ironpdf.headerfooter.HtmlHeaderFooter;
import com.ironsoftware.ironpdf.headerfooter.TextHeaderFooter;
import com.ironsoftware.ironpdf.image.DrawImageOptions;
import com.ironsoftware.ironpdf.image.ImageBehavior;
import com.ironsoftware.ironpdf.image.ToImageOptions;
import com.ironsoftware.ironpdf.metadata.MetadataManager;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;
import com.ironsoftware.ironpdf.render.ChromePdfRenderOptions;
import com.ironsoftware.ironpdf.security.PdfSecuritySettings;
import com.ironsoftware.ironpdf.stamp.HorizontalAlignment;
import com.ironsoftware.ironpdf.stamp.HtmlStamper;
import com.ironsoftware.ironpdf.stamp.Stamper;
import com.ironsoftware.ironpdf.stamp.VerticalAlignment;
import com.ironsoftware.ironpdf.staticapi.*;
import org.apache.commons.io.FilenameUtils;

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
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class PdfDocument implements Printable {

    private final InternalPdfDocument internalPdfDocument;

    //region Constructor
    private final BookmarkManager bookmarkManager;
    private final MetadataManager metadataManager;
    private final AnnotationManager annotationManager;
    private FormManager formManager;

    private PdfDocument(InternalPdfDocument static_pdfDocument) {
        internalPdfDocument = static_pdfDocument;
        bookmarkManager = new BookmarkManager(this.internalPdfDocument);
        metadataManager = new MetadataManager(this.internalPdfDocument);
        annotationManager = new AnnotationManager(this.internalPdfDocument);
        formManager = new FormManager(this.internalPdfDocument);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @param password    Optional user password if the PDF document is encrypted.
     */
    public PdfDocument(Path pdfFilePath, String password) throws IOException {
        this(pdfFilePath, password, "");
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath   The PDF file path.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,
     *                      modifying restrictions etc..).
     */
    public PdfDocument(Path pdfFilePath, String password, String ownerPassword) throws IOException {
        this(Files.readAllBytes(pdfFilePath), password, ownerPassword);
    }

    //endregion

    //region Attachment

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfData       The PDF file data as byte array.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,
     *                      modifying restrictions etc..).
     */
    public PdfDocument(byte[] pdfData, String password, String ownerPassword) throws IOException {
        internalPdfDocument = com.ironsoftware.ironpdf.staticapi.PdfDocument_Api.FromBytes(pdfData,
                password, ownerPassword);
        bookmarkManager = new BookmarkManager(this.internalPdfDocument);
        metadataManager = new MetadataManager(this.internalPdfDocument);
        annotationManager = new AnnotationManager(this.internalPdfDocument);
    }
    //endregion

    // region BackgroundForeground

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
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
    public PdfDocument(byte[] pdfData, String password) throws IOException {
        this(pdfData, password, "");
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfData The PDF file data as byte array.
     */
    public PdfDocument(byte[] pdfData) throws IOException {
        this(pdfData, "", "");
    }

    /**
     * Static method that joins (concatenates) 2 PDF documents together into one PDF document. If the
     * PDF contains form fields the form field in the resulting PDF's name will be appended with
     * '_{index}' e.g. 'Name' will be 'Name_0'
     *
     * @param A A PDF
     * @param B A Seconds PDF
     * @return A new, merged {@link PdfDocument}
     */
    public static PdfDocument Merge(PdfDocument A, PdfDocument B) throws IOException {
        return Merge(Arrays.asList(A, B));
    }

    //endregion

    //region Bookmark

    /**
     * Static method that joins (concatenates) multiple PDF documents together into one compiled PDF
     * document. If the PDF contains form fields the form field in the resulting PDF's name will be
     * appended with '_{index}' e.g. 'Name' will be 'Name_0'
     *
     * @param Documents A IEnumerable of PdfDocument.  To merge existing PDF files you may use the
     *                  PdfDocument.FromFile static method in conjunction with Merge.
     * @return A new, merged {@link PdfDocument}
     */
    public static PdfDocument Merge(List<PdfDocument> Documents) throws IOException {
        return new PdfDocument(Page_Api.MergePage(
                Documents.stream().map(x -> x.internalPdfDocument).collect(Collectors.toList())));
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @param password    Optional user password if the PDF document is encrypted.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     */
    public static PdfDocument FromFile(Path pdfFilePath, String password) throws IOException {
        return FromFile(pdfFilePath, password, "");
    }

    //endregion

    //region Page

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath   The PDF file path.
     * @param password      Optional user password if the PDF document is encrypted.
     * @param ownerPassword Optional password if the PDF document is protected by owner (printing,
     *                      modifying restrictions etc..).
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     */
    public static PdfDocument FromFile(Path pdfFilePath, String password, String ownerPassword)
            throws IOException {
        return new PdfDocument(pdfFilePath, password, ownerPassword);
    }

    /**
     * Opens an existing PDF document for editing.
     *
     * @param pdfFilePath The PDF file path.
     * @return An IronPdf.PdfDocument object as loaded from the file path.
     */
    public static PdfDocument FromFile(Path pdfFilePath) throws IOException {
        return FromFile(pdfFilePath, "", "");
    }

    /**
     * Converts a single image file to an identical PDF document of matching dimensions. The default
     * PaperSize is A4. You can set it via ImageToPdfConverter.PaperSize. Note:
     * Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.
     *
     * @param imagesPath A list of file path of the image file.
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or
     * served over the web.
     */
    public static PdfDocument FromImage(List<Path> imagesPath) throws IOException {

        return FromImage(imagesPath, ImageBehavior.CENTERED_ON_PAGE);
    }

    /**
     * Converts a single image file to an identical PDF document of matching dimensions. The default
     * PaperSize is A4. You can set it via ImageToPdfConverter.PaperSize. Note:
     * Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.
     *
     * @param imagesPath    A list of file path of the image file.
     * @param imageBehavior Describes how image should be placed on the PDF page
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or
     * served over the web.
     */
    public static PdfDocument FromImage(List<Path> imagesPath, ImageBehavior imageBehavior)
            throws IOException {

        return new PdfDocument(Image_Api.ImageToPdf(imagesPath.stream().map(x -> {
            try {
                return Files.readAllBytes(x);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()).iterator(), imageBehavior));
    }

    /**
     * Converts a single image file to an identical PDF document of matching dimensions. The default
     * PaperSize is A4. You can set it via ImageToPdfConverter.PaperSize. Note:
     * Imaging.ImageBehavior.CropPage will set PaperSize equal to ImageSize.
     *
     * @param imagesPath    A list of file path of the image file.
     * @param imageBehavior Describes how image should be placed on the PDF page
     * @param renderOptions Rendering options
     * @return Returns a {@link PdfDocument} document which can then be edited, saved or
     * served over the web.
     */
    public static PdfDocument FromImage(List<Path> imagesPath, ImageBehavior imageBehavior,
                                        ChromePdfRenderOptions renderOptions) throws IOException {

        return new PdfDocument(Image_Api.ImageToPdf(imagesPath.stream().map(x -> {
            try {
                return Files.readAllBytes(x);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()).iterator(), imageBehavior));
    }

    /**
     * Collection of attachments name contained within the pdf document
     */
    public final java.util.Iterator<String> getAttachments() throws IOException {
        return com.ironsoftware.ironpdf.staticapi.Attachment_Api.GetPdfAttachmentCollection(
                this.internalPdfDocument);
    }

    /**
     * Adds a background to each page of this PDF. The background is copied from a first page in
     * another PDF document.
     *
     * @param backgroundPdf The background PDF document.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument AddBackgroundPdf(PdfDocument backgroundPdf) throws IOException {
        return AddBackgroundPdf(backgroundPdf, new BackgroundForegroundOptions());
    }

    /**
     * Adds a background to selected page(s) of this PDF. The background is copied from a selected
     * page in another PDF document.
     *
     * @param backgroundPdf The background PDF document.
     * @param options        The BackgroundForegroundOption.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument AddBackgroundPdf(PdfDocument backgroundPdf,
                                              BackgroundForegroundOptions options) throws IOException {
        com.ironsoftware.ironpdf.staticapi.BackgroundForeground_Api.AddBackground(
                this.internalPdfDocument,
                backgroundPdf.internalPdfDocument,
                options.getPageSelection().getPageList(),
                options.getBackgroundForegroundPdfPageIndex());
        return this;
    }

    /**
     * Adds a foreground to each page of this PDF. The foreground is copied from a first page of
     * another PDF document.
     *
     * @param foregroundPdf The foreground PDF document.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument AddForegroundPdf(PdfDocument foregroundPdf) throws IOException {
        return AddForegroundPdf(foregroundPdf, new BackgroundForegroundOptions());
    }

    /**
     * Adds a foreground to selected page(s) of this PDF. The foreground is copied from a selected
     * page in another PDF document.
     *
     * @param foregroundPdf The foreground PDF document.
     * @param options        The BackgroundForegroundOption.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument AddForegroundPdf(PdfDocument foregroundPdf,
                                              BackgroundForegroundOptions options) throws IOException {
        com.ironsoftware.ironpdf.staticapi.BackgroundForeground_Api.AddForeground(
                this.internalPdfDocument,
                foregroundPdf.internalPdfDocument,
                options.getPageSelection().getPageList(),
                options.getBackgroundForegroundPdfPageIndex());
        return this;
    }

    /**
     * Gets bookmark manager for this PDF document.
     */
    public BookmarkManager getBookmarkManager() {
        return bookmarkManager;
    }

    /**
     * Creates a new PDF by copying a page from this PdfDocument.
     *
     * @param PageIndex Index of the page.  Note: Page 1 has index 0...
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument CopyPage(int PageIndex) throws IOException {
        return CopyPages(new ArrayList<>(Collections.singletonList(PageIndex)));
    }

    /**
     * Creates a new PDF by copying a range of pages from this PdfDocument.
     *
     * @param PageIndexes An IEnumerable of page indexes to copy into the new PDF.
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument CopyPages(java.lang.Iterable<Integer> PageIndexes) throws IOException {
        return new PdfDocument(
                com.ironsoftware.ironpdf.staticapi.Page_Api.CopyPage(internalPdfDocument, PageIndexes));
    }

    /**
     * Creates a new PDF by copying a range of pages from this {@link PdfDocument}.
     *
     * @param StartIndex The index of the first PDF page to copy. Note: Page 1 has index 0
     * @param EndIndex   The index of the last PDF page to copy.
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument CopyPages(int StartIndex, int EndIndex) throws IOException {
        ArrayList<Integer> pageIndexes = new ArrayList<>();
        for (int i = StartIndex; i <= EndIndex; i++) {
            pageIndexes.add(i);
        }

        return CopyPages(pageIndexes);
    }

    //endregion

    //region HeaderFooter

    //region TextHeaderFooter

    /**
     * Appends another PDF to the end of the current {@link PdfDocument} If AnotherPdfFile contains
     * form fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name' will be
     * 'Name_'
     *
     * @param AnotherPdfFile PdfDocument to append.
     * @return A new {@link PdfDocument}
     */
    public final PdfDocument AppendPdf(PdfDocument AnotherPdfFile) throws IOException {
        Page_Api.AppendPdf(this.internalPdfDocument, AnotherPdfFile.internalPdfDocument);
        return this;
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param AnotherPdfFile Another PdfDocument...
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument InsertPdf(PdfDocument AnotherPdfFile) throws IOException {
        return InsertPdf(AnotherPdfFile, 0);
    }

    /**
     * Inserts another PDF into the current PdfDocument, starting at a given Page Index. If
     * AnotherPdfFile contains form fields, those fields will be appended with '_' in the resulting
     * PDF. e.g. 'Name' will be 'Name_'
     *
     * @param AnotherPdfFile Another PdfDocument.
     * @param AtIndex        Index at which to insert the new content.  Note: Page 1 has index 0...
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument InsertPdf(PdfDocument AnotherPdfFile, int AtIndex) throws IOException {
        Page_Api.InsertPage(internalPdfDocument, AnotherPdfFile.internalPdfDocument, AtIndex);
        return this;
    }

    /**
     * Adds another PDF to the beginning of the current PdfDocument If AnotherPdfFile contains form
     * fields, those fields will be appended with '_' in the resulting PDF. e.g. 'Name' will be
     * 'Name_'
     *
     * @param AnotherPdfFile PdfDocument to prepend.
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument PrependPdf(PdfDocument AnotherPdfFile) throws IOException {
        Page_Api.InsertPage(internalPdfDocument, AnotherPdfFile.internalPdfDocument, 0);
        return this;
    }

    //endregion

    //region HtmlHeaderFooter

    /**
     * Removes a range of pages from the PDF
     *
     * @param pageSelection A selected page index(es). Default is PageSelection.AllPages().
     * @return Returns this PdfDocument object, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument RemovePages(PageSelection pageSelection) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Page_Api.RemovePage(internalPdfDocument,
                pageSelection.getPageList());
        return this;
    }

    /**
     * Gets list of all pages information.
     *
     * @return list of all pages information.
     */
    public final List<PageInfo> GetPagesInfo() throws IOException {
        return com.ironsoftware.ironpdf.staticapi.Page_Api.GetPagesInfo(internalPdfDocument);
    }

    /**
     * Gets list of all pages information.
     *
     * @param pageSelection A selected page index(es). Default is PageSelection.AllPages().
     * @return a map of page index and page information.
     */
    public final Map<Integer, PageInfo> GetPagesInfo(PageSelection pageSelection) throws IOException {
        List<PageInfo> pagesInfos = com.ironsoftware.ironpdf.staticapi.Page_Api.GetPagesInfo(
                internalPdfDocument);
        return pageSelection.getPageList().stream().collect(Collectors.toMap(x -> x, pagesInfos::get));
    }

    /**
     * Rotates all pages of the PdfDocument by a specified number of degrees.
     *
     * @param rotation Degrees of rotation
     */
    public final void RotateAllPages(PageRotation rotation) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Page_Api.RotatePage(internalPdfDocument, rotation);
    }

    //endregion

    //endregion

    //region Image

    /**
     * Rotates one page of the PdfDocument by a specified number of degrees.
     *
     * @param pageRotation      Degrees of rotation
     * @param pageSelection A selected page index(es). Default is PageSelection.AllPages().
     */
    public final void RotatePage(PageRotation pageRotation, PageSelection pageSelection)
            throws IOException {
        com.ironsoftware.ironpdf.staticapi.Page_Api.RotatePage(internalPdfDocument, pageRotation,
                pageSelection.getPageList());
    }

    /**
     * Renders TEXT page headers to an existing PDF File
     *
     * @param header A new instance of IronPdf.TextHeaderFooter that defines the header content and
     *               layout.
     */
    public PdfDocument AddTextHeader(TextHeaderFooter header) throws IOException {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddTextHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page headers to an existing PDF File
     *
     * @param header             A new instance of IronPdf.TextHeaderFooter that defines the header
     *                           content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     */
    public PdfDocument AddTextHeader(TextHeaderFooter header, HeaderFooterOptions headerFooterOptions)
            throws IOException {
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddTextHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page footers to an existing PDF File
     *
     * @param footer A new instance of IronPdf.TextHeaderFooter that defines the footer content and
     *               layout.
     */
    public PdfDocument AddTextFooter(TextHeaderFooter footer) throws IOException {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddTextFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders TEXT page footer to an existing PDF File
     *
     * @param footer             A new instance of IronPdf.TextHeaderFooter that defines the footer
     *                           content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     */
    public PdfDocument AddTextFooter(TextHeaderFooter footer, HeaderFooterOptions headerFooterOptions)
            throws IOException {
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddTextHeader(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getPdfTitle());
        return this;
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header A new instance of IronPdf.HtmlHeaderFooter that defines the header content and
     *               layout.
     */
    public PdfDocument AddHtmlHeader(HtmlHeaderFooter header) throws IOException {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddHtmlHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page headers to an existing PDF File
     *
     * @param header             A new instance of IronPdf.HtmlHeaderFooter that defines the header
     *                           content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     */
    public PdfDocument AddHtmlHeader(HtmlHeaderFooter header, HeaderFooterOptions headerFooterOptions)
            throws IOException {
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddHtmlHeader(internalPdfDocument, header,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    //endregion

    //region CompressImages

    /**
     * Renders HTML page footers to an existing PDF File
     *
     * @param footer A new instance of IronPdf.HtmlHeaderFooter that defines the footer content and
     *               layout.
     * @throws IOException When IronPdfEngine is down.
     */
    public PdfDocument AddHtmlFooter(HtmlHeaderFooter footer) throws IOException {
        HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddHtmlFooter(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }

    /**
     * Renders HTML page footer to an existing PDF File
     *
     * @param footer             A new instance of IronPdf.HtmlHeaderFooter that defines the footer
     *                           content and layout.
     * @param headerFooterOptions HeaderFooterOption.
     */
    public PdfDocument AddHtmlFooter(HtmlHeaderFooter footer, HeaderFooterOptions headerFooterOptions)
            throws IOException {
        com.ironsoftware.ironpdf.staticapi.HeaderFooter_Api.AddHtmlHeader(internalPdfDocument, footer,
                headerFooterOptions.getFirstPageNumber(),
                headerFooterOptions.getPageIndexesToAddHeaderFootersTo(),
                headerFooterOptions.getMarginLeftMm(), headerFooterOptions.getMarginRightMm(),
                headerFooterOptions.getMarginTopMm(), headerFooterOptions.getMarginBottomMm(),
                headerFooterOptions.getRenderPdfCssMediaType(), headerFooterOptions.getPdfTitle(),
                headerFooterOptions.getHtmlTitle());
        return this;
    }
    //endregion

    //region ExtractAllBitmaps

    /**
     * Draws a bitmap image to the PDF document
     *
     * @param bitmapFilePath The bitmap image file path.
     */
    public final void DrawImage(Path bitmapFilePath, DrawImageOptions option) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Image_Api.DrawBitmap(internalPdfDocument,
                Files.readAllBytes(bitmapFilePath), option.getPageSelection().getPageList(), option.getX(),
                option.getY(),
                option.getWidth(), option.getHeight());
    }

    /**
     * Draws a bitmap image to the PDF document
     *
     * @param bitmapBytes image byte array
     */
    public final void DrawImage(byte[] bitmapBytes, DrawImageOptions option) throws IOException {
        com.ironsoftware.ironpdf.staticapi.Image_Api.DrawBitmap(internalPdfDocument,
                bitmapBytes, option.getPageSelection().getPageList(), option.getX(), option.getY(),
                option.getWidth(), option.getHeight());
    }

    /**
     * Rasterizes (renders) the PDF into BufferedImage objects.  1 BufferedImage for each page.
     *
     * @return An array of BufferedImage objects.
     */
    public final List<BufferedImage> ToBufferedImage() throws IOException {
        return ToBufferedImage(new ToImageOptions());
    }

    /**
     * Rasterizes (renders) the PDF into BufferedImage objects.  1 BufferedImage for each page.
     *
     * @param options The {@link ToImageOptions}
     * @return An array of BufferedImage objects.
     */
    public final List<BufferedImage> ToBufferedImage(ToImageOptions options) throws IOException {
        return com.ironsoftware.ironpdf.staticapi.Image_Api.PdfToImage(internalPdfDocument,
                        options.getPageSelection().getPageList(),
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

    //endregion

    //region FromFile

    /**
     * Renders the pages of the PDF as PNG (Portable Network Graphic) files and saves them to disk.
     * <p>Specific image dimensions and page numbers may be given as optional parameters</p>
     * <p>fileNamePattern should normally contain an asterisk (*) character which will be substituted
     * for the page numbers</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.
     *                        E.g.  C:\images\pdf_pages_*.png
     * @param options          The {@link ToImageOptions}
     * @return An array of the file paths of the image files created.
     */
    public final List<String> ToPngImages(String fileNamePattern, ToImageOptions options)
            throws IOException {
        return ToImage(fileNamePattern, "png", options);
    }

    /**
     * Renders the pages of the PDF as specific image files type and saves them to disk. <p>Specific
     * image dimensions and page numbers may be given as optional parameters</p> <p>FileNamePattern
     * should normally contain an asterisk (*) character which will be substituted for the page
     * numbers</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.
     *                        E.g.  C:\images\pdf_page_*.jpg
     * @param imageFileType   a specific image file type without dot.  E.g.  "jpg", "png", "bmp",
     *                        "gif", "tiff"
     * @param options          The {@link ToImageOptions}
     * @return An array of the file paths of the image files created.
     */
    public List<String> ToImage(String fileNamePattern, String imageFileType, ToImageOptions options)
            throws IOException {

        List<byte[]> dataList = com.ironsoftware.ironpdf.staticapi.Image_Api.PdfToImage(
                internalPdfDocument, options.getPageSelection().getPageList(),
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
     * Renders the pages of the PDF as JPEG files and saves them to disk. <p>Specific image dimensions
     * and page numbers may be given as optional parameters</p> <p>FileNamePattern should normally
     * contain an asterisk (*) character which will be substituted for the page numbers</p>
     *
     * @param fileNamePattern A full or partial file path for the output files containing an asterisk.
     *                        E.g.  C:\images\pdf_page_*.jpg
     * @param options          The {@link ToImageOptions}
     * @return An array of the file paths of the image files created.
     */
    public final List<String> ToJpegImages(String fileNamePattern, ToImageOptions options)
            throws IOException {
        return ToImage(fileNamePattern, "jpg", options);
    }

    //endregion

    //region FromImage

    /**
     * Compress existing images using JPG encoding and the specified settings
     *
     * @param quality Quality (1 - 100) to use during compression
     */
    public final void CompressImages(int quality) throws IOException {
        CompressImages(quality, false);
    }

    /**
     * Compress existing images using JPG encoding and the specified settings
     *
     * @param quality            Quality (1 - 100) to use during compression
     * @param scaleToVisibleSize Scale down the image resolution according to its visible size in the
     *                           PDF document; may cause distortion with some image configurations.
     *                           Default is false.
     */
    public final void CompressImages(int quality, boolean scaleToVisibleSize) throws IOException {
        if (quality < 1 || quality > 100) {
            throw new IndexOutOfBoundsException(String.format(
                    "Invalid quality specifier (%1$s) when compressing images. Quality must be between 1 and 100.",
                    quality));
        }
        com.ironsoftware.ironpdf.staticapi.Compress_Api.CompressImages(internalPdfDocument, quality,
                scaleToVisibleSize);
    }

    /**
     * Finds all embedded Images from within the PDF and returns as list of image bytes
     *
     * @return IEnumerable of Image.  The extracted images as System.Drawing Objects
     */
    public final List<BufferedImage> ExtractAllImages() throws IOException {
        return ExtractAllRawImages().stream().map(bytes -> {
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

    //endregion

    //region Save

    /**
     * Finds all embedded Images from within the PDF and returns as list of image bytes
     *
     * @return IEnumerable of Image bytes.  The extracted images as System.Drawing Objects
     */
    public final List<byte[]> ExtractAllRawImages() throws IOException {
        if (com.ironsoftware.ironpdf.staticapi.Page_Api.GetPagesInfo(internalPdfDocument).size()
                == 0) {
            return new ArrayList<>();
        }
        return com.ironsoftware.ironpdf.staticapi.Image_Api.ExtractAllImages(internalPdfDocument);
    }

    /**
     * Finds all embedded Images from within the PDF and returns as list of image bytes
     *
     * @param pageSelection A selected page index(es). Default is PageSelection.AllPages().
     * @return IEnumerable of Image bytes.  The extracted images as System.Drawing Objects
     */
    public final List<BufferedImage> ExtractAllImagesFromPages(PageSelection pageSelection)
            throws IOException {
        return ExtractAllRawImagesFromPages(pageSelection).stream().map(bytes -> {
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
    //endregion

    //region Security

    /**
     * Finds all embedded Images from within the PDF and returns then as System.Drawing.Image objects
     *
     * @param pageSelection A selected page index(es). Default is PageSelection.AllPages().
     * @return IEnumerable of Image.  The extracted images as System.Drawing Objects
     */
    public final List<byte[]> ExtractAllRawImagesFromPages(PageSelection pageSelection)
            throws IOException {
        if (com.ironsoftware.ironpdf.staticapi.Page_Api.GetPagesInfo(internalPdfDocument).size()
                == 0) {
            return new ArrayList<>();
        }
        return com.ironsoftware.ironpdf.staticapi.Image_Api.ExtractAllImages(internalPdfDocument,
                pageSelection.getPageList());
    }

    /**
     * Saves the PdfDocument to a file. <p>Supports site relative paths staring with "~/" in .Net
     * Framework Web 4+ Applications</p>
     *
     * @param FilePath File Path
     * @return This PdfDocument for fluid code notation.
     */
    public final PdfDocument SaveAs(Path FilePath) throws IOException {
        com.ironsoftware.ironpdf.staticapi.PdfDocument_Api.SaveAs(internalPdfDocument,
                FilePath.toAbsolutePath().toString());
        return this;
    }

    /**
     * Gets the binary data for the full PDF file as a byte array.
     */
    public final byte[] getBinaryData() throws IOException {
        return PdfDocument_Api.GetBytes(internalPdfDocument);
    }

    /**
     * Gets the owner password and enables 128-bit encryption of PDF content. An owner password is one
     * used to enable and disable all other security settings. <p>OwnerPassword must be set to a non-empty
     * string value for AllowUserCopyPasteContent, AllowUserAnnotation, AllowUserFormData,
     * AllowUserPrinting, AllowUserEdits to be restricted.
     */
    public final String getOwnerPassword() throws IOException {
        return Security_Api.GetPdfSecuritySettings(internalPdfDocument).getOwnerPassword();
    }

    /**
     * Sets the owner password and enables 128-bit encryption of PDF content. An owner password is one
     * used to * enable and disable all other security settings. <p>OwnerPassword must be set to a non-empty
     * string value * for AllowUserCopyPasteContent, AllowUserAnnotation, AllowUserFormData,
     * AllowUserPrinting, AllowUserEdits to be * restricted.
     */
    public final void setOwnerPassword(String value) throws IOException {
        Security_Api.GetPdfSecuritySettings(internalPdfDocument).setOwnerPassword(value);
    }

    /**
     * Gets a Password used to protect and encrypt the PDF File. Setting a password will cause IronPDF
     * to automatically protect the PDF file content using strong 128-bit encryption. Setting the
     * password to null will remove any existing password.
     */
    public final String getPassword() throws IOException {
        return Security_Api.GetPdfSecuritySettings(internalPdfDocument).getOwnerPassword();
    }

    /**
     * Sets a Password used to protect and encrypt the PDF File. Setting a password will cause IronPDF
     * to automatically protect the PDF file content using strong 128-bit encryption. Setting the
     * password to null will remove any existing password.
     */
    public final void setPassword(String value) throws IOException {
        Security_Api.GetPdfSecuritySettings(internalPdfDocument).setOwnerPassword(value);
    }

    /**
     * Removes all user and owner password security for a PDF document.  Also disables content
     * encryption.
     * <p>Content is encrypted at 128 bit. Copy and paste of content is disallowed. Annotations and
     * form
     * editing are disabled.</p>
     */
    public final void RemovePasswordsAndEncryption() throws IOException {
        Security_Api.RemovePasswordsAndEncryption(internalPdfDocument);
    }
    //endregion

    //region Metadata

    /**
     * Makes this PDF document read only such that: <p>Content is encrypted at 128 bit. Copy and paste
     * of content is disallowed. Annotations and form editing are disabled.</p>
     *
     * @param ownerPassword The owner password for the PDF.  A string for owner password is required
     *                      to enable PDF encryption and all document security options.
     */
    public void MakePdfDocumentReadOnly(String ownerPassword) throws IOException {
        Security_Api.MakePdfDocumentReadOnly(internalPdfDocument, ownerPassword);
    }

    /**
     * Sets advanced security settings for the PDF. <p>Allows the developer to control user access
     * passwords, encryption, and also who may edit, print and copy content from the PDF document</p>
     *
     * @param pdfSecuritySettings Advanced security settings for this PDF as an instance of {@link PdfSecuritySettings}
     */
    public void SetPdfSecuritySettings(com.ironsoftware.ironpdf.security.PdfSecuritySettings pdfSecuritySettings) throws IOException {
        Security_Api.SetPdfSecuritySettings(internalPdfDocument, pdfSecuritySettings);
    }

    //endregion

    //region Annotation

    /**
     * Gets advanced security settings for the PDF. <p>Allows the developer to control user access
     * passwords, encryption, and also who may edit, print and copy content from the PDF document</p>
     */
    public com.ironsoftware.ironpdf.security.PdfSecuritySettings GetPdfSecuritySettings()
            throws IOException {
        return Security_Api.GetPdfSecuritySettings(internalPdfDocument);
    }

    /**
     * Gets metadata manager for this PDF document.
     */
    public MetadataManager getMetadataManager() {
        return metadataManager;
    }

    //endregion

    //region Form

    /**
     * Gets metadata manager for this PDF document.
     */
    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    /**
     * Gets form manager for this PDF document.
     */
    public FormManager getFormManager() {
        return formManager;
    }
    //endregion

    //region Print

    /**
     * Prints this PDF by sending it to the computer's printer. <p>For advanced real-world printing
     * options please implement your own java.awt.print code. This class {@link PdfDocument}
     * implements java.awt.print.Printable.
     */
    public void Print() throws PrinterException, IOException {
        com.ironsoftware.ironpdf.staticapi.Print_Api.Print(internalPdfDocument, true);
    }

    /**
     * Prints this PDF by sending it to the computer's printer. <p>For advanced real-world printing
     * options please implement your own java.awt.print code. This class {@link PdfDocument}
     * implements java.awt.print.Printable.
     */
    public void PrintWithoutDialog() throws PrinterException, IOException {
        com.ironsoftware.ironpdf.staticapi.Print_Api.Print(internalPdfDocument, false);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        return internalPdfDocument.print(graphics, pageFormat, pageIndex);
    }

    //endregion

    //region Stamp

    /**
     * Adds Watermark to PDF, Please use {@link #ApplyStamp(Stamper)} for more control. <p>For
     * more information and a code example please visit: https:
     * //ironpdf.com/tutorials/csharp-edit-pdf-complete-tutorial/#add-a-watermark-to-a-pdf</p>
     *
     * @param html              The HTML fragment which will be stamped onto your PDF.
     * @param opacity           Watermark transparent value. 0 is invisible, 100 if fully opaque.
     * @param verticalAlignment The vertical alignment of the watermark relative to the page.
     * @return Returns this  {@link PdfDocument}, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument ApplyWatermark(String html, int opacity,
                                            VerticalAlignment verticalAlignment) throws IOException {
        return ApplyWatermark(html, opacity, verticalAlignment, HorizontalAlignment.CENTER);
    }

    /**
     * Adds Watermark to PDF, Please use {@link #ApplyStamp(Stamper)} for more control. <p>For
     * more information and a code example please visit: https:
     * //ironpdf.com/tutorials/csharp-edit-pdf-complete-tutorial/#add-a-watermark-to-a-pdf</p>
     *
     * @param html                The HTML fragment which will be stamped onto your PDF.
     * @param opacity             Watermark transparent value. 0 is invisible, 100 if fully opaque.
     * @param verticalAlignment   The vertical alignment of the watermark relative to the page.
     * @param horizontalAlignment The horizontal alignment of the watermark relative to the page.
     * @return Returns this  {@link PdfDocument}, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument ApplyWatermark(String html, int opacity,
                                            VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment)
            throws IOException {
        HtmlStamper stamper = new HtmlStamper(html);
        stamper.setVerticalAlignment(verticalAlignment);
        stamper.setHorizontalAlignment(horizontalAlignment);
        stamper.setOpacity(opacity);
        ApplyStamp(stamper);
        return this;
    }

    /**
     * Edits the PDF by applying the  {@link Stamper}'s rendered to every page.
     *
     * @param stamper The  {@link Stamper} object that has the HTML to be stamped onto the PDF.
     * @return Returns this {@link PdfDocument}, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument ApplyStamp(Stamper stamper) throws IOException {
        ApplyStamp(stamper, PageSelection.AllPages());
        return this;
    }

    /**
     * Edits the PDF by applying the  {@link Stamper}'s rendered to only selected page(s).
     *
     * @param stamper       The {@link Stamper} object that has the HTML to be stamped onto the PDF.
     * @param pageSelection A selected page index(es). Default is PageSelection.AllPages().
     * @return Returns this  {@link PdfDocument}, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument ApplyStamp(Stamper stamper, PageSelection pageSelection)
            throws IOException {
        Stamp_Api.ApplyStamp(internalPdfDocument, stamper, pageSelection.getPageList());
        return this;
    }

    /**
     * Adds Watermark to PDF, Please use {@link #ApplyStamp(Stamper)} for more control. <p>For
     * more information and a code example please visit: https:
     * //ironpdf.com/tutorials/csharp-edit-pdf-complete-tutorial/#add-a-watermark-to-a-pdf</p>
     *
     * @param html    The HTML fragment which will be stamped onto your PDF.
     * @param opacity Watermark transparent value. 0 is invisible, 100 if fully opaque.
     * @return Returns this  {@link PdfDocument}, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument ApplyWatermark(String html, int opacity) throws IOException {
        return ApplyWatermark(html, opacity, VerticalAlignment.MIDDLE, HorizontalAlignment.CENTER);
    }

    /**
     * Adds Watermark to PDF, Please use {@link #ApplyStamp(Stamper)} for more control. <p>For
     * more information and a code example please visit: https:
     * //ironpdf.com/tutorials/csharp-edit-pdf-complete-tutorial/#add-a-watermark-to-a-pdf</p>
     *
     * @param html The HTML fragment which will be stamped onto your PDF.
     * @return Returns this  {@link PdfDocument}, allowing for a 'fluent'  chained in-line
     * code style
     */
    public final PdfDocument ApplyWatermark(String html) throws IOException {
        return ApplyWatermark(html, 50, VerticalAlignment.MIDDLE, HorizontalAlignment.CENTER);
    }

    //endregion

    //region Text

    /**
     * Extracts the written text content from the PDF and returns it as a string. <p>Pages are
     * separated by 4 consecutive Environment.NewLines</p>
     *
     * @return All text in the PDF as a string.
     */
    public final String ExtractAllText() throws IOException {
        return com.ironsoftware.ironpdf.staticapi.Text_Api.ExtractAllText(internalPdfDocument);
    }

    /**
     * Extracts the text content from one page of the PDF and returns it as a string.
     *
     * @param pageSelection A selected page index(es). Default is PageSelection.AllPages().
     * @return The text extracted from the PDF page as a string.
     */
    public final String ExtractTextFromPage(PageSelection pageSelection) throws IOException {
        return com.ironsoftware.ironpdf.staticapi.Text_Api.ExtractAllText(internalPdfDocument,
                pageSelection.getPageList());
    }

    /**
     * Replace the specified old text with new text on a given page
     *
     * @param pageIndex Page index to search for old text to replace
     * @param oldText   Old text to remove
     * @param newText   New text to add
     */
    public final void ReplaceTextOnPage(int pageIndex, String oldText, String newText)
            throws IOException {
        com.ironsoftware.ironpdf.staticapi.Text_Api.ReplaceTextOnPage(internalPdfDocument, pageIndex,
                oldText, newText);
    }

    //endregion
}
