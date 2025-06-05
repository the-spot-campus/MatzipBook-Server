package com.example.matzipbookserver.member.controller.dto.response;

import com.example.matzipbookserver.member.domain.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberInfoResponse(
        Long id,
        String email,
        String nickname,
        String provider,
        String providerId,
        String birth,
        String gender,
        String profileImagePath,
        String university,
        LocalDateTime createdAt
) {
    public static MemberInfoResponse from(Member m) {
        return MemberInfoResponse.builder()
                .id(m.getId())
                .email(m.getEmail())
                .nickname(m.getNickname())
                .provider(m.getProvider())
                .providerId(m.getProviderId())
                .birth(m.getBirth())
                .gender(m.getGender())
                .profileImagePath(m.getProfileImagePath())
                .university(m.getUniversity())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
