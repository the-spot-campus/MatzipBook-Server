package com.example.matzipbookserver.aws.controller.dto.reqeust;

import org.springframework.web.multipart.MultipartFile;

public record S3SingleUploadRequest(
        MultipartFile profileImage
) {
}
