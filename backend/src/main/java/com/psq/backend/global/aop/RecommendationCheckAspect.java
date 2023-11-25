package com.psq.backend.global.aop;

import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.problem.application.ProblemService;
import com.psq.backend.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RecommendationCheckAspect {
    private final SessionUtil sessionUtil;
    private final MemberService memberService;
    private final ProblemService problemService;

    @After("@annotation(com.psq.backend.util.annotation.RecommendationCheck)")
    public void recommendationCheck() {
        log.info("[AOP] 문제 추천 여부 검사 시작");
        SessionInfo sessionInfo = sessionUtil.getSessionInfo();
        Member member = memberService.getMemberById(sessionInfo.getId());

        if (!member.isRecommended()) {
            log.info("{} 유저는 문제 추천 대상입니다.", member.getEmail());
            problemService.recommendProblem(member);
        }
    }
}
