package com.example.matzipbookserver.global.response.success;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreSuccessCode implements SuccessCode {

    OK("STORE_SEARCH_SUCCESS", HttpStatus.OK, "가게 조회 성공"),
    ;



    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
