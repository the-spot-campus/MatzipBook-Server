package com.example.matzipbookserver.s3.controller.dto.response;

import jakarta.validation.constraints.NotBlank;

public record S3FileResponse(
        @NotBlank(message = "파일 이름이 공백이면 안 됩니다.")
        String fileName,
        @NotBlank(message = "URL 경로는 공백일 수 없습니다.")
        String fileURL) {
}