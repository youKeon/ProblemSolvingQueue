package com.problem.solving.bookmark.persistence;


import com.problem.solving.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
