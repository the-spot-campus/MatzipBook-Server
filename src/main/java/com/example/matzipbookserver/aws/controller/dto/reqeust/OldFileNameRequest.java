package com.example.matzipbookserver.aws.controller.dto.reqeust;

import lombok.Builder;

@Builder
public record OldFileNameRequest(
        String oldFileName
) {
    public static OldFileNameRequest of(String url){
        return OldFileNameRequest.builder()
                .oldFileName(url)
                .build();
    }
}