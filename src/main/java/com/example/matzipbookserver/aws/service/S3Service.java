package com.example.matzipbookserver.aws.service;

import com.example.matzipbookserver.aws.controller.dto.reqeust.DeleteProfileRequest;
import com.example.matzipbookserver.aws.controller.dto.reqeust.OldFileNameRequest;
import com.example.matzipbookserver.aws.controller.dto.reqeust.S3SingleUploadRequest;
import com.example.matzipbookserver.aws.controller.dto.reqeust.UploadProfileRequest;
import com.example.matzipbookserver.aws.controller.dto.response.S3File;
import com.example.matzipbookserver.aws.controller.dto.response.UploadProfileResponse;
import com.example.matzipbookserver.aws.util.S3Uploader;
import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.S3ErrorCode;
import com.example.matzipbookserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AwsService {
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;

    @Transactional
    public UploadProfileResponse uploadProfileImage(UploadProfileRequest request) {
        if (request.profileImage() == null || request.profileImage().isEmpty())
            throw new RestApiException(S3ErrorCode.POST_IMAGE_ERROR);

        memberRepository.findUrlById(request.member().getId())
                .ifPresent(url -> s3Uploader.delete(OldFileNameRequest.of(url)));

        S3File s3File = s3Uploader.singleUpload(new S3SingleUploadRequest(request.profileImage()));
        memberRepository.updateUrlById(s3File.fileURL(), request.member().getId());

        return new UploadProfileResponse(s3File.fileURL());
    }

    @Transactional
    public void deleteProfileImage(DeleteProfileRequest request){
        String profileImageUrl = memberRepository.findUrlById(request.member().getId())
                .orElseThrow(() -> new RestApiException(S3ErrorCode.DELETE_IMAGE_ERROR));
//        Member 도메인에 Image 엔티티 만들고, 수정
        s3Uploader.delete(profileImageUrl)
    }
}