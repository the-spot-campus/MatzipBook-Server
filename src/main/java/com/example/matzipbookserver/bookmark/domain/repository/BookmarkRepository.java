package com.example.matzipbookserver.bookmark.domain.repository;

import com.example.matzipbookserver.bookmark.domain.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserId(Long userId);
}