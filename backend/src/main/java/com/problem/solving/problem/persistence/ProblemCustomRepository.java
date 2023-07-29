package com.problem.solving.problem.persistence;

import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemCustomRepository {
    Page<Problem> findAllProblem(Long memberId,
                                 Integer level,
                                 Category category,
                                 Boolean isSolved,
                                 Pageable pageable);

}
