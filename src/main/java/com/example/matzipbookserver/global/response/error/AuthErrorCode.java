package com.example.matzipbookserver.global.response.error;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {
    KAKAO_TOKEN_REQUEST_FAIL("AUTH-001", HttpStatus.BAD_REQUEST, "카카오 토큰 요청에 실패했습니다." ),
    KAKAO_USER_INFO_FAIL("AUTH-002",HttpStatus.BAD_REQUEST, "카카오 사용자 정보 요청에 실패했습니다."),
    KAKAO_EMAIL_NOT_PROVIDED("AUTH-003", HttpStatus.BAD_REQUEST, "이메일 정보가 제공되지 않았습니다."),
    APPLE_TOKEN_REQUEST_FAIL("AUTH-004", HttpStatus.BAD_REQUEST, "애플 토큰 요청에 실패했습니다."),
    APPLE_EMAIL_NOT_PROVIDED("AUTH-005", HttpStatus.BAD_REQUEST, "애플 이메일 정보가 제공되지 않았습니다."),
    APPLE_ID_TOKEN_PARSE_FAILED("AUTH-006", HttpStatus.UNAUTHORIZED, "Apple ID Token 파싱에 실패했습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(String developCode, HttpStatus httpStatus, String message) {
        this.developCode = developCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getDevelopCode() {
        return developCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


