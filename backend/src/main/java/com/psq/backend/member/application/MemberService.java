package com.psq.backend.member.application;

import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.dto.request.MemberSignInRequest;
import com.psq.backend.member.dto.request.MemberSignUpRequest;
import com.psq.backend.member.exception.DuplicatedEmailException;
import com.psq.backend.member.exception.InValidLoginRequestException;
import com.psq.backend.member.exception.NoSuchMemberException;
import com.psq.backend.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static com.psq.backend.util.PasswordUtil.encodePassword;
import static com.psq.backend.util.PasswordUtil.generateSalt;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void signup(MemberSignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) throw new DuplicatedEmailException();
        String salt = generateSalt();
        String encodedPassword = encodePassword(request.getPassword(), salt);

        memberRepository.save(request.toEntity(encodedPassword, salt));
    }

    public void signin(MemberSignInRequest request, HttpSession session) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(NoSuchMemberException::new);
        if (!isPasswordEqual(request, member)) throw new InValidLoginRequestException();
        setSessionInfo(session, member);
    }

    private void setSessionInfo(HttpSession session, Member member) {
        SessionInfo sessionInfo = new SessionInfo(member.getId(), member.getEmail());
        session.setAttribute("sessionInfo", sessionInfo);
    }

    private boolean isPasswordEqual(MemberSignInRequest request, Member member) {
        return member.getPassword().equals(encodePassword(request.getPassword(), member.getSalt()));
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
    }

    @Scheduled(cron = "0 0 21 * * ?")
    protected void initializeRecommendation() {
        log.info("[추천 여부 초기화 메서드 실행]");
        Long initializedCount = memberRepository.initializeRecommendation();
        log.info("초기화된 사용자 계정 개수 : {}", initializedCount);
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }
}

