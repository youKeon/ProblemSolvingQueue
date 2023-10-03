package com.psq.backend.problem.presentation;

import com.psq.backend.common.annotation.ControllerTest;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.dto.request.ProblemSaveRequest;
import com.psq.backend.problem.dto.request.ProblemUpdateRequest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.psq.backend.common.docs.ApiDocumentUtil.getDocumentRequest;
import static com.psq.backend.common.docs.ApiDocumentUtil.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProblemControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/problems";
    private Long problemId;

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123", "salt");
        problemId = 1L;
        ReflectionTestUtils.setField(member, "id", 1L);

        session = new MockHttpSession();
        session.setAttribute("sessionInfo", new SessionInfo(member.getId(), member.getEmail()));
        request = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("문제 리스트를 조회된다")
    public void getProblems() throws Exception {
        // given
        ProblemListResponse test1 = new ProblemListResponse("test1", 3, Category.DFS, false);
        ProblemListResponse test2 = new ProblemListResponse("test2", 3, Category.DFS, false);
        ProblemListResponse test3 = new ProblemListResponse("test3", 3, Category.DFS, false);
        List<ProblemListResponse> responseList = Arrays.asList(test1, test2, test3);

        // when
        when(problemService.getProblemList(any(), any(), any(), any(), any()))
                .thenReturn(responseList);

        // then
        mockMvc.perform(get(baseURL)
                        .param("level", "3")
                        .param("category", "DFS")
                        .param("isSolved", "false")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value(responseList.get(0).getUrl()))
                .andExpect(jsonPath("$[0].level").value(responseList.get(0).getLevel()))
                .andExpect(jsonPath("$[0].solved").value(responseList.get(0).isSolved()))
                .andExpect(jsonPath("$[0].category").value("DFS"))

                .andExpect(jsonPath("$[1].url").value(responseList.get(1).getUrl()))
                .andExpect(jsonPath("$[1].level").value(responseList.get(1).getLevel()))
                .andExpect(jsonPath("$[1].solved").value(responseList.get(1).isSolved()))
                .andExpect(jsonPath("$[1].category").value("DFS"))

                .andExpect(jsonPath("$[2].url").value(responseList.get(2).getUrl()))
                .andExpect(jsonPath("$[2].level").value(responseList.get(2).getLevel()))
                .andExpect(jsonPath("$[2].solved").value(responseList.get(2).isSolved()))
                .andExpect(jsonPath("$[2].category").value("DFS"))

                .andDo(print())
                .andDo(document("problem/findAll/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("level").description("문제 레벨").optional(),
                                parameterWithName("category").description("문제 유형").optional(),
                                parameterWithName("isSolved").description("문제 풀이 여부").optional(),
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(
                                fieldWithPath("[].url").description("문제 URL"),
                                fieldWithPath("[].level").description("문제 레벨"),
                                fieldWithPath("[].category").description("문제 유형"),
                                fieldWithPath("[].solved").description("문제 풀이 여부")
                        )
                ));
    }


    @Test
    @DisplayName("문제가 저장된다")
    public void saveProblem() throws Exception {
        // given
        ProblemSaveRequest request = new ProblemSaveRequest("title", "test", Category.DFS, 3);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())

                .andDo(print())
                .andDo(document("problem/save/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("url").type(JsonFieldType.STRING).description("문제 URL"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 이름"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("문제 유형"),
                                fieldWithPath("level").type(JsonFieldType.NUMBER).description("문제 레벨")
                        )
                ));
    }

    @Test
    @DisplayName("문제 저장 시 URL이 없으면 예외가 발생한다")
    public void saveProblemEmptyUrlException() throws Exception {
        // given
        ProblemSaveRequest request = new ProblemSaveRequest("", "title", Category.DFS, 3);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("url 필드는 공백일 수 없습니다. (전달된 값: )"))

                .andDo(print())
                .andDo(document("problem/save/fail/emptyUrl",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 저장 시 title이 없으면 예외가 발생한다")
    public void saveProblemEmptyTitleException() throws Exception {
        // given
        ProblemSaveRequest request = new ProblemSaveRequest("url", "", Category.DFS, 3);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("title 필드는 공백일 수 없습니다. (전달된 값: )"))

                .andDo(print())
                .andDo(document("problem/save/fail/emptyTitle",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 저장 시 category가 없으면 예외가 발생한다")
    public void saveProblemEmptyCategoryException() throws Exception {
        // given
        ProblemSaveRequest request = new ProblemSaveRequest("url", "title", null, 3);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("category 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("problem/save/fail/emptyCategory",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }


    @Test
    @DisplayName("문제 저장 시 level이 없으면 예외가 발생한다")
    public void saveProblemEmptyLevelException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest("title", "ps", Category.DFS, null);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("level 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("problem/save/fail/emptyLevel",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }


    @Test
    @DisplayName("문제 저장 시 level이 1보다 작으면 예외가 발생한다")
    public void saveProblemLowLevelException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest("title", "ps", Category.DFS, 0);

        // when, then
        mockMvc.perform(post(baseURL)
                        .locale(new Locale("ko", "KR"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("level 필드는 1 이상이어야 합니다 (전달된 값: 0)"))

                .andDo(print())
                .andDo(document("problem/save/fail/lowLevel",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 저장 시 level이 5보다 높으면 예외가 발생한다")
    public void saveProblemHighLevelException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest("title", "ps", Category.DFS, 8);

        // when, then
        mockMvc.perform(post(baseURL)
                        .locale(new Locale("ko", "KR"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("level 필드는 5 이하여야 합니다 (전달된 값: 8)"))

                .andDo(print())
                .andDo(document("problem/save/fail/highLevel",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("Id로 문제를 단건 조회한다")
    public void getProblemById() throws Exception {
        //given
        ProblemResponse response = new ProblemResponse("title", "test1", 1, Category.DFS, false, LocalDateTime.now());

        // when
        when(problemService.getProblemInfo(problemId)).thenReturn(response);

        // then
        mockMvc.perform(get(baseURL + "/{id}", problemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.url").value(response.getUrl()))
                .andExpect(jsonPath("$.level").value(response.getLevel()))
                .andExpect(jsonPath("$.solved").value(response.isSolved()))
                .andExpect(jsonPath("$.category").value("DFS"))

                .andDo(print())
                .andDo(document("problem/find/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        responseFields(
                                fieldWithPath("title").description("문제 제목"),
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("solved").description("문제 풀이 여부"),
                                fieldWithPath("updatedAt").description("마지막 풀이 시간")
                        )
                ));
    }

    @Test
    @DisplayName("가장 먼저 저장한 문제를 조회한다")
    public void pollProblem() throws Exception {
        // given
        ProblemResponse response = new ProblemResponse("title", "test1", 1, Category.DFS, false, LocalDateTime.now());

        // when
        when(problemService.pollProblem(any())).thenReturn(response);

        // then
        mockMvc.perform(get(baseURL + "/poll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.url").value(response.getUrl()))
                .andExpect(jsonPath("$.level").value(response.getLevel()))
                .andExpect(jsonPath("$.solved").value(response.isSolved()))
                .andExpect(jsonPath("$.category").value("DFS"))

                .andDo(print())
                .andDo(document("problem/pull/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("title").description("문제 제목"),
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("solved").description("문제 풀이 여부"),
                                fieldWithPath("updatedAt").description("마지막 풀이 시간")
                        )
                ));
    }


    @Test
    @DisplayName("문제를 삭제한다")
    public void deleteProblem() throws Exception {
        // when, then
        mockMvc.perform(delete(baseURL + "/{id}", problemId))
                .andExpect(status().isNoContent())

                .andDo(print())
                .andDo(document("problem/delete/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        )));
    }


    @Test
    @DisplayName("문제를 수정한다")
    public void updateProblem() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("url", "title", Category.DFS, 3, false);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())

                .andDo(print())
                .andDo(document("problem/update/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").type(JsonFieldType.STRING).description("문제 URL"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 이름"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("문제 유형"),
                                fieldWithPath("level").type(JsonFieldType.NUMBER).description("문제 레벨"),
                                fieldWithPath("isSolved").type(JsonFieldType.BOOLEAN).description("문제 풀이 여부")
                        )
                ));
    }

    @Test
    @DisplayName("문제 수정 시 url이 없으면 예외가 발생한다")
    public void updateProblemUrlEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest(null, "title", Category.DFS, 3, false);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("url 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("problem/update/fail/emptyUrl",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("isSolved").description("문제 풀이 여부")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 수정 시 title이 없으면 예외가 발생한다")
    public void updateProblemTitleEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("url", null, Category.DFS, 3, false);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("title 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("problem/update/fail/emptyTitle",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("isSolved").description("문제 풀이 여부")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }
    @Test
    @DisplayName("문제 수정 시 category가 없으면 예외가 발생한다")
    public void updateProblemCategoryEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("url", "title", null, 3, false);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("category 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("problem/update/fail/emptyCategory",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("isSolved").description("문제 풀이 여부")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 수정 시 isSolved가 없으면 예외가 발생한다")
    public void updateProblemIsSolvedEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", "title", Category.DFS, 3, null);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("isSolved 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("problem/update/fail/emptyIsSolved",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("isSolved").description("문제 풀이 여부")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 수정 시 level이 없으면 예외가 발생한다")
    public void updateProblemLevelEmptyException() throws Exception {
        // given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", "title", Category.DFS, null, false);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("level 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("problem/update/fail/emptyLevel",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("isSolved").description("문제 풀이 여부")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 수정 시 level이 1보다 작으면 예외가 발생한다")
    public void updateProblemLowLevelEmptyException() throws Exception {
        // given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", "title", Category.DFS, 0, false);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .locale(new Locale("ko", "KR"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("level 필드는 1 이상이어야 합니다 (전달된 값: 0)"))

                .andDo(print())
                .andDo(document("problem/update/fail/lowLevel",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("isSolved").description("문제 풀이 여부")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("문제 수정 시 level이 5보다 크면 예외가 발생한다")
    public void updateProblemHighLevelEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", "title", Category.DFS, 8, false);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .locale(new Locale("ko", "KR"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("level 필드는 5 이하여야 합니다 (전달된 값: 8)"))

                .andDo(print())
                .andDo(document("problem/update/fail/highLevel",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        ),
                        requestFields(
                                fieldWithPath("url").description("문제 URL"),
                                fieldWithPath("title").description("문제 이름"),
                                fieldWithPath("category").description("문제 유형"),
                                fieldWithPath("level").description("문제 레벨"),
                                fieldWithPath("isSolved").description("문제 풀이 여부")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("(논리적)삭제된 문제를 되돌린다")
    public void recoveryProblem() throws Exception {
        // when, then
        mockMvc.perform(put(baseURL + "/{id}/recovery", problemId))
                .andExpect(status().isNoContent())

                .andDo(print())
                .andDo(document("problem/recovery/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        )));
    }

    @Test
    @DisplayName("문제 풀이 횟수를 1 증가시킨다")
    public void increaseSolvedCountTest() throws Exception {
        // when, then
        mockMvc.perform(patch(baseURL + "/{id}", problemId))
                .andExpect(status().isNoContent())

                .andDo(print())
                .andDo(document("problem/increaseSolvedCount/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("문제 ID")
                        )));
    }
}
