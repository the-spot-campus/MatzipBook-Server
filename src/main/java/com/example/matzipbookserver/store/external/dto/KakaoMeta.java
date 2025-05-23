package com.example.matzipbookserver.store.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMeta(
        @JsonProperty("total_count") int totalCount,
        @JsonProperty("pageable_count") int pageableCount,
        @JsonProperty("is_end") boolean isEnd
) {
}
