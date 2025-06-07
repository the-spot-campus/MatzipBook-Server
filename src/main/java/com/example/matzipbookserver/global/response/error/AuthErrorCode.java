package com.example.matzipbookserver.global.response.error;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {
    KAKAO_TOKEN_REQUEST_FAIL("AUTH_KAKAO_TOKEN_REQUEST_FAIL_001", HttpStatus.BAD_REQUEST, "카카오 토큰 요청에 실패했습니다."),
    KAKAO_USER_INFO_FAIL("AUTH_KAKAO_USER_INFO_FAIL_002", HttpStatus.BAD_REQUEST, "카카오 사용자 정보 요청에 실패했습니다."),
    KAKAO_EMAIL_NOT_PROVIDED("AUTH_KAKAO_EMAIL_NOT_PROVIDED_003", HttpStatus.BAD_REQUEST, "이메일 정보가 제공되지 않았습니다."),

    APPLE_TOKEN_REQUEST_FAIL("AUTH_APPLE_TOKEN_REQUEST_FAIL_004", HttpStatus.BAD_REQUEST, "애플 토큰 요청에 실패했습니다."),
    APPLE_EMAIL_NOT_PROVIDED("AUTH_APPLE_EMAIL_NOT_PROVIDED_005", HttpStatus.BAD_REQUEST, "애플 이메일 정보가 제공되지 않았습니다."),
    APPLE_ID_TOKEN_PARSE_FAILED("AUTH_APPLE_ID_TOKEN_PARSE_FAILED_006", HttpStatus.UNAUTHORIZED, "Apple ID Token 파싱에 실패했습니다."),

    INVALID_TOKEN("AUTH_INVALID_TOKEN_007", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("AUTH_EXPIRED_REFRESH_TOKEN_008", HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    NOT_ACCESS_TOKEN_FOR_REISSUE("AUTH_NOT_ACCESS_TOKEN_FOR_REISSUE_009", HttpStatus.BAD_REQUEST, "재발급 대상이 아닌 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN_DB("AUTH_EXPIRED_REFRESH_TOKEN_DB_010", HttpStatus.UNAUTHORIZED, "서버에서 만료 처리된 리프레시 토큰입니다."),
    NULL_REFRESH_TOKEN("AUTH_NULL_REFRESH_TOKEN_011", HttpStatus.UNAUTHORIZED, "DB에 존재하지 않는 리프레시 토큰입니다.");


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


