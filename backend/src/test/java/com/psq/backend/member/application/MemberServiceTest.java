package com.psq.backend.member.application;

import com.psq.backend.common.annotation.ServiceTest;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.dto.request.MemberSignInRequest;
import com.psq.backend.member.dto.request.MemberSignUpRequest;
import com.psq.backend.member.exception.DuplicatedEmailException;
import com.psq.backend.member.exception.NoSuchMemberException;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberServiceTest extends ServiceTest {

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", encodePassword, salt);
        problem1 = new Problem(member, "title", "problem1", 3, Category.DFS, false);
        problem2 = new Problem(member, "title", "problem2", 3, Category.DFS, false);
        problem3 = new Problem(member, "title", "problem3", 3, Category.DFS, false);

        ReflectionTestUtils.setField(member, "id", 1L);

        ReflectionTestUtils.setField(problem1, "id", 1L);
        ReflectionTestUtils.setField(problem2, "id", 2L);
        ReflectionTestUtils.setField(problem3, "id", 3L);

        session = new MockHttpSession();
        sessionInfo = new SessionInfo(member.getId(), member.getEmail());
    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아 회원가입을 한다")
    void signUpTest() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), member.getPassword());

        // when
        when(memberRepository.existsMemberByEmail(request.getEmail())).thenReturn(false);

        // then
        assertDoesNotThrow(() -> memberService.signup(request));
    }

    @Test
    @DisplayName("회원가입 시 이메일이 중복되면 예외가 발생한다")
    void signUpDuplicatedEmailExceptionTest() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), member.getPassword());

        // when
        when(memberRepository.existsMemberByEmail(request.getEmail())).thenReturn(true);

        // then
        assertThatThrownBy(
                () -> memberService.signup(request))
                .isInstanceOf(DuplicatedEmailException.class)
                .hasMessageContaining("");
    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아 로그인을 한다")
    void signInTest() {
        // given
        MemberSignInRequest request = new MemberSignInRequest(member.getEmail(), password);

        // when
        when(memberRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.ofNullable(member));

        // then
        assertDoesNotThrow(() -> memberService.signin(request, session));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 예외가 발생한다")
    void signInNoSuchEmailExceptionTest() {
        // given
        String 존재하지_않는_이메일 = "noSuch@Email.com";
        MemberSignInRequest request = new MemberSignInRequest(존재하지_않는_이메일, "123456789");

        // when, then
        assertThatThrownBy(
                () -> memberService.signin(request, session))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("로그인에 실패했습니다.");
    }

    @Test
    @DisplayName("저장된 계정의 비밀번호와 로그인 요청으로 들어온 비밀번호가 다르면 예외가 발생한다")
    void signInNotEqualPasswordExceptionTest() {
        // given
        String DB와_다른_비밀번호 = "notEqualPassword";
        MemberSignInRequest request = new MemberSignInRequest(member.getEmail(), DB와_다른_비밀번호);

        // when
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(member));

        // when, then
        assertThatThrownBy(
                () -> memberService.signin(request, session))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("로그인에 실패했습니다.");
    }

    @Test
    @DisplayName("세션 정보를 가져온다")
    public void getSessionInfoTest() throws Exception {
        // when
        when(request.getSession()).thenReturn(session);

        request.getSession().setAttribute("sessionInfo", sessionInfo);
        SessionInfo actual = memberService.getSessionInfo(request);

        // then
        assertEquals(member.getId(), actual.getId());
        assertEquals(member.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("세션 정보가 없으면 예외가 발생한다")
    public void getInvalidSessionExceptionTest() throws Exception {
        // given
        HttpSession mockSession = mock(HttpSession.class);

        // when
        when(request.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("sessionInfo")).thenReturn(null);

        // then
        assertThatThrownBy(
                () -> memberService.getSessionInfo(request))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("잘못된 세션 정보입니다.");
    }
}
