package com.psq.backend.problem.dto.response;

import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProblemResponse {
    private String title;
    private String url;
    private int level;
    private Category category;
    private boolean isSolved;

    public static ProblemResponse from(Problem problem) {
        return new ProblemResponse(
                problem.getTitle(),
                problem.getUrl(),
                problem.getLevel(),
                problem.getCategory(),
                problem.isSolved()
        );
    }
}
