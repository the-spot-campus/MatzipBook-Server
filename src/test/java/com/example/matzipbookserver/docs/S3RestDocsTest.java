package com.example.matzipbookserver.docs;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.CommonErrorResponseField;
import com.example.matzipbookserver.global.response.CommonSuccessResponseField;
import com.example.matzipbookserver.global.response.SuccessResponse;
import com.example.matzipbookserver.global.response.error.S3ErrorCode;
import com.example.matzipbookserver.global.response.success.S3SuccessCode;
import com.example.matzipbookserver.s3.controller.dto.response.UploadProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class S3RestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("s3 프로필 이미지 업로드 성공")
    void signUpSuccess() throws Exception {
        // given
        MockMultipartFile multipartFile = dummyGenerator.createMockMultipartFile();

        UploadProfileResponse mockResponse = new UploadProfileResponse("test_url");
        doReturn(mockResponse).when(s3Service)
                .uploadProfileImage(any());

        // when & then
        mockMvc.perform(multipart("/api/profile")
                        .file(multipartFile)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(document("profile/upload",
                        requestParts(partWithName("profileImage").description("프로필 이미지 파일")),
                        responseFields(CommonSuccessResponseField.createCommonResponseFields())
                                .andWithPrefix("result.",
                                        fieldWithPath("path").description("이미지 경로"))
                ));
    }

    @Test
    @DisplayName("s3 프로필 이미지 업로드 실패")
    void signUpFail() throws Exception {
        // given
        MockMultipartFile multipartFile = dummyGenerator.createMockMultipartFile();

        doThrow(new RestApiException(S3ErrorCode.PROFILE_UPLOAD_FAILED))
                .when(s3Service).uploadProfileImage(any());

        // when & then
        mockMvc.perform(multipart("/api/profile")
                        .file(multipartFile)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andDo(document("profile/upload-fail",
                        requestParts(partWithName("profileImage").description("프로필 이미지 파일")),
                        responseFields(CommonErrorResponseField.createCommonErrorResponseFields())));
    }

    @Test
    @DisplayName("s3 프로필 이미지 삭제 성공")
    void deleteProfileImageSuccess() throws Exception {
        // given
        final SuccessResponse<String> response = SuccessResponse.of(
                S3SuccessCode.PROFILE_DELETED_SUCCESS, "프로필 이미지를 성공적으로 삭제하였습니다.");

        doReturn(true).when(s3Service).deleteProfileImage(any());

        // When and Then
        mockMvc.perform(delete("/api/profile")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("profile/delete",
                        responseFields(
                                CommonSuccessResponseField.createCommonResponseFields()
                        )
                ));
    }

    @Test
    @DisplayName("s3 프로필 이미지 삭제 실패")
    void deleteProfileImageFail() throws Exception {
        // given
        doThrow(new RestApiException(S3ErrorCode.DELETE_IMAGE_ERROR))
                .when(s3Service).deleteProfileImage(any());

        // When and Then
        mockMvc.perform(delete("/api/profile")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("profile/delete-fail",
                        responseFields(
                                CommonErrorResponseField.createCommonErrorResponseFields()
                        )
                ));
    }
}