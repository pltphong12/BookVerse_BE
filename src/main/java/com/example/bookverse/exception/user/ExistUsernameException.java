package com.example.bookverse.exception.user;

public class ExistUsernameException extends RuntimeException {
    public ExistUsernameException(String message) {
        super(message);
    }
}
