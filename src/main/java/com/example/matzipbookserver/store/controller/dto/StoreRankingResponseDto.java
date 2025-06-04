package com.example.matzipbookserver.store.controller.dto;

import com.example.matzipbookserver.store.domain.Store;

public record StoreRankingResponseDto(
        Long id,
        String kakaoPlaceId,
        String name,
        String address,
        double x,
        double y,
        int voteCount
) {

    public static StoreRankingResponseDto from(Store store) {
        return new StoreRankingResponseDto(
                store.getId(),
                store.getKakaoPlaceId(),
                store.getName(),
                store.getAddress(),
                store.getX(),
                store.getY(),
                store.getVoteCount()
        );
    }
}
