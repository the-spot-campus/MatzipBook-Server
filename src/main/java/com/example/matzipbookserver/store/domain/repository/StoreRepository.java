package com.example.matzipbookserver.store.domain.repository;

import com.example.matzipbookserver.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByKakaoPlaceId(String kakaoPlaceId);
}
