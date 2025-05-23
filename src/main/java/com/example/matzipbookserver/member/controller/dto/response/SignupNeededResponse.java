package com.example.matzipbookserver.member.controller.dto.response;

public record SignupNeededResponse(
        String email,
        String providerId
) implements LoginResponse {}
