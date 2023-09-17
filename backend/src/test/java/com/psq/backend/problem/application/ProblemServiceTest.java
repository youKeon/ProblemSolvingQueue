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
import com.psq.backend.problem.exception.InvalidProblemException;
import com.psq.backend.problem.exception.NoSuchProblemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
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
        List<ProblemListResponse> problemList = Arrays.asList(
                new ProblemListResponse(problem1.getUrl(), problem1.getLevel(), problem1.getCategory(), problem1.isSolved()),
                new ProblemListResponse(problem2.getUrl(), problem2.getLevel(), problem2.getCategory(), problem2.isSolved()),
                new ProblemListResponse(problem3.getUrl(), problem3.getLevel(), problem3.getCategory(), problem3.isSolved())
        );

        // when
        when(problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable)).thenReturn(problemList);
        when(memberService.getMember(request)).thenReturn(member);

        List<ProblemListResponse> actual = problemService.getProblemList(request, 3, Category.DFS, false, pageable);

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(3),
                () -> assertThat(actual.get(0).getCategory()).isEqualTo(problem1.getCategory()),
                () -> assertThat(actual.get(0).getLevel()).isEqualTo(problem1.getLevel()),

                () -> assertThat(actual.get(1).getUrl()).isEqualTo(problem2.getUrl()),
                () -> assertThat(actual.get(1).getCategory()).isEqualTo(problem2.getCategory()),
                () -> assertThat(actual.get(1).getLevel()).isEqualTo(problem2.getLevel()),

                () -> assertThat(actual.get(2).getUrl()).isEqualTo(problem3.getUrl()),
                () -> assertThat(actual.get(2).getCategory()).isEqualTo(problem3.getCategory()),
                () -> assertThat(actual.get(2).getLevel()).isEqualTo(problem3.getLevel())
        );
    }

    @Test
    @DisplayName("문제 리스트 조회 시 사용자의 세션 정보가 존재하지 않는 경우 예외가 발생한다")
    public void getProblemListNoSuchMemberExceptionTest() throws Exception {
        // when
        when(memberService.getMember(request)).thenThrow(new NoSuchMemberException());

        // then
        assertThatThrownBy(
                () -> problemService.getProblemList(request, 3, Category.DFS, false, pageable))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("문제 리스트 조회 시 문제 리스트가 없는 경우 예외가 발생한다")
    public void getProblemsEmptyException() throws Exception {
        // when
        when(memberService.getMember(request)).thenReturn(member);
        when(problemRepository.findAllProblem(member.getId(), 3, Category.DFS, false, pageable))
                .thenReturn(Collections.emptyList());

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
        when(memberService.getMember(request)).thenReturn(member);

        // then
        assertDoesNotThrow(() -> problemService.save(request, saveRequest));
    }

    @Test
    @DisplayName("문제 저장 시 존재하지 않는 유저 정보로 문제를 저장하면 예외가 발생한다")
    public void registerProblemEmptyMemberException() throws Exception {
        // given
        ProblemSaveRequest saveRequest = new ProblemSaveRequest("title","problem", Category.DFS, 3);

        // when
        when(memberService.getMember(request)).thenThrow(new NoSuchMemberException());

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
    @DisplayName("문제 삭제 시 존재하지 않는 문제를 삭제하려는 경우 예외가 발생한다")
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

        ProblemResponse response = new ProblemResponse(problem1.getTitle(), problem1.getUrl(), problem1.getLevel(), problem1.getCategory(), problem1.isSolved());

        // when
        when(problemRepository.pollProblem(member.getId())).thenReturn(Optional.of(response));
        when(memberService.getMember(request)).thenReturn(member);

        ProblemResponse actual = problemService.pollProblem(request);

        // then
        assertAll(
                () -> assertThat(actual.getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(actual.getLevel()).isEqualTo(problem1.getLevel()),
                () -> assertThat(actual.getCategory()).isEqualTo(problem1.getCategory())
        );
    }

    @Test
    @DisplayName("문제를 poll할 때 poll할 수 있는 문제가 없는 경우 예외가 발생한다")
    public void pollProblemEmptyProblemException() throws Exception {
        // when
        when(memberService.getMember(request)).thenReturn(member);

        // then
        assertThatThrownBy(
                () -> problemService.pollProblem(request))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("문제를 poll할 때 존재하지 않는 사용자 id로 문제를 poll하면 예외가 발생한다")
    void pollProblemEmptyMemberException() {
        // when
        when(memberService.getMember(request)).thenThrow(new NoSuchMemberException());

        // then
        assertThatThrownBy(
                () ->problemService.pollProblem(request))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("Id로 문제를 단건 조회한다")
    public void getProblemById() throws Exception {
        // given
        ProblemResponse response = new ProblemResponse(problem1.getTitle(), problem1.getUrl(), problem1.getLevel(), problem1.getCategory(), problem1.isSolved());

        // when
        when(problemRepository.findProblem(problem1.getId())).thenReturn(Optional.ofNullable(response));
        ProblemResponse actual = problemService.getProblemInfo(problem1.getId());

        // then
        assertAll(
                () -> assertThat(actual.getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(actual.getCategory()).isEqualTo(problem1.getCategory()),
                () -> assertThat(actual.getLevel()).isEqualTo(problem1.getLevel())
        );
    }

    @Test
    @DisplayName("문제 단건 조회 시 존재하지 않는 문제를 조회하면 예외가 발생한다")
    public void getProblemByIdEmptyException() throws Exception {
        // given
        Long 존재하지_않는_문제_ID = 0L;

        // when, then
        assertThatThrownBy(
                () -> problemService.getProblemInfo(존재하지_않는_문제_ID))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("문제를 수정한다")
    public void updateProblem() throws Exception {
        // given
        ProblemUpdateRequest request = new ProblemUpdateRequest(
                "afterUpdate",
                "afterTitle",
                Category.DFS,
                3,
                true);


        // when
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.of(problem1));
        problemService.update(problem1.getId(), request);

        // then
        assertAll(
                () -> assertEquals(problem1.getUrl(), request.getUrl()),
                () -> assertEquals(problem1.getTitle(), request.getTitle()),
                () -> assertEquals(problem1.getCategory(), request.getCategory()),
                () -> assertEquals(problem1.isSolved(), request.getIsSolved()),
                () -> assertEquals(problem1.getLevel(), request.getLevel())
        );
    }
    @Test
    @DisplayName("문제 수정 시 존재하지 않는 문제를 수정하면 예외가 발생한다")
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
    @DisplayName("삭제된 문제를 되돌린다")
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
    @DisplayName("문제를 되돌릴 시 존재하지 않는 문제를 되돌리면 예외가 발생한다")
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
    @DisplayName("문제를 되돌릴 시 isDeleted가 false(삭제X)인  예외가 발생한다")
    public void recoveryNotDeletedProblemException() throws Exception {
        // when, then
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.ofNullable(problem1));

        assertThatThrownBy(
                () -> problemService.recovery(problem1.getId()))
                .isInstanceOf(InvalidProblemException.class)
                .hasMessageContaining("삭제되지 않은 문제입니다.");
    }

    @Test
    @DisplayName("Problem 엔티티를 조회한다")
    public void getProblemTest() throws Exception {
        // when
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.ofNullable(problem1));
        Problem actual = problemService.getProblem(problem1.getId());

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(problem1);
    }

    @Test
    @DisplayName("Problem 엔티티 조회 시 존재하지 않는 엔티티를 조회하면 예외가 발생한")
    public void getProblemNoSuchProblemException() throws Exception {
        // when, then
        assertThatThrownBy(
                () -> problemService.getProblem(problem1.getId()))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }
}
