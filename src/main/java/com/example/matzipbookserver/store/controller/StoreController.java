package com.example.matzipbookserver.store.controller;

import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.StoreSuccessCode;
import com.example.matzipbookserver.store.controller.dto.StoreRankingResponseDto;
import com.example.matzipbookserver.store.controller.dto.StoreResponseDto;
import com.example.matzipbookserver.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/store")
@RestController
public class StoreController {

    private final StoreService storeService;
    private static final int MAX_RADIUS = 10000; // 10km

    @GetMapping("/{kakaoPlaceId}")
    public ResponseEntity<SuccessResponse<StoreResponseDto>> getStoreDetail(
            @PathVariable String kakaoPlaceId,
            @RequestParam String storeName,
            @RequestParam double x,
            @RequestParam double y) {
        return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.OK, storeService.getPlaceDetail(kakaoPlaceId, storeName, x, y))) ;
    }

    @GetMapping("/ranking")
    public ResponseEntity<SuccessResponse<Page<StoreRankingResponseDto>>> getStoreRanking(
            @RequestParam double x,     //latitude 경도
            @RequestParam double y,     //longitude 위도
            @RequestParam(required = false, defaultValue = "2000")int radius,  //미터로 받기
            Pageable pageable
    ) {
        int minimumRadius = Math.min(radius, MAX_RADIUS);

        Page<StoreRankingResponseDto> ranking = storeService.getNearStoreRanking(x, y, minimumRadius, pageable);
        return ResponseEntity.ok(SuccessResponse.of(StoreSuccessCode.OK, ranking));
    }
}