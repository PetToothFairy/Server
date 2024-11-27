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
import com.example.server.service.InvalidTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tooth")
public class ToothController {
    private final JwtTokenService jwtTokenService;
    private final InvalidTokenService invalidTokenService;

    @GetMapping("")
    public ResponseEntity<?> mypageGetinfo(@RequestHeader("AccessToken") String AccessToken) {
        // 1. RefreshToken Valid?
        try {
            if(jwtTokenService.validateAccessToken(AccessToken) == false) {
                throw new CException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Check userId
        Long userId;
        try {
            userId = jwtTokenService.extractIdFromAccessToken(AccessToken);
            if(invalidTokenService.existsById(userId) == false) {
                throw new CException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorCode.INVALID_TOKEN);
        }
        
        // 3. Create New Data


        return ResponseEntity
            .status(ErrorCode.SUCCESS.getStatus())
            .body(CErrorResponse.builder()
                .status(ErrorCode.SUCCESS.getStatus())
                .message(".")
                .build()
            );
    }
}
