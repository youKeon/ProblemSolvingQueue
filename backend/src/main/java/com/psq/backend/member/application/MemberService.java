package com.psq.backend.member.application;

import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.dto.request.MemberSignInRequest;
import com.psq.backend.member.dto.request.MemberSignUpRequest;
import com.psq.backend.member.exception.DuplicatedEmailException;
import com.psq.backend.member.exception.NoSuchMemberException;
import com.psq.backend.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.psq.backend.member.util.PasswordUtil.encodePassword;
import static com.psq.backend.member.util.PasswordUtil.generateSalt;


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

    public Member getMemberInfo(HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute("sessionInfo");
        if (sessionInfo == null) throw new NoSuchMemberException("잘못된 세션 정보입니다.");

        Long memberId = sessionInfo.getId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchMemberException()
        );

        return member;
    }
}
