package com.example.matzipbookserver.store.external;

import com.example.matzipbookserver.global.config.KakaoFeignConfig;
import com.example.matzipbookserver.store.external.dto.KakaoSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "kakaoLocalClient",
        url = "https://dapi.kakao.com",
        configuration = KakaoFeignConfig.class
)
public interface KakaoLocalClient {

    @GetMapping("/v2/local/search/keyword.json")
    KakaoSearchResponse searchPlaceByKeywordAndPos(
            @RequestParam("query") String query,
            @RequestParam("x") Double x,
            @RequestParam("y") Double y,
            @RequestParam(value = "radius", required = false) int radius, // optional, default: 1000m
            @RequestParam("category_group_code") String categoryGroupCode
    );
}
