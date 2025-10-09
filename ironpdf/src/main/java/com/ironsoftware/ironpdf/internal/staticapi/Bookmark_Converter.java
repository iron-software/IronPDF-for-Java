package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.bookmark.BookmarkDestinations;
import com.ironsoftware.ironpdf.internal.proto.PdfBookmarkCollectionP;
import com.ironsoftware.ironpdf.internal.proto.PdfiumBookmarkDescriptorCollectionP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ironsoftware.ironpdf.bookmark.BookmarkDestinations.PAGE;

final class Bookmark_Converter {

    static List<Bookmark> fromProto(PdfiumBookmarkDescriptorCollectionP proto) {
        return convertDescriptorsToBookmarks(proto.getBookmarkDescriptorsList().stream().map(Bookmark_Converter::fromProto)
                .collect(Collectors.toList()));
    }

    static BookmarkDescriptor fromProto(com.ironsoftware.ironpdf.internal.proto.PdfiumBookmarkDescriptorP proto) {
        return new BookmarkDescriptor(
            proto.getHierarchy(),
            proto.getPageIndex(),
            proto.getText(),
            proto.getItemId(),
            proto.getParentItemId()
        );
    }

    public static List<Bookmark> convertDescriptorsToBookmarks(List<BookmarkDescriptor> descriptors) {
        List<Bookmark> bookmarks = new ArrayList<>();

        final String regex = "\\\\";
        for (int i = 0; i < descriptors.size(); i++) {
            BookmarkDescriptor descriptor = descriptors.get(i);

            String text = descriptor.getText();
            int pageIndex = descriptor.getPageIndex();

            // Setting parentBookmarkText using the hierarchy
            String[] hierarchyParts = Arrays.stream(descriptor.getHierarchy().split(regex)).filter(part -> !part.isEmpty()).toArray(String[]::new);
            String parentBookmarkText = "";
            if (hierarchyParts.length > 1) { // Check to ensure there's a parent
                parentBookmarkText = hierarchyParts[hierarchyParts.length - 1]; // Parent is the second last element in split
            }

            // Setting previous and next bookmark text
            String previousBookmarkText = "";
            String nextBookmarkText = "";

            if (i > 0) {
                BookmarkDescriptor prevDescriptor = descriptors.get(i - 1);
                String[] prevHierarchyParts = Arrays.stream(prevDescriptor.getHierarchy().split(regex)).filter(part -> !part.isEmpty()).toArray(String[]::new);
                String prevParent = (prevHierarchyParts.length > 1) ? prevHierarchyParts[prevHierarchyParts.length - 1] : "";

                if (parentBookmarkText != null && parentBookmarkText.equals(prevParent)) {
                    previousBookmarkText = prevDescriptor.getText();
                }
            }

            if (i < descriptors.size() - 1) {
                BookmarkDescriptor nextDescriptor = descriptors.get(i + 1);
                String[] nextHierarchyParts = Arrays.stream(nextDescriptor.getHierarchy().split(regex)).filter(part -> !part.isEmpty()).toArray(String[]::new);
                String nextParent = (nextHierarchyParts.length > 1) ? nextHierarchyParts[nextHierarchyParts.length - 1] : "";

                if (parentBookmarkText != null && parentBookmarkText.equals(nextParent)) {
                    nextBookmarkText = nextDescriptor.getText();
                }
            }

            bookmarks.add(new Bookmark(text, pageIndex, parentBookmarkText, PAGE, nextBookmarkText, previousBookmarkText));
        }

        return bookmarks;
    }
}
