package com.problem.solving.member.application;

import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.dto.request.MemberSignInRequest;
import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.member.exception.DuplicatedEmailException;
import com.problem.solving.member.exception.NoSuchMemberException;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
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

    public List<ProblemListResponse> getProblemList(HttpServletRequest request,
                                                    Integer level,
                                                    Category category,
                                                    Boolean isSolved,
                                                    Pageable pageable) {


        SessionInfo sessionInfo = getSessionInfo(request);
        Long memberId = sessionInfo.getId();
        if (!memberRepository.existsById(memberId))
            throw new NoSuchProblemException();

        Page<Problem> problemList = problemRepository.findAllProblem(memberId, level, category, isSolved, pageable);
        if (problemList.getNumberOfElements() == 0) throw new NoSuchProblemException();

        return problemList.stream()
                .map(ProblemListResponse::from)
                .collect(Collectors.toList());
    }

    public SessionInfo createSessionInfo(MemberSignInRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new NoSuchMemberException("로그인에 실패했습니다.")
        );

        if (!member.getPassword().equals(request.getPassword()))
            throw new NoSuchMemberException("로그인에 실패했습니다.");

        return new SessionInfo(member.getId(), member.getEmail());
    }

    public SessionInfo getSessionInfo(HttpServletRequest request) {
        return (SessionInfo) request.getSession().getAttribute("sessionInfo");
    }
}
