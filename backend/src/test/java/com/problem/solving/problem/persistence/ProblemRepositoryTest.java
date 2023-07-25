package com.problem.solving.problem.persistence;

import com.problem.solving.common.RepositoryTest;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ProblemRepositoryTest extends RepositoryTest {
    @Autowired ProblemRepository problemRepository;
    
    @Test
    @DisplayName("가장 먼저 등록한 문제를 조회한다")
    public void pollProblemTest() throws Exception {
        //given
        Problem problem = new Problem("url", 3, Category.DFS, false);

        //when
        Optional<Problem> actual = problemRepository.findFirstByOrderByCreatedAtAsc();

        //then
        Assertions.assertThat(problem.getUrl()).isEqualTo(actual.get().getUrl());
    }


}
