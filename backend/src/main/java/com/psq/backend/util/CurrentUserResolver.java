package com.psq.backend.util;

import com.psq.backend.member.annotation.CurrentUser;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.exception.NoSuchMemberException;
import com.psq.backend.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        SessionInfo sessionInfo = (SessionInfo) httpSession.getAttribute("sessionInfo");
        if (sessionInfo == null) throw new NoSuchMemberException("잘못된 세션 정보입니다.");
        Long memberId = sessionInfo.getId();
        return memberRepository.findById(memberId).orElseThrow(
                NoSuchMemberException::new);
    }
}