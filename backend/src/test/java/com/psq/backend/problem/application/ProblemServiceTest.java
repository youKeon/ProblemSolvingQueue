package com.psq.backend.problem.application;

import com.psq.backend.common.annotation.ServiceTest;
import com.psq.backend.member.application.MemberService;
import com.psq.backend.member.domain.Member;
import com.psq.backend.member.domain.SessionInfo;
import com.psq.backend.member.exception.NoSuchMemberException;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import com.psq.backend.problem.dto.request.ProblemSaveRequest;
import com.psq.backend.problem.dto.request.ProblemUpdateRequest;
import com.psq.backend.problem.dto.response.ProblemListResponse;
import com.psq.backend.problem.dto.response.ProblemResponse;
import com.psq.backend.problem.exception.NoSuchProblemException;
import com.psq.backend.problem.exception.NotDeletedProblemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


public class ProblemServiceTest extends ServiceTest {
    @Mock
    private MemberService memberService;

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", password, salt);
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
    @DisplayName("세션 정보와 주어진 필터 조건으로(난이도, 풀이 여부, 문제 유형 등) 문제 리스트를 조회한다")
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
    @DisplayName("문제 리스트 조회 시 사용자의 세션 정보가 존재하지 않는 경우 예외가 발생한다")
    public void getProblemListNoSuchMemberExceptionTest() throws Exception {
        // given
        SessionInfo 존재하지_않는_세션_정보 = new SessionInfo(0L, "noSuch@email.com");
        when(memberService.getSessionInfo(request)).thenReturn(존재하지_않는_세션_정보);

        // when, then
        assertThatThrownBy(
                () -> problemService.getProblemList(request, 3, Category.DFS, false, pageable))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
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
        ProblemSaveRequest saveRequest = new ProblemSaveRequest("url", "title", Category.DFS, 3);

        // when
        when(memberRepository.findById(member.getId())).thenReturn(Optional.ofNullable(member));
        when(memberService.getSessionInfo(request)).thenReturn(sessionInfo);

        // then
        assertDoesNotThrow(() -> problemService.save(request, saveRequest));
    }

    @Test
    @DisplayName("존재하지 않는 유저 정보로 문제를 저장하면 예외가 발생한다")
    public void registerProblemEmptyMemberException() throws Exception {
        // given
        ProblemSaveRequest saveRequest = new ProblemSaveRequest("title","problem", Category.DFS, 3);

        // when
        when(memberService.getSessionInfo(request)).thenReturn(sessionInfo);
        when(memberRepository.findById(sessionInfo.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(
                 () -> problemService.save(request, saveRequest))
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
    @DisplayName("가장 먼저 저장한 문제를 poll한다")
    public void pollProblem() throws Exception {
        // given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        LocalDateTime dateTime1 = LocalDateTime.parse("2023-08-12 20:44:17.423552", formatter);
        ReflectionTestUtils.setField(problem1, "createdAt", dateTime1);

        LocalDateTime dateTime2 = LocalDateTime.parse("2023-08-13 20:44:17.423552", formatter);
        ReflectionTestUtils.setField(problem2, "createdAt", dateTime2);

        LocalDateTime dateTime3 = LocalDateTime.parse("2023-08-14 20:44:17.423552", formatter);
        ReflectionTestUtils.setField(problem3, "createdAt", dateTime3);

        // when
        when(problemRepository.pollProblem(member.getId())).thenReturn(Optional.ofNullable(problem1));
        when(memberService.getSessionInfo(request)).thenReturn(sessionInfo);
        when(memberRepository.existsById(member.getId())).thenReturn(true);

        ProblemResponse result = problemService.pollProblem(request);

        // then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(result.getLevel()).isEqualTo(problem1.getLevel()),
                () -> assertThat(result.getCategory()).isEqualTo(problem1.getCategory())
        );
    }

    @Test
    @DisplayName("poll할 수 있는 문제가 없는 경우 예외가 발생한다")
    public void pollProblemEmptyException() throws Exception {
        // when
        when(memberService.getSessionInfo(request)).thenReturn(sessionInfo);
        when(memberRepository.existsById(member.getId())).thenReturn(true);

        // then
        assertThatThrownBy(
                () -> problemService.pollProblem(request))
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
