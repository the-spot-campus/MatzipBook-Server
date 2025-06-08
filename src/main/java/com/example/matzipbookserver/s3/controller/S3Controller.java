package com.example.matzipbookserver.uploader.controller;

import com.example.matzipbookserver.uploader.controller.dto.reqeust.DeleteProfileRequest;
import com.example.matzipbookserver.uploader.controller.dto.reqeust.UploadProfileRequest;
import com.example.matzipbookserver.uploader.controller.dto.response.UploadProfileResponse;
import com.example.matzipbookserver.uploader.service.S3Service;
import com.example.matzipbookserver.global.resolver.CurrentMember;
import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.S3SuccessCode;
import com.example.matzipbookserver.member.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class S3Controller {
    private final S3Service S3Service;

    @PostMapping(value = "/upload/profile", consumes = "multipart/form-data")
    public SuccessResponse<UploadProfileResponse> uploadProfileImage(
            @CurrentMember Member member,
            @RequestParam("profileImage") MultipartFile profileImage) {
        UploadProfileRequest request = new UploadProfileRequest(member, profileImage);
        return SuccessResponse.of(S3SuccessCode.PROFILE_UPLOAD_SUCCESS, S3Service.uploadProfileImage(request));
    }

    @DeleteMapping("/delete/profile")
    public SuccessResponse<String> deleteProfileImage(
            @CurrentMember Member member
    ) {
        S3Service.deleteProfileImage(new DeleteProfileRequest(member));
        return SuccessResponse.of(S3SuccessCode.PROFILE_UPLOAD_SUCCESS, "프로필 이미지를 성공적으로 삭제하였습니다.");
    }
}