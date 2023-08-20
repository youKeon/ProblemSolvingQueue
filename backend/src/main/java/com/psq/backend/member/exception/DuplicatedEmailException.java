package com.psq.backend.member.exception;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(final String message) {
        super(message);
    }

    public DuplicatedEmailException() {
        this("이미 등록된 이메일입니다.");
    }

}
