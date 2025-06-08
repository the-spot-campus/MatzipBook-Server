package com.example.matzipbookserver.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    POST_IMAGE_ERROR("S3_001", HttpStatus.BAD_REQUEST, "올바르지 않은 이미지 저장 접근입니다."),
    DELETE_IMAGE_ERROR("S3_002", HttpStatus.BAD_REQUEST, "올바르지 않은 이미지 삭제 접근입니다."),
    PROFILE_UPLOAD_FAILED("S3_003", HttpStatus.INTERNAL_SERVER_ERROR,"이미지 업로드를 실패하였습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}