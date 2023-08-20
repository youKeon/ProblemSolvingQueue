package com.psq.backend.problem.persistence;

import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.psq.backend.problem.domain.QProblem.problem;


@RequiredArgsConstructor
public class ProblemCustomRepositoryImpl implements ProblemCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Problem> findAllProblem(Long memberId,
                                        Integer level,
                                        Category category,
                                        Boolean isSolved,
                                        Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(problem.member.id.eq(memberId));

        Optional.ofNullable(level).ifPresent(value -> builder.and(problem.level.eq(value)));
        Optional.ofNullable(category).ifPresent(value -> builder.and(problem.category.eq(value)));
        Optional.ofNullable(isSolved).ifPresent(value -> builder.and(problem.isSolved.eq(value)));


        List<Problem> result = jpaQueryFactory
                .selectFrom(problem)
                .where(builder)
                .orderBy(problem.createdAt.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<Problem> problemList = result.stream()
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(problemList, pageable, result::size);
    }

    @Override
    public Optional<Problem> pollProblem(Long memberId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(problem)
                        .where(problem.member.id.eq(memberId))
                        .orderBy(problem.createdAt.asc())
                        .limit(1)
                        .fetchOne()
        );
    }
}
