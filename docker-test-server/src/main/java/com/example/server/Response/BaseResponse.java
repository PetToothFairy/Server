package com.example.server.Response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private final int status;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;


    public static <T> BaseResponse<T> success(SuccessBase success, T data) {
        return new BaseResponse<>(success.getStatus(), success.getMessage(), data);
    }

    public static <T> BaseResponse<T> error(ErrorBase error) {
        return new BaseResponse<>(error.getStatus(), error.getMessage(), null);
    }

    public static <T> BaseResponse<T> error(ErrorBase error, T data) {
        return new BaseResponse<>(error.getStatus(), error.getMessage(), data);
    }

    public static BaseResponse<Exception> error(ErrorBase error, Exception exception) {
        return new BaseResponse<>(error.getStatus(), error.getMessage(), exception);
    }
}
