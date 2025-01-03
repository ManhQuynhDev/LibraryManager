package com.dev.library.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class AppError {
    public enum ErrorCode {
        UNKNOWN_ERROR,
        INVALID_REQUEST,
        METHOD_NOT_ALLOWED,
        NOT_FOUND,
        DUPLICATE,
        UNAUTHORIZED,
        DATA_INVALID,
        ACCOUNT_EXIST,
        ACCOUNT_NOT_FOUND
    }

    private ErrorCode code;
    private String message;

    public AppError(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
