package com.psq.backend.member.exception;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(final String message) {
        super(message);
    }

    public InvalidEmailFormatException() {
        this("잘못된 이메일입니다.");
    }

}
