package com.problem.solving.problem.dto.request;

import com.problem.solving.problem.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class ProblemUpdateRequest {
    @NotBlank(message = "공백일 수 없습니다.")
    private String url;
    @NotNull(message = "공백일 수 없습니다.")
    private Category category;
    @NotNull(message = "공백일 수 없습니다.")
    private Boolean isSolved;
    @Min(1) @Max(5)
    @NotNull(message = "공백일 수 없습니다.")
    private Integer level;

}
