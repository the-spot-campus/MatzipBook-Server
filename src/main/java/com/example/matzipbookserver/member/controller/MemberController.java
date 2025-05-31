package com.example.matzipbookserver.member.controller;

import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.MemberSuccessCode;
import com.example.matzipbookserver.member.controller.dto.request.FcmTokenRequest;
import com.example.matzipbookserver.member.controller.dto.response.MemberInfoResponse;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.repository.MemberRepository;
import com.example.matzipbookserver.member.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.matzipbookserver.global.resolver.CurrentMember;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberRepository memberRepository;
    private final FcmTokenService fcmTokenService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(@CurrentMember Member member) {
        return ResponseEntity.ok(new MemberInfoResponse(member));
    }


    @PostMapping("/fcm")
    public ResponseEntity<?> saveFcmToken(@CurrentMember Member member, @RequestBody FcmTokenRequest request) {
        fcmTokenService.saveOrUpdate(member, request.fcmToken());
        return ResponseEntity.ok(new SuccessResponse<>(MemberSuccessCode.FCM_TOKEN_SAVED,null));
    }
}
