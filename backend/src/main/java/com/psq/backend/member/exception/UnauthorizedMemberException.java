package com.psq.backend.member.exception;

public class UnauthorizedMemberException extends RuntimeException {
    public UnauthorizedMemberException(final String message) {
        super(message);
    }

    public UnauthorizedMemberException() {
        this("권한이 없는 사용자입니다.");
    }
}
