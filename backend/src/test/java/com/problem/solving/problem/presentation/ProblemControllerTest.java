package com.problem.solving.problem.presentation;

import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.dto.response.ProblemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProblemControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/problems";
    private Long problemId;

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
        problemId = 1L;
        ReflectionTestUtils.setField(member, "id", 1L);

        session = new MockHttpSession();
        session.setAttribute("sessionInfo", new SessionInfo(member.getId(), member.getEmail()));
    }

    @Test
    @DisplayName("문제 리스트를 조회된다")
    public void getProblems() throws Exception {
        // given
        ProblemListResponse test1 = new ProblemListResponse("test1", 1, Category.DFS, false);
        ProblemListResponse test2 = new ProblemListResponse("test2", 2, Category.DFS, false);
        ProblemListResponse test3 = new ProblemListResponse("test3", 3, Category.DFS, false);
        List<ProblemListResponse> responses = Arrays.asList(test1, test2, test3);

        // when
        when(problemService.getProblemList(request, 3, Category.DFS, false, pageable))
                .thenReturn(responses);

        // then
        mockMvc.perform(get(baseURL + "/problems")
                        .session(session)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("문제가 저장된다")
    public void registerProblem() throws Exception {
        // given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title", "test", Category.DFS, 3);

        // when
        willDoNothing()
                .given(problemService)
                .save(any());

        // then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("문제를 저장할 때 URL이 없으면 예외가 발생한다")
    public void registerProblemGetException() throws Exception {
        // given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title", null, Category.DFS, 3);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("level이 없으면 예외가 발생한다")
    public void getLevelEmptyException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title", "ps", Category.DFS, null);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("level이 1보다 작으면 예외가 발생한다")
    public void getLowLevelException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title", "ps", Category.DFS, 0);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("level이 5보다 높으면 예외가 발생한다")
    public void getHighLevelException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title", "ps", Category.DFS, 8);

        // when, then
        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Id로 문제를 단건 조회한다")
    public void getProblemById() throws Exception {
        //given
        ProblemResponse response = new ProblemResponse("test1", 1, Category.DFS, false);

        // when
        when(problemService.getProblem(problemId)).thenReturn(response);

        // then
        mockMvc.perform(get(baseURL + "/{id}", problemId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("가장 먼저 저장한 문제를 조회한다")
    public void pollProblem() throws Exception {
        // given
        ProblemResponse response = new ProblemResponse("test1", 1, Category.DFS, false);

        // when
        when(problemService.pollProblem()).thenReturn(response);

        // then
        mockMvc.perform(get(baseURL + "/poll"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("문제를 삭제한다")
    public void deleteProblem() throws Exception {
        // when
        doNothing().when(problemService).delete(problemId);

        // then
        mockMvc.perform(delete(baseURL + "/{id}", problemId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("문제를 수정한다")
    public void updateProblem() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("url", Category.DFS, false, 3);
        doNothing().when(problemService).update(problemId, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("문제 수정 시 url이 없으면 예외가 발생한다")
    public void updateUrlEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest(null, Category.DFS, false, 3);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 category가 없으면 예외가 발생한다")
    public void updateCategoryEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", null, false, 3);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 isSolved가 없으면 예외가 발생한다")
    public void updateIsSolvedEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, null, 3);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 level이 없으면 예외가 발생한다")
    public void updateLevelEmptyException() throws Exception {
        // given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, null);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("문제 수정 시 level이 1보다 작으면 예외가 발생한다")
    public void updateLowLevelEmptyException() throws Exception {
        // given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, 0);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 level이 5보다 크면 예외가 발생한다")
    public void updateHighLevelEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, 6);

        // when, then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("(논리적)삭제된 문제를 되돌린다")
    public void recoveryProblem() throws Exception {
        //given
        doNothing().when(problemService).recovery(problemId);

        // then
        mockMvc.perform(put(baseURL + "/{id}/recovery", problemId))
                .andExpect(status().isOk());
    }
}
