package com.psq.backend.bookmark.persistence;

import com.psq.backend.bookmark.domain.Bookmark;
import com.psq.backend.common.annotation.RepositoryTest;
import com.psq.backend.member.domain.Member;
import com.psq.backend.problem.domain.Category;
import com.psq.backend.problem.domain.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BookmarkRepositoryTest extends RepositoryTest {
    private Bookmark bookmark1;
    private Bookmark bookmark2;
    private Problem problem1;
    private Problem problem2;
    private Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(new Member("yukeon97@gmail.com", "123", "salt"));
        problem1 = problemRepository.save(
                new Problem(
                        this.member,
                        "title",
                        "test1",
                        1,
                        Category.DFS,
                        false));

        problem2 = problemRepository.save(
                new Problem(
                        this.member,
                        "title",
                        "test2",
                        2,
                        Category.BFS,
                        false));
        bookmark1 = bookmarkRepository.save(new Bookmark(member, problem1));
        bookmark2 = bookmarkRepository.save(new Bookmark(member, problem2));
    }

    @Test
    @DisplayName("사용자 id를 입력 받으면 사용자가 북마크로 등록한 문제를 조회한다")
    public void getBookmarkedProblemList() throws Exception {
        // given
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkByFetchJoin(member.getId());

        // when, then
        assertAll(
                () -> assertThat(bookmarkList).hasSize(2),
                () -> assertThat(bookmarkList.get(0).getProblem()).isEqualTo(problem1),
                () -> assertThat(bookmarkList.get(0).getMember()).isEqualTo(member),

                () -> assertThat(bookmarkList.get(1).getProblem()).isEqualTo(problem2),
                () -> assertThat(bookmarkList.get(1).getMember()).isEqualTo(member)

        );
    }

    @Test
    @DisplayName("사용자가 북마크로 등록한 문제가 없으면 빈 리스트를 반환한다")
    public void getEmptyBookmarkedProblemList() throws Exception {
        // given
        Long newMemberId = 0L;
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkByFetchJoin(newMemberId);

        // when, then
        assertThat(bookmarkList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("이미 존재하는 북마크를 조회하면 true를 반환한다")
    public void getExistBookmark() throws Exception {
        // when, then
        boolean actual = bookmarkRepository.isExistedBookmark(member.getId(), problem1.getId());
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 북마크를 조회하면 false를 반환한다")
    public void getNotExistBookmark() throws Exception {
        // given
        Long newMemberId = 0L;
        Long newProblemId = 0L;

        // when
        boolean actual = bookmarkRepository.isExistedBookmark(newMemberId, newProblemId);

        // then
        assertThat(actual).isFalse();
    }
}
