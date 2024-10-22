package com.ironsoftware.ironpdf.internal.staticapi;

public class BookmarkDescriptor {
    private String hierarchy;
    private int pageIndex;
    private String text;

    // Getter and Setter for Hierarchy
    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    // Getter and Setter for PageIndex
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    // Getter and Setter for Text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BookmarkDescriptor(String hierarchy, int pageIndex, String text) {
        this.hierarchy = hierarchy;
        this.pageIndex = pageIndex;
        this.text = text;
    }
}