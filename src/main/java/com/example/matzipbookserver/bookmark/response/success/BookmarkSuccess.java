package com.example.matzipbookserver.bookmark.response.success;

import com.example.matzipbookserver.global.response.success.SuccessCode;
import org.springframework.http.HttpStatus;

public enum BookmarkSuccess implements SuccessCode {

    CREATED("BOOKMARK-001", HttpStatus.CREATED, "북마크가 추가되었습니다."),
    DELETED("BOOKMARK-002", HttpStatus.NO_CONTENT, "북마크가 삭제되었습니다."),
    FETCHED("BOOKMARK-003", HttpStatus.OK, "북마크 목록 조회에 성공했습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    BookmarkSuccess(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
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