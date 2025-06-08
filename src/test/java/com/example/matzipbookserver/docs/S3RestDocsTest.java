package com.example.matzipbookserver.docs;

import com.example.matzipbookserver.global.BaseIntegrationTest;
import com.example.matzipbookserver.global.DummyGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class S3ControllerTest extends BaseRestDocsTest {

    @Autowired
    private DummyGenerator dummyGenerator;

    @Test
    @DisplayName("API - 회원가입")
    void signUp() throws Exception {
        final SignUpRequest request = createSampleSignUpRequest();

        MockMultipartFile multipartFile = createMultipartFile();
        MockMultipartFile requestMultipartFile = new MockMultipartFile("request",
                null, "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

        dummyGenerator.authenticateEmail(request.email());

        // When and Then
        mockMvc.perform(multipart("/members")
                        .file(multipartFile)
                        .file(requestMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member-signup",
                        requestParts(
                                partWithName("profileImage").description("프로필 이미지 파일"),
                                partWithName("request").description("회원가입 정보")
                        ),
                        requestPartFields("request",
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("mbti").description("MBTI ex) INTJ"),
                                fieldWithPath("gender").description("성별 ex) MALE, FEMALE"),
                                fieldWithPath("nationality").description("국적"),
                                fieldWithPath("birthday").description("생년월일"),
                                fieldWithPath("aboutMe").description("자기소개")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }


}