package com.psq.backend.bookmark.dto.request;

import com.psq.backend.bookmark.domain.Bookmark;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Problem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
public class BookmarkSaveRequest {
    @NotNull(message = "공백일 수 없습니다.")
    private Long problemId;
    public Bookmark toEntity(Member member, Problem problem) {
        return new Bookmark(member, problem);
    }
}
