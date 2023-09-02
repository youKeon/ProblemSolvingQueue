package com.psq.backend.bookmark.persentation;

import com.psq.backend.bookmark.dto.request.BookmarkSaveRequest;
import com.psq.backend.common.annotation.ControllerTest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.psq.backend.common.docs.ApiDocumentUtil.getDocumentRequest;
import static com.psq.backend.common.docs.ApiDocumentUtil.getDocumentResponse;
import static com.psq.backend.problem.domain.Category.BFS;
import static com.psq.backend.problem.domain.Category.DFS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/bookmark";
    private Long bookmarkId;
    private Long memberId;
    private Long problemId;
    @BeforeEach
    void setup() {
        memberId = 1L;
        problemId = 2L;
        bookmarkId = 3L;
    }

    @Test
    @DisplayName("문제 id를 받아 북마크에 등록한다")
    public void registerBookmark() throws Exception {
        // given
        BookmarkSaveRequest saveRequest = new BookmarkSaveRequest(problemId);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andExpect(status().isCreated())

                .andDo(print())
                .andDo(document("bookmark/save/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("problemId").description("문제 ID")
                        )
                ));
    }
    @Test
    @DisplayName("북마크 등록 시 문제 id가 null인 경우 예외가 발생한다")
    public void registerBookmarkMemberIdNullExceptionTest() throws Exception {
        // given
        BookmarkSaveRequest request = new BookmarkSaveRequest(null);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("problemId 필드는 공백일 수 없습니다. (전달된 값: null)"))

                .andDo(print())
                .andDo(document("bookmark/save/fail/emptyProblemId",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("problemId").description("문제 ID")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));
    }

    @Test
    @DisplayName("북마크 id를 받아 북마크를 삭제한다")
    public void deleteBookmark() throws Exception {
        // given
        willDoNothing()
                .given(bookmarkService)
                .delete(bookmarkId);

        // when, then
        mockMvc.perform(delete(baseURL + "/{id}", bookmarkId))
                .andExpect(status().isOk())

                .andDo(print())
                .andDo(document("bookmark/delete/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("북마크 ID")
                        ))
                );
    }

    @Test
    @DisplayName("사용자가 북마크로 등록한 문제를 조회한다")
    public void getBookmarkedProblemListTest() throws Exception {
        // given
        List<ProblemListResponse> responseList = new ArrayList<>(Arrays.asList(
                new ProblemListResponse("url1", 1, DFS, false),
                new ProblemListResponse("url2", 2, BFS, false)
        ));

        // when
        when(bookmarkService.getBookmarkList(any())).thenReturn(responseList);

        // then
        mockMvc.perform(get(baseURL))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].url").value(responseList.get(0).getUrl()))
                .andExpect(jsonPath("$[0].level").value(responseList.get(0).getLevel()))
                .andExpect(jsonPath("$[0].category").value("DFS"))
                .andExpect(jsonPath("$[0].solved").value(responseList.get(0).isSolved()))

                .andExpect(jsonPath("$[1].url").value(responseList.get(1).getUrl()))
                .andExpect(jsonPath("$[1].level").value(responseList.get(1).getLevel()))
                .andExpect(jsonPath("$[1].category").value("BFS"))
                .andExpect(jsonPath("$[1].solved").value(responseList.get(1).isSolved()))

                .andDo(print())
                .andDo(document("bookmark/findAll/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[].url").description("문제 URL"),
                                fieldWithPath("[].level").description("문제의 난이도 레벨"),
                                fieldWithPath("[].category").description("문제의 카테고리"),
                                fieldWithPath("[].solved").description("문제의 해결 여부")
                        )
                ));
    }
}
