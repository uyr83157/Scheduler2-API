package com.example.scheduler2.exception;

public class DuplicateUserAccountException extends RuntimeException {
    public DuplicateUserAccountException(String message) {
        super(message);
    }
}
