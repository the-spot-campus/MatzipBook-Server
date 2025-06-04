package com.example.matzipbookserver.bookmark.controller;

import com.example.matzipbookserver.bookmark.controller.dto.response.BookmarkInfo;
import com.example.matzipbookserver.bookmark.controller.dto.response.BookmarkResponse;
import com.example.matzipbookserver.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/{userId}")
    public ResponseEntity<BookmarkResponse> getUserBookmarks(@PathVariable Long userId) {
        List<BookmarkInfo> bookmarks = bookmarkService.getBookmarksByUserId(userId);
        return ResponseEntity.ok(new BookmarkResponse(bookmarks));
    }
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