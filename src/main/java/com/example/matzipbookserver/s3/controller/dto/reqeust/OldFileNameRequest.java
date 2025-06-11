package com.example.matzipbookserver.s3.controller.dto.reqeust;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record OldFileNameRequest(
        @NotBlank(message = "파일 이름이 공백이면 안 됩니다.")
        String oldFileName
) {
    public static OldFileNameRequest of(String url) {
        return OldFileNameRequest.builder()
                .oldFileName(url)
                .build();
    }
}