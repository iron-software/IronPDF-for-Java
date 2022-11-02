package com.ironsoftware.ironpdf.edit;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A PageSelection is a collection of pages from a PDF document. It can be used to perform
 * operations on a subset of pages from a PDF document.
 */
public class PageSelection {

    /**
     * The left margin of the header on the page in mm. Default is 0mm.
     */
    private List<Integer> pagesList = null;

    public static PageSelection AllPages() {
        return new PageSelection();
    }

    /**
     * Specific page index. Note: Page 1 has index 0
     */
    public static PageSelection SinglePage(int pageIndex) {
        PageSelection ps = new PageSelection();
        ps.setPageList(Collections.singleton(pageIndex));
        return ps;
    }

    /**
     * First page
     */
    public static PageSelection FirstPage() {
        PageSelection ps = new PageSelection();
        ps.setPageList(Collections.singleton(0));
        return ps;
    }

    /**
     * The list of pages to be used.
     *
     * @param startIndex The index of the first PDF page. Note: Page 1 has index 0
     * @param endIndex   The index of the last PDF page.
     */
    public static PageSelection PageRange(int startIndex, int endIndex) {
        PageSelection ps = new PageSelection();
        ps.setPageList(IntStream.range(startIndex, endIndex).boxed().collect(Collectors.toList()));
        return ps;
    }

    /**
     * The list of pages to be used.
     *
     * @param pageList The list of pages index of the PDF. Note: Page 1 has index 0
     */
    public static PageSelection PageRange(Iterable<Integer> pageList) {
        PageSelection ps = new PageSelection();
        ps.setPageList(pageList);
        return ps;
    }

    public List<Integer> getPageList() {
        return new ArrayList<>(pagesList);
    }

    public void setPageList(Iterable<Integer> marginLeftMm) {
        pagesList = Lists.newArrayList(marginLeftMm);
    }
}
