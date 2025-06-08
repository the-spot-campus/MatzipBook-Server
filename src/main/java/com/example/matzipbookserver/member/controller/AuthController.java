package com.example.matzipbookserver.member.controller;


import com.example.matzipbookserver.global.resolver.CurrentMember;
import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.MemberSuccessCode;
import com.example.matzipbookserver.member.controller.dto.request.AppleLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.FcmTokenRequest;
import com.example.matzipbookserver.member.controller.dto.request.KakaoLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.SignUpRequest;
import com.example.matzipbookserver.member.controller.dto.response.*;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.service.AuthService;
import com.example.matzipbookserver.member.service.FcmTokenService;
import com.example.matzipbookserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final FcmTokenService fcmTokenService;
    private final MemberService memberService;

    @PostMapping("/login/kakao")
    public SuccessResponse<? extends LoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        LoginResponse response = authService.kakaoLogin(request.code(), request.fcmToken());

        if(response instanceof KakaoLoginResponse) {
            return SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS, response);
        } else {
            return SuccessResponse.of(MemberSuccessCode.SIGNUP_REQUIRED, response);
        }
    }

    @PostMapping("/login/apple")
    public SuccessResponse<? extends LoginResponse> appleLogin(@RequestBody AppleLoginRequest request) {

        LoginResponse response = authService.appleLogin(request.code(), request.fcmToken());

        if(response instanceof AppleLoginResponse) {
            return SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS, response);
        } else {
            return SuccessResponse.of(MemberSuccessCode.SIGNUP_REQUIRED, response);
        }
    }

    @PostMapping("/signup")
    public SuccessResponse<SignUpResponse> signup(
            @RequestPart("signUpRequest") SignUpRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        SignUpResponse response = memberService.signup(request, profileImage);
        return SuccessResponse.of(MemberSuccessCode.SIGNUP_SUCCESS, response);
    }

    @PostMapping("/fcm")
    public SuccessResponse<FcmTokenSaveResponse> saveFcmToken(@CurrentMember Member member, @RequestBody FcmTokenRequest request) {
        fcmTokenService.saveOrUpdate(member, request.fcmToken());
        return SuccessResponse.of(MemberSuccessCode.FCM_TOKEN_SAVED, new FcmTokenSaveResponse("success"));
    }
}