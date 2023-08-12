package com.problem.solving.problem.application;

import com.problem.solving.common.annotation.ServiceTest;
import com.problem.solving.member.application.MemberService;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.domain.SessionInfo;
import com.problem.solving.member.exception.NoSuchMemberException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.exception.NotDeletedProblemException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ProblemServiceTest extends ServiceTest {
    @Mock
    private MemberService memberService;

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
        when(problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable)).thenReturn(problemPage);
        when(memberRepository.existsById(member.getId())).thenReturn(true);
        when(memberService.getSessionInfo(request)).thenReturn(sessionInfo);

        List<ProblemListResponse> result = problemService.getProblemList(request, 3, Category.DFS, false, pageable);

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
        when(memberRepository.existsById(sessionInfo.getId())).thenReturn(false);
        when(memberService.getSessionInfo(request)).thenReturn(sessionInfo);

        // when, then
        assertThatThrownBy(
                () -> problemService.getProblemList(request, 3, Category.DFS, false, pageable))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("문제 리스트가 없는 경우 예외가 발생한다")
    public void getProblemsEmptyException() throws Exception {
        // given
        Page<Problem> page = Page.empty();

        // when
        when(memberService.getSessionInfo(request)).thenReturn(sessionInfo);
        when(memberRepository.existsById(member.getId())).thenReturn(true);
        when(problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable)).thenReturn(page);

        // then
        assertThatThrownBy(
                () -> problemService.getProblemList(request, 3, Category.DFS, false, pageable))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("문제를 저장한다")
    public void registerProblem() throws Exception {
        // given
        ProblemSaveRequest request = new ProblemSaveRequest(member.getId(), "title","problem", Category.DFS, 3);

        // when
        when(memberRepository.findById(member.getId())).thenReturn(Optional.ofNullable(member));

        // then
        assertDoesNotThrow(() -> problemService.save(request));
    }

    @Test
    @DisplayName("존재하지 않는 유저 정보로 문제를 저장하면 예외가 발생한다")
    public void registerProblemEmptyMemberException() throws Exception {
        // given
        Long 존재하지_않는_사용자_ID = 0L;

        ProblemSaveRequest request = new ProblemSaveRequest(존재하지_않는_사용자_ID, "title","problem", Category.DFS, 3);

        // when
        when(memberRepository.findById(존재하지_않는_사용자_ID)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(
                () -> problemService.save(request))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("문제를 soft delete한다")
    public void deleteProblem() throws Exception {
        // when
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.ofNullable(problem1));
        problemService.delete(problem1.getId());

        // then
        assertThat(problem1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 문제를 삭제하려는 경우 예외가 발생한다")
    public void deleteProblemEmptyException() throws Exception {
        // given
        Long 존재하지_않는_문제_ID = 0L;
        // when, then
        assertThatThrownBy(
                () -> problemService.delete(존재하지_않는_문제_ID))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("가장 먼저 저장한 문제를 조회한다")
    public void pollProblem() throws Exception {
        // when
        when(problemRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.ofNullable(problem1));
        ProblemResponse result = problemService.pollProblem();

        // then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(result.getLevel()).isEqualTo(problem1.getLevel()),
                () -> assertThat(result.getCategory()).isEqualTo(problem1.getCategory())
        );
    }

    @Test
    @DisplayName("문제가 없는 경우 예외가 발생한다")
    public void pollProblemEmptyException() throws Exception {
        // when, then
        assertThatThrownBy(
                () -> problemService.pollProblem())
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("Id로 문제를 단건 조회한다")
    public void getProblemById() throws Exception {
        // when
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.ofNullable(problem1));
        ProblemResponse result = problemService.getProblem(problem1.getId());

        // then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(result.getCategory()).isEqualTo(problem1.getCategory()),
                () -> assertThat(result.getLevel()).isEqualTo(problem1.getLevel())
        );
    }

    @Test
    @DisplayName("존재하지 않는 문제를 단건 조회하면 예외가 발생한다")
    public void getProblemByIdEmptyException() throws Exception {
        // given
        Long 존재하지_않는_문제_ID = 0L;

        // when, then
        assertThatThrownBy(
                () -> problemService.getProblem(존재하지_않는_문제_ID))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("문제를 수정한다")
    public void updateProblem() throws Exception {
        // given
        ProblemUpdateRequest request = new ProblemUpdateRequest(
                "afterUpdate",
                Category.DFS,
                true,
                3);


        // when
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.of(problem1));
        problemService.update(problem1.getId(), request);

        // then
        assertAll(
                () -> assertEquals(problem1.getUrl(), request.getUrl()),
                () -> assertEquals(problem1.getCategory(), request.getCategory()),
                () -> assertEquals(problem1.isSolved(), request.getIsSolved()),
                () -> assertEquals(problem1.getLevel(), request.getLevel())
        );
    }
    @Test
    @DisplayName("존재하지 않는 문제를 수정하면 예외가 발생한다")
    public void updateEmptyProblemException() throws Exception {
        // given
        Long 존재하지_않는_문제_ID = 0L;

        // when, then
        assertThatThrownBy(
                () -> problemService.update(존재하지_않는_문제_ID, any(ProblemUpdateRequest.class)))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("(논리적)삭제된 문제를 되돌린다")
    public void recoveryProblem() throws Exception {
        // given
        problem1.softDelete();

        // when
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.ofNullable(problem1));
        problemService.recovery(problem1.getId());

        // then
        assertThat(problem1.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 문제를 되돌리면 예외가 발생한다")
    public void recoveryNotExistProblemException() throws Exception {
        // given
        Long 존재하지_않는_문제_ID = 0L;

        // when, then
        assertThatThrownBy(
                () -> problemService.recovery(존재하지_않는_문제_ID))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("isDeleted가 false(삭제X)인 문제를 되돌리면 예외가 발생한다")
    public void recoveryNotDeletedProblemException() throws Exception {
        // when, then
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.ofNullable(problem1));

        assertThatThrownBy(
                () -> problemService.recovery(problem1.getId()))
                .isInstanceOf(NotDeletedProblemException.class)
                .hasMessageContaining("삭제되지 않은 문제입니다.");
    }
}
