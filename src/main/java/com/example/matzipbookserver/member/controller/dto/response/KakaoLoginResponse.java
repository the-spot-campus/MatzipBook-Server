package com.example.matzipbookserver.member.controller.dto.response;


public record KakaoLoginResponse (
    String jwtToken,
    UserInfo user
) implements LoginResponse{
    public record UserInfo(
            Long id,
            String email
    ) {}
}
