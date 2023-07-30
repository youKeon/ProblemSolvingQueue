package com.problem.solving.bookmark.exception;

public class NoSuchBookmarkException extends RuntimeException{
    public NoSuchBookmarkException(final String message) {
        super(message);
    }

    public NoSuchBookmarkException() {
        this("존재하지 않는 북마크입니다.");
    }
}
