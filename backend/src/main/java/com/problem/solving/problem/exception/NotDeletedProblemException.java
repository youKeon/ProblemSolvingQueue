package com.problem.solving.problem.exception;

public class NotDeletedProblemException extends RuntimeException{
    public NotDeletedProblemException(final String message) {
        super(message);
    }

    public NotDeletedProblemException() {
        this("삭제되지 않은 문제입니다.");
    }
}
