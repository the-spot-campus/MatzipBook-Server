package com.example.matzipbookserver.store.domain.repository;

import com.example.matzipbookserver.store.domain.Store;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByKakaoPlaceId(String kakaoPlaceId);


    @Query(value = """
    SELECT *,
           (6371 * acos(cos(radians(:latitude)) * cos(radians(s.y)) *
                      cos(radians(s.x) - radians(:longitude)) +
                      sin(radians(:latitude)) * sin(radians(s.y)))) AS distance
    FROM store s 
    WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.y)) *
                     cos(radians(s.x) - radians(:longitude)) +
                     sin(radians(:latitude)) * sin(radians(s.y)))) <= :radius
    ORDER BY s.vote_count DESC
    """, countQuery = """
    SELECT COUNT(*)
    FROM store s 
    WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.y)) *
                     cos(radians(s.x) - radians(:longitude)) +
                     sin(radians(:latitude)) * sin(radians(s.y)))) <= :radius 
    """, nativeQuery = true)
    Page<Store> findNearByVoteCount(@Param("longitude") double longitude,
                                    @Param("latitude") double latitude,
                                    @Param("radius") double radius,
                                    Pageable pageable);

}
