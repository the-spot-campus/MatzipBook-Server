package com.example.matzipbookserver;

import com.example.matzipbookserver.bookmark.controller.BookmarkController;
import com.example.matzipbookserver.member.controller.AuthController;
import com.example.matzipbookserver.member.controller.MemberController;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.s3.controller.S3Controller;
import com.example.matzipbookserver.store.controller.StoreController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseRestDocsTest {

    @SpyBean protected AuthController authController;
    @SpyBean protected MemberController memberController;
    @SpyBean protected S3Controller s3Controller;
    @SpyBean protected StoreController storeController;
    @SpyBean protected BookmarkController bookmarkController;

    @Autowired protected MockMvc mockMvc;
    @Autowired protected DummyGenerator dummyGenerator;

    protected Member member;
    protected String GIVEN_ACCESS_TOKEN;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
    }
}