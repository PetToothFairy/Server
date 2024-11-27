package com.example.server.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CExceptionHandler {
    @ExceptionHandler(CException.class)
    protected ResponseEntity<CErrorResponse> handleCustomException(CException e) {
        log.error("CustomException: {}", e.getErrorCode());
        return CErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // Null 값 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CErrorResponse> handleNullPointerException(
            NullPointerException ex) {
            
            CErrorResponse response = CErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("Required value is null")
            .build();
            
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CErrorResponse> handleException(Exception e) {
        log.error("HandleException: {}", e.getMessage());
        return CErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
