package com.example.matzipbookserver;

import com.example.matzipbookserver.s3.controller.dto.reqeust.S3SingleUploadRequest;
import com.example.matzipbookserver.s3.controller.dto.response.S3File;
import com.example.matzipbookserver.s3.util.S3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    @SpyBean protected S3Uploader awsS3Uploader;
    @Autowired private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUpSpy() {
        //DATABASE
        databaseCleanUp.execute();

        //AWS
        S3File tmpFile = new S3File(DummyGenerator.GIVEN_FILE_NAME, DummyGenerator.GIVEN_FILE_URL);
        doReturn(tmpFile).when(awsS3Uploader).singleUpload(new S3SingleUploadRequest(any(MultipartFile.class)));
        doReturn(true).when(awsS3Uploader).delete(any());
    }
}