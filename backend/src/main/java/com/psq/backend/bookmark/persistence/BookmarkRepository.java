package com.psq.backend.bookmark.persistence;


import com.psq.backend.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.problem WHERE b.member.id = :memberId")
    List<Bookmark> findBookmarkByFetchJoin(Long memberId);

    boolean existsBookmarkByMember_IdAndProblem_Id(Long memberId, Long problemId);
}
