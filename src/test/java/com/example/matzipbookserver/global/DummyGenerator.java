package com.example.matzipbookserver;

import com.example.matzipbookserver.s3.controller.dto.response.S3File;
import com.example.matzipbookserver.bookmark.domain.repository.BookmarkRepository;
import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import com.example.matzipbookserver.member.controller.dto.request.SignUpRequest;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.domain.MemberImage;
import com.example.matzipbookserver.member.repository.FcmTokenRepository;
import com.example.matzipbookserver.member.repository.MemberRepository;
import com.example.matzipbookserver.store.domain.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyGenerator {
    @Autowired MemberRepository memberRepository;
    @Autowired StoreRepository storeRepository;
    @Autowired FcmTokenRepository fcmTokenRepository;
    @Autowired BookmarkRepository bookmarkRepository;

    public static final String GIVEN_EMAIL = "tmp@example.com";
    public static final String GIVEN_NICKNAME = "tmpNickName";
    public static final String GIVEN_PROVIDER= "kakao";
    public static final String GIVEN_PROVIDER_ID = "kakao123";
    public static final String GIVEN_GENDER = "MALE";
    public static final String GIVEN_UNIVERSITY = "부경대학교";
    public static final String GIVEN_BIRTHDAY = "2001-07-02";
    public static final String GIVEN_FILE_NAME = "test";
    public static final String GIVEN_FILE_URL = "/test";
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 다수 멤버 생성 메서드
    public List<Member> generateMultiMember(Integer memberCounts) {
        ArrayList<Member> result = new ArrayList<>();

        for (int i = 0; i < memberCounts; i++) {
            String tmpProviderId = GIVEN_PROVIDER_ID + i;
            MemberImage memberImage = makeImage();
            Member member = makeMember(tmpProviderId, memberImage);
            result.add(member);
        }

        return memberRepository.saveAll(result);
    }

    // 단일 멤버 생성 메서드
    public Member generateSingleMember() {
        MemberImage memberImage = makeImage();
        Member member = makeMember(GIVEN_EMAIL, memberImage);
        return memberRepository.save(member);
    }

    private MemberImage makeImage() {
        return MemberImage.from(new S3File(GIVEN_FILE_NAME, GIVEN_FILE_URL));
    }

    private Member makeMember(String providerId, MemberImage memberImage) {
        SignUpRequest signUp = SignUpRequest.from(
                GIVEN_EMAIL, GIVEN_PROVIDER, providerId, GIVEN_NICKNAME, GIVEN_BIRTHDAY, GIVEN_GENDER, GIVEN_UNIVERSITY);
        return memberRepository.save(Member.from(signUp, memberImage));
    }

    // MultipartFile 생성 메서드
    public MultipartFile generateMultipartFile() {
        String fileName = "test";
        String path = "/test";
        String contentType = "image/png";
        byte[] content = fileName.getBytes();
        return new MockMultipartFile(fileName, path, contentType, content);
    }

    //AccessToken 생성 메서드
    public String generateAccessToken(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member.getProvider(), member.getProviderId());
//        refreshToken 관련 코드 수정 필요
//        Token refreshToken = new Token(member);
//        refreshToken.putRefreshToken(accessToken);
//        tokenRepository.save(refreshToken);
        return accessToken;
    }
}