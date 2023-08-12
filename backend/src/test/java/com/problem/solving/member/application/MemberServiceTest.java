package com.problem.solving.member.application;

import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.dto.request.MemberSignUpRequest;
import com.problem.solving.member.exception.DuplicatedEmailException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private ProblemRepository problemRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;
    private static final Pageable pageable = PageRequest.of(0, 3);
    private SessionInfo sessionInfo;
    private Member member;
    private Problem problem1, problem2, problem3;
    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
        problem1 = new Problem(member, "title", "problem1", 3, Category.DFS, false);
        problem2 = new Problem(member, "title", "problem2", 3, Category.DFS, false);
        problem3 = new Problem(member, "title", "problem3", 3, Category.DFS, false);


        ReflectionTestUtils.setField(member, "id", 1L);

        ReflectionTestUtils.setField(problem1, "id", 1L);
        ReflectionTestUtils.setField(problem2, "id", 2L);
        ReflectionTestUtils.setField(problem3, "id", 3L);

        sessionInfo = new SessionInfo(member.getId(), member.getEmail());
    }

    @Test
    @DisplayName("회원 id와 페이지를 입력하면 문제 리스트를 조회한다")
    public void getProblemListTest() throws Exception {
        // given
        List<Problem> problems = Arrays.asList(problem1, problem2, problem3);
        Page<Problem> problemPage = new PageImpl<>(problems, pageable, problems.size());

        // when
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("sessionInfo")).thenReturn(sessionInfo);
        when(problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable)).thenReturn(problemPage);
        when(memberRepository.existsById(member.getId())).thenReturn(true);

        List<ProblemListResponse> result = memberService.getProblemList(request, 3, Category.DFS, false, pageable);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(0).getCategory()).isEqualTo(problem1.getCategory()),
                () -> assertThat(result.get(0).getLevel()).isEqualTo(problem1.getLevel()),

                () -> assertThat(result.get(1).getUrl()).isEqualTo(problem2.getUrl()),
                () -> assertThat(result.get(1).getCategory()).isEqualTo(problem2.getCategory()),
                () -> assertThat(result.get(1).getLevel()).isEqualTo(problem2.getLevel()),

                () -> assertThat(result.get(2).getUrl()).isEqualTo(problem3.getUrl()),
                () -> assertThat(result.get(2).getCategory()).isEqualTo(problem3.getCategory()),
                () -> assertThat(result.get(2).getLevel()).isEqualTo(problem3.getLevel())
        );
    }

    @Test
    @DisplayName("사용자 정보가 존재하지 않는 경우 예외가 발생한다")
    public void getProblemListNoSuchMemberExceptionTest() throws Exception {
        // given
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("sessionInfo")).thenReturn(sessionInfo);
        when(memberRepository.existsById(sessionInfo.getId())).thenReturn(false);

        // when, then
        assertThatThrownBy(
                () -> memberService.getProblemList(request, 3, Category.DFS, false, pageable))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("문제 리스트가 없는 경우 예외가 발생한다")
    public void getProblemsEmptyException() throws Exception {
        // given
        Page<Problem> page = Page.empty();

        // when
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("sessionInfo")).thenReturn(sessionInfo);
        when(problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable)).thenReturn(page);
        when(memberRepository.existsById(member.getId())).thenReturn(true);

        // then
        assertThatThrownBy(
                () -> memberService.getProblemList(request, 3, Category.DFS, false, pageable))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아 회원가입을 한다")
    void signUpTest() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("yukeon@gmail.com", "1234");

        // when
        when(memberRepository.existsMemberByEmail(request.getEmail())).thenReturn(false);

        // then
        assertDoesNotThrow(() -> memberService.signup(request));
    }

    @Test
    @DisplayName("회원가입 시 이메일이 중복되면 예외가 발생한다")
    void signUpDuplicatedEmailExceptionTest() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("yukeon@gmail.com", "1234");

        // when
        when(memberRepository.existsMemberByEmail(request.getEmail())).thenReturn(true);

        // then
        assertThatThrownBy(
                () -> memberService.signup(request))
                .isInstanceOf(DuplicatedEmailException.class)
                .hasMessageContaining("");
    }
}
