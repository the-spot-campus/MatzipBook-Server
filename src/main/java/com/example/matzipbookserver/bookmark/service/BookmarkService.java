package com.example.matzipbookserver.bookmark.service;

import com.example.matzipbookserver.bookmark.controller.dto.response.BookmarkInfo;
import com.example.matzipbookserver.bookmark.domain.repository.BookmarkRepository;
import com.example.matzipbookserver.bookmark.domain.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public List<BookmarkInfo> getBookmarksByUserId(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);
        return bookmarks.stream()
                .map(bookmark -> new BookmarkInfo(bookmark.getStoreId()))
                .toList();
    }
}