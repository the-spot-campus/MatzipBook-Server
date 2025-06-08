package com.example.matzipbookserver.member.controller.dto.response;

import com.example.matzipbookserver.member.domain.Member;
import lombok.Builder;

@Builder
public record SignUpResponse(
        Long id,
        String email,
        String nickname,
        String jwtToken
) {
    public static SignUpResponse from(Member member, String jwt){
        return builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .jwtToken(jwt)
                .build();
    }
}
