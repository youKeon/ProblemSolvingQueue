package com.psq.backend.problem.persistence;

import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProblemCustomRepository {
    List<ProblemListResponse> findAllProblem(Long memberId,
                                             Integer level,
                                             Category category,
                                             Boolean isSolved,
                                             Pageable pageable);

    Optional<ProblemResponse> pollProblem(Long memberId);

    Optional<ProblemResponse> findProblem(Long problemId);

    long deleteSoftDeletedProblem();

    long increaseSovledCount(Long problemId);
}
