package com.problem.solving.bookmark.persistence;


import com.problem.solving.bookmark.domain.Bookmark;
import com.problem.solving.member.domain.Member;
import com.problem.solving.problem.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.problem WHERE b.member.id = :memberId")
    List<Bookmark> findBookmarkByFetchJoin(Long memberId);

    boolean existsBookmarkByMember_IdAndProblem_Id(Long memberId, Long problemId);
}
