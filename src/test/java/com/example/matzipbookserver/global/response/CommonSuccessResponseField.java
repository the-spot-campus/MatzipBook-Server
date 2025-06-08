package com.example.matzipbookserver.global.response;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class CommonSuccessResponseField {
    public static FieldDescriptor[] createCommonResponseFields() {
        return new FieldDescriptor[]{
                fieldWithPath("code").description("요청 응답 코드"),
                fieldWithPath("message").description("응답 메시지"),
                fieldWithPath("result").description("응답 데이터")
        };
    }
}
