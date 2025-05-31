package com.example.matzipbookserver.member.controller;

import com.example.matzipbookserver.global.exception.ApiExceptionHandler;
import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import com.example.matzipbookserver.global.response.error.AuthErrorCode;
import com.example.matzipbookserver.member.controller.dto.request.AppleLoginRequest;
import com.example.matzipbookserver.member.controller.dto.request.KakaoLoginRequest;
import com.example.matzipbookserver.member.controller.dto.response.AppleLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.KakaoLoginResponse;
import com.example.matzipbookserver.member.controller.dto.response.SignupNeededResponse;
import com.example.matzipbookserver.member.service.AuthService;
import com.example.matzipbookserver.member.service.FcmTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean private FcmTokenService fcmTokenService;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context)
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
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("code111", "fcmToken111");
        KakaoLoginResponse response = new KakaoLoginResponse("fake-jwt-token", new KakaoLoginResponse.UserInfo(1L,"test@email.com"));

        Mockito.when(authService.kakaoLogin(anyString(), anyString())).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/auth/login/kakao")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-001"))
                .andExpect(jsonPath("$.result.jwtToken").value("fake-jwt-token"))
                .andExpect(jsonPath("$.result.user.email").value("test@email.com"))
                .andDo(document("kakao-login-success",
                        requestFields(
                                fieldWithPath("code").description("카카오 인가 코드"),
                                fieldWithPath("fcmToken").description("FCM 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("result.jwtToken").description("JWT 토큰"),
                                fieldWithPath("result.user.id").description("사용자 ID"),
                                fieldWithPath("result.user.email").description("사용자 이메일")
                        )
                ));


    }

    @Test
    void 카카오_로그인_회원가입필요_테스트() throws Exception {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("code111", "fcmToken111");
        KakaoLoginResponse response = new KakaoLoginResponse("fake-jwt-token", new KakaoLoginResponse.UserInfo(1L,"test@email.com"));


        Mockito.when(authService.kakaoLogin(anyString(), anyString()))
                .thenReturn(new SignupNeededResponse("test@email.com", "kakao123"));


        // when&then
        mockMvc.perform(post("/api/auth/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-002"))
                .andExpect(jsonPath("$.result.email").value("test@email.com"))
                .andDo(document("kakao-login-need-signup",
                        requestFields(
                                fieldWithPath("code").description("카카오 인가 코드"),
                                fieldWithPath("fcmToken").description("FCM 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("result.email").description("사용자 이메일"),
                                fieldWithPath("result.providerId").description("소셜 제공자 식별자")
                        )
                ));

    }



    @Test
    void 애플_로그인_성공_테스트() throws Exception {
        // given
        AppleLoginRequest request = new AppleLoginRequest("code111", "fcmToken111");
        AppleLoginResponse response = new AppleLoginResponse("fake-jwt-token", new AppleLoginResponse.UserInfo(1L,"test@email.com"));

        Mockito.when(authService.appleLogin(anyString(), anyString())).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/auth/login/apple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-001"))
                .andExpect(jsonPath("$.result.jwtToken").value("fake-jwt-token"))
                .andExpect(jsonPath("$.result.user.email").value("test@email.com"))
                .andDo(document("apple-login-success",
                        requestFields(
                                fieldWithPath("code").description("애플 인가 코드"),
                                fieldWithPath("fcmToken").description("FCM 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("result.jwtToken").description("JWT 토큰"),
                                fieldWithPath("result.user.id").description("사용자 ID"),
                                fieldWithPath("result.user.email").description("사용자 이메일")
                        )
                ));

    }

    @Test
    void 애플_로그인_회원가입필요_테스트() throws Exception {
        // given
        AppleLoginRequest request = new AppleLoginRequest("code111", "fcmToken111");
        AppleLoginResponse response = new AppleLoginResponse("fake-jwt-token", new AppleLoginResponse.UserInfo(1L,"test@email.com"));


        Mockito.when(authService.appleLogin(anyString(), anyString()))
                .thenReturn(new SignupNeededResponse("test@email.com", "apple123"));



        // when&then
        mockMvc.perform(post("/api/auth/login/apple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER-002"))
                .andExpect(jsonPath("$.result.email").value("test@email.com"))
                .andDo(print())
                .andDo(document("apple-login-need-signup",
                        requestFields(
                                fieldWithPath("code").description("애플 인가 코드"),
                                fieldWithPath("fcmToken").description("FCM 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("result.email").description("사용자 이메일"),
                                fieldWithPath("result.providerId").description("소셜 제공자 식별자")
                        )
                ));

    }



    @Test
    void 애플_로그인_토큰파싱_실패_테스트() throws Exception {
        //given
        AppleLoginRequest request = new AppleLoginRequest("code111", "fcmToken111");

        Mockito.when(authService.appleLogin(anyString(), anyString()))
                .thenThrow(new RestApiException(AuthErrorCode.APPLE_ID_TOKEN_PARSE_FAILED));

        //when&then
        mockMvc.perform(post("/api/auth/login/apple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("AUTH-006")));
//                .andDo(document("apple-login-token-parse-failed",
//                        requestFields(
//                                fieldWithPath("code").description("애플 인가 코드"),
//                                fieldWithPath("fcmToken").description("FCM 토큰")
//                        ),
//                        responseFields(
//                        fieldWithPath("code").description("에러 코드"),
//                        fieldWithPath("message").description("에러 메시지")
//                        )
//                ));

    }



}
