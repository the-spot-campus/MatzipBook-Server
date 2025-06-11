package com.example.matzipbookserver.s3.controller.dto.reqeust;

import com.example.matzipbookserver.member.domain.Member;
import jakarta.validation.constraints.NotNull;

public record DeleteProfileRequest(
        @NotNull(message = "사용자가 존재해야 합니다.")
        Member member
) {
}