package com.psq.backend.problem.persistence;

import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.dto.response.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.psq.backend.problem.domain.QProblem.problem;


@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemCustomRepository {
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 5;

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<ProblemListResponse> findAllProblem(Long memberId,
                                                    Integer level,
                                                    Category category,
                                                    Boolean isSolved,
                                                    Pageable pageable) {

        return jpaQueryFactory
                .select(new QProblemListResponse(
                        problem.url,
                        problem.level,
                        problem.category,
                        problem.isSolved
                ))
                .from(problem)
                .where(
                        problem.member.id.eq(memberId),
                        problem.isDeleted.isFalse(),
                        eqLevel(level),
                        eqCategory(category),
                        eqIsSolved(isSolved)
                )
                .orderBy(problem.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }


    @Override
    public Optional<ProblemResponse> pollProblem(Long memberId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(new QProblemResponse(
                                problem.title,
                                problem.url,
                                problem.level,
                                problem.category,
                                problem.isSolved,
                                problem.updatedAt

                        ))
                        .from(problem)
                        .where(
                                problem.member.id.eq(memberId),
                                problem.isDeleted.isFalse()
                        )
                        .orderBy(problem.createdAt.asc())
                        .fetchFirst()
        );
    }

    @Override
    public Optional<ProblemResponse> findProblem(Long problemId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(new QProblemResponse(
                                problem.title,
                                problem.url,
                                problem.level,
                                problem.category,
                                problem.isSolved,
                                problem.updatedAt
                        ))
                        .from(problem)
                        .where(
                                problem.id.eq(problemId),
                                problem.isDeleted.isFalse()
                        )
                        .fetchFirst()
        );
    }

    @Override
    public long deleteSoftDeletedProblem() {
        return jpaQueryFactory
                .delete(problem)
                .where(
                        problem.isDeleted.isTrue(),
                        problem.updatedAt.before(LocalDateTime.now().minusDays(3))
                )
                .execute();
    }

    @Override
    public long increaseSolvedCount(Long problemId) {
        return jpaQueryFactory
                .update(problem)
                .set(problem.solvedCount, problem.solvedCount.add(1))
                .where(problem.id.eq(problemId))
                .execute();
    }

    @Override
    public ProblemRecommendResponse recommendProblem(Long memberId) {
        ProblemWeakCategoryAvgLevelResponse categoryWithAvgLevel = findWeakestCategory();
        if (categoryWithAvgLevel == null) return null;

        Category weakestCategory = categoryWithAvgLevel.getCategory();
        Integer avgLevel = levelExceptionCheck(categoryWithAvgLevel.getLevel());

        return jpaQueryFactory
                .select(new QProblemRecommendResponse(
                        problem.title,
                        problem.url,
                        problem.level,
                        problem.category
                ))
                .from(problem)
                .where(
                        problem.member.id.eq(memberId),
                        problem.isDeleted.isFalse(),
                        problem.category.eq(weakestCategory),
                        problem.isRecommended.isFalse(), // 3일 안에 추천된 기록이 있으면 제외
                        problem.level.eq(avgLevel)
                )
                .orderBy(
                        problem.solvedCount.asc() // 풀이 횟수가 적은 문제가 우선
                )
                .fetchFirst();
    }

    @Override
    public void initializeRecommendedProblem() {
        jpaQueryFactory
                .update(problem)
                .set(problem.isRecommended, false)
                .where(
                        problem.isDeleted.isFalse(),
                        problem.isRecommended.isTrue(),
                        problem.updatedAt.before(LocalDateTime.now().minusDays(3))
                )
                .execute();
    }

    private ProblemWeakCategoryAvgLevelResponse findWeakestCategory() {
        return jpaQueryFactory
                .select(new QProblemWeakCategoryAvgLevelResponse(
                        problem.category,
                        problem.level.avg().intValue()
                ))
                .from(problem)
                .groupBy(problem.category)
                .orderBy(problem.level.avg().desc())
                .fetchFirst();
    }

    /**
     * 평균 난이도가 최소(1) -> 난이도 최대(5) 반환
     * 평균 난이도가 최대(5) -> 난이도 최소(1) 반환
     */
    private int levelExceptionCheck(int avgLevel) {
        if (avgLevel == MAX_LEVEL) return MIN_LEVEL;
        if (avgLevel == MIN_LEVEL) return MAX_LEVEL;
        return avgLevel;
    }


    private BooleanExpression eqLevel(Integer level) {
        if (level == null) return null;
        return problem.level.eq(level);
    }

    private BooleanExpression eqCategory(Category category) {
        if (category == null) return null;
        return problem.category.eq(category);
    }

    private BooleanExpression eqIsSolved(Boolean isSolved) {
        if (isSolved == null) return null;
        return problem.isSolved.eq(isSolved);
    }
}
