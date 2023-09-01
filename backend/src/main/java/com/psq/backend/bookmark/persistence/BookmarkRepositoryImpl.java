package com.psq.backend.bookmark.persistence;

import com.psq.backend.bookmark.domain.Bookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.psq.backend.bookmark.domain.QBookmark.bookmark;
import static com.psq.backend.problem.domain.QProblem.problem;

@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Bookmark> findBookmarkByFetchJoin(Long memberId) {
        return jpaQueryFactory
                .selectFrom(bookmark)
                .join(bookmark.problem, problem).fetchJoin()
                .where(bookmark.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public boolean isExistedBookmark(Long memberId, Long problemId) {
        return jpaQueryFactory
                .selectOne()
                .from(bookmark)
                .where(
                        bookmark.member.id.eq(memberId),
                        bookmark.problem.id.eq(problemId)
                )
                .fetchFirst() != null;
    }
}
