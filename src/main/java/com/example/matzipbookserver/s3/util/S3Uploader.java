package com.example.matzipbookserver.s3.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.matzipbookserver.s3.controller.dto.reqeust.OldFileNameRequest;
import com.example.matzipbookserver.s3.controller.dto.reqeust.S3SingleUploadRequest;
import com.example.matzipbookserver.s3.controller.dto.response.S3FileResponse;
import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.properties.AmazonS3Properties;
import com.example.matzipbookserver.global.response.error.S3ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3 amazonS3;
    private final AmazonS3Properties amazonS3Properties;

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10 MB

    public S3FileResponse singleUpload(@Valid S3SingleUploadRequest s3SingleUploadRequest){
        return uploadToS3(s3SingleUploadRequest);
    }

    public boolean deleteImage(@Valid OldFileNameRequest oldFileNameRequest) {
        if (!amazonS3.doesObjectExist(amazonS3Properties.getBucket(), oldFileNameRequest.oldFileName())) {
            throw new AmazonS3Exception("Object " + oldFileNameRequest.oldFileName() + " does not exist!");
        }

        amazonS3.deleteObject(amazonS3Properties.getBucket(), oldFileNameRequest.oldFileName());
        return true;
    }

    private S3FileResponse uploadToS3(@Valid S3SingleUploadRequest s3SingleUploadRequest) {
        checkFileLimit(s3SingleUploadRequest);

        String uuidName = generateUUIDFileName(s3SingleUploadRequest);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(s3SingleUploadRequest.profileImage().getContentType());
        metadata.setContentLength(s3SingleUploadRequest.profileImage().getSize());
        try {
            amazonS3.putObject(
                    new PutObjectRequest(
                            amazonS3Properties.getBucket(),
                            uuidName,
                            s3SingleUploadRequest.profileImage().getInputStream(),
                            metadata
                    )
            );

            return new S3FileResponse(uuidName, amazonS3.getUrl(amazonS3Properties.getBucket(), uuidName).toString());
        } catch (IOException e) {
            throw new RestApiException(S3ErrorCode.PROFILE_UPLOAD_FAILED);
        }
    }

    private void checkFileLimit(@Valid S3SingleUploadRequest s3SingleUploadRequest) {
        if (s3SingleUploadRequest.profileImage().getSize() > MAX_IMAGE_SIZE) {
            throw new RestApiException(S3ErrorCode.POST_IMAGE_ERROR);
        }
    }

    private String generateUUIDFileName(@Valid S3SingleUploadRequest s3SingleUploadRequest) {
        String contentType = s3SingleUploadRequest.profileImage().getContentType();
        String fileExtension = contentType != null && contentType.contains("/")
                ? "." + contentType.split("/")[1]
                : ".png";
        return "profile/" + UUID.randomUUID() + fileExtension;
    }
}