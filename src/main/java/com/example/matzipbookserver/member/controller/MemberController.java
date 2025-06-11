package com.example.matzipbookserver.member.controller;

import com.example.matzipbookserver.global.resolver.CurrentMember;
import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.MemberSuccessCode;
import com.example.matzipbookserver.member.controller.dto.response.MemberInfoResponse;
import com.example.matzipbookserver.member.domain.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @GetMapping("/me")
    public SuccessResponse<MemberInfoResponse> getCurrentMember(@CurrentMember Member member) {
        return SuccessResponse.of(MemberSuccessCode.MEMBER_INFO_SUCCESS, MemberInfoResponse.from(member));
    }
}
