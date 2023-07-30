package com.problem.solving.problem.dto.request;

import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class ProblemSaveRequest {
    @NotNull(message = "공백일 수 없습니다.")
    private Long memberId;

    @NotBlank(message = "공백일 수 없습니다.")
    private String url;
    @NotBlank(message = "공백일 수 없습니다.")
    private String title;
    @NotNull(message = "공백일 수 없습니다.")
    private Category category;
    @Min(1) @Max(5)
    @NotNull(message = "공백일 수 없습니다.")
    private Integer level;

    public Problem toEntity(Member member) {
        return new Problem(member, title, url, level, category, false);
    }
}
