package com.problem.solving.problem.presentation;

import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.exception.InvalidProblemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProblemControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/problems";
    private Member member;
    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
    }

    @Test
    @DisplayName("문제가 저장된다")
    public void registerProblem() throws Exception {
        //given
        Long memberId = 1L;

        ProblemSaveRequest request = new ProblemSaveRequest(memberId, "title", "test", Category.DFS, 3);
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
        Long memberId = 1L;

        ProblemSaveRequest request = new ProblemSaveRequest(memberId, "title", null, Category.DFS, 3);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                .addProblem(any());

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
        Long memberId = 1L;

        ProblemSaveRequest request = new ProblemSaveRequest(memberId, "title", "ps", Category.DFS, null);
        willThrow(new InvalidProblemException("0이상 5이하입니다."))
                .given(problemService)
                .addProblem(any());

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
        Long memberId = 1L;

        ProblemSaveRequest request = new ProblemSaveRequest(memberId, "title", "ps", Category.DFS, 0);
        willThrow(new InvalidProblemException("0이상 5이하입니다."))
                .given(problemService)
                .addProblem(any());

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
        Long memberId = 1L;
        ProblemSaveRequest request = new ProblemSaveRequest(memberId, "title", "ps", Category.DFS, 8);
        willThrow(new InvalidProblemException("0이상 5이하입니다."))
                .given(problemService)
                .addProblem(any());

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
        Long id = 1L;
        ProblemResponse response = new ProblemResponse("test1", 1, Category.DFS, false);
        given(problemService.getProblem(id)).willReturn(response);

        // then
        mockMvc.perform(get(baseURL + "/{id}", id))
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
        Long id = 1L;
        doNothing().when(problemService).delete(id);

        // then
        mockMvc.perform(delete(baseURL + "/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("문제를 수정한다")
    public void updateProblem() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest("url", Category.DFS, false, 3);
        doNothing().when(problemService).update(id, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("문제 수정 시 url이 없으면 예외가 발생한다")
    public void updateUrlEmptyException() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest(null, Category.DFS, false, 3);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                        .update(id, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 category가 없으면 예외가 발생한다")
    public void updateCategoryEmptyException() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", null, false, 3);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                .update(id, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 isSolved가 없으면 예외가 발생한다")
    public void updateIsSolvedEmptyException() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, null, 3);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                .update(id, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 level이 없으면 예외가 발생한다")
    public void updateLevelEmptyException() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, null);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                .update(id, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("문제 수정 시 level이 1보다 작으면 예외가 발생한다")
    public void updateLowLevelEmptyException() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, 0);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                .update(id, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제 수정 시 level이 5보다 크면 예외가 발생한다")
    public void updateHighLevelEmptyException() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest("test", Category.DFS, true, 6);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                .update(id, request);

        // then
        mockMvc.perform(put(baseURL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
