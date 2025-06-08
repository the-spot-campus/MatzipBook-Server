package com.example.matzipbookserver.s3.controller.dto.reqeust;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record S3SingleUploadRequest(
        MultipartFile profileImage
) {
    public static S3SingleUploadRequest of(MultipartFile profileImage){
        return builder()
                .profileImage(profileImage)
                .build();
    }
}