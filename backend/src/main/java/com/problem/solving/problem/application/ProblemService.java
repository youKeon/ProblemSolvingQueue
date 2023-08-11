package com.problem.solving.problem.application;

import com.problem.solving.member.domain.Member;
import com.problem.solving.member.exception.NoSuchMemberException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.exception.NotDeletedProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;



    public void save(ProblemSaveRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(
                () -> new NoSuchMemberException()
        );
        Problem problem = request.toEntity(member);
        problemRepository.save(problem);
    }

    public void delete(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        problem.softDelete();
    }

    public ProblemResponse getProblem(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        return ProblemResponse.from(problem);
    }

    public ProblemResponse pollProblem() {
        Problem problem = problemRepository.findFirstByOrderByCreatedAtAsc().orElseThrow(
                () -> new NoSuchProblemException()
        );
        return ProblemResponse.from(problem);
    }

    public void update(Long id,
                       ProblemUpdateRequest request) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        problem.update(request);
    }

    public void recovery(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(
                () -> new NoSuchProblemException()
        );
        if (!problem.isDeleted()) throw new NotDeletedProblemException();
        problem.recovery();
    }
}
