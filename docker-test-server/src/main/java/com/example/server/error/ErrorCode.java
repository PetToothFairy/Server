package com.example.server.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "성공"),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    REESSUIE_TOKEN(402, "토큰을 재발급 받아야합니다."),
    ALREADY_REGISTER_USER(403, "이미 가입한 유저입니다."),
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다."),
    DATABASE_ERROR(501, "데이터베이스 오류가 발생했습니다");

    private final int status;
    private final String message;
}
