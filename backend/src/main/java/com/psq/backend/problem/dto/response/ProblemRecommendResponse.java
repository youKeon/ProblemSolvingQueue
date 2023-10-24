package com.psq.backend.problem.dto.response;

import com.psq.backend.problem.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProblemRecommendResponse {
    private String title;
    private String url;
    private int level;
    private Category category;

    @QueryProjection
    public ProblemRecommendResponse(String title, String url, int level, Category category) {
        this.title = title;
        this.url = url;
        this.level = level;
        this.category = category;
    }
}
