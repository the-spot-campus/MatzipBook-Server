package com.example.matzipbookserver.member.controller.dto.response;

import lombok.Builder;

@Builder
public record SignupResponse (
        Long id,
        String email,
        String nickname
) {}
