package com.example.matzipbookserver.member.service;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.AuthErrorCode;
import com.example.matzipbookserver.member.controller.dto.response.KakaoLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.AppleLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.LoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.SignupNeededResponse;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.repository.MemberRepository;
import com.example.matzipbookserver.member.util.AppleOAuthUtil;
import com.example.matzipbookserver.member.util.dto.AppleIdTokenPayload;
import com.example.matzipbookserver.member.util.dto.AppleTokenResponse;
import com.example.matzipbookserver.member.util.dto.KakaoTokenDTO;
import com.example.matzipbookserver.member.util.dto.KakaoUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.matzipbookserver.member.util.KakaoOAuthUtil;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthUtil kakaoOAuthUtil;
    private final MemberRepository memberRepository;
    private final FcmTokenService fcmTokenService;
    private final AppleOAuthUtil appleOAuthUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse kakaoLogin(String code, String fcmToken) {


        KakaoTokenDTO tokenDto;
        try {
            tokenDto = kakaoOAuthUtil.requestToken(code);
        } catch (Exception e) {
            throw new RestApiException(AuthErrorCode.KAKAO_TOKEN_REQUEST_FAIL);
        }

        KakaoUserDTO userInfo;
        try {
            userInfo = kakaoOAuthUtil.requestUserInfo(tokenDto.accessToken());
        } catch (Exception e) {
            throw new RestApiException(AuthErrorCode.KAKAO_USER_INFO_FAIL);
        }
        if (userInfo.kakaoAccount() == null || userInfo.kakaoAccount().email() == null) {
            throw new RestApiException(AuthErrorCode.KAKAO_EMAIL_NOT_PROVIDED);
        }

        String email = userInfo.kakaoAccount().email();
        String providerId = String.valueOf(userInfo.id());



        Optional<Member> member = memberRepository.findByProviderAndProviderId("kakao",providerId);

        if(member.isPresent()){
            fcmTokenService.saveOrUpdate(member.get(), fcmToken);
            String jwt = jwtTokenProvider.createAccessToken(member.get().getEmail());
            return new KakaoLoginResponse(jwt, new KakaoLoginResponse.UserInfo(member.get().getId(), member.get().getEmail()));
        } else {
            return new SignupNeededResponse(email, providerId);
        }

    }

    public LoginResponse appleLogin(String code, String fcmToken) {

        AppleTokenResponse tokenResponse;

        try {
            tokenResponse = appleOAuthUtil.requestToken(code);
        } catch (Exception e) {
            throw new RestApiException(AuthErrorCode.APPLE_TOKEN_REQUEST_FAIL);
        }

        AppleIdTokenPayload userInfo;

        try {
            userInfo = appleOAuthUtil.parseIdToken(tokenResponse.id_token());
        } catch (Exception e) {
            throw new RestApiException(AuthErrorCode.APPLE_ID_TOKEN_PARSE_FAILED);
        }

        if (userInfo.email() == null) {
            throw new RestApiException(AuthErrorCode.APPLE_EMAIL_NOT_PROVIDED);
        }


        String email = userInfo.email();
        String providerId = userInfo.sub();

        Optional<Member> member = memberRepository.findByProviderAndProviderId("apple",providerId);

        if (member.isPresent()) {
            fcmTokenService.saveOrUpdate(member.get(), fcmToken);
            String jwt = jwtTokenProvider.createAccessToken(member.get().getEmail());
            return new AppleLoginResponse(jwt, new AppleLoginResponse.UserInfo(member.get().getId(), member.get().getEmail()));
        } else {
            return new SignupNeededResponse(email, providerId);
        }
    }

}
