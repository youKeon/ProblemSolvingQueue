package com.problem.solving.bookmark.application;

import com.problem.solving.bookmark.domain.Bookmark;
import com.problem.solving.bookmark.dto.request.BookmarkSaveRequest;
import com.problem.solving.bookmark.exception.DuplicatedBookmarkException;
import com.problem.solving.bookmark.exception.NoSuchBookmarkException;
import com.problem.solving.common.annotation.ServiceTest;
import com.problem.solving.member.domain.Member;
import com.problem.solving.member.exception.NoSuchMemberException;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.problem.domain.Problem;
import com.problem.solving.problem.dto.response.ProblemListResponse;
import com.problem.solving.problem.exception.NoSuchProblemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

public class BookmarkServiceTest extends ServiceTest {

    @BeforeEach
    void setup() {
        bookmark = new Bookmark(member, problem1);

        member = new Member("yukeon97@gmail.com", "123");

        problem1 = new Problem(member, "title", "problem1", 3, Category.DFS, false);
        problem2 = new Problem(member, "title", "problem2", 3, Category.BFS, false);
        problem3 = new Problem(member, "title", "problem3", 3, Category.SORT, false);

        ReflectionTestUtils.setField(bookmark, "id", 1L);

        ReflectionTestUtils.setField(member, "id", 1L);

        ReflectionTestUtils.setField(problem1, "id", 1L);
        ReflectionTestUtils.setField(problem2, "id", 2L);
        ReflectionTestUtils.setField(problem3, "id", 3L);
    }

    @Test
    @DisplayName("사용자 id와 문제 id를 받아 북마크에 등록한다")
    void saveBookmarkTest() {
        // given
        BookmarkSaveRequest request = new BookmarkSaveRequest(member.getId(), problem1.getId());

        // when
        when(problemRepository.findById(request.getProblemId())).thenReturn(Optional.ofNullable(problem1));
        when(memberRepository.findById(request.getMemberId())).thenReturn(Optional.ofNullable(member));

        // then
        assertDoesNotThrow(() -> bookmarkService.save(request));
    }

    @Test
    @DisplayName("동일한 문제로 북마크를 등록하면 예외가 발생한다")
    void saveDuplicatedBookmarkTest() {
        // given
        BookmarkSaveRequest request = new BookmarkSaveRequest(member.getId(), problem1.getId());

        // when
        when(problemRepository.findById(request.getProblemId())).thenReturn(Optional.ofNullable(problem1));
        when(memberRepository.findById(request.getMemberId())).thenReturn(Optional.ofNullable(member));
        when(bookmarkRepository.existsBookmarkByMember_IdAndProblem_Id(request.getMemberId(), problem1.getId()))
                .thenReturn(true);


        // then
        assertThatThrownBy(
                () -> bookmarkService.save(request))
                .isInstanceOf(DuplicatedBookmarkException.class)
                .hasMessageContaining("이미 존재하는 북마크입니다.");
    }

    @Test
    @DisplayName("북마크 id를 받아 북마크를 삭제한다")
    void deleteBookmarkTest() {
        // when
        when(bookmarkRepository.findById(bookmark.getId())).thenReturn(Optional.ofNullable(bookmark));

        // then
        assertDoesNotThrow(() -> bookmarkService.delete(member.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 북마크 id를 받아 북마크를 삭제하면 예외가 발생한다")
    void deleteNoSuchBookmarkExceptionTest() {
        // given
        Long 존재하지_않는_북마크_ID = 0L;

        // then
        assertThatThrownBy(
                () -> bookmarkService.delete(존재하지_않는_북마크_ID))
                .isInstanceOf(NoSuchBookmarkException.class)
                .hasMessageContaining("존재하지 않는 북마크입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 id로 북마크를 등록하면 예외가 발생한다")
    void saveNoSuchMemberIdBookmarkExceptionTest() {
        // given
        Long 존재하지_않는_사용자_ID = 0L;
        BookmarkSaveRequest request = new BookmarkSaveRequest(존재하지_않는_사용자_ID, problem1.getId());

        // then
        assertThatThrownBy(
                () -> bookmarkService.save(request))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 문제 id로 북마크를 등록하면 예외가 발생한다")
    void saveNoSuchProblemIdExceptionTest() {
        // given
        Long 존재하지_않는_문제_ID = 0L;
        BookmarkSaveRequest request = new BookmarkSaveRequest(member.getId(), 존재하지_않는_문제_ID);

        // when
        when(memberRepository.findById(request.getMemberId())).thenReturn(Optional.ofNullable(member));

        // then
        assertThatThrownBy(
                () -> bookmarkService.save(request))
                .isInstanceOf(NoSuchProblemException.class)
                .hasMessageContaining("존재하지 않는 문제입니다.");
    }

    @Test
    @DisplayName("사용자 id를 입력받아 북마크로 등록된 문제를 조회한다")
    public void getBookmarkedProblemList() throws Exception {
        // given
        List<Bookmark> bookmarkList = new ArrayList<>(Arrays.asList(
                new Bookmark(member, problem1),
                new Bookmark(member, problem2),
                new Bookmark(member, problem3)
        ));

        // when
        when(bookmarkRepository.findBookmarkByFetchJoin(member.getId())).thenReturn(bookmarkList);
        List<ProblemListResponse> actual = bookmarkService.getBookmarkList(member.getId());

        // then
        assertAll(
                () -> assertThat(actual.get(0).getLevel()).isEqualTo(problem1.getLevel()),
                () -> assertThat(actual.get(0).getUrl()).isEqualTo(problem1.getUrl()),
                () -> assertThat(actual.get(0).getCategory()).isEqualTo(problem1.getCategory()),

                () -> assertThat(actual.get(1).getLevel()).isEqualTo(problem2.getLevel()),
                () -> assertThat(actual.get(1).getUrl()).isEqualTo(problem2.getUrl()),
                () -> assertThat(actual.get(1).getCategory()).isEqualTo(problem2.getCategory()),

                () -> assertThat(actual.get(2).getLevel()).isEqualTo(problem3.getLevel()),
                () -> assertThat(actual.get(2).getUrl()).isEqualTo(problem3.getUrl()),
                () -> assertThat(actual.get(2).getCategory()).isEqualTo(problem3.getCategory())
        );
    }

    @Test
    @DisplayName("입력 받은 사용자 id에 해당되는 북마크가 없으면 예외가 발생한다")
    void getBookmarkEmptyExceptionTest() {
        // given
        Long memberId = 0L;

        // when
        when(bookmarkRepository.findBookmarkByFetchJoin(memberId)).thenReturn(Collections.emptyList());

        // then
        assertThatThrownBy(
                () -> bookmarkService.getBookmarkList(memberId))
                .isInstanceOf(NoSuchBookmarkException.class)
                .hasMessageContaining("존재하지 않는 북마크입니다.");
    }
}
