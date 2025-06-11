package com.example.matzipbookserver.s3.controller.dto.reqeust;

import com.example.matzipbookserver.global.validation.NotEmptyFile;
import com.example.matzipbookserver.member.domain.Member;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadProfileRequest(
        @NotNull(message = "사용자가 존재해야 합니다.")
        Member member,
        @NotEmptyFile
        MultipartFile profileImage
) {
}