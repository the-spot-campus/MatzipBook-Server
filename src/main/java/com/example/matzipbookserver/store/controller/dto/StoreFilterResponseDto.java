package com.example.matzipbookserver.store.controller.dto;

import com.example.matzipbookserver.store.domain.Store;
import lombok.Builder;

import java.util.List;

@Builder
public record StoreFilterResponseDto(
    Long id,
    String name,
    String address,
    double x,
    double y,
    double rating,
    List<String> foodCategories,
    List<String> moodCategories
) {

    public static StoreFilterResponseDto from(Store store) {
        return StoreFilterResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .x(store.getX())
                .y(store.getY())
                .rating(store.getVoteCount())
                .foodCategories(
                        store.getStoreFoodCategories().stream()
                                .map(sfc -> sfc.getFoodCategory().getName())
                                .toList()
                )
                .moodCategories(
                        store.getStoreMoodCategories().stream()
                                .map(smc -> smc.getMoodCategory().getName())
                                .toList()
                )
                .build();
    }
}
