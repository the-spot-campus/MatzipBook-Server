package com.example.matzipbookserver.store.external.dto;

import java.util.List;

public record KakaoSearchResponse(
        KakaoMeta meta,
        List<KakaoDocument> documents
) {
}
