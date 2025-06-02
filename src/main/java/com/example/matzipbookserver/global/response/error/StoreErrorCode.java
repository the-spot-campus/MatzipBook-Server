package com.example.matzipbookserver.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements ErrorCode{

    STORE_NOT_EXIST("PLACE_NOT_FOUND_001", HttpStatus.NOT_FOUND,"카카오에서 장소를 찾을 수 없음"),
    STORE_FILTER_FAILED("STORE_FILTER_ERROR_001", HttpStatus.INTERNAL_SERVER_ERROR, "가게 필터링 중 오류 발생"),
    INVALID_FILTER_CONDITION("STORE_FILTER_ERROR_002", HttpStatus.BAD_REQUEST, "잘못된 필터링 조건");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String message;
}
