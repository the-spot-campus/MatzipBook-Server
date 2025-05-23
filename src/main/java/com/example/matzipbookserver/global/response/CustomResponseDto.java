package com.example.matzipbookserver.global.response;

public record CustomResponseDto<T> (String code, String message, T result) {
}