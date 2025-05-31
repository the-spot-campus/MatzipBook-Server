package com.example.matzipbookserver.member.controller.dto.request;

public record SignupRequest (
        String email,
        String provider,
        String providerId,
        String nickname,
        String birth,
        String gender,
        String profileImagePath,
        String university
) {}
