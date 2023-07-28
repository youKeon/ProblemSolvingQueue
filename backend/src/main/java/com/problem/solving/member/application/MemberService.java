package com.problem.solving.member.application;

import com.problem.solving.member.domain.Member;
import com.problem.solving.member.dto.request.MemberJoinRequest;
import com.problem.solving.member.exception.DuplicatedEmailException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    public void join(MemberJoinRequest request) {
        emailDuplicateCheck(request.getEmail());
        memberRepository.save(request.toEntity());
    }

    private void emailDuplicateCheck(String email) {
        if (memberRepository.findByEmail(email) != null) throw new DuplicatedEmailException();
    }

    public void delete(Long id) {
        Member member = memberRepository.findById(id).get();
        System.out.println(member.getEmail());
    }

    public List<ProblemListResponse> getProblemList(Long id, Pageable pageable) {
        Page<Problem> problemList = problemRepository.findAllProblem(id, pageable);
        if (problemList.getNumberOfElements() == 0) throw new NoSuchProblemException("문제가 없습니다.");

        return problemList.stream()
                .map(ProblemListResponse::from)
                .collect(Collectors.toList());
    }
}
