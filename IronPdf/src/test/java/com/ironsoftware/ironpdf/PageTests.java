package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.TestBase;
import com.ironsoftware.ironpdf.PdfDocument;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.ironsoftware.ironpdf.page.PageInfo;
import com.ironsoftware.ironpdf.page.PageRotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class PageTests extends TestBase {

    @Test
    public final void RemovePageTest() throws IOException {
        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/twoPages.pdf"));

        doc.removePages(PageSelection.singlePage(0));
        Assertions.assertEquals(1, doc.getPagesInfo().size());
    }

    @Test
    public final void MergePageTest() throws IOException {

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        PdfDocument docGoogle = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        PdfDocument mergedPdf = PdfDocument.merge(doc, docGoogle);

        Assertions.assertEquals(2, mergedPdf.getPagesInfo().size());
    }

    @Test
    public final void InsertPageTest() throws IOException {

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/twoPages.pdf"));

        PdfDocument docSub = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        List<PageInfo> docSubPageInfo = docSub.getPagesInfo();

        doc.insertPdf(docSub, 0);

        List<PageInfo> pageInfo = doc.getPagesInfo();
        Assertions.assertEquals(3, pageInfo.size());
        Assertions.assertEquals(docSubPageInfo.get(0).getHeight(), pageInfo.get(0).getHeight());
    }

    @Test
    public final void AppendPdfTest() throws IOException {

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        List<PageInfo> mainDocPageInfo = doc.getPagesInfo();

        PdfDocument docGoogle = PdfDocument.fromFile(getTestPath("/Data/google.pdf"));

        doc.appendPdf(docGoogle);

        List<PageInfo> pageInfo = doc.getPagesInfo();
        Assertions.assertEquals(2, pageInfo.size());
        Assertions.assertEquals(mainDocPageInfo.get(0).getHeight(), pageInfo.get(0).getHeight());
    }

    @Test
    public final void RotatePageTest() throws IOException {

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));

        List<PageInfo> pageInfoBefore = doc.getPagesInfo();
        Assertions.assertEquals(PageRotation.NONE, pageInfoBefore.get(0).getPageRotation());

        // TODO rotate without PageSelection
        doc.rotatePage(PageRotation.CLOCKWISE_90, PageSelection.allPages());

        List<PageInfo> pageInfoAfter = doc.getPagesInfo();
        Assertions.assertEquals(PageRotation.CLOCKWISE_90, pageInfoAfter.get(0).getPageRotation());
    }

    @Test
    public final void CopyPageTest() throws IOException {

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        List<PageInfo> pageInfoBefore = doc.getPagesInfo();

        Assertions.assertEquals(1, pageInfoBefore.size());

        PdfDocument copiedDoc = doc.copyPage(0);

        List<PageInfo> copiedDocPageInfo = copiedDoc.getPagesInfo();
        Assertions.assertEquals(1, copiedDocPageInfo.size());
        Assertions.assertEquals(pageInfoBefore.get(0).getHeight(),
                copiedDocPageInfo.get(0).getHeight());
    }

    @Test
    public final void GetPagesInfoTest() throws IOException {

        PdfDocument doc = PdfDocument.fromFile(getTestPath("/Data/empty.pdf"));
        List<PageInfo> pageInfo = doc.getPagesInfo();

        Assertions.assertEquals(1, pageInfo.size());
    }

}
