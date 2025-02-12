package com.example.scheduler2.exception;

// 이메일 혹은 비밀번호 불일치
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
