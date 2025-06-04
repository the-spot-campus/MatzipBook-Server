package com.example.matzipbookserver.bookmark.domain.repository;

import com.example.matzipbookserver.bookmark.domain.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    List<BookmarkEntity> findByUserId(Long userId);

    void deleteByUserIdAndStoreId(Long userId, Long storeId);
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}