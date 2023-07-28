package com.problem.solving.problem.persistence;

import com.problem.solving.common.annotation.RepositoryTest;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ProblemRepositoryTest extends RepositoryTest {
    @Autowired ProblemRepository problemRepository;
    @Autowired MemberRepository memberRepository;

    private Problem problem;
    private Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(new Member("yukeon97@gmail.com", "123"));
        problem = problemRepository.save(
                new Problem(
                        this.member,
                        "test",
                        1,
                        Category.DFS,
                        false));
    }

    @Test
    @DisplayName("가장 먼저 등록한 문제를 조회한다")
    public void pollProblemTest() throws Exception {
        //given

        //when
        Optional<Problem> actual = problemRepository.findFirstByOrderByCreatedAtAsc();

        //then
        Assertions.assertThat(problem.getUrl()).isEqualTo(actual.get().getUrl());
    }


}
