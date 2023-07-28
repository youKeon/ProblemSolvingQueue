package com.problem.solving.problem.persistence;

import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.QProblem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.problem.solving.problem.domain.QProblem.*;

@RequiredArgsConstructor
public class ProblemCustomRepositoryImpl implements ProblemCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Problem> findAllProblem(Long memberId, Pageable pageable) {
        List<Problem> result = jpaQueryFactory
                .selectFrom(problem)
                .where(problem.member.id.eq(memberId))
                .orderBy(problem.createdAt.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<Problem> problemList = result.stream()
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(problemList, pageable, result::size);
    }

    @Override
    public Page<Problem> filterProblems(Pageable pageable) {
        return null;
    }
}
