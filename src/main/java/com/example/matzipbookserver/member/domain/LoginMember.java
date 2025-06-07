package com.example.matzipbookserver.member.domain;


public record LoginMember(
        String provider,
        String providerId
) {}