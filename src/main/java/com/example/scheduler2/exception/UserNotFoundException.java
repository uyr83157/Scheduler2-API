package com.example.scheduler2.exception;

// 해당하는 회원 없음
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
