package com.example.matzipbookserver.store.controller;

import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.StoreSuccessCode;
import com.example.matzipbookserver.store.controller.dto.StoreResponseDto;
import com.example.matzipbookserver.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/store")
@RestController
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{kakaoPlaceId}")
    public ResponseEntity<SuccessResponse<StoreResponseDto>> getStoreDetail(
            @PathVariable String kakaoPlaceId,
            @RequestParam String storeName,
            @RequestParam double x,
            @RequestParam double y) {
        return SuccessResponse.of(StoreSuccessCode.OK, storeService.getPlaceDetail(kakaoPlaceId, storeName, x, y));
    }

}
