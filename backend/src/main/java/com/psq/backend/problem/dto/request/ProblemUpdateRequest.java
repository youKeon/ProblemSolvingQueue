package com.psq.backend.problem.dto.request;

import com.psq.backend.problem.domain.Category;
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

    @NotBlank(message = "공백일 수 없습니다.")
    private String title;

    @NotNull(message = "공백일 수 없습니다.")
    private Category category;

    @Min(1) @Max(5)
    @NotNull(message = "공백일 수 없습니다.")
    private Integer level;

    @NotNull(message = "공백일 수 없습니다.")
    private Boolean isSolved;
}
