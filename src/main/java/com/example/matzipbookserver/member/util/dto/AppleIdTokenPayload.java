package com.example.matzipbookserver.member.util.dto;

public record AppleIdTokenPayload (
    String sub, // 애플 식별자
    String email // 유저 이메일
) {}
