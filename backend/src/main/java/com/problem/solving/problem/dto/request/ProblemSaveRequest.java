package com.problem.solving.problem.dto.request;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class ProblemSaveRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String url;
    private Category category;
    @Min(1) @Max(5)
    private int level;

    public Problem toEntity() {
        return Problem.builder()
                .url(url)
                .category(category)
                .level(level)
                .build();
    }
}
