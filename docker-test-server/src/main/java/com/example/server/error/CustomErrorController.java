package com.example.server.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ErrorResponse errorResponse;

        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());

            switch (statusCode) {
                case 500:
                    errorResponse = ErrorResponse.builder()
                            .status(500)
                            .message("서버 내부 오류")
                            .code("INTERNAL_SERVER_ERROR")
                            .build();
                    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

                case 401:
                    errorResponse = ErrorResponse.builder()
                            .status(401)
                            .message("유효하지 않은 토큰")
                            .code("UNAUTHORIZED")
                            .build();
                    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

                case 400:
                    errorResponse = ErrorResponse.builder()
                            .status(400)
                            .message("잘못된 액세스토큰 형식")
                            .code("WRONG_ACCESSTOKEN")
                            .build();
                    return new ResponseEntity<>(errorResponse, HttpStatus.PAYMENT_REQUIRED);

                default:
                    errorResponse = ErrorResponse.builder()
                            .status(statusCode)
                            .message("알 수 없는 오류가 발생했습니다")
                            .code("UNKNOWN_ERROR")
                            .build();
                    return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
            }
        }

        errorResponse = ErrorResponse.builder()
                .status(500)
                .message("알 수 없는 오류가 발생했습니다")
                .code("UNKNOWN_ERROR")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}