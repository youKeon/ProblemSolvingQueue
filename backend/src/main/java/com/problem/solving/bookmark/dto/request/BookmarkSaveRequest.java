package com.problem.solving.bookmark.dto.request;

import com.problem.solving.bookmark.domain.Bookmark;
import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.domain.Problem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class BookmarkSaveRequest {
    @NotNull(message = "공백일 수 없습니다.")
    private Long memberId;

    @NotNull(message = "공백일 수 없습니다.")
    private Long problemId;

    public Bookmark toEntity(Member member, Problem problem) {
        return new Bookmark(member, problem);
    }
}
