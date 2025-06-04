package com.example.matzipbookserver.bookmark.service;

import com.example.matzipbookserver.bookmark.controller.dto.request.BookmarkRequest;
import com.example.matzipbookserver.bookmark.controller.dto.response.BookmarkInfo;
import com.example.matzipbookserver.bookmark.domain.entity.BookmarkEntity;
import com.example.matzipbookserver.bookmark.domain.repository.BookmarkRepository;
import com.example.matzipbookserver.bookmark.response.error.BookmarkError;
import com.example.matzipbookserver.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public List<BookmarkInfo> getBookmarksByUserId(Long userId) {
        List<BookmarkEntity> bookmarks = bookmarkRepository.findByUserId(userId);
        return bookmarks.stream()
                .map(bookmark -> new BookmarkInfo(bookmark.getStoreId()))
                .toList();
    }

    public void saveBookmark(BookmarkRequest request) {
        boolean exists = bookmarkRepository.existsByUserIdAndStoreId(request.userId(), request.storeId());
        if (exists) {
            throw new RestApiException(BookmarkError.ALREADY_BOOKMARKED);
        }

        BookmarkEntity bookmark = BookmarkEntity.builder()
                .userId(request.userId())
                .storeId(request.storeId())
                .build();

        bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(BookmarkRequest request) {
        boolean exists = bookmarkRepository.existsByUserIdAndStoreId(request.userId(), request.storeId());
        if (!exists) {
            throw new RestApiException(BookmarkError.BOOKMARK_NOT_FOUND);
        }

        bookmarkRepository.deleteByUserIdAndStoreId(request.userId(), request.storeId());
    }
}