package com.example.matzipbookserver.member.controller;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import com.example.matzipbookserver.global.response.error.AuthErrorCode;
import com.example.matzipbookserver.global.response.error.MemberErrorCode;
import com.example.matzipbookserver.member.controller.dto.request.AppleLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.KakaoLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.SignUpRequest;
import com.example.matzipbookserver.member.controller.dto.response.AppleLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.KakaoLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.SignUpResponse;
import com.example.matzipbookserver.member.controller.dto.response.SignupNeededResponse;
import com.example.matzipbookserver.member.service.AuthService;
import com.example.matzipbookserver.member.service.FcmTokenService;
import com.example.matzipbookserver.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.AuthControllerTestConfig.class)
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private RestDocumentationResultHandler restDocs;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private FcmTokenService fcmTokenService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{class-name}/{method-name}"))
                .alwaysDo(print())
                .build();
    }

    @TestConfiguration
    static class AuthControllerTestConfig {
        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }

        @Bean
        public FcmTokenService fcmTokenService() {
            return Mockito.mock(FcmTokenService.class);
        }
    }

    @Test
    void 카카오_로그인_성공_테스트() throws Exception {
        KakaoLoginRequest request = new KakaoLoginRequest("code111", "fcmToken111");
        KakaoLoginResponse response = new KakaoLoginResponse("fake-jwt-token", new KakaoLoginResponse.UserInfo(1L, "test@email.com"));

        Mockito.when(authService.kakaoLogin(anyString(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-001"))
                .andExpect(jsonPath("$.result.jwtToken").value("fake-jwt-token"))
                .andExpect(jsonPath("$.result.user.email").value("test@email.com"));
    }

    @Test
    void 카카오_로그인_회원가입필요_테스트() throws Exception {
        KakaoLoginRequest request = new KakaoLoginRequest("code111", "fcmToken111");
        Mockito.when(authService.kakaoLogin(anyString(), anyString()))
                .thenReturn(new SignupNeededResponse("test@email.com", "kakao123"));

        mockMvc.perform(post("/api/auth/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-002"))
                .andExpect(jsonPath("$.result.email").value("test@email.com"));
    }

    @Test
    void 애플_로그인_성공_테스트() throws Exception {
        AppleLoginRequest request = new AppleLoginRequest("code111", "fcmToken111");
        AppleLoginResponse response = new AppleLoginResponse("fake-jwt-token", new AppleLoginResponse.UserInfo(1L, "test@email.com"));

        Mockito.when(authService.appleLogin(anyString(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login/apple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-001"))
                .andExpect(jsonPath("$.result.jwtToken").value("fake-jwt-token"))
                .andExpect(jsonPath("$.result.user.email").value("test@email.com"));
    }

    @Test
    void 애플_로그인_회원가입필요_테스트() throws Exception {
        AppleLoginRequest request = new AppleLoginRequest("code111", "fcmToken111");
        Mockito.when(authService.appleLogin(anyString(), anyString()))
                .thenReturn(new SignupNeededResponse("test@email.com", "apple123"));

        mockMvc.perform(post("/api/auth/login/apple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-002"))
                .andExpect(jsonPath("$.result.email").value("test@email.com"));
    }

    @Test
    void 애플_로그인_토큰파싱_실패_테스트() throws Exception {
        AppleLoginRequest request = new AppleLoginRequest("code111", "fcmToken111");

        Mockito.when(authService.appleLogin(anyString(), anyString()))
                .thenThrow(new RestApiException(AuthErrorCode.APPLE_ID_TOKEN_PARSE_FAILED));

        mockMvc.perform(post("/api/auth/login/apple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("AUTH-006")));
    }

    @Test
    void 회원가입_성공_테스트() throws Exception {
        MockMultipartFile file = generateMockProfileImageFile();

        SignUpRequest request = generateSignUpRequest();

        MockMultipartFile jsonPart = generateJsonPart(request);

        SignUpResponse response = SignUpResponse.builder()
                .id(1L)
                .email("test@email.com")
                .nickname("테스터")
                .jwtToken("jwt.token.value")
                .build();

        Mockito.when(memberService.signup(Mockito.any(), Mockito.any())).thenReturn(response);

        mockMvc.perform(multipart("/api/auth/signup")
                        .file(file)
                        .file(jsonPart)
                        .with(csrf())
                        .contentType("multipart/form-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-003"))
                .andExpect(jsonPath("$.result.email").value("test@email.com"))
                .andExpect(jsonPath("$.result.jwtToken").value("jwt.token.value"))
                .andDo(document("auth/signup",
                        requestParts(
                                partWithName("signUpRequest").description("회원가입 요청 JSON"),
                                partWithName("profileImage").description("프로필 이미지 (선택)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("result.id").description("회원 ID"),
                                fieldWithPath("result.email").description("이메일"),
                                fieldWithPath("result.nickname").description("닉네임"),
                                fieldWithPath("result.jwtToken").description("JWT 토큰")
                        )
                ));
    }

    @Test
    void 회원가입_중복_실패_테스트() throws Exception {
        MockMultipartFile file = generateMockProfileImageFile();

        SignUpRequest request = generateSignUpRequest();

        MockMultipartFile jsonPart = generateJsonPart(request);

        Mockito.when(memberService.signup(Mockito.any(), Mockito.any()))
                .thenThrow(new RestApiException(MemberErrorCode.ALREADY_REGISTERED));

        mockMvc.perform(multipart("/api/auth/signup")
                        .file(file)
                        .file(jsonPart)
                        .with(csrf())
                        .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("MEMBER-001")));
    }

    private MockMultipartFile generateJsonPart(SignUpRequest request) throws JsonProcessingException {
        return new MockMultipartFile(
                "signUpRequest",
                "", "application/json",
                objectMapper.writeValueAsBytes(request)
        );
    }

    private static SignUpRequest generateSignUpRequest() {
        return new SignUpRequest(
                "test@email.com", "kakao", "kakao123", "테스터", "2004-04-21",
                "F", "부경대학교"
        );
    }

    private static MockMultipartFile generateMockProfileImageFile() {
        return new MockMultipartFile(
                "profileImage",
                "profile.png",
                "image/png",
                "test-image-content".getBytes()
        );
    }
}
