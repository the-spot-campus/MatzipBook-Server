package com.example.matzipbookserver.store.domain.repository;

import com.example.matzipbookserver.store.domain.MoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoodCategoryRepository extends JpaRepository<MoodCategory, Long> {

    @Query("SELECT mc.name FROM MoodCategory mc")
    List<String> findAllNames();
}
