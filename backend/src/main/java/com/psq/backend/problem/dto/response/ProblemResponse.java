package com.psq.backend.problem.dto.response;

import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ProblemResponse {
    private String title;
    private String url;
    private int level;
    private Category category;
    private boolean isSolved;

    @QueryProjection
    public ProblemResponse(String title, String url, int level, Category category, boolean isSolved) {
        this.title = title;
        this.url = url;
        this.level = level;
        this.category = category;
        this.isSolved = isSolved;
    }
}
