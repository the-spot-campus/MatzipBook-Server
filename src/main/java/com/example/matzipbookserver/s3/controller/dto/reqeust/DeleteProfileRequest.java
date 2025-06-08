package com.example.matzipbookserver.s3.controller.dto.reqeust;

import com.example.matzipbookserver.member.domain.Member;

public record DeleteProfileRequest(
        Member member
) {
}
