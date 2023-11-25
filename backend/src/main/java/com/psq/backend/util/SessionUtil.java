package com.psq.backend.util;

import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.exception.NoSuchMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class SessionUtil {
    private final HttpSession httpSession;
    private final static String SESSION_INFO = "sessionInfo";


    public SessionInfo getSessionInfo() {
        SessionInfo sessionInfo = (SessionInfo) httpSession.getAttribute(SESSION_INFO);
        if (sessionInfo == null) throw new NoSuchMemberException("잘못된 세션 정보입니다.");
        return sessionInfo;
    }
}
