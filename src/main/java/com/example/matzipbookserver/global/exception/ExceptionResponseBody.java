package com.example.matzipbookserver.global.exception;


import com.example.matzipbookserver.global.response.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Getter;

@Getter
@JsonPropertyOrder(value = {"errorCode"})
public class ExceptionResponseBody {
    private final String errorCode;
    private final String errorDescription;
    @JsonProperty("details")
    private final String detailMessages;
    private final List<String> errors;

    private ExceptionResponseBody(String errorCode, String errorDescription, String detailMessages, List<String> errors) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.detailMessages = detailMessages;
        this.errors = errors;
    }

    public static ExceptionResponseBody of(ErrorCode errorCode) {
        return new ExceptionResponseBody(errorCode.getDevelopCode(), errorCode.getErrorDescription(), null, null);
    }

    public static ExceptionResponseBody of(ErrorCode errorCode, String detailMessages) {
        return new ExceptionResponseBody(errorCode.getDevelopCode(), errorCode.getErrorDescription(), detailMessages, null);
    }

    public static ExceptionResponseBody of(ErrorCode errorCode, List<String> errors) {
        return new ExceptionResponseBody(errorCode.getDevelopCode(), errorCode.getErrorDescription(), null, errors);
    }

    public static ExceptionResponseBody of(ErrorCode errorCode, String detailMessages, List<String> errors) {
        return new ExceptionResponseBody(errorCode.getDevelopCode(), errorCode.getErrorDescription(), detailMessages, errors);
    }
}
