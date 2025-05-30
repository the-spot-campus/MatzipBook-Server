package com.example.matzipbookserver.store.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoDocument(
        String id,
        @JsonProperty("place_name") String placeName,
        @JsonProperty("category_name") String categoryName,
        String phone,
        @JsonProperty("address_name") String addressName,
        @JsonProperty("road_address_name") String roadAddressName,
        String x,
        String y,
        @JsonProperty("place_url") String placeUrl
) {
}
