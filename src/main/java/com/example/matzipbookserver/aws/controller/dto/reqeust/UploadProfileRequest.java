package com.example.matzipbookserver.aws.controller.dto.reqeust;

import com.example.matzipbookserver.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

public record UploadProfileRequest(
        Member member,
        MultipartFile profileImage
) {
}
