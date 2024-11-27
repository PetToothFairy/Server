package com.example.server.error;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CErrorResponse {
    private final int status;
    private final Object message;

    public static ResponseEntity<CErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CErrorResponse.builder()
                        .status(errorCode.getStatus())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
