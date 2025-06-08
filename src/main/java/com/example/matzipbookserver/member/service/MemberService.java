package com.example.matzipbookserver.member.service;

import com.example.matzipbookserver.s3.controller.dto.reqeust.S3SingleUploadRequest;
import com.example.matzipbookserver.s3.controller.dto.response.S3File;
import com.example.matzipbookserver.s3.util.S3Uploader;
import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import com.example.matzipbookserver.member.controller.dto.request.SignUpRequest;
import com.example.matzipbookserver.member.controller.dto.response.SignUpResponse;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.domain.MemberImage;
import com.example.matzipbookserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.matzipbookserver.global.response.error.MemberErrorCode;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Uploader s3Uploader;

    public SignUpResponse signup(SignUpRequest request, MultipartFile imageFile) {
        memberRepository.findByProviderAndProviderId(request.provider(), request.providerId())
                .ifPresent(member -> {
                    throw new RestApiException(MemberErrorCode.ALREADY_REGISTERED);
                });

        S3File s3File = s3Uploader.singleUpload(S3SingleUploadRequest.of(imageFile));
        MemberImage profileImage = MemberImage.from(s3File);
        Member member = Member.from(request, profileImage);

        Member saved = memberRepository.save(member);
        String jwt = jwtTokenProvider.createAccessToken(saved.getProvider(), saved.getProviderId());

        return SignUpResponse.from(saved, jwt);
    }
}