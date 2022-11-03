package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.bookmark.BookmarkDestinations;
import com.ironsoftware.ironpdf.internal.proto.BookmarkCollection;

import java.util.List;
import java.util.stream.Collectors;

final class Bookmark_Converter {

    static List<Bookmark> fromProto(BookmarkCollection proto) {
        return proto.getBookmarksList().stream().map(Bookmark_Converter::fromProto)
                .collect(Collectors.toList());
    }


    static Bookmark fromProto(com.ironsoftware.ironpdf.internal.proto.Bookmark proto) {
        return new Bookmark(proto.getText(),
                proto.getPageIndex(),
                proto.hasParentBookmarkText() ? proto.getParentBookmarkText() : null,
                fromProto(proto.getDestinationType()),
                proto.hasNextBookmarkText() ? proto.getNextBookmarkText() : null,
                proto.hasPreviousBookmarkText() ? proto.getPreviousBookmarkText() : null);
    }


    static BookmarkDestinations fromProto(
            com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations input) {
        return BookmarkDestinations.values()[input.getEnumValue()];
    }


    static com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations toProto(
            BookmarkDestinations input) {
        com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
