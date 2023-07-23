package com.problem.solving.problem.exception;

public class NoSuchProblemException extends RuntimeException{
    public NoSuchProblemException(final String message) {
        super(message);
    }

    public NoSuchProblemException() {
        this("존재하지 않는 문제입니다.");
    }
}
