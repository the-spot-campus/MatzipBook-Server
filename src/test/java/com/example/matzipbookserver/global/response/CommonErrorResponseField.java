package com.example.matzipbookserver.global.response;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class CommonErrorResponseField {
    public static FieldDescriptor[] createCommonErrorResponseFields() {
        return new FieldDescriptor[]{
                fieldWithPath("errorCode").description("요청 응답 에러 코드"),
                fieldWithPath("errorDescription").description("응답 메시지"),
                fieldWithPath("details").description("내부 에러 메시지"),
                fieldWithPath("errors").description("필드 에러 오류 설명")
        };
    }
}