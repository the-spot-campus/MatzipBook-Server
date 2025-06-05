package com.example.matzipbookserver.member.service;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import com.example.matzipbookserver.member.controller.dto.request.SignupRequest;
import com.example.matzipbookserver.member.controller.dto.response.SignupResponse;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.matzipbookserver.global.response.error.MemberErrorCode;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest request) {
        memberRepository.findByProviderAndProviderId(request.provider(), request.providerId()).ifPresent(member -> {
            throw new RestApiException(MemberErrorCode.ALREADY_REGISTERED);
        });

        Member member = new Member(
                request.provider(),
                request.providerId(),
                request.email(),
                request.nickname(),
                request.birth(),
                request.gender(),
                request.profileImagePath(),
                request.university()
        );
        Member saved = memberRepository.save(member);

        String jwt = jwtTokenProvider.createAccessToken(saved.getProvider(), saved.getProviderId());

        return new SignupResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getNickname(),
                jwt
        );
    }
}
