package com.psq.backend.problem.persistence;

import com.psq.backend.common.annotation.RepositoryTest;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemRecommendResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProblemRepositoryTest extends RepositoryTest {

    private static final Pageable pageable = PageRequest.of(0, 2);
    
    private Problem problem1;
    private Problem problem2;
    private Member member;

    @PersistenceContext
    private EntityManager entityManager;
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
        Optional<ProblemResponse> actual = problemRepository.pollProblem(member.getId());

        // then
        assertThat(problem1.getUrl()).isEqualTo(actual.get().getUrl());
    }

    @Test
    @DisplayName("질문이 없는 경우 빈 값이 반환된다")
    public void pollProblemEmptyTest() throws Exception {
        // given
        clearProblem();

        // when
        Optional<ProblemResponse> actual = problemRepository.pollProblem(member.getId());

        // then
        assertThat(actual).isEmpty();
    }
    
    @Test
    @DisplayName("전체 질문을 조회한다")
    public void findAllProblemTest() throws Exception {
        // when
        List<ProblemListResponse> actual = problemRepository.findAllProblem(member.getId(), 1, Category.DFS, false, pageable);

        // then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(problem1);
        assertThat(actual.get(1)).usingRecursiveComparison().isEqualTo(problem2);
    }

    @Test
    @DisplayName("질문이 없는 경우 빈 리스트를 반환한다")
    public void findEmptyProblemTest() throws Exception {
        // given
        clearProblem();

        // when
        List<ProblemListResponse> actual = problemRepository.findAllProblem(member.getId(), null, null, null, pageable);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("문제를 조회한다")
    public void findProblemTest() throws Exception {
        // given
        ProblemResponse response = new ProblemResponse(problem1.getTitle(), problem1.getUrl(), problem1.getLevel(), problem1.getCategory(), problem1.isSolved(), problem1.getUpdatedAt());

        // when
        Optional<ProblemResponse> actual = problemRepository.findProblem(problem1.getId());

        // then
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("논리적 삭제가 3일 전에 된 문제는 물리적 삭제가 된다")
    public void deleteSoftDeletedProblemTest() {
        // given
        problem1.softDelete();

        entityManager
                .createQuery("UPDATE Problem p SET p.updatedAt = :date WHERE p.id = :id")
                .setParameter("date", LocalDateTime.now().minusDays(4))
                .setParameter("id", problem1.getId())
                .executeUpdate();

        entityManager.refresh(problem1);

        // when
        long 삭제된_문제_수 = problemRepository.deleteSoftDeletedProblem();

        // then
        assertThat(삭제된_문제_수).isEqualTo(1);
    }

    @Test
    @DisplayName("문제 풀이 횟수를 1 증가시킨다")
    public void increaseSolvedCountTest() throws Exception {
        // when
        long increasedCount = problemRepository.increaseSolvedCount(problem1.getId());

        // then
        assertThat(increasedCount).isEqualTo(1);
    }

    @Test
    @DisplayName("추천 문제를 반환한다")
    public void recommendProblemTest() {
        // given
        clearProblem();
        Problem problem1 = new Problem(member, "title1", "url1", 1, Category.DFS, false);
        Problem problem2 = new Problem(member, "title2", "url2", 2, Category.DFS, false);
        Problem problem3 = new Problem(member, "title3", "url3", 3, Category.DFS, false);
        Problem problem4 = new Problem(member, "title4", "url4", 4, Category.DFS, false);
        Problem problem5 = new Problem(member, "title5", "url5", 5, Category.DFS, false);

        problemRepository.save(problem1);
        problemRepository.save(problem2);
        problemRepository.save(problem3);
        problemRepository.save(problem4);
        problemRepository.save(problem5);

        // when
        List<ProblemRecommendResponse> actual = problemRepository.recommendProblem(member.getId());

        // then
        assertThat(actual.size()).isEqualTo(2);

        assertThat(actual.get(0).getLevel()).isEqualTo(2);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(problem2);

        assertThat(actual.get(1).getLevel()).isEqualTo(4);
        assertThat(actual.get(1)).usingRecursiveComparison().isEqualTo(problem4);
    }

    @Test
    @DisplayName("추천 문제의 평균 난이도가 최대값(1)이면 난이도 4, 5 문제를 반환한다")
    public void recommendProblemLowLevelExceptionTest() {
        // given
        clearProblem();
        for (int i = 0; i < 20; i++) {
            problemRepository.save(new Problem(member, "title" + i, "url" + i, 1, Category.DFS, false));
        }
        Problem problem5 = new Problem(member, "title5", "url5", 4, Category.DFS, false);
        Problem problem6 = new Problem(member, "title6", "url6", 5, Category.DFS, false);

        problemRepository.save(problem5);
        problemRepository.save(problem6);

        // when
        List<ProblemRecommendResponse> actual = problemRepository.recommendProblem(member.getId());

        // then
        assertThat(actual.size()).isEqualTo(2);

        assertThat(actual.get(0).getLevel()).isEqualTo(4);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(problem5);

        assertThat(actual.get(1).getLevel()).isEqualTo(5);
        assertThat(actual.get(1)).usingRecursiveComparison().isEqualTo(problem6);
    }

    @Test
    @DisplayName("추천 문제의 평균 난이도가 최대값(5)이면 난이도 1, 2 문제를 반환한다")
    public void recommendProblemLowHighExceptionTest() {
        // given
        clearProblem();
        for (int i = 0; i < 20; i++) {
            problemRepository.save(new Problem(member, "title" + i, "url" + i, 5, Category.DFS, false));
        }
        Problem problem5 = new Problem(member, "title5", "url5", 1, Category.DFS, false);
        Problem problem6 = new Problem(member, "title6", "url6", 2, Category.DFS, false);

        problemRepository.save(problem5);
        problemRepository.save(problem6);

        // when
        List<ProblemRecommendResponse> actual = problemRepository.recommendProblem(member.getId());

        // then
        assertThat(actual.size()).isEqualTo(2);

        assertThat(actual.get(0).getLevel()).isEqualTo(1);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(problem5);

        assertThat(actual.get(1).getLevel()).isEqualTo(2);
        assertThat(actual.get(1)).usingRecursiveComparison().isEqualTo(problem6);
    }

    @Test
    @DisplayName("3일 전에 추천된 문제 초기화 테스트")
    public void initializeRecommendedProblemTest() {
        // given
        problem1.recommended();
        entityManager
                .createQuery("UPDATE Problem p SET p.updatedAt = :date WHERE p.id = :id")
                .setParameter("date", LocalDateTime.now().minusDays(4))
                .setParameter("id", problem1.getId())
                .executeUpdate();
        entityManager.refresh(problem1);

        // when
        problemRepository.initializeRecommendedProblem();
        entityManager.refresh(problem1);

        // then
        assertThat(problem1.isRecommended()).isFalse();
    }

    private void clearProblem() {
        problemRepository.deleteAll();
    }
}
