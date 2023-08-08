package com.problem.solving.bookmark.persentation;

import com.problem.solving.bookmark.dto.request.BookmarkSaveRequest;
import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.problem.solving.problem.domain.Category.BFS;
import static com.problem.solving.problem.domain.Category.DFS;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("사용자 id와 문제 id를 받아 북마크에 등록한다")
    public void registerBookmark() throws Exception {
        //given
        BookmarkSaveRequest request = new BookmarkSaveRequest(memberId, problemId);

        willDoNothing()
                .given(bookmarkService)
                        .register(request);

        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("북마크에 등록 시 사용자 id가 null인 경우 예외가 발생한다")
    public void registerBookmarkMemberIdNullExceptionTest() throws Exception {
        //given
        BookmarkSaveRequest request = new BookmarkSaveRequest(null, problemId);

        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("북마크에 등록 시 문제 id가 null인 경우 예외가 발생한다")
    public void registerBookmarkProblemIdNullExceptionTest() throws Exception {
        //given
        BookmarkSaveRequest request = new BookmarkSaveRequest(memberId, null);

        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("북마크 id를 받아 북마크를 삭제한다")
    public void deleteBookmark() throws Exception {
        //given
        willDoNothing()
                .given(bookmarkService)
                .delete(bookmarkId);

        // when, then
        mockMvc.perform(delete(baseURL + "/{id}", bookmarkId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("사용자 id를 받아 북마크로 등록한 문제를 조회한다")
    public void getBookmarkedProblemListTest() throws Exception {
        //given
        List<ProblemListResponse> responses = new ArrayList<>(Arrays.asList(
                new ProblemListResponse("url1", 1, DFS, false),
                new ProblemListResponse("url2", 2, BFS, false)
        ));

        // when
        when(bookmarkService.getBookmarkList(memberId)).thenReturn(responses);

        // then
        mockMvc.perform(get(baseURL + "/{id}", memberId))
                .andExpect(status().isOk());
    }
}
