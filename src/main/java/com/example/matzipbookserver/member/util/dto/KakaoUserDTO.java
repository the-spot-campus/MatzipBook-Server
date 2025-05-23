package com.example.matzipbookserver.member.util.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserDTO (
        Long id,
        @JsonProperty("connected_at")
        String connectedAt,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public record KakaoAccount (
            String email
    ) {}

}