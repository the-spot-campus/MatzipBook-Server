package com.example.matzipbookserver.store.controller.dto;

import com.example.matzipbookserver.store.domain.Store;
import lombok.Builder;

@Builder
public record StoreResponseDto(
        String kakaoPlaceId,
        String name,
        String address,
        String roadAddress,
        String phone,
        double x,
        double y,
        String kakaoPlaceUrl
) {

    public static StoreResponseDto from(Store store) {
        return new StoreResponseDto(
                store.getKakaoPlaceId(),
                store.getName(),
                store.getAddress(),
                store.getRoadAddress(),
                store.getPhone(),
                store.getX(),
                store.getY(),
                store.getKakaoPlaceUrl()
        );
    }
}
