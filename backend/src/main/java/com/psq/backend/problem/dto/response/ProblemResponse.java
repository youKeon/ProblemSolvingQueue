package com.psq.backend.problem.dto.response;

import com.psq.backend.problem.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProblemResponse {
    private String title;
    private String url;
    private int level;
    private Category category;
    private boolean isSolved;
    private LocalDateTime updatedAt;

    @QueryProjection
    public ProblemResponse(String title, String url, int level, Category category, boolean isSolved, LocalDateTime updatedAt) {
        this.title = title;
        this.url = url;
        this.level = level;
        this.category = category;
        this.isSolved = isSolved;
        this.updatedAt = updatedAt;
    }
}
