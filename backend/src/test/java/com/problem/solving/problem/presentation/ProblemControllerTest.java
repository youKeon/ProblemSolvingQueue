package com.problem.solving.problem.presentation;

import com.problem.solving.common.annotation.ControllerTest;
import com.problem.solving.problem.domain.Type;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.response.ProblemsResponse;
import com.problem.solving.problem.exception.InvalidProblemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProblemControllerTest extends ControllerTest {
    private static String baseURL = "/api/v1/problems";
    @Test
    @DisplayName("문제가 저장된다")
    public void registerProblem() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest("test", Type.DFS, 3);
        willDoNothing()
                .given(problemService)
                .register(any());

        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("URL이 없으면 예외가 발생한다")
    public void registerProblemGetException() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest(null, Type.DFS, 3);
        willThrow(new InvalidProblemException("공백일 수 없습니다."))
                .given(problemService)
                .register(any());

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
        ProblemSaveRequest request = new ProblemSaveRequest("ps", Type.DFS, 0);
        willThrow(new InvalidProblemException("0이상 5이하입니다."))
                .given(problemService)
                .register(any());

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
        ProblemSaveRequest request = new ProblemSaveRequest("ps", Type.DFS, 8);
        willThrow(new InvalidProblemException("0이상 5이하입니다."))
                .given(problemService)
                .register(any());

        mockMvc.perform(post(baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제가 조회된다")
    public void getProblems() throws Exception {
        //given
        ProblemsResponse test1 = ProblemsResponse.builder().url("test1").level(1).type(Type.DFS).build();
        ProblemsResponse test2 = ProblemsResponse.builder().url("test2").level(2).type(Type.BFS).build();
        ProblemsResponse test3 = ProblemsResponse.builder().url("test3").level(3).type(Type.SORT).build();
        List<ProblemsResponse> responses = Arrays.asList(test1, test2, test3);

        given(problemService.getProblems()).willReturn(responses);

        mockMvc.perform(get(baseURL))
                .andExpect(status().isOk());
    }
}
