package com.problem.solving.problem.application;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemCustomRepository;
import com.problem.solving.problem.persistence.ProblemCustomRepositoryImpl;
import com.problem.solving.problem.persistence.ProblemRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ProblemServiceTest {
    @InjectMocks
    private ProblemService problemService;
    @Mock
    private ProblemRepository problemRepository;

    private static Pageable pageable = PageRequest.of(0, 3);

    @Test
    @DisplayName("문제를 저장한다")
    public void registerProblem() throws Exception {
        //given
        ProblemSaveRequest request = new ProblemSaveRequest("TEST", Category.DFS, 3);

        //when
        problemService.addProblem(request);

        //then
        verify(problemRepository, times(1)).save(any(Problem.class));
    }

    @Test
    @DisplayName("문제를 soft delete한다")
    public void deleteProblem() throws Exception {
        //given
        Long id = 1L;
        Problem problem = new Problem("test", 3, Category.DFS, false);

        //when
        when(problemRepository.findById(id)).thenReturn(Optional.ofNullable(problem));
        problemService.delete(id);

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
        Problem test1 = new Problem("test1", 1, Category.DFS, false);
        Problem test2 = new Problem("test2", 2, Category.BFS, false);
        Problem test3 = new Problem("test3", 3, Category.SORT, false);

        List<Problem> problems = Arrays.asList(test1, test2, test3);

        Page<Problem> problemPage = new PageImpl<>(problems, pageable, problems.size());

        //when
        when(problemRepository.findAllProblem(pageable)).thenReturn(problemPage);
        List<ProblemListResponse> result = problemService.getProblemList(pageable);

        //then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(0).getCategory()).isEqualTo(test1.getCategory()),
                () -> assertThat(result.get(0).getLevel()).isEqualTo(test1.getLevel()),

                () -> assertThat(result.get(1).getUrl()).isEqualTo(test2.getUrl()),
                () -> assertThat(result.get(1).getCategory()).isEqualTo(test2.getCategory()),
                () -> assertThat(result.get(1).getLevel()).isEqualTo(test2.getLevel()),

                () -> assertThat(result.get(2).getUrl()).isEqualTo(test3.getUrl()),
                () -> assertThat(result.get(2).getCategory()).isEqualTo(test3.getCategory()),
                () -> assertThat(result.get(2).getLevel()).isEqualTo(test3.getLevel())
        );
    }

    @Test
    @DisplayName("문제 리스트가 없는 경우 예외가 발생한다")
    public void getProblemsEmptyException() throws Exception {
        //when, then
        Page<Problem> page = Page.empty();

        when(problemRepository.findAllProblem(pageable)).thenReturn(page);

        assertThrows(NoSuchProblemException.class, () -> problemService.getProblemList(pageable));
    }

    @Test
    @DisplayName("가장 먼저 저장한 문제를 조회한다")
    public void pollProblemTest() throws Exception {
        //given
        Problem problem = new Problem("test", 3, Category.DFS, false);

        //when
        when(problemRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.ofNullable(problem));
        ProblemResponse result = problemService.pollProblem();

        //then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem.getUrl()),
                () -> assertThat(result.getLevel()).isEqualTo(problem.getLevel()),
                () -> assertThat(result.getCategory()).isEqualTo(problem.getCategory())
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
        Problem problem = new Problem("test", 3, Category.DFS, false);

        //when
        when(problemRepository.findById(problem.getId())).thenReturn(Optional.ofNullable(problem));
        ProblemResponse result = problemService.getProblem(problem.getId());

        //then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem.getUrl()),
                () -> assertThat(result.getCategory()).isEqualTo(problem.getCategory()),
                () -> assertThat(result.getLevel()).isEqualTo(problem.getLevel())
        );
    }

    @Test
    @DisplayName("존재하지 않는 문제를 단건 조회하면 예외가 발생한다")
    public void getProblemByIdEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.getProblem(1L))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제를 수정한다")
    public void updateProblem() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest(
                "afterUpdate",
                Category.DFS,
                true,
                3);

        Problem problem = new Problem("test", 3, Category.DFS, false);

        //when
        when(problemRepository.findById(any(Long.class))).thenReturn(Optional.of(problem));
        problemService.update(id, request);

        //then
        assertAll(
                () -> assertEquals(problem.getUrl(), request.getUrl()),
                () -> assertEquals(problem.getCategory(), request.getCategory()),
                () -> assertEquals(problem.isSolved(), request.getIsSolved()),
                () -> assertEquals(problem.getLevel(), request.getLevel())
        );
    }
    @Test
    @DisplayName("존재하지 않는 문제를 수정하면 예외가 발생한다")
    public void updateEmptyProblemException() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest(
                "afterUpdate",
                Category.DFS,
                true,
                3);

        //when, then
        assertThatThrownBy(
                () -> problemService.update(id, any(ProblemUpdateRequest.class)))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }
}
