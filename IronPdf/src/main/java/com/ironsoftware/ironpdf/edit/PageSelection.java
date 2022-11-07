package com.ironsoftware.ironpdf.edit;

import com.google.common.collect.Lists;
import com.ironsoftware.ironpdf.internal.staticapi.InternalPdfDocument;
import com.ironsoftware.ironpdf.internal.staticapi.Page_Api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A PageSelection is a collection of pages from a PDF document. 
 * <p>PageSelection can be used to perform operations on a subset of pages from a PDF document.</p>
 * <p>See: {@link com.ironsoftware.ironpdf.PdfDocument}</p>
 */
public class PageSelection {

    private List<Integer> pagesList = new ArrayList<>();

    /**
     * Every page of the PDF.
     */
    public static PageSelection allPages() {
        return new PageSelection();
    }

    /**
     * Specific page index. <p>Note: Page 1 has index 0</p>
     */
    public static PageSelection singlePage(int pageIndex) {
        PageSelection ps = new PageSelection();
        ps.setPageList(Lists.newArrayList(pageIndex));
        return ps;
    }

    /**
     * First page (page index 0).
     */
    public static PageSelection firstPage() {
        PageSelection ps = new PageSelection();
        ps.setPageList(Lists.newArrayList(0));
        return ps;
    }

    /**
     * First page (page index 0).
     */
    public static PageSelection lastPage() {
        PageSelection ps = new PageSelection();
        ps.setPageList(Lists.newArrayList(-1));
        return ps;
    }

    /**
     * The selection of pages to be used.
     *
     * @param startIndex The index of the first PDF page. Note: Page 1 has index 0
     * @param endIndex   The index of the last PDF page.
     */
    public static PageSelection pageRange(int startIndex, int endIndex) {
        PageSelection ps = new PageSelection();
        ps.setPageList(IntStream.range(startIndex, endIndex).boxed().collect(Collectors.toList()));
        return ps;
    }

    /**
     * PageSelection factory.  Generates a list of page indexes to be used.
     *
     * @param pageList The list of pages index of the PDF. Note: Page 1 has index 0
     */
    public static PageSelection pageRange(List<Integer> pageList) {
        PageSelection ps = new PageSelection();
        ps.setPageList(pageList);
        return ps;
    }

    public List<Integer> getPageList(InternalPdfDocument internalPdfDocument) {
        return pagesList.stream().map(i->{
            if(i == -1){
                return Page_Api.getPagesInfo(internalPdfDocument).size() - 1;
            }
            return i;
        }).collect(Collectors.toList());
    }

    public void setPageList(List<Integer> pageList) {
        pagesList = Lists.newArrayList(pageList);
    }
}
