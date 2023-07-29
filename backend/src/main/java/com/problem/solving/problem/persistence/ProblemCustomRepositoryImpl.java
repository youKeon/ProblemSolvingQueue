package com.problem.solving.problem.persistence;

import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.QProblem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.problem.solving.problem.domain.QProblem.*;

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
}
