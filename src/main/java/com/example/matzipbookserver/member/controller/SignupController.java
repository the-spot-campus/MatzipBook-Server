package com.example.matzipbookserver.member.controller;

import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.success.MemberSuccessCode;
import com.example.matzipbookserver.member.controller.dto.request.SignupRequest;
import com.example.matzipbookserver.member.controller.dto.response.SignupResponse;
import com.example.matzipbookserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
public class SignupController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        SignupResponse response = memberService.signup(request);
        return SuccessResponse.of(MemberSuccessCode.SIGNUP_SUCCESS,response);
    }
}
