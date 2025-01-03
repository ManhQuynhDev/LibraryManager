package com.dev.library.core.exception;

public class UserAccountExitsException extends RuntimeException {
    public UserAccountExitsException(String message) {
        super(message);
    }
}
