package com.ironsoftware.ironpdf.staticapi.page;


import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;
import com.ironsoftware.ironpdf.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.staticapi.Page_Api;
import com.ironsoftware.ironpdf.staticapi.PdfDocument_Api;
import com.ironsoftware.ironpdf.staticapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PageApiTests extends TestBase {

    @Test
    public final void RemovePageTest() throws IOException {
        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/twoPages.pdf"));

        Page_Api.RemovePage(doc, Collections.singletonList(0));
        Assertions.assertEquals(1, Page_Api.GetPagesInfo(doc).size());
    }

    @Test
    public final void MergePageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        InternalPdfDocument docGoogle = PdfDocument_Api.FromFile(getTestFile("/Data/google.pdf"));

        InternalPdfDocument mergedPdf = Page_Api.MergePage(Arrays.asList(doc, docGoogle));

        Assertions.assertEquals(2, Page_Api.GetPagesInfo(mergedPdf).size());
    }

    @Test
    public final void InsertPageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/twoPages.pdf"));

        InternalPdfDocument docSub = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        List<PageInfo> docSubPageInfo = Page_Api.GetPagesInfo(docSub);

        Page_Api.InsertPage(doc, docSub, 0);

        List<PageInfo> pageInfo = Page_Api.GetPagesInfo(doc);
        Assertions.assertEquals(3, pageInfo.size());
        Assertions.assertEquals(docSubPageInfo.get(0).getHeight(), pageInfo.get(0).getHeight());
    }

    @Test
    public final void AppendPdfTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        List<PageInfo> mainDocPageInfo = Page_Api.GetPagesInfo(doc);

        InternalPdfDocument docGoogle = PdfDocument_Api.FromFile(getTestFile("/Data/google.pdf"));

        Page_Api.AppendPdf(doc, docGoogle);

        List<PageInfo> pageInfo = Page_Api.GetPagesInfo(doc);
        Assertions.assertEquals(2, pageInfo.size());
        Assertions.assertEquals(mainDocPageInfo.get(0).getHeight(), pageInfo.get(0).getHeight());
    }

    @Test
    public final void RotatePageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));

        List<PageInfo> pageInfoBefore = Page_Api.GetPagesInfo(doc);
        Assertions.assertEquals(PageRotation.NONE, pageInfoBefore.get(0).getPageRotation());

        Page_Api.RotatePage(doc, PageRotation.CLOCKWISE_90);

        List<PageInfo> pageInfoAfter = Page_Api.GetPagesInfo(doc);
        Assertions.assertEquals(PageRotation.CLOCKWISE_90, pageInfoAfter.get(0).getPageRotation());
    }

    @Test
    public final void CopyPageTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        List<PageInfo> pageInfoBefore = Page_Api.GetPagesInfo(doc);

        Assertions.assertEquals(1, pageInfoBefore.size());

        InternalPdfDocument copiedDoc = Page_Api.CopyPage(doc, Collections.singletonList(0));

        List<PageInfo> copiedDocPageInfo = Page_Api.GetPagesInfo(copiedDoc);
        Assertions.assertEquals(1, copiedDocPageInfo.size());
        Assertions.assertEquals(pageInfoBefore.get(0).getHeight(),
                copiedDocPageInfo.get(0).getHeight());
    }

    @Test
    public final void GetPagesInfoTest() throws IOException {

        InternalPdfDocument doc = PdfDocument_Api.FromFile(getTestFile("/Data/empty.pdf"));
        List<PageInfo> pageInfo = Page_Api.GetPagesInfo(doc);

        Assertions.assertEquals(1, pageInfo.size());
    }
}
