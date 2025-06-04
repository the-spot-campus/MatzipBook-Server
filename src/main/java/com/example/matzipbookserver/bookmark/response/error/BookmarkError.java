package com.example.matzipbookserver.bookmark.response.error;

import com.example.matzipbookserver.global.response.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BookmarkError implements ErrorCode {

    ALREADY_BOOKMARKED("BOOKMARK-ERROR-001", HttpStatus.CONFLICT, "이미 북마크된 가게입니다."),
    BOOKMARK_NOT_FOUND("BOOKMARK-ERROR-002", HttpStatus.NOT_FOUND, "해당 북마크가 존재하지 않습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String message;

    BookmarkError(String developCode, HttpStatus httpStatus, String message) {
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