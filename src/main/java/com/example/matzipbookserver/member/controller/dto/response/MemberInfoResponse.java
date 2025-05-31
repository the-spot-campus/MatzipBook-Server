package com.example.matzipbookserver.member.controller.dto.response;

import com.example.matzipbookserver.member.domain.Member;

import java.time.LocalDateTime;

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
    public MemberInfoResponse(Member m) {
        this(
                m.getId(),
                m.getEmail(),
                m.getNickname(),
                m.getProvider(),
                m.getProviderId(),
                m.getBirth(),
                m.getGender(),
                m.getProfileImagePath(),
                m.getUniversity(),
                m.getCreatedAt()
        );
    }
}
