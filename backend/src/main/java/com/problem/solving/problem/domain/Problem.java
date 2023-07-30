package com.problem.solving.problem.domain;

import com.problem.solving.common.BaseEntity;
import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.exception.InvalidProblemException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
public class Problem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer level;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false)
    private boolean isSolved = false;

    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Problem(Member member,
                   String title,
                   String url,
                   Integer level,
                   Category category,
                   boolean isSolved) {

        validateProblemUrl(url);
        validateProblemCategory(category.toString());
        validateProblemLevel(level);
        this.title = title;
        this.member = member;
        this.url = url;
        this.level = level;
        this.category = category;
        this.isSolved = isSolved;
    }

    public void softDelete() {
        isDeleted = true;
    }

    public void update(ProblemUpdateRequest request) {
        validateProblemUrl(request.getUrl());
        validateProblemCategory(request.getCategory().toString());
        validateProblemLevel(request.getLevel());

        this.url = request.getUrl();
        this.level = request.getLevel();
        this.category = request.getCategory();
        this.isSolved = request.getIsSolved();
    }

    private void validateProblemUrl(String url) {
        if (url.length() == 0) {
            throw new InvalidProblemException("URL은 공백일 수 없습니다.");
        }
    }

    private void validateProblemLevel(Integer level) {
        if (level == null || level <= 0 || level >= 6) {
            throw new InvalidProblemException("난이도는 1 이상 5 이하입니다.");
        }
    }

    private void validateProblemCategory(String category) {
        try {
            Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProblemException("존재하지 않는 문제 유형입니다.");
        }
    }
}
