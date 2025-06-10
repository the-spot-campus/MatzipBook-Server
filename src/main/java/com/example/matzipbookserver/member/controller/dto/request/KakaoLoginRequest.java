package com.example.matzipbookserver.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record KakaoLoginRequest(
        @NotBlank(message = "code는 빈칸이면 안됩니다.")
        String code,
        @NotBlank(message = "fcmToken은 빈칸이면 안됩니다.")
        String fcmToken
) {
}
