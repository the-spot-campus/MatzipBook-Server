package com.example.matzipbookserver.global.response.success;

import org.springframework.http.HttpStatus;

public enum MemberSuccessCode implements SuccessCode {
    LOGIN_SUCCESS(HttpStatus.OK,"MEMBER-001","로그인 성공"),
    SIGNUP_REQUIRED(HttpStatus.OK, "MEMBER-002", "회원가입 필요");

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
