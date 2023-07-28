package com.problem.solving.bookmark.domain;

import com.problem.solving.common.BaseEntity;
import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.domain.Problem;
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
}
