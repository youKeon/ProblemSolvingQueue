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

    public SessionInfo signin(MemberSignInRequest request, HttpSession session) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new NoSuchMemberException("로그인에 실패했습니다.")
        );

        if (!member.getPassword().equals(request.getPassword()))
            throw new NoSuchMemberException("로그인에 실패했습니다.");

        SessionInfo sessionInfo = new SessionInfo(member.getId(), member.getEmail());
        session.setAttribute("sessionInfo", sessionInfo);

        return sessionInfo;
    }

    public SessionInfo getSessionInfo(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute("sessionInfo");
        if (sessionInfo == null) throw new NoSuchMemberException("잘못된 세션 정보입니다.");
        return sessionInfo;
    }
}
