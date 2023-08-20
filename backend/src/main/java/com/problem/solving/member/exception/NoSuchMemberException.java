package com.problem.solving.member.exception;

public class NoSuchMemberException extends RuntimeException{
    public NoSuchMemberException(final String message) {
        super(message);
    }

    public NoSuchMemberException() {
        this("존재하지 않는 사용자입니다.");
    }
}
