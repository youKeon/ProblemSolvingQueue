package com.psq.backend.util;

import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionUtilTest {
    @InjectMocks
    private SessionUtil sessionUtil;
    @Mock
    private HttpSession httpSession;

    @Test
    @DisplayName("세션 정보를 조회한다")
    public void getSessionInfoTest() throws Exception {
        // given
        SessionInfo sessionInfo = new SessionInfo(1L, "yukeon@gmail.com");

        // when
        when(httpSession.getAttribute("sessionInfo")).thenReturn(sessionInfo);
        SessionInfo actual = sessionUtil.getSessionInfo();

        // then
        assertThat(actual.getId()).isEqualTo(sessionInfo.getId());
        assertThat(actual.getEmail()).isEqualTo(sessionInfo.getEmail());
    }

    @Test
    @DisplayName("세션 정보가 존재하지 않으면 예외가 발생한다")
    public void getSessionInfoExceptionTest() throws Exception {
        // given, when, then
        assertThatThrownBy(
                () -> sessionUtil.getSessionInfo())
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("잘못된 세션 정보입니다.");
    }

}
