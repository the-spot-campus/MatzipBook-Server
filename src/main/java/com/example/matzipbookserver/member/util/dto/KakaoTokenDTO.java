package com.example.matzipbookserver.member.util.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenDTO (
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("expires_in")
    int expiresIn,

    @JsonProperty("scope")
    String scope,

    @JsonProperty("refresh_token_expires_in")
    int refreshTokenExpiresIn
) {}
