package com.example.matzipbookserver.s3.controller.dto.reqeust;

import com.example.matzipbookserver.global.validation.NotEmptyFile;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record S3SingleUploadRequest(
        @NotEmptyFile
        MultipartFile profileImage
) {
    public static S3SingleUploadRequest of(MultipartFile profileImage){
        return builder()
                .profileImage(profileImage)
                .build();
    }
}