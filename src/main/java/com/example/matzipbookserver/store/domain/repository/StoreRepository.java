package com.example.matzipbookserver.store.domain.repository;

import com.example.matzipbookserver.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByKakaoPlaceId(String kakaoPlaceId);

    //필터링 쿼리 (완전 일치 필터링)
    @Query("""
            SELECT s FROM Store s
            JOIN s.storeFoodCategories sfc
            JOIN sfc.foodCategory fc
            JOIN s.storeMoodCategories smc
            JOIN smc.moodCategory mc
            WHERE (:foods IS NULL OR fc.name IN :foods)
              AND (:moods IS NULL OR mc.name IN :moods)
            GROUP BY s.id
            HAVING (:foodCount = 0 OR COUNT(DISTINCT fc.name) = :foodCount)
               AND (:moodCount = 0 OR COUNT(DISTINCT mc.name) = :moodCount)
        """)
    List<Store> findByFilter(
            @Param("foods") List<String> foods,
            @Param("moods") List<String> moods,
            @Param("foodCount") long foodCount,
            @Param("moodCount") long moodCount
    );
}
