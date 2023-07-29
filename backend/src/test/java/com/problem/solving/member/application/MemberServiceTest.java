package com.problem.solving.member.application;

import com.problem.solving.member.domain.Member;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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
    private static final Pageable pageable = PageRequest.of(0, 3);
    private Member member;
    private Problem problem1;
    private Problem problem2;
    private Problem problem3;
    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
        problem1 = new Problem(member, "problem1", 3, Category.DFS, false);
        problem2 = new Problem(member, "problem2", 3, Category.DFS, false);
        problem3 = new Problem(member, "problem3", 3, Category.DFS, false);

    }

    @Test
    @DisplayName("회원 id와 페이지를 입력하면 문제 리스트를 조회한다")
    public void getProblems() throws Exception {
        //given
        Long memberId = 1L;
        List<Problem> problems = Arrays.asList(problem1, problem2, problem3);

        Page<Problem> problemPage = new PageImpl<>(problems, pageable, problems.size());

        //when
        when(problemRepository.findAllProblem(memberId, pageable)).thenReturn(problemPage);
        List<ProblemListResponse> result = memberService.getProblemList(memberId, pageable);

        //then
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
    @DisplayName("문제 리스트가 없는 경우 예외가 발생한다")
    public void getProblemsEmptyException() throws Exception {
        //when, then
        Long memberId = 1L;
        Page<Problem> page = Page.empty();

        when(problemRepository.findAllProblem(memberId, pageable)).thenReturn(page);

        assertThrows(NoSuchProblemException.class, () -> memberService.getProblemList(memberId, pageable));
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
    void signUpEmailDuplicatedExceptionTest() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("yukeon@gmail.com", "1234");

        // when
        when(memberRepository.existsMemberByEmail(request.getEmail())).thenReturn(true);

        // then
        assertThatThrownBy(
                () -> memberService.signup(request))
                .isInstanceOf(DuplicatedEmailException.class)
                .hasMessageContaining("이미 등록된 이메일입니다.");
    }
}
