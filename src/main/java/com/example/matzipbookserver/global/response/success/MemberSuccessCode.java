package com.example.matzipbookserver.global.response.success;

import org.springframework.http.HttpStatus;

public enum MemberSuccessCode implements SuccessCode {
    LOGIN_SUCCESS(HttpStatus.OK,"MEMBER_LOGIN_SUCCESS_001","로그인 성공"),
    SIGNUP_REQUIRED(HttpStatus.OK, "MEMBER_SIGNUP_REQUIRED_002", "회원가입 필요"),
    SIGNUP_SUCCESS(HttpStatus.OK, "MEMBER_SIGNUP_SUCCESS_003","회원가입 성공"),
    FCM_TOKEN_SAVED(HttpStatus.OK, "MEMBER_FCM_SAVED_004", "FCM 토큰 저장 완료"),
    MEMBER_INFO_SUCCESS(HttpStatus.OK,"MEMBER_INFO_SUCCESS_005", "회원 정보 조회 성공"),
    REISSUE_TOKEN_SUCCESS(HttpStatus.OK, "REISSUE_TOKEN_SUCCESS_006", "토큰 재발급 완료");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    MemberSuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
