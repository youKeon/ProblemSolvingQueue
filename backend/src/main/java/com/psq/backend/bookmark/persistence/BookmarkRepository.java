package com.psq.backend.bookmark.persistence;


import com.psq.backend.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkCustomRepository {
}
