package com.problem.solving.problem.application;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Type;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.response.ProblemsResponse;
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
        problemService.register(request);

        //then
        verify(problemRepository, times(1)).save(any(Problem.class));
    }

    @Test
    @DisplayName("문제 리스트를 조회한다")
    public void getProblems() throws Exception {
        //given
        Problem test1 = Problem.builder().url("test1").type(Type.DFS).level(1).build();
        Problem test2 = Problem.builder().url("test2").type(Type.BFS).level(1).build();
        Problem test3 = Problem.builder().url("test3").type(Type.SORT).level(3).build();

        List<Problem> problems = Arrays.asList(test1, test2, test3);

        //when
        when(problemRepository.findAll()).thenReturn(problems);
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


}
