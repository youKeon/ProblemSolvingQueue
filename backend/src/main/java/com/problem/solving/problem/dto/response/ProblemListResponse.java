package com.problem.solving.problem.dto.response;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProblemListResponse {
    private String url;
    private int level;
    private Category category;
    private boolean isSolved;

    public static ProblemListResponse from(Problem problem) {
        return new ProblemListResponse(problem.getUrl(),
                problem.getLevel(),
                problem.getCategory(),
                problem.isSolved());
    }
}
