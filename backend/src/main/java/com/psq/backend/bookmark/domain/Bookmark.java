package com.psq.backend.bookmark.domain;

import com.psq.backend.common.BaseEntity;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Problem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public Bookmark(Member member, Problem problem) {
        this.member = member;
        this.problem = problem;
    }
}
