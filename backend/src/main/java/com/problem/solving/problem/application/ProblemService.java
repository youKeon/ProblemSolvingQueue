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
import com.problem.solving.problem.exception.InvalidProblemException;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.exception.NotDeletedProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
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

    public void recovery(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException("문제를 찾을 수 없습니다.")
        );
        if (!problem.isDeleted()) throw new NotDeletedProblemException("삭제되지 않은 문제입니다.");
        problem.recovery();
    }
}
