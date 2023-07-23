package com.problem.solving.problem.exception;

public class InvalidProblemException extends RuntimeException{
    public InvalidProblemException(final String message) {
        super(message);
    }

    public InvalidProblemException() {
        this("잘못된 문제입니다.");
    }
}
