package com.example.matzipbookserver.global.response.error;


import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {

    ALREADY_REGISTERED("MEMBER-001",HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
    MEMBER_NOT_FOUND("MEMBER-002", HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(String developCode, HttpStatus httpStatus, String message) {
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
