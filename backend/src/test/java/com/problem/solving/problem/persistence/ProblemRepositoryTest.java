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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProblemRepositoryTest extends RepositoryTest {
    @Autowired ProblemRepository problemRepository;
    @Autowired MemberRepository memberRepository;
    private static final Pageable pageable = PageRequest.of(0, 2);
    
    private Problem problem1;
    private Problem problem2;
    private Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(new Member("yukeon97@gmail.com", "123"));
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
        //when
        Optional<Problem> actual = problemRepository.findFirstByOrderByCreatedAtAsc();

        //then
        Assertions.assertThat(problem1.getUrl()).isEqualTo(actual.get().getUrl());
    }

    @Test
    @DisplayName("질문이 없는 경우 빈 값이 반환된다")
    public void pollProblemEmptyTest() throws Exception {
        //given
        clearProblem();

        //when
        Optional<Problem> actual = problemRepository.findFirstByOrderByCreatedAtAsc();

        //then
        Assertions.assertThat(actual).isEmpty();
    }
    
    @Test
    @DisplayName("전체 질문을 조회한다(페이징 적용)")
    public void findAllProblem() throws Exception {
        // when
        Page<Problem> result = problemRepository.findAllProblem(member.getId(), 1, Category.DFS, false, pageable);

        // then
        List<Problem> problems = result.getContent();
        assertEquals(2, problems.size());
        assertTrue(problems.contains(problem1));
        assertTrue(problems.contains(problem2));
    }

    @Test
    @DisplayName("질문이 없는 경우 빈 리스트를 반환한다")
    public void findEmptyProblem() throws Exception {
        //given
        clearProblem();

        // when
        Page<Problem> result = problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable);

        // then
        assertTrue(result.isEmpty());
    }

    private void clearProblem() {
        problemRepository.deleteAll();
    }
}
