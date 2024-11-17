package com.example.server.service;

import org.springframework.stereotype.Service;

import com.example.server.error.CException;
import com.example.server.error.ErrorCode;
import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final 필드의 생성자 자동 생성
public class InvalidTokenService {
    private final UserRepository userRepository;

    /* Input : Token, Output : Boolean
    * 1. AccessToken 유(2)/무(401)
    * 2. AccessToken Expire Yes(2-1)/No(3)
    * 2-1. RefreshToken 유(2-2)/무(401)
    * 2-2. RefreshToken Expire Yes(401)/No(2-3)
    * 2-3. AccessToken 재발급(402)
    * 3. return
    * Tip. Token 유/무는 데이터베이스로 Check한다.
    */ 
    public void isTokenInvalid(String AccessToken) throws Exception {
        // 1. AccessToken 유(2)/무(401)
        boolean flagAccessToken = userRepository.existsByAccessToken(AccessToken);
        if (flagAccessToken == false)
            throw new CException(ErrorCode.INVALID_TOKEN);
            
        // 2. AccessToken Expire Yes(2-1)/No(3)
        boolean flagAccessTokenExpire = userRepository.isAccessTokenInvalid(AccessToken);
        if (flagAccessTokenExpire == false) {
            return;
        }

        // 2-1. RefreshToken 유(2-2)/무(401)
        String RefreshToken = userRepository.getRefreshTokenByAccessToken(AccessToken);
        if(RefreshToken == null) {
            throw new CException(ErrorCode.INVALID_TOKEN);  
        }

        // 2-2. RefreshToken Expire Yes(401)/No(2-3)
        boolean flagRefreshTokenExpire = userRepository.isRefreshTokenInvalid(RefreshToken);
        if(flagRefreshTokenExpire == true) {
            throw new CException(ErrorCode.INVALID_TOKEN);  
        }

        // 2-3. AccessToken 재발급(402)
        throw new CException(ErrorCode.REESSUIE_TOKEN);  
    }
}
