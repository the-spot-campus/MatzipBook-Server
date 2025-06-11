package com.example.matzipbookserver.s3.service;

import com.example.matzipbookserver.s3.controller.dto.reqeust.DeleteProfileRequest;
import com.example.matzipbookserver.s3.controller.dto.reqeust.OldFileNameRequest;
import com.example.matzipbookserver.s3.controller.dto.reqeust.S3SingleUploadRequest;
import com.example.matzipbookserver.s3.controller.dto.reqeust.UploadProfileRequest;
import com.example.matzipbookserver.s3.controller.dto.response.S3FileResponse;
import com.example.matzipbookserver.s3.controller.dto.response.UploadProfileResponse;
import com.example.matzipbookserver.s3.util.S3Uploader;
import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.S3ErrorCode;
import com.example.matzipbookserver.member.domain.MemberImage;
import com.example.matzipbookserver.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class S3Service {
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;

    @Transactional
    public UploadProfileResponse uploadProfileImage(@Valid UploadProfileRequest request) {
        memberRepository.findMemberImageById(request.member().getId())
                .ifPresent(image -> s3Uploader.deleteImage(OldFileNameRequest.of(image.getFileName())));

        S3FileResponse s3File = s3Uploader.singleUpload(new S3SingleUploadRequest(request.profileImage()));
        request.member().changeImage(s3File);
        memberRepository.save(request.member());

        return new UploadProfileResponse(s3File.fileURL());
    }

    @Transactional
    public boolean deleteProfileImage(@Valid DeleteProfileRequest request) {
        MemberImage profileImage = memberRepository.findMemberImageById(request.member().getId())
                .orElseThrow(() -> new RestApiException(S3ErrorCode.DELETE_IMAGE_ERROR));
        s3Uploader.deleteImage(new OldFileNameRequest(profileImage.getFileName()));

        request.member().deleteImage();
        memberRepository.save(request.member());

        return true;
    }
}