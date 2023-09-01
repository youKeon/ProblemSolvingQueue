package com.psq.backend.problem.persistence;

import com.psq.backend.common.annotation.RepositoryTest;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProblemRepositoryTest extends RepositoryTest {

    private static final Pageable pageable = PageRequest.of(0, 2);
    
    private Problem problem1;
    private Problem problem2;
    private Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(new Member("yukeon97@gmail.com", "123", "salt"));
        problem1 = problemRepository.save(
                new Problem(
                        this.member,
                        "title",
                        "test",
                        1,
                        Category.DFS,
                        false));

        problem2 = problemRepository.save(
                new Problem(
                        this.member,
                        "title",
                        "test",
                        1,
                        Category.DFS,
                        false));
    }

    @Test
    @DisplayName("가장 먼저 등록한 문제를 조회한다")
    public void pollProblemTest() throws Exception {
        // when
        Optional<Problem> actual = problemRepository.pollProblem(member.getId());

        // then
        assertThat(problem1.getUrl()).isEqualTo(actual.get().getUrl());
    }

    @Test
    @DisplayName("질문이 없는 경우 빈 값이 반환된다")
    public void pollProblemEmptyTest() throws Exception {
        // given
        clearProblem();

        // when
        Optional<Problem> actual = problemRepository.pollProblem(member.getId());

        // then
        assertThat(actual).isEmpty();
    }
    
    @Test
    @DisplayName("전체 질문을 조회한다(페이징 적용)")
    public void findAllProblem() throws Exception {
        // when
        List<Problem> actual = problemRepository.findAllProblem(member.getId(), 1, Category.DFS, false, pageable);

        // then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(problem1);
        assertThat(actual.get(1)).usingRecursiveComparison().isEqualTo(problem2);
    }

    @Test
    @DisplayName("질문이 없는 경우 빈 리스트를 반환한다")
    public void findEmptyProblem() throws Exception {
        // given
        clearProblem();

        // when
        List<Problem> actual = problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable);

        // then
        assertThat(actual).isEmpty();
    }

    private void clearProblem() {
        problemRepository.deleteAll();
    }
}
