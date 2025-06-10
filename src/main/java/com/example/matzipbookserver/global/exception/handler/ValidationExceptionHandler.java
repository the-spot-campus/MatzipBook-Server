package com.example.matzipbookserver.global.exception.handler;

import com.example.matzipbookserver.global.exception.ExceptionResponseBody;
import com.example.matzipbookserver.global.response.error.ErrorCode;
import com.example.matzipbookserver.global.response.error.ValidationErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;

@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class ValidationExceptionHandler {

    private final ErrorCode errorCode = ValidationErrorCode.VALIDATION_FAILED;

    // 1. @RequestBody DTO의 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseBody> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .toList();

        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, "요청 필드 검증 실패", errors));
    }

    // 2. @RequestParam, @PathVariable 등의 단일 파라미터 검증 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseBody> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> {
                    String parameter = v.getPropertyPath().toString().split("\\.")[1];
                    return parameter + " " + v.getMessage();
                })
                .toList();

        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, "요청 파라미터 유효성 실패", errors));
    }

    // 3. multipart/form-data 요청에서 특정 Part가 누락된 경우
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ExceptionResponseBody> handleMissingServletRequestPart(MissingServletRequestPartException ex) {
        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, ex.getRequestPartName() + " 요청에 필요한 파트가 없습니다"));
    }

    // 4. @ModelAttribute 또는 multipart/form-data에서 바인딩 실패
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponseBody> handleBindException(BindException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .toList();

        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, "데이터 바인딩 실패", errors));
    }

    // 5. 필수 @RequestParam 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponseBody> handleMissingServletRequestParam(MissingServletRequestParameterException ex) {
        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, ex.getParameterName() + " 필수 요청 파라미터가 없습니다"));
    }

    // 6. @RequestParam 등에서 타입이 안 맞는 경우 (예: int에 "abc")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponseBody> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, ex.getName() + " 요청 타입이 잘못되었습니다"));
    }

    // 7. JSON 파싱 실패 (예: 날짜 형식 오류, 잘못된 JSON 구조 등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseBody> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, "요청 본문(JSON)을 읽을 수 없습니다. 형식이 올바른지 확인해주세요."));
    }

    // 8. 지원하지 않는 Content-Type (예: text/plain으로 JSON 요청)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponseBody> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return createExceptionResponse(errorCode,
                ExceptionResponseBody.of(errorCode, "지원하지 않는 미디어 타입입니다."));
    }

    private ResponseEntity<ExceptionResponseBody> createExceptionResponse(ErrorCode errorCode, ExceptionResponseBody exceptionResponseBody) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(exceptionResponseBody);
    }
}