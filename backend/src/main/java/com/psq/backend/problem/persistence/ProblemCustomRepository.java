package com.psq.backend.problem.persistence;

import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProblemCustomRepository {
    Page<Problem> findAllProblem(Long memberId,
                                 Integer level,
                                 Category category,
                                 Boolean isSolved,
                                 Pageable pageable);

    Optional<Problem> pollProblem(Long memberId);
}
