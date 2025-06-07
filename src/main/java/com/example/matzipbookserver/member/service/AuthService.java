package com.example.matzipbookserver.member.service;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import com.example.matzipbookserver.global.response.error.AuthErrorCode;
import com.example.matzipbookserver.member.controller.dto.response.*;
import com.example.matzipbookserver.member.domain.LoginMember;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.domain.RefreshToken;
import com.example.matzipbookserver.member.repository.MemberRepository;
import com.example.matzipbookserver.member.repository.RefreshTokenRepository;
import com.example.matzipbookserver.member.util.AppleOAuthUtil;
import com.example.matzipbookserver.member.util.dto.AppleIdTokenPayload;
import com.example.matzipbookserver.member.util.dto.AppleTokenResponse;
import com.example.matzipbookserver.member.util.dto.KakaoTokenDTO;
import com.example.matzipbookserver.member.util.dto.KakaoUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.matzipbookserver.member.util.KakaoOAuthUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthUtil kakaoOAuthUtil;
    private final MemberRepository memberRepository;
    private final FcmTokenService fcmTokenService;
    private final AppleOAuthUtil appleOAuthUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
            Member m = member.get();
            fcmTokenService.saveOrUpdate(member.get(), fcmToken);

            LoginMember loginMember = new LoginMember("kakao", providerId);

            // AccessToken 생성
            String accessToken = jwtTokenProvider.generateAccessToken(loginMember);

            // RefreshToken 생성
            RefreshToken tokenEntity = refreshTokenRepository.save(new RefreshToken(m, null));
            String refreshToken = jwtTokenProvider.generateRefreshToken(loginMember, tokenEntity.getId());
            tokenEntity.putRefreshToken(refreshToken);

            return new KakaoLoginResponse(
                    new AuthToken(accessToken, refreshToken),
                    new KakaoLoginResponse.UserInfo(m.getId(), m.getEmail())
            );
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
            Member m = member.get();
            fcmTokenService.saveOrUpdate(member.get(), fcmToken);
            LoginMember loginMember = new LoginMember("apple", providerId);
            String accessToken = jwtTokenProvider.generateAccessToken(loginMember);

            RefreshToken tokenEntity = refreshTokenRepository.save(new RefreshToken(m, null));
            String refreshToken = jwtTokenProvider.generateRefreshToken(loginMember, tokenEntity.getId());
            tokenEntity.putRefreshToken(refreshToken);

            return new AppleLoginResponse(
                    new AuthToken(accessToken, refreshToken),
                    new AppleLoginResponse.UserInfo(m.getId(), m.getEmail())
            );
        } else {
            return new SignupNeededResponse(email, providerId);
        }
    }

    @Transactional
    public AuthToken reissue(final AuthToken authToken) {
        if(jwtTokenProvider.isNotExpiredToken(authToken.accessToken())){
            throw new RestApiException(AuthErrorCode.NOT_ACCESS_TOKEN_FOR_REISSUE);
        }

        if (!jwtTokenProvider.isNotExpiredToken(authToken.refreshToken())) {
            throw new RestApiException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        Long tokenId = jwtTokenProvider.getTokenIdFromToken(authToken.refreshToken());
        RefreshToken token = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_REFRESH_TOKEN));

        if (token.isExpired()) {
            throw new RestApiException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        // AccessToken 재발급
        LoginMember loginMember = jwtTokenProvider.getLoginMemberFromToken(authToken.refreshToken());
        String newAccessToken = jwtTokenProvider.generateAccessToken(loginMember);

        // 최근 로그인 시간 업데이트
        token.changeRecentLogin();

        return new AuthToken(newAccessToken, authToken.refreshToken());
    }



}
