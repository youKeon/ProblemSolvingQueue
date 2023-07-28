package com.problem.solving.problem.application;

import com.problem.solving.member.domain.Member;
import com.problem.solving.member.exception.NoSuchMemberException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;

    public void addProblem(ProblemSaveRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(
                () -> new NoSuchMemberException()
        );
        Problem problem = request.toEntity(member);

        problemRepository.save(problem);
    }

    public void delete(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        problem.softDelete();
    }

    public ProblemResponse getProblem(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        return ProblemResponse.from(problem);
    }

    public ProblemResponse pollProblem() {
        Problem problem = problemRepository.findFirstByOrderByCreatedAtAsc().orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        return ProblemResponse.from(problem);
    }

    public void update(Long id,
                       ProblemUpdateRequest request) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        problem.update(request);
    }

    public void test() {
        Member member = new Member("yukeon", "123");
        memberRepository.save(member);
        Category[] categories = {Category.DFS, Category.BFS, Category.SORT, Category.STACK, Category.QUEUE};
        int[] level = {1, 2, 3, 4, 5};
        for (int i = 0; i < 10000; i++) {
            problemRepository.save(new Problem(member, "url" + i, level[i % 5], categories[i % 5], false));
        }
    }

}
