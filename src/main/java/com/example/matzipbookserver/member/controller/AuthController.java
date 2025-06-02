package com.example.matzipbookserver.member.controller;


import com.example.matzipbookserver.global.resolver.CurrentMember;
import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.MemberSuccessCode;
import com.example.matzipbookserver.member.controller.dto.request.AppleLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.FcmTokenRequest;
import com.example.matzipbookserver.member.controller.dto.request.KakaoLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.SignupRequest;
import com.example.matzipbookserver.member.controller.dto.response.*;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.service.AuthService;
import com.example.matzipbookserver.member.service.FcmTokenService;
import com.example.matzipbookserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final FcmTokenService fcmTokenService;
    private final MemberService memberService;



    // 테스트용
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        return ResponseEntity.ok("카카오 인가코드 수신 완료: " + code);
    }

    @PostMapping("/login/kakao")
    public SuccessResponse<? extends LoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        LoginResponse response = authService.kakaoLogin(request.code(), request.fcmToken());

        if(response instanceof KakaoLoginResponse) {
            return SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS,response);
        } else {
            return SuccessResponse.of(MemberSuccessCode.SIGNUP_REQUIRED,response);
        }
    }

    // 테스트용
    @PostMapping("/apple/callback")
    public ResponseEntity<?> appleCallback(@RequestParam String code,
                                           @RequestParam(required = false) String id_token) {
        return ResponseEntity.ok("애플 인가코드 수신 완료: " + code);
    }

    @PostMapping("/login/apple")
    public SuccessResponse<? extends LoginResponse> appleLogin(@RequestBody AppleLoginRequest request) {

        LoginResponse response = authService.appleLogin(request.code(), request.fcmToken());

        if(response instanceof AppleLoginResponse) {
            return SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS,response);
        } else {
            return SuccessResponse.of(MemberSuccessCode.SIGNUP_REQUIRED,response);
        }
    }

    @PostMapping("/signup")
    public SuccessResponse<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = memberService.signup(request);
        return SuccessResponse.of(MemberSuccessCode.SIGNUP_SUCCESS,response);
    }

    @GetMapping("/me")
    public SuccessResponse<MemberInfoResponse>getCurrentMember(@CurrentMember Member member) {
        return SuccessResponse.of(MemberSuccessCode.MEMBER_INFO_SUCCESS, MemberInfoResponse.from(member));
    }


    @PostMapping("/fcm")
    public SuccessResponse<FcmTokenSaveResponse> saveFcmToken(@CurrentMember Member member, @RequestBody FcmTokenRequest request) {
        fcmTokenService.saveOrUpdate(member, request.fcmToken());
        return SuccessResponse.of(MemberSuccessCode.FCM_TOKEN_SAVED, new FcmTokenSaveResponse("success"));
    }


}
