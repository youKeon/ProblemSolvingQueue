package com.problem.solving.member.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(final String message) {
        super(message);
    }

    public InvalidEmailException() {
        this("잘못된 이메일입니다.");
    }

}
