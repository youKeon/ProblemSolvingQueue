package com.problem.solving.problem.application;

import com.problem.solving.member.domain.Member;
import com.problem.solving.member.exception.NoSuchMemberException;
import com.problem.solving.member.persistence.MemberRepository;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.dto.request.ProblemSaveRequest;
import com.problem.solving.problem.dto.request.ProblemUpdateRequest;
import com.problem.solving.problem.dto.response.ProblemResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import com.problem.solving.problem.persistence.ProblemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ProblemServiceTest {
    @InjectMocks
    private ProblemService problemService;
    @Mock
    private ProblemRepository problemRepository;
    @Mock
    private MemberRepository memberRepository;
    private static Pageable pageable = PageRequest.of(0, 3);
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
    @DisplayName("문제를 저장한다")
    public void registerProblem() throws Exception {
        //given
        Long memberId = 1L;

        ProblemSaveRequest request = new ProblemSaveRequest(memberId, "problem", Category.DFS, 3);

        //when
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        problemService.addProblem(request);

        //then
        verify(problemRepository, times(1)).save(any(Problem.class));
    }

    @Test
    @DisplayName("존재하지 않는 유저 정보로 문제를 저장하면 예외가 발생한다")
    public void registerProblemEmptyMemberException() throws Exception {
        //given
        Long memberId = 0L;

        ProblemSaveRequest request = new ProblemSaveRequest(memberId, "problem", Category.DFS, 3);

        //when
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //then
        assertThrows(NoSuchMemberException.class, () -> {
            problemService.addProblem(request);
        });
    }

    @Test
    @DisplayName("문제를 soft delete한다")
    public void deleteProblem() throws Exception {
        //given
        Long id = 1L;

        //when
        when(problemRepository.findById(id)).thenReturn(Optional.ofNullable(problem1));
        problemService.delete(id);

        //then
        assertThat(problem1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 문제를 삭제하려는 경우 예외가 발생한다")
    public void deleteProblemEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.delete(1L))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("가장 먼저 저장한 문제를 조회한다")
    public void pollProblem() throws Exception {
        //given

        //when
        when(problemRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.ofNullable(problem1));
        ProblemResponse result = problemService.pollProblem();

        //then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(result.getLevel()).isEqualTo(problem1.getLevel()),
                () -> assertThat(result.getCategory()).isEqualTo(problem1.getCategory())
        );
    }

    @Test
    @DisplayName("문제가 없는 경우 예외가 발생한다")
    public void pollProblemEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.pollProblem())
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("Id로 문제를 단건 조회한다")
    public void getProblemById() throws Exception {

        //when
        when(problemRepository.findById(problem1.getId())).thenReturn(Optional.ofNullable(problem1));
        ProblemResponse result = problemService.getProblem(problem1.getId());

        //then
        assertAll(
                () -> assertThat(result.getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(result.getCategory()).isEqualTo(problem1.getCategory()),
                () -> assertThat(result.getLevel()).isEqualTo(problem1.getLevel())
        );
    }

    @Test
    @DisplayName("존재하지 않는 문제를 단건 조회하면 예외가 발생한다")
    public void getProblemByIdEmptyException() throws Exception {
        //when, then
        assertThatThrownBy(
                () -> problemService.getProblem(1L))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("문제를 수정한다")
    public void updateProblem() throws Exception {
        //given
        Long id = 1L;
        ProblemUpdateRequest request = new ProblemUpdateRequest(
                "afterUpdate",
                Category.DFS,
                true,
                3);


        //when
        when(problemRepository.findById(any(Long.class))).thenReturn(Optional.of(problem1));
        problemService.update(id, request);

        //then
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
        //given
        Long id = 1L;

        //when, then
        assertThatThrownBy(
                () -> problemService.update(id, any(ProblemUpdateRequest.class)))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("문제를 찾을 수 없습니다.");
    }
}
