package com.example.matzipbookserver.bookmark.controller.dto.response;

import java.util.List;

public record BookmarkResponse(
        List<BookmarkInfo> bookmarks
) {}
