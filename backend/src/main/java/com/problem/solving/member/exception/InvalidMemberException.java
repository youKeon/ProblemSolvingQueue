package com.problem.solving.member.exception;

public class InvalidMemberException extends RuntimeException {
    public InvalidMemberException(final String message) {
        super(message);
    }

    public InvalidMemberException() {
        this("잘못된 이메일입니다.");
    }

}
