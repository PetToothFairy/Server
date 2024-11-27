package com.example.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.error.CErrorResponse;
import com.example.server.error.CException;
import com.example.server.error.ErrorCode;
import com.example.server.jwt.JwtTokenService;
import com.example.server.model.Token;
import com.example.server.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    // RefreshToken으로 AccessToken과 RefreshToken 새로 발급
    @GetMapping("")
    public ResponseEntity<?> refreshAccessToken (@RequestHeader("RefreshToken") String RefreshToken) {
        // 1. RefreshToken Valid?
        try {
            if(jwtTokenService.validateRefreshToken(RefreshToken) == false) {
                throw new CException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Get userId & radomId from RefreshToken
        Long userId = null;
        String randomId = null;
        try {
            userId = jwtTokenService.extractIdFromRefreshToken(RefreshToken);
            randomId = jwtTokenService.extractRandomIdFromRefreshToken(RefreshToken);
        } catch (Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }

        if(userId == null || randomId == null)
            throw new CException(ErrorCode.INVALID_TOKEN);

        // 3. Check userId & radomId
        try {
            if(refreshTokenService.checkIdAndRandomId(userId, randomId) == false) {
                throw new CException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }

        // 4. JWT AccessToken, RefreshToken 토큰 발급
        Token token = new Token();
        String new_randomId = jwtTokenService.generateRandomId();
        try {
            String accessToken = jwtTokenService.createAccessToken(userId);
            String refreshToken = jwtTokenService.createRefreshToken(userId, new_randomId);
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
        } catch(Exception e) {
            throw new CException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 5. RefreshToken RandomID Database에 수정
        try {
            refreshTokenService.updateRandomIdByUserId(userId, new_randomId);
        } catch(Exception e) {
            throw new CException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
            .status(ErrorCode.SUCCESS.getStatus())
            .body(CErrorResponse.builder()
                .status(ErrorCode.SUCCESS.getStatus())
                .message(token)
                .build()
            );
    }
}
