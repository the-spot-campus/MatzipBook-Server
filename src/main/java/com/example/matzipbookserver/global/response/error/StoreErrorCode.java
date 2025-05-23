package com.example.matzipbookserver.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements ErrorCode{

    STORE_NOT_EXIST("PLACE_NOT_FOUND_001", HttpStatus.NOT_FOUND,"카카오에서 장소를 찾을 수 없음");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String message;
}
