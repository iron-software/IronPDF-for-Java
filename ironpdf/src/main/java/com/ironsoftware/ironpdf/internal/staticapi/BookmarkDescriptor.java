package com.ironsoftware.ironpdf.internal.staticapi;

public class BookmarkDescriptor {
    private String hierarchy;
    private int pageIndex;
    private String text;
    private String itemId;
    private String parentItemId;

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
    
    // Getter and Setter for Item ID
    public String getItemId() {
        return itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    // Getter and Setter for Parent Item ID
    public String getParentItemId() {
        return parentItemId;
    }

    public void setParentItemId(String parentItemId) {
        this.parentItemId = parentItemId;
    }

    public BookmarkDescriptor(String hierarchy, int pageIndex, String text, String itemId, String parentItemId) {
        this.hierarchy = hierarchy;
        this.pageIndex = pageIndex;
        this.text = text;
        this.itemId = itemId;
        this.parentItemId = parentItemId;
    }
}