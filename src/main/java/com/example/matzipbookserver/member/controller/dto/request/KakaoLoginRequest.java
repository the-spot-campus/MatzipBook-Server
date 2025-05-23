package com.example.matzipbookserver.member.controller.dto.request;

public record KakaoLoginRequest(
        String code,
        String fcmToken
) {}
