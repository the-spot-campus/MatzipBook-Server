package com.example.matzipbookserver.member.controller;


import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.MemberSuccessCode;
import com.example.matzipbookserver.member.controller.dto.request.AppleLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.KakaoLoginRequest;
import com.example.matzipbookserver.member.controller.dto.response.AppleLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.KakaoLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.LoginResponse;
import com.example.matzipbookserver.member.service.AuthService;
import com.example.matzipbookserver.member.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final FcmTokenService fcmTokenService;


    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        return ResponseEntity.ok("카카오 인가코드 수신 완료: " + code);
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        LoginResponse response = authService.kakaoLogin(request.code(), request.fcmToken());

        if(response instanceof KakaoLoginResponse) {
            return SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS,response);
        } else {
            return SuccessResponse.of(MemberSuccessCode.SIGNUP_REQUIRED,response);
        }
    }


    @PostMapping("/apple/callback")
    public ResponseEntity<?> appleCallback(@RequestParam String code,
                                           @RequestParam(required = false) String id_token) {
        return ResponseEntity.ok("애플 인가코드 수신 완료: " + code);
    }

    @PostMapping("/login/apple")
    public ResponseEntity<?> appleLogin(@RequestBody AppleLoginRequest request) {

        LoginResponse response = authService.appleLogin(request.code(), request.fcmToken());

        if(response instanceof AppleLoginResponse) {
            return SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS,response);
        } else {
            return SuccessResponse.of(MemberSuccessCode.SIGNUP_REQUIRED,response);
        }
    }


}
