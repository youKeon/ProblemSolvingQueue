package com.problem.solving.slack.dto.request;

import com.problem.solving.problem.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class TodayProblemSolvingRequest {
    @NotBlank(message = "공백일 수 없습니다.")
    private String url;
    @NotBlank(message = "공백일 수 없습니다.")
    private String title;
    @NotNull(message = "공백일 수 없습니다.")
    private Category category;
}
