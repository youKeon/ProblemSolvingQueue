package com.psq.backend.bookmark.persistence;


import com.psq.backend.bookmark.domain.Bookmark;

import java.util.List;

public interface BookmarkCustomRepository {
    List<Bookmark> findBookmarkByFetchJoin(Long memberId);
    boolean isExistedBookmark(Long memberId, Long problemId);
}
