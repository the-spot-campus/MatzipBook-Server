package com.example.matzipbookserver.global.exception;

import com.example.matzipbookserver.global.response.error.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class ApiExceptionHandler {
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ExceptionResponseBody> businessExceptionHandler(final RestApiException apiException) {
        final ErrorCode errorCode = apiException.getErrorCode();
        final String details = apiException.getMessage();

        return createExceptionResponse(errorCode, ExceptionResponseBody.of(errorCode, details));
    }

    private ResponseEntity<ExceptionResponseBody> createExceptionResponse(ErrorCode errorCode, ExceptionResponseBody exceptionResponseBody) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(exceptionResponseBody);
    }
}