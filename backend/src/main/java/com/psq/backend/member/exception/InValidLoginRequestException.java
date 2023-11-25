package com.psq.backend.member.exception;

public class InValidLoginRequestException extends RuntimeException {
    public InValidLoginRequestException(final String message) {
        super(message);
    }

    public InValidLoginRequestException() {
        this("로그인에 실패했습니다.");
    }
}
