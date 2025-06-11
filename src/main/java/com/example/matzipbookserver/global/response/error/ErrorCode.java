package com.example.matzipbookserver.global.response.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getDevelopCode();
    HttpStatus getHttpStatus();
    String getErrorDescription();
}