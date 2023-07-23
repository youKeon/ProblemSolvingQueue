package com.problem.solving.problem.dto.request;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Type;
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
    private Type type;
    @Min(1) @Max(5)
    private int level;

    public Problem toEntity() {
        return Problem.builder()
                .url(url)
                .type(type)
                .level(level)
                .build();
    }
}
