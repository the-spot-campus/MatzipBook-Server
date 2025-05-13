package com.example.matzipbookserver.global.response;

public record CustomResponseDto<T> (String code, T result) {
}