package com.problem.solving.bookmark.exception;

public class DuplicatedBookmarkException extends RuntimeException{
    public DuplicatedBookmarkException(final String message) {
        super(message);
    }

    public DuplicatedBookmarkException() {
        this("이미 존재하는 북마크입니다.");
    }
}
