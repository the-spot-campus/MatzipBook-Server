package com.example.matzipbookserver.store.service;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.StoreErrorCode;
import com.example.matzipbookserver.store.controller.dto.StoreResponseDto;
import com.example.matzipbookserver.store.domain.Store;
import com.example.matzipbookserver.store.domain.repository.StoreRepository;
import com.example.matzipbookserver.store.external.KakaoLocalClient;
import com.example.matzipbookserver.store.external.dto.KakaoDocument;
import com.example.matzipbookserver.store.external.dto.KakaoSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final KakaoLocalClient kakaoLocalClient;
    private final StoreRepository storeRepository;

    public StoreResponseDto getPlaceDetail(String kakaoPlaceId, String placeName, double x, double y) {
        Optional<Store> storeOptional = storeRepository.findByKakaoPlaceId(kakaoPlaceId);
        if(storeOptional.isPresent()) {
            return StoreResponseDto.from(storeOptional.get());
        }

        KakaoSearchResponse searchRes = kakaoLocalClient.searchPlaceByKeywordAndPos(placeName, x, y, 1000, "FD6"); //1km내 검색

        KakaoDocument document = searchRes.documents().stream()
                .filter(doc -> doc.id().equals(kakaoPlaceId))
                .findFirst()
                .orElseThrow(() -> new RestApiException(StoreErrorCode.STORE_NOT_EXIST));

        Store newStore = Store.builder()
                .kakaoPlaceId(document.id())
                .name(document.placeName())
                .address(document.addressName())
                .roadAddress(document.roadAddressName())
                .x(Double.valueOf(document.x()))
                .y(Double.valueOf(document.y()))
                .categoryName(document.categoryName())
                .phone(document.phone())
                .kakaoPlaceUrl(document.placeUrl()).build();
        storeRepository.save(newStore);
        return StoreResponseDto.from(newStore);
    }
}
