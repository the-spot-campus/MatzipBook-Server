package com.example.matzipbookserver.store.controller;


import com.example.matzipbookserver.global.response.success.StoreSuccessCode;
import com.example.matzipbookserver.store.controller.dto.StoreRankingResponseDto;
import com.example.matzipbookserver.store.controller.dto.StoreResponseDto;
import com.example.matzipbookserver.store.domain.Store;
import com.example.matzipbookserver.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    // result for @BeforeEach
    private Store store1;
    private Store store2;
    private StoreResponseDto storeResponseDto1;
    private StoreResponseDto storeResponseDto2;
    private StoreRankingResponseDto storeRankingDto1;
    private StoreRankingResponseDto storeRankingDto2;

    // Controller에 정의된 MAX_RADIUS를 테스트에서도 참조하기 위함
    // 실제 Controller의 MAX_RADIUS 값과 동일하게 유지하거나,
    // Controller에서 해당 상수를 public으로 변경하여 직접 참조하는 것도 방법입니다.
    private static final int CONTROLLER_MAX_RADIUS = 10000;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDoc) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDoc)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint()) // 요청 본문을 예쁘게 출력
                                .withResponseDefaults(prettyPrint())) // 응답 본문을 예쁘게 출력
                .alwaysDo(print()) // 모든 요청/응답을 콘솔에 출력 (디버깅용)
                .build();


        store1 = Store.builder()
                .id(1L)
                .kakaoPlaceId("8099717")
                .categoryName("음식점 > 한식 > 해물,생선 > 복어")
                .name("초원복국 대연점")
                .address("부산 남구 대연동 18-8")
                .roadAddress("부산 남구 황령대로492번길 30")
                .phone("02-123-4567")
                .x(129.105899018046)
                .y(35.1377701131047)
                .kakaoPlaceUrl("http://place.map.kakao.com/8099717")
                .voteCount(10)
                .build();

        store2 = Store.builder()
                .id(2L)
                .kakaoPlaceId("1493830808")
                .categoryName("음식점 > 일식 > 일본식라면")
                .name("겐쇼심야라멘")
                .address("부산 남구 대연동 18-8")
                .roadAddress("부산 남구 용소로13번길 16")
                .phone("02-123-4567")
                .x(129.105899018046)
                .y(35.1377701131047)
                .kakaoPlaceUrl("http://place.map.kakao.com/8099717")
                .voteCount(20)
                .build();

        storeResponseDto1 = StoreResponseDto.from(store1);
        storeResponseDto2 = StoreResponseDto.from(store2);
        storeRankingDto1 = StoreRankingResponseDto.from(store1);
        storeRankingDto2 = StoreRankingResponseDto.from(store2);

    }


    @Test
    void 가게_상세_조회_성공() throws Exception {
        // Given
        String kakaoPlaceId = store1.getKakaoPlaceId();
        String storeName = store1.getName();
        double x = store1.getX();
        double y = store1.getY();

        Mockito.when(storeService.getPlaceDetail(kakaoPlaceId, storeName, x, y))
                .thenReturn(storeResponseDto1);

        // When
        mockMvc.perform(get("/api/store/{kakaoPlaceId}", kakaoPlaceId)
                        .param("storeName", storeName)
                        .param("x", String.valueOf(x))
                        .param("y", String.valueOf(y))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.code").value(StoreSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.body.message").value(StoreSuccessCode.OK.getMessage()))
                .andExpect(jsonPath("$.body.result.kakaoPlaceId").value(storeResponseDto1.kakaoPlaceId()))
                .andExpect(jsonPath("$.body.result.name").value(storeResponseDto1.name()))
                .andExpect(jsonPath("$.body.result.address").value(storeResponseDto1.address()))
                .andExpect(jsonPath("$.body.result.roadAddress").value(storeResponseDto1.roadAddress()))
                .andExpect(jsonPath("$.body.result.phone").value(storeResponseDto1.phone()))
                .andExpect(jsonPath("$.body.result.x").value(storeResponseDto1.x()))
                .andExpect(jsonPath("$.body.result.y").value(storeResponseDto1.y()))
                .andExpect(jsonPath("$.body.result.kakaoPlaceUrl").value(storeResponseDto1.kakaoPlaceUrl()))
                .andDo(document("store/get-store-detail-success", // 스니펫 이름 명확히
                        pathParameters(
                                parameterWithName("kakaoPlaceId").description("카카오 장소 ID")
                        ),
                        queryParameters( // Spring REST Docs 3.0 부터는 requestParameters() 로 통합될 수 있음
                                parameterWithName("storeName").description("가게 이름"),
                                parameterWithName("x").description("경도 (Longitude)"),
                                parameterWithName("y").description("위도 (Latitude)")
                        ),
                        responseFields(
                                fieldWithPath("headers").type(JsonFieldType.OBJECT).description("응답 헤더 정보 (현재 구현에서는 비어 있음)").optional(),
                                fieldWithPath("statusCode").type(JsonFieldType.STRING).description("HTTP 상태 코드 문자열 (예: OK)"),
                                fieldWithPath("statusCodeValue").type(JsonFieldType.NUMBER).description("HTTP 상태 코드 숫자 (예: 200)"),
                                fieldWithPath("body.code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("body.message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("body.result.kakaoPlaceId").type(JsonFieldType.STRING).description("카카오 장소 ID"),
                                fieldWithPath("body.result.name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("body.result.address").type(JsonFieldType.STRING).description("지번 주소"),
                                fieldWithPath("body.result.roadAddress").type(JsonFieldType.STRING).description("도로명 주소"),
                                fieldWithPath("body.result.phone").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("body.result.x").type(JsonFieldType.NUMBER).description("경도 (Longitude)"),
                                fieldWithPath("body.result.y").type(JsonFieldType.NUMBER).description("위도 (Latitude)"),
                                fieldWithPath("body.result.kakaoPlaceUrl").type(JsonFieldType.STRING).description("카카오 장소 상세 URL")
                        )
                ));

    }


    @Test
    void 가게_랭킹_조회_성공() throws Exception{
        // Given
        double x = 127.12;
        double y = 35.13;
        int radius = 2000;
        //Pageable pageable = PageRequest.of(0, 2); // 테스트를 위해 size를 작게 조정
        Pageable pageable = PageRequest.of(0,10);
        // 랭킹 결과는 voteCount가 높은 store2가 먼저 오도록 설정
        List<StoreRankingResponseDto> rankingList = Arrays.asList(storeRankingDto2, storeRankingDto1);
        Page<StoreRankingResponseDto> storeRankingPage = new PageImpl<>(rankingList, pageable, rankingList.size());

        Mockito.when(storeService.getNearStoreRanking(eq(x), eq(y), eq(radius), any(Pageable.class)))
                .thenReturn(storeRankingPage);

        // When & Then
        mockMvc.perform(get("/api/store/ranking")
                        .param("x", String.valueOf(x))
                        .param("y", String.valueOf(y))
//                        .param("radius", String.valueOf(radius))
//                        .param("page", String.valueOf(pageable.getPageNumber()))
//                        .param("size", String.valueOf(pageable.getPageSize()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.code").value(StoreSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.body.message").value(StoreSuccessCode.OK.getMessage()))
                .andExpect(jsonPath("$.body.result.content[0].name").value(storeRankingDto2.name())) // 첫번째 결과는 store2
                .andExpect(jsonPath("$.body.result.content[0].voteCount").value(storeRankingDto2.voteCount()))
                .andExpect(jsonPath("$.body.result.content[1].name").value(storeRankingDto1.name())) // 두번째 결과는 store1
                .andExpect(jsonPath("$.body.result.content[1].voteCount").value(storeRankingDto1.voteCount()))
                .andExpect(jsonPath("$.body.result.totalPages").value(1))
                .andExpect(jsonPath("$.body.result.totalElements").value(2))
                .andExpect(jsonPath("$.body.result.number").value(pageable.getPageNumber()))
                .andExpect(jsonPath("$.body.result.size").value(pageable.getPageSize()))
                .andDo(document("store/get-store-ranking-success",
                        queryParameters(
                                parameterWithName("x").description("중심 경도 (Longitude)"),
                                parameterWithName("y").description("중심 위도 (Latitude)"),
                                parameterWithName("radius").description("검색 반경 (미터 단위, 기본값 2000, 최대 10000)").optional(),
                                parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                parameterWithName("size").description("페이지 당 항목 수").optional()
                        ),
                        responseFields(
                                fieldWithPath("headers").type(JsonFieldType.OBJECT).description("응답 헤더 정보 (현재 구현에서는 비어 있음)").optional(),
                                fieldWithPath("statusCode").type(JsonFieldType.STRING).description("HTTP 상태 코드 문자열 (예: OK)"),
                                fieldWithPath("statusCodeValue").type(JsonFieldType.NUMBER).description("HTTP 상태 코드 숫자 (예: 200)"),
                                fieldWithPath("body.code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("body.message").type(JsonFieldType.STRING).description("응답 메시지"),
                                // Page Content
                                fieldWithPath("body.result.content[]").type(JsonFieldType.ARRAY).description("가게 랭킹 목록"),
                                fieldWithPath("body.result.content[].id").type(JsonFieldType.NUMBER).description("가게 내부 ID"),
                                fieldWithPath("body.result.content[].kakaoPlaceId").type(JsonFieldType.STRING).description("카카오 장소 ID"),
                                fieldWithPath("body.result.content[].name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("body.result.content[].address").type(JsonFieldType.STRING).description("지번 주소"),
                                fieldWithPath("body.result.content[].x").type(JsonFieldType.NUMBER).description("경도 (Longitude)"),
                                fieldWithPath("body.result.content[].y").type(JsonFieldType.NUMBER).description("위도 (Latitude)"),
                                fieldWithPath("body.result.content[].voteCount").type(JsonFieldType.NUMBER).description("득표 수"),
                                // Pageable information
                                fieldWithPath("body.result.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("body.result.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("body.result.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("body.result.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("미정렬 여부"),
                                fieldWithPath("body.result.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 없음 여부"),
                                fieldWithPath("body.result.pageable.offset").type(JsonFieldType.NUMBER).description("페이지 오프셋"),
                                fieldWithPath("body.result.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
                                fieldWithPath("body.result.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("미페이징 여부"),
                                // Page meta result
                                fieldWithPath("body.result.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("body.result.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("body.result.totalElements").type(JsonFieldType.NUMBER).description("총 요소 수"),
                                fieldWithPath("body.result.size").type(JsonFieldType.NUMBER).description("현재 페이지의 크기"),
                                fieldWithPath("body.result.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호 (0부터 시작)"),
                                fieldWithPath("body.result.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("body.result.sort.unsorted").type(JsonFieldType.BOOLEAN).description("미정렬 여부"),
                                fieldWithPath("body.result.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 없음 여부"),
                                fieldWithPath("body.result.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("body.result.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 실제 요소 수"),
                                fieldWithPath("body.result.empty").type(JsonFieldType.BOOLEAN).description("결과 없음 여부")
                        )
                ));

    }


}
