package com.example.matzipbookserver.bookmark.controller;

import com.example.matzipbookserver.bookmark.controller.dto.request.BookmarkRequest;
import com.example.matzipbookserver.bookmark.controller.dto.response.BookmarkInfo;
import com.example.matzipbookserver.bookmark.controller.dto.response.BookmarkResponse;
import com.example.matzipbookserver.bookmark.response.success.BookmarkSuccess;
import com.example.matzipbookserver.bookmark.service.BookmarkService;
import com.example.matzipbookserver.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/{userId}")
    public SuccessResponse<BookmarkResponse> getUserBookmarks(@PathVariable Long userId) {
        List<BookmarkInfo> bookmarks = bookmarkService.getBookmarksByUserId(userId);
        return SuccessResponse.of(BookmarkSuccess.FETCHED, new BookmarkResponse(bookmarks));
    }

    @PostMapping
    public SuccessResponse<Void> saveBookmark(@RequestBody BookmarkRequest request) {
        bookmarkService.saveBookmark(request);
        return SuccessResponse.of(BookmarkSuccess.CREATED, null);
    }

    @DeleteMapping
    public SuccessResponse<Void> deleteBookmark(@RequestBody BookmarkRequest request) {
        bookmarkService.deleteBookmark(request);
        return SuccessResponse.of(BookmarkSuccess.DELETED, null);
    }
}