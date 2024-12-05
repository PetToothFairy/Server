package com.example.server.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessBase {
    SUCCESS(200, ".");

    private final int status;
    private final String message;
}
