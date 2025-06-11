package com.example.matzipbookserver.s3.service;

import com.example.matzipbookserver.global.BaseIntegrationTest;
import com.example.matzipbookserver.global.DummyGenerator;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.s3.controller.dto.reqeust.DeleteProfileRequest;
import com.example.matzipbookserver.s3.controller.dto.reqeust.UploadProfileRequest;
import com.example.matzipbookserver.s3.controller.dto.response.UploadProfileResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collection;
import java.util.List;

class S3ServiceTest extends BaseIntegrationTest {
    @Autowired S3Service s3Service;
    @Autowired DummyGenerator dummyGenerator;

    @TestFactory
    @DisplayName("s3 service 프로필 이미지 업로드 및 삭제")
    Collection<DynamicTest> uploadProfileImageServiceDynamicTest() {
        // given
        Member member = dummyGenerator.createSingleMemberWithoutMemberImage();
        MockMultipartFile file = dummyGenerator.createMockMultipartFile();

        return List.of(
                DynamicTest.dynamicTest("프로필 이미지 업로드 수행", () -> {
                    // given
                    UploadProfileRequest request = new UploadProfileRequest(member, file);

                    // when
                    UploadProfileResponse result = s3Service.uploadProfileImage(request);

                    // then
                    Assertions.assertThat(result.path()).isNotNull();
                }),
                DynamicTest.dynamicTest("프로필 이미지 삭제 수행", () -> {
                    // given
                    DeleteProfileRequest request = new DeleteProfileRequest(member);

                    // when
                    boolean result = s3Service.deleteProfileImage(request);

                    // then
                    Assertions.assertThat(result).isTrue();
                })
        );
    }
}