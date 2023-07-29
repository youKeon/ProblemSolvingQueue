package com.problem.solving.member.application;

import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.member.exception.DuplicatedEmailException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Category;
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

    public void signup(MemberSignUpRequest request) {
        if (!memberRepository.existsMemberByEmail(request.getEmail())) memberRepository.save(request.toEntity());
        else throw new DuplicatedEmailException();
    }

    public List<ProblemListResponse> getProblemList(Long id,
                                                    Integer level,
                                                    Category category,
                                                    Boolean isSolved,
                                                    Pageable pageable) {

        Page<Problem> problemList = problemRepository.findAllProblem(id, level, category, isSolved, pageable);
        if (problemList.getNumberOfElements() == 0) throw new NoSuchProblemException("문제가 없습니다.");

        return problemList.stream()
                .map(ProblemListResponse::from)
                .collect(Collectors.toList());
    }
}
