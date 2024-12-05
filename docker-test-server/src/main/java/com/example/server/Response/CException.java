package com.example.server.Response;

import lombok.Getter;

@Getter
public class CException extends RuntimeException {
    private final ErrorBase errorBase;

    public CException(ErrorBase errorBase) {
        super(errorBase.getMessage());
        this.errorBase = errorBase;
    }
}
