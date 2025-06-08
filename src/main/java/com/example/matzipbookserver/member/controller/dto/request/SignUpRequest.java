package com.example.matzipbookserver.member.controller.dto.request;

import lombok.Builder;

@Builder
public record SignUpRequest(
        String email,
        String provider,
        String providerId,
        String nickname,
        String birth,
        String gender,
        String university
) {
    public static SignUpRequest from(String email, String provider, String providerId, String nickname, String birth, String gender, String university){
        return SignUpRequest.builder()
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .nickname(nickname)
                .birth(birth)
                .gender(gender)
                .university(university)
                .build();
    }
}