package com.example.matzipbookserver.s3.controller.dto.response;

import jakarta.validation.constraints.NotBlank;

public record UploadProfileResponse(
        @NotBlank(message = "URL 경로는 공백일 수 없습니다.")
        String path
) {
}