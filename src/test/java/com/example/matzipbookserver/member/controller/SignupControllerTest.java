package com.example.matzipbookserver.member.controller;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import com.example.matzipbookserver.global.response.error.MemberErrorCode;
import com.example.matzipbookserver.member.controller.dto.request.SignupRequest;
import com.example.matzipbookserver.member.controller.dto.response.SignupResponse;
import com.example.matzipbookserver.member.repository.MemberRepository;
import com.example.matzipbookserver.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = SignupController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SignupControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @MockBean private MemberService memberService;
    @MockBean private JwtTokenProvider jwtTokenProvider;
    @MockBean private MemberRepository memberRepository;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    @Test
    void 회원가입_성공_테스트() throws Exception {
        // given
        SignupRequest request = new SignupRequest(
                "test@email.com", "kakao", "kakao123", "테스터", "2004-04-21",
                "F", "https://image.com/profile.png","부경대학교"
        );

        SignupResponse response = new SignupResponse(1L, "test@email.com","테스터", "jwt.token.value");

        Mockito.when(memberService.signup(Mockito.any())).thenReturn(response);

        //when & then
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-003"))
                .andExpect(jsonPath("$.result.email").value("test@email.com"))
                .andExpect(jsonPath("$.result.jwtToken").value("jwt.token.value"))
                .andDo(document("signup-success",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("provider").description("소셜 로그인 제공자"),
                                fieldWithPath("providerId").description("소셜 고유 식별자"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("birth").description("생년월일 (yyyy-MM-dd)"),
                                fieldWithPath("gender").description("성별 (M/F)"),
                                fieldWithPath("profileImagePath").description("프로필 이미지 URL"),
                                fieldWithPath("university").description("대학교 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("result.id").description("회원 ID"),
                                fieldWithPath("result.email").description("이메일"),
                                fieldWithPath("result.nickname").description("닉네임"),
                                fieldWithPath("result.jwtToken").description("발급된 JWT 토큰")
                        )
                ));
    }

    @Test
    void 회원가입_중복_실패_테스트() throws Exception {
        SignupRequest request = new SignupRequest(
                "test@email.com", "apple123", "apple", "테스터",
                "2004-04-21", "F", "https://image.com/profile.png", "부경대학교"
        );

        Mockito.when(memberService.signup(Mockito.any()))
                .thenThrow(new RestApiException(MemberErrorCode.ALREADY_REGISTERED));

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("MEMBER-001")));
//                .andDo(document("signup-already-registered",
//                requestFields(
//                        fieldWithPath("email").description("이메일"),
//                        fieldWithPath("provider").description("소셜 로그인 제공자"),
//                        fieldWithPath("providerId").description("소셜 고유 식별자"),
//                        fieldWithPath("nickname").description("사용자 닉네임"),
//                        fieldWithPath("birth").description("생년월일 (yyyy-MM-dd)"),
//                        fieldWithPath("gender").description("성별 (M/F)"),
//                        fieldWithPath("profileImagePath").description("프로필 이미지 URL"),
//                        fieldWithPath("university").description("대학교 이름")
//                ),
//                responseFields(
//                        fieldWithPath("code").description("에러 코드"),
//                        fieldWithPath("message").description("에러 메시지")
//                )
//        ));
    }


}
