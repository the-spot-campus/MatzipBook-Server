package com.example.matzipbookserver.member.controller.dto.response;

public record AuthToken(
        String accessToken,
        String refreshToken
) {}
