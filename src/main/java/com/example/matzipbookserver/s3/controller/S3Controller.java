package com.example.matzipbookserver.s3.controller;

import com.example.matzipbookserver.global.resolver.CurrentMember;
import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.S3SuccessCode;
import com.example.matzipbookserver.global.validation.NotEmptyFile;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.s3.controller.dto.reqeust.DeleteProfileRequest;
import com.example.matzipbookserver.s3.controller.dto.reqeust.UploadProfileRequest;
import com.example.matzipbookserver.s3.controller.dto.response.UploadProfileResponse;
import com.example.matzipbookserver.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service S3Service;

    @PostMapping(value = "/profile", consumes = "multipart/form-data")
    public SuccessResponse<UploadProfileResponse> uploadProfileImage(
            @CurrentMember Member member,
            @NotEmptyFile @RequestPart("profileImage") MultipartFile multipartFile) {
        UploadProfileRequest request = new UploadProfileRequest(member, multipartFile);
        return SuccessResponse.of(S3SuccessCode.PROFILE_UPLOAD_SUCCESS, S3Service.uploadProfileImage(request));
    }

    @DeleteMapping("/profile")
    public SuccessResponse<String> deleteProfileImage(
            @CurrentMember Member member
    ) {
        S3Service.deleteProfileImage(new DeleteProfileRequest(member));
        return SuccessResponse.of(S3SuccessCode.PROFILE_DELETED_SUCCESS, "프로필 이미지를 성공적으로 삭제하였습니다.");
    }
}