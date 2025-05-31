package com.example.matzipbookserver.member.controller.dto.response;

public record SignupResponse (
        Long id,
        String email,
        String nickname,
        String jwtToken
) {}
