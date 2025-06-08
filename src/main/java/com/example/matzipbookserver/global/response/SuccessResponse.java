package com.example.matzipbookserver.global.response;

import com.example.matzipbookserver.global.response.success.SuccessCode;
import org.springframework.http.ResponseEntity;

public class SuccessResponse<T> extends ResponseEntity<CustomResponseDto<T>> {
    public SuccessResponse(final SuccessCode successCode, final T result) {
        super(new CustomResponseDto<>(successCode.getCode(), successCode.getMessage(), result),
                successCode.getHttpStatus()
        );
    }

    public static <T> SuccessResponse<T> of(final SuccessCode successCode, final T result) {
        return new SuccessResponse<>(successCode, result);
    }

    public static SuccessResponse<String> of(final SuccessCode successCode) {
        return new SuccessResponse<>(successCode, successCode.getMessage());
    }
}