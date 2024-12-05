package com.example.server.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorBase {
    // 400 에러
    REGISTERED_USER(400, "이미 존재하는 유저"),
    NULL_VALUE(400, "필수 변수에 NULL값 존재"),
    KAKAO_INVALID_TOKEN(401, "유효하지 않은 '소셜' 액세스 토큰"),
    INVALID_TOKEN(401, "유효하지 않은 토큰"),
    GHOST_USER(403, "존재하는 않는 유저"),

    // 500 에러 
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다.");

    private final int status;
    private final String message;
}
