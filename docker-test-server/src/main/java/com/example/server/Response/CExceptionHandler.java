package com.example.server.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CExceptionHandler {
    @ExceptionHandler(CException.class)
    protected ResponseEntity<BaseResponse<ErrorBase>> handleCustomException(CException error) {
        BaseResponse<ErrorBase> response = BaseResponse.error(error.getErrorBase());

        return ResponseEntity
            .status(error.getErrorBase().getStatus())
            .body(response);
    }

    // Null 값 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<BaseResponse<ErrorBase>> handleNullPointerException() {
        BaseResponse<ErrorBase> response = BaseResponse.error(ErrorBase.NULL_VALUE);
            
        return ResponseEntity
            .status(ErrorBase.NULL_VALUE.getStatus())
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseResponse<Exception>> handleException(Exception exception) {
        BaseResponse<Exception> response = BaseResponse.error(ErrorBase.INTERNAL_SERVER_ERROR, exception);

        return ResponseEntity
            .status(ErrorBase.INTERNAL_SERVER_ERROR.getStatus())
            .body(response);
    }
}
