package com.problem.solving.problem.dto.response;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ProblemsResponse {
    private String url;
    private int level;
    private Type type;
    private boolean isSolved;

    public static ProblemsResponse from(Problem problem) {
        return ProblemsResponse.builder()
                .url(problem.getUrl())
                .level(problem.getLevel())
                .isSolved(problem.isSolved())
                .type(problem.getType())
                .build();
    }
}
