package com.example.matzipbookserver.s3.controller.dto.reqeust;

import com.example.matzipbookserver.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

public record UploadProfileRequest(
        Member member,
        MultipartFile profileImage
) {
}
