package com.problem.solving.problem.application;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Type;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.dto.response.ProblemsResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ProblemServiceTest {
    @InjectMocks
    private ProblemService problemService;

    @Mock
    private ProblemRepository problemRepository;

    @Test
    @DisplayName("문제를 저장한다")
    public void registerProblem() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest("TEST", Type.DFS, 3);

        //when
        problemService.addProblem(request);

        //then
        verify(problemRepository, times(1)).save(any(Problem.class));
    }

    @Test
    @DisplayName("문제를 soft delete한다")
    public void deleteProblem() throws Exception {
        //given
        Problem problem = Problem.builder()
                .id(1L)
                .url("test")
                .build();

        //when
        when(problemRepository.findById(1L)).thenReturn(Optional.ofNullable(problem));
        problemService.delete(problem.getId());

        //then
        assertThat(problem.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 문제를 삭제하려는 경우 예외가 발생한다")
    public void deleteProblemEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.delete(1L))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제 리스트를 조회한다")
    public void getProblems() throws Exception {
        //given
        Problem test1 = Problem.builder().url("test1").type(Type.DFS).level(1).build();
        Problem test2 = Problem.builder().url("test2").type(Type.BFS).level(2).build();
        Problem test3 = Problem.builder().url("test3").type(Type.SORT).level(3).build();

        List<Problem> problems = Arrays.asList(test1, test2, test3);

        //when
        when(problemRepository.findAllByOrderByCreatedAtAsc()).thenReturn(problems);
        List<ProblemsResponse> result = problemService.getProblems();

        //then
        assertAll(
                () -> assertThat(result.get(0).getUrl()).isEqualTo(test1.getUrl()),
                () -> assertThat(result.get(0).getType()).isEqualTo(test1.getType()),
                () -> assertThat(result.get(0).getLevel()).isEqualTo(test1.getLevel()),

                () -> assertThat(result.get(1).getUrl()).isEqualTo(test2.getUrl()),
                () -> assertThat(result.get(1).getType()).isEqualTo(test2.getType()),
                () -> assertThat(result.get(1).getLevel()).isEqualTo(test2.getLevel()),

                () -> assertThat(result.get(2).getUrl()).isEqualTo(test3.getUrl()),
                () -> assertThat(result.get(2).getType()).isEqualTo(test3.getType()),
                () -> assertThat(result.get(2).getLevel()).isEqualTo(test3.getLevel())
        );
    }

    @Test
    @DisplayName("문제 리스트가 없는 경우 예외가 발생한다")
    public void getProblemsEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.getProblems())
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제가 없습니다.");
    }

    @Test
    @DisplayName("가장 먼저 저장한 문제를 조회한다")
    public void pollProblemTest() throws Exception {
        //given
        Problem problem = Problem.builder()
                .id(1L)
                .url("test")
                .level(2)
                .type(Type.DFS)
                .build();

        //when
        when(problemRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.ofNullable(problem));
        ProblemResponse result = problemService.pollProblem();

        //then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem.getUrl()),
                () -> assertThat(result.getLevel()).isEqualTo(problem.getLevel()),
                () -> assertThat(result.getType()).isEqualTo(problem.getType())
        );
    }

    @Test
    @DisplayName("문제가 없는 경우 예외가 발생한다")
    public void pollProblemEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.pollProblem())
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("Id로 문제를 단건 조회한다")
    public void getProblemById() throws Exception {
        //given
        Problem problem = Problem.builder()
                .id(1L)
                .url("test")
                .type(Type.DFS)
                .level(3)
                .build();

        //when
        when(problemRepository.findById(problem.getId())).thenReturn(Optional.ofNullable(problem));
        ProblemResponse result = problemService.getProblem(problem.getId());

        //then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem.getUrl()),
                () -> assertThat(result.getType()).isEqualTo(problem.getType()),
                () -> assertThat(result.getLevel()).isEqualTo(problem.getLevel())
        );
    }

    @Test
    @DisplayName("존재하지 않는 Id로 단건 조회하면 예외가 발생한다")
    public void getProblemByIdEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.getProblem(1L))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }
}
