package com.psq.backend.problem.dto.response;

import com.psq.backend.problem.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProblemWeakCategoryAvgLevelResponse {
    private Category category;
    private int level;

    @QueryProjection
    public ProblemWeakCategoryAvgLevelResponse(Category category, int level) {
        this.category = category;
        this.level = level;
    }
}
