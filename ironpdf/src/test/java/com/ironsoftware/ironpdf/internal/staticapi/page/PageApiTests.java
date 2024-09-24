package com.ironsoftware.ironpdf.internal.staticapi.page;


import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Page_Api;
import com.ironsoftware.ironpdf.internal.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PageApiTests extends TestBase {

    @Test
    public final void RemovePageTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/twoPages.pdf"));

        Page_Api.removePage(doc, Collections.singletonList(0));
        Assertions.assertEquals(1, Page_Api.getPagesInfo(doc).size());
    }

    @Test
    public final void MergePageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        InternalPdfDocument docGoogle = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));

        InternalPdfDocument mergedPdf = Page_Api.mergePage(Arrays.asList(doc, docGoogle));

        Assertions.assertEquals(2, Page_Api.getPagesInfo(mergedPdf).size());
    }

    @Test
    public final void InsertPageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/twoPages.pdf"));

        InternalPdfDocument docSub = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        List<PageInfo> docSubPageInfo = Page_Api.getPagesInfo(docSub);

        Page_Api.insertPage(doc, docSub, 0);

        List<PageInfo> pageInfo = Page_Api.getPagesInfo(doc);
        Assertions.assertEquals(3, pageInfo.size());
        Assertions.assertEquals(docSubPageInfo.get(0).getHeight(), pageInfo.get(0).getHeight());
    }

    @Test
    public final void AppendPdfTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        List<PageInfo> mainDocPageInfo = Page_Api.getPagesInfo(doc);

        InternalPdfDocument docGoogle = PdfDocument_Api.fromFile(getTestFile("/Data/google.pdf"));

        Page_Api.appendPdf(doc, docGoogle);

        List<PageInfo> pageInfo = Page_Api.getPagesInfo(doc);
        Assertions.assertEquals(2, pageInfo.size());
        Assertions.assertEquals(mainDocPageInfo.get(0).getHeight(), pageInfo.get(0).getHeight());
    }

    @Test
    public final void RotatePageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));

        List<PageInfo> pageInfoBefore = Page_Api.getPagesInfo(doc);
        Assertions.assertEquals(PageRotation.NONE, pageInfoBefore.get(0).getPageRotation());

        Page_Api.setPageRotation(doc, PageRotation.CLOCKWISE_90);

        List<PageInfo> pageInfoAfter = Page_Api.getPagesInfo(doc);
        Assertions.assertEquals(PageRotation.CLOCKWISE_90, pageInfoAfter.get(0).getPageRotation());
    }

    @Test
    public final void CopyPageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        List<PageInfo> pageInfoBefore = Page_Api.getPagesInfo(doc);

        Assertions.assertEquals(1, pageInfoBefore.size());

        InternalPdfDocument copiedDoc = Page_Api.copyPage(doc, Collections.singletonList(0));

        List<PageInfo> copiedDocPageInfo = Page_Api.getPagesInfo(copiedDoc);
        Assertions.assertEquals(1, copiedDocPageInfo.size());
        Assertions.assertEquals(pageInfoBefore.get(0).getHeight(),
                copiedDocPageInfo.get(0).getHeight());
    }

    @Test
    public final void GetPagesInfoTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.fromFile(getTestFile("/Data/empty.pdf"));
        List<PageInfo> pageInfo = Page_Api.getPagesInfo(doc);

        Assertions.assertEquals(1, pageInfo.size());
    }
}
