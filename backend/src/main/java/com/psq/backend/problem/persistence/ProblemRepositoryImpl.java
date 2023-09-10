package com.psq.backend.problem.persistence;

import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import com.psq.backend.problem.dto.response.QProblemListResponse;
import com.psq.backend.problem.dto.response.QProblemResponse;
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
                                problem.isSolved
                        ))
                        .from(problem)
                        .where(problem.member.id.eq(memberId))
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
                                problem.isSolved
                        ))
                        .from(problem)
                        .where(problem.id.eq(problemId))
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
