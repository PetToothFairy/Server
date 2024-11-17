package com.example.server.error;

import lombok.Getter;

@Getter
public class CException extends RuntimeException {
    private final ErrorCode errorCode;

    public CException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
