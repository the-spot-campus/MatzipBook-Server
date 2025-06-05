package com.example.matzipbookserver.store.domain.repository;

import com.example.matzipbookserver.store.domain.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

    @Query("SELECT fc.name FROM FoodCategory fc")
    List<String> findAllNames();
}
