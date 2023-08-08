package com.problem.solving.problem.presentation;

import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProblemControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/problems";
    private Member member;
    private Long problemId;
    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
        problemId = 1L;
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    @Test
    @DisplayName("문제가 저장된다")
    public void registerProblem() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title", "test", Category.DFS, 3);
        willDoNothing()
                .given(problemService)
                .addProblem(any());

        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("문제를 저장할 때 URL이 없으면 예외가 발생한다")
    public void registerProblemGetException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title", null, Category.DFS, 3);

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
        given(problemService.getProblem(problemId)).willReturn(response);

        // then
        mockMvc.perform(get(baseURL + "/{id}", problemId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("가장 먼저 저장한 문제를 조회한다")
    public void pollProblem() throws Exception {
        //given
        ProblemResponse response = new ProblemResponse("test1", 1, Category.DFS, false);
        given(problemService.pollProblem()).willReturn(response);

        // then
        mockMvc.perform(get(baseURL + "/poll"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("문제를 삭제한다")
    public void deleteProblem() throws Exception {
        //given
        doNothing().when(problemService).delete(problemId);

        // then
        mockMvc.perform(delete(baseURL + "/{id}", problemId))
                .andExpect(status().isNoContent());
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
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("문제 수정 시 url이 없으면 예외가 발생한다")
    public void updateUrlEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest(null, Category.DFS, false, 3);

        // then
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

        // then
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

        // then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 level이 없으면 예외가 발생한다")
    public void updateLevelEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, null);

        // then
        mockMvc.perform(put(baseURL + "/{id}", problemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("문제 수정 시 level이 1보다 작으면 예외가 발생한다")
    public void updateLowLevelEmptyException() throws Exception {
        //given
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, 0);

        // then
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

        // then
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
                .andExpect(status().isNoContent());
    }
}
