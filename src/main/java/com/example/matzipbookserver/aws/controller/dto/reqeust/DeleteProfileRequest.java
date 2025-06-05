package com.example.matzipbookserver.aws.controller.dto.reqeust;

import com.example.matzipbookserver.member.domain.Member;

public record DeleteProfileRequest(
        Member member
) {
}
