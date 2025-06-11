package com.example.matzipbookserver.member.controller.dto.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String email;
    private String provider;
    private String providerId;
    private String nickname;
    private String birth;
    private String gender;
    private String university;

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