package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.bookmark.Bookmark;
import com.ironsoftware.ironpdf.bookmark.BookmarkDestinations;
import com.ironsoftware.ironpdf.internal.proto.BookmarkCollection;

import java.util.List;
import java.util.stream.Collectors;

final class Bookmark_Converter {

    static List<Bookmark> FromProto(BookmarkCollection proto) {
        return proto.getBookmarksList().stream().map(Bookmark_Converter::FromProto)
                .collect(Collectors.toList());
    }


    static Bookmark FromProto(com.ironsoftware.ironpdf.internal.proto.Bookmark proto) {
        return new Bookmark(proto.getText(),
                proto.getPageIndex(),
                proto.hasParentBookmarkText() ? proto.getParentBookmarkText() : null,
                FromProto(proto.getDestinationType()),
                proto.hasNextBookmarkText() ? proto.getNextBookmarkText() : null,
                proto.hasPreviousBookmarkText() ? proto.getPreviousBookmarkText() : null);
    }


    static BookmarkDestinations FromProto(
            com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations input) {
        return BookmarkDestinations.values()[input.getEnumValue()];
    }


    static com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations ToProto(
            BookmarkDestinations input) {
        com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations.Builder tempVar = com.ironsoftware.ironpdf.internal.proto.BookmarkDestinations.newBuilder();
        tempVar.setEnumValue(input.ordinal());
        return tempVar.build();
    }
}
