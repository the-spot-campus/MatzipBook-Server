package com.example.matzipbookserver.docs;

import com.example.matzipbookserver.global.DatabaseCleanUp;
import com.example.matzipbookserver.global.DummyGenerator;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.s3.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseRestDocsTest {

    @MockBean protected S3Service s3Service;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected DummyGenerator dummyGenerator;
    @Autowired private DatabaseCleanUp databaseCleanUp;

    protected Member member;
    protected String GIVEN_ACCESS_TOKEN;

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
        member = dummyGenerator.createSingleMemberWithMemberImage();
        GIVEN_ACCESS_TOKEN = dummyGenerator.createAccessToken(member);
    }
}