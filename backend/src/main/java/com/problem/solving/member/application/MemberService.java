package com.problem.solving.member.application;

import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.dto.request.MemberSignInRequest;
import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.member.exception.DuplicatedEmailException;
import com.problem.solving.member.exception.NoSuchMemberException;
import com.problem.solving.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.problem.solving.member.util.PasswordUtil.encodePassword;
import static com.problem.solving.member.util.PasswordUtil.generateSalt;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void signup(MemberSignUpRequest request) {
        if (memberRepository.existsMemberByEmail(request.getEmail()))
            throw new DuplicatedEmailException();

        String salt = generateSalt();
        String encodedPassword = encodePassword(request.getPassword(), salt);

        memberRepository.save(request.toEntity(encodedPassword, salt));
    }

    public String signin(MemberSignInRequest request, HttpSession session) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new NoSuchMemberException("로그인에 실패했습니다.")
        );

        if (!member.getPassword().equals(
                encodePassword(request.getPassword(), member.getSalt()))
        )
            throw new NoSuchMemberException("로그인에 실패했습니다.");

        SessionInfo sessionInfo = new SessionInfo(member.getId(), member.getEmail());
        session.setAttribute("sessionInfo", sessionInfo);
        return session.getId();
    }

    public SessionInfo getSessionInfo(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute("sessionInfo");
        if (sessionInfo == null) throw new NoSuchMemberException("잘못된 세션 정보입니다.");
        return sessionInfo;
    }
}
