package com.example.matzipbookserver.uploader.service;

import com.example.matzipbookserver.uploader.controller.dto.reqeust.DeleteProfileRequest;
import com.example.matzipbookserver.uploader.controller.dto.reqeust.OldFileNameRequest;
import com.example.matzipbookserver.uploader.controller.dto.reqeust.S3SingleUploadRequest;
import com.example.matzipbookserver.uploader.controller.dto.reqeust.UploadProfileRequest;
import com.example.matzipbookserver.uploader.controller.dto.response.S3File;
import com.example.matzipbookserver.uploader.controller.dto.response.UploadProfileResponse;
import com.example.matzipbookserver.uploader.util.S3Uploader;
import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.S3ErrorCode;
import com.example.matzipbookserver.member.domain.MemberImage;
import com.example.matzipbookserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;

    @Transactional
    public UploadProfileResponse uploadProfileImage(UploadProfileRequest request) {
        if (request.profileImage() == null || request.profileImage().isEmpty())
            throw new RestApiException(S3ErrorCode.POST_IMAGE_ERROR);

        memberRepository.findMemberImageById(request.member().getId())
                .ifPresent(image -> s3Uploader.delete(OldFileNameRequest.of(image.getFileName())));

        S3File s3File = s3Uploader.singleUpload(new S3SingleUploadRequest(request.profileImage()));
        request.member().changeImage(s3File);
        memberRepository.save(request.member());

        return new UploadProfileResponse(s3File.fileURL());
    }

    @Transactional
    public void deleteProfileImage(DeleteProfileRequest request) {
        MemberImage profileImage = memberRepository.findMemberImageById(request.member().getId())
                .orElseThrow(() -> new RestApiException(S3ErrorCode.DELETE_IMAGE_ERROR));
        s3Uploader.delete(new OldFileNameRequest(profileImage.getFileName()));

        request.member().deleteImage();
        memberRepository.save(request.member());
    }
}