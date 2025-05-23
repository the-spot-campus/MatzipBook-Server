package com.example.matzipbookserver.member.util.dto;

public record AppleTokenResponse (
        String access_token,
        String id_token,
        String refresh_token,
        String token_type,
        Long expires_in
) {}
