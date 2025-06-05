package com.example.matzipbookserver.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3SuccessCode implements SuccessCode {
    PROFILE_UPLOAD_SUCCESS("S3_001", HttpStatus.OK, "프로필 이미지를 성공적으로 업로드하였습니다"),
    PROFILE_DELETED_SUCCESS("S3_002", HttpStatus.OK, "프로필 이미지를 성공적으로 삭제하였습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}